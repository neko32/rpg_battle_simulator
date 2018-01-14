package org.tanuneko.battle.model

case class Status(
                   val name: String,
                   val job: Jobs,
                   var statusTypes: Seq[StatusType] = Seq(StatusType.Active),
                   var hp: Int = 100,
                   var mp: Int = 0,
                   var offense: Int,
                   var defense: Int,
                   var power: Int,
                   var agility: Int,
                   var inteligence: Int,
                   var luck: Int,
                   var exp: Long,
                   var gold: Long
)

sealed trait StatusType

object StatusType {
  case object Active extends StatusType
  case object Confusion extends StatusType
  case object Dying extends StatusType
}