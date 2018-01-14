package org.tanuneko.battle.util

import org.scalatest.AsyncWordSpec
import org.tanuneko.battle.model.{Jobs, Status}
import scala.reflect.runtime.universe._

class ReflectionUtilsSpec extends AsyncWordSpec {

  "ReflecionUtils" should {

    "allow to update field by name" in {
      val s = sampleStatus("tako", 100)
      ReflectionUtils.updateVariableViaReflection(s, 5000)("offense")
      ReflectionUtils.updateVariableViaReflection(s, 32L)("exp")
      ReflectionUtils.updateVariableViaReflection(s, 300)("orga")
      assert(s.offense === 5000)
      assert(s.exp === 32L)
    }

    "throw NoSuchException if unavailable field is updated" in {
      val s = sampleStatus("tako", 100)
      ReflectionUtils.updateVariableViaReflection(s, 300)("orga") match {
        case Left(e) => {
          assert(e.isInstanceOf[NoSuchElementException])
        }
        case Right(_) => fail("Left must be returned")
      }
    }
  }


  private def sampleStatus(name: String, hp: Int = 100, job: Jobs = Jobs.Fighter): Status = {
    Status(
      name = name,
      job = job,
      hp = hp,
      mp = 0,
      offense = 76,
      defense = 104,
      power = 20,
      agility = 10,
      inteligence = 10,
      luck = 5,
      exp = 0L,
      gold = 0L
    )
  }
}
