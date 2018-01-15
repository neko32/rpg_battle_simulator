package org.tanuneko.battle.util

import org.scalatest.AsyncWordSpec

class PercentUtilsSpec extends AsyncWordSpec {

  "PercentUtils" should {
    "Calculate percentage correctly" in {
      assert(PercentUtils.percentage(100, 20) === 20)
      assert(PercentUtils.percentage(100, 50) === 50)
      assert(PercentUtils.percentage(330, 15) === 49)
    }
  }
}
