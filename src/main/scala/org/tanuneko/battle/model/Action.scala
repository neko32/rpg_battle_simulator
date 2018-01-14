package org.tanuneko.battle.model

sealed trait Action

object Action {
  case object PHYSICAL_ATTACK_PUNCH extends Action
  case object PHYSICAL_ATTACK_KICK extends Action
  case object PHYSICAL_ATTACK_SWORD extends Action
  case object PHYSICAL_ATTACK_KNIFE extends Action
  case object GUARD extends Action
}
