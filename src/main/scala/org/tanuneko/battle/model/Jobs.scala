package org.tanuneko.battle.model

import org.tanuneko.battle.model.Jobs.{Fighter, Magician}

sealed trait Jobs {
  lazy val waitTime: Int = 40
  lazy val actions: List[Action] = List(Action.PHYSICAL_ATTACK_PUNCH, Action.GUARD)
}

object Jobs {

  case object Fighter extends Jobs {
    override lazy val waitTime = 5
  }

  case object Magician extends Jobs {
    override lazy val waitTime = 50
  }

  case object Ninja extends Jobs {
    override lazy val waitTime = 30
  }

  case object Priest extends Jobs {
    override lazy val waitTime = 45
  }

  case object Thief extends Jobs {
    override lazy val waitTime = 30
  }

}

