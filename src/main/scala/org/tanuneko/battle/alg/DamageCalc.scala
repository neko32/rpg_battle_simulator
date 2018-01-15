package org.tanuneko.battle.alg

import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.model.Status

import scala.util.Random

object DamageCalc {

  private lazy val log = Logger(getClass)

  def calc(o: Status, d: Status, name: Option[String] = None): Int = {
    val ov = o.offenseOverride.getOrElse(o.offense)
    val dv = d.defenseOverride.getOrElse(d.defense)
    if(name.isDefined) {
      log.info(s"calculating damage - offense:${ov}, defense:${dv} - offense ${name}")
    } else {
      log.info(s"calculating damage - offense:${ov}, defense:${dv}")
    }
    val e = (ov - dv / 2) / 2
    e match {
      case x: Int if x < 1 => Random.nextInt(2)
      case x: Int if x >= 1 && x < 8 =>
        Random.nextInt(2) match {
          case 0 => e - 1
          case _ => e + 1
        }
      case _ =>
        val x = (e * 7) / 8
        val y = ((e * 224) + ((e / 4) + 1) * 255) / 256
        val max = y - x + 1
        Random.nextInt(max) + x
    }
  }
}
