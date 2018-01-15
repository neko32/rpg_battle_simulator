package org.tanuneko.battle.model

import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.util.ReflectionUtils

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
                   var intelligence: Int,
                   var luck: Int,
                   var exp: Long,
                   var gold: Long,
                   var hpOverride: Option[Int] = None,
                   var mpOverride: Option[Int] = None,
                   var offenseOverride: Option[Int] = None,
                   var defenseOverride: Option[Int] = None,
                   var powerOverride: Option[Int] = None,
                   var agilityOverride: Option[Int] = None,
                   var intelligenceOverride: Option[Int] = None,
                   var luckOverride: Option[Int] = None,
                   var overrideExpiry: Map[Int, String] = Map.empty
) {

  private lazy val log = Logger(getClass)

  def evalExpiry(turn: Int): Unit = {
    for((turnToExpiry, attr) <- overrideExpiry) {
      if(turn == turnToExpiry) {
        log.info(s"${name}'s ${attr} expiring at turn ${turn}")
        attr match {
          case "hp" => hpOverride = None
          case "mp" => mpOverride = None
          case "offense" => offenseOverride = None
          case "defense" => defenseOverride = None
          case "power" => powerOverride = None
          case "agility" => agilityOverride = None
          case "intelligence" => intelligenceOverride = None
          case "luck" => luckOverride = None
        }
      }
    }
  }
}

sealed trait StatusType

object StatusType {
  case object Active extends StatusType
  case object Confusion extends StatusType
  case object Dying extends StatusType
}