package org.tanuneko.battle.alg.opponentpicker

import org.tanuneko.battle.model._

import scala.util.Random

trait JobSpecificOpponentPick {


  def pick(myTeamName: String, roster: Map[String, Team], status: Status, includeDead: Boolean)
  :Option[Opponent] = {
    status.job match {
      case Jobs.Fighter => pickAsFighter(myTeamName, roster, status, includeDead)
      case Jobs.Magician => pickAsMagician(myTeamName, roster, status, includeDead)
    }
  }

  def pickAsFighter(myTeamName: String, roster: Map[String, Team], status: Status, includeDead: Boolean)
  :Option[Opponent] = {
    val opponents = filterOpponentsByConfusion(myTeamName, roster, status, includeDead)
    if(opponents.isEmpty) {
      None
    } else {
      val idx = Random.nextInt(opponents.size)
      Some(opponents(idx))
    }
  }

  def pickAsMagician(myTeamName: String, roster: Map[String, Team], status: Status, includeDead: Boolean)
  :Option[Opponent] = {
    val opponents = filterOpponentsByConfusion(myTeamName, roster, status, includeDead)
    if(opponents.isEmpty) {
      None
    } else {
      val idx = Random.nextInt(opponents.size)
      Some(opponents(idx))
    }
  }

  def filterOpponentsByConfusion(myTeamName: String, roster: Map[String, Team], status: Status, includeDead: Boolean): List[Opponent] = {
    val teamNames = if(status.statusTypes.contains(StatusType.Confusion)) {
      roster.keysIterator.toList
    } else {
      roster.keysIterator.toList.filterNot(_ == myTeamName)
    }

    val teamToAttack = teamNames(Random.nextInt(teamNames.size))
    val opponents = roster(teamToAttack).members.toList

    if(!includeDead) {
      opponents.filterNot(_._2.status.statusTypes == StatusType.Dying).map { x => Opponent(teamToAttack, x._1, x._2)}
    } else {
      opponents.map { x => Opponent(teamToAttack, x._1, x._2)}
    }
  }
}
