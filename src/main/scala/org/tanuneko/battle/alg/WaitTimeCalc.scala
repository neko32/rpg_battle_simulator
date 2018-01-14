package org.tanuneko.battle.alg

import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.model.Status

import scala.util.Random

object WaitTimeCalc {

  private lazy val log = Logger(getClass)

  def calc(status: Status): Int = {
    log.debug(s"luck - ${status.luck}, agility - ${status.agility}, waitBase - ${status.job.waitTime}")
    val x = status.luck / 100
    val luckRand = if(x == 0) 0 else Random.nextInt(x)
    val v = status.job.waitTime - (status.agility / 100) - luckRand
    val r = if(v < 0) 0 else v
    log.debug(s"wait time - ${r}")
    r
  }

}
