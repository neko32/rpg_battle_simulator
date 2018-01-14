package org.tanuneko.battle.alg

import org.scalatest.WordSpec
import org.tanuneko.battle.model.{Jobs, Status}

import scala.util.Random

class DamageCalcSpec extends WordSpec {

  "DamageCalc" should {

    "Calc damage" in {
      val o = status(76, 50, 120, 0, "attacker")
      val d = status(30, 104, 250, 120, "defender")
      val r = for(i <- 0 to 10000) yield DamageCalc.calc(o, d)
      assert(r.exists { x =>
        x >= 10 && x <= 14
      })
    }

    "test" in {
      val s = status(30, 30, 30, 30, "tako")
      println(s)
    }

  }


  def status(offense: Int,
             defense: Int,
             hp: Int,
             mp: Int,
             name: String) = {
    Status(
      name = name,
      job = Jobs.Fighter,
      hp = hp,
      mp = mp,
      offense = offense,
      defense = defense,
      power = 20,
      agility = 10,
      inteligence = 10,
      luck = 5,
      exp = 0L,
      gold = 0L
    )
  }
}
