package org.tanuneko.battle.alg

import org.scalatest.AsyncWordSpec
import org.tanuneko.battle.model.{Jobs, Status}

class WaitTimeCalcSpec extends AsyncWordSpec {

  "WaitTimeCalc" should {

    "Generate wait time and neither luck nor agility effect if luck is less than 100" in {
      val s = status(50, 0, Jobs.Fighter)
      val r = for(i <- 0 to 10) yield WaitTimeCalc.calc(s)
      assert(r.forall(_ == s.job.waitTime))
    }

    "Generate wait time with luck consideration if luck is equal more than 100" in {
      val s = status(68, 568, Jobs.Fighter)
      val r = for(i <- 0 to 10) yield WaitTimeCalc.calc(s)
      assert(r.exists(_ < s.job.waitTime))
    }

    "Generate wait time with luck and agility consideration if those value is equal more than 100" in {
      val s = status(568, 568, Jobs.Fighter)
      val r = for(i <- 0 to 10) yield WaitTimeCalc.calc(s)
      assert(r.exists(_ < s.job.waitTime))
    }
  }

  def status(agility: Int, luck: Int, job: Jobs) = {
    Status(
      name = "x",
      job = job,
      hp = 10,
      mp = 5,
      offense = 0,
      defense = 0,
      power = 20,
      agility = agility,
      intelligence = 10,
      luck = luck,
      exp = 0L,
      gold = 0L
    )
  }
}
