package org.tanuneko.battle.model

trait CommandType

object CommandType {
  case object UPDATE_HP extends CommandType
  case object DYING extends CommandType
  case object NOOP extends CommandType
  case object UPDATE_INT_STATUS extends CommandType
}
