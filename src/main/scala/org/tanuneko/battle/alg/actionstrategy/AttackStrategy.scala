package org.tanuneko.battle.alg.actionstrategy

import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.alg.DamageCalc
import org.tanuneko.battle.alg.opponentpicker.JobSpecificOpponentPick
import org.tanuneko.battle.command.Command
import org.tanuneko.battle.model.Jobs.Fighter
import org.tanuneko.battle.model._
import org.tanuneko.battle.util.PercentUtils

import scala.util.Random

trait ActionStrategy extends JobSpecificOpponentPick with GenericActions {

  private lazy val log = Logger(getClass)

  def doAction(name: String, myTeamName: String, roster: Map[String, Team], status: Status, includeDead: Boolean, specialFlag: Option[Any] = None)
  : Command = {
    status.job match {
      case Jobs.Fighter => doFighterAction(name, myTeamName, roster, status, includeDead)
      case Jobs.Magician => doMagicianAction(name, myTeamName, roster, status, includeDead)
    }
  }

  def doFighterAction(name: String, myTeamName: String, roster: Map[String, Team], status: Status, includeDead: Boolean, specialFlag: Option[Any] = None)
  : Command = {
    // move action from jobs to status
    // and define different action per action
    val opponents = pick(myTeamName, roster, status, includeDead)
    if(opponents.isEmpty) {
      log.debug(s"${name}: no action taken as no opponent is found")
      Command(myTeamName, name, "NA", "NA", CommandType.NOOP, "NA", "")
    } else {
      val v = Random.nextInt(100)
      if(v <= 80) {
        physicalRegularAttack(myTeamName, opponents.get.team, status, opponents.get.prof.status)
      } else {
        physicalRegularAttack(myTeamName, opponents.get.team, status, opponents.get.prof.status)
        //guard(status.name, status)
      }
    }
  }

  def doMagicianAction(name: String, myTeamName: String, roster: Map[String, Team], status: Status, includeDead: Boolean): Command = {
    val opponents = pick(myTeamName, roster, status, includeDead)
    if(opponents.isEmpty) {
      log.debug(s"${name}: no action taken as no opponent is found")
      Command(myTeamName, name, "NA", "NA", CommandType.NOOP, "NA", "")
    } else {
      physicalRegularAttack(myTeamName, opponents.get.team, status, opponents.get.prof.status)
    }
  }
}


trait GenericActions {

  private lazy val log = Logger(getClass)

  def physicalRegularAttack(otName: String, dtName: String, o: Status, d: Status) = {
    log.info(s"${o.name} is attacking ${d.name}[Physical Regular Attack]")
    val prevHp = d.hp
    val curHp = prevHp - DamageCalc.calc(o, d)
    d.hp = if(curHp < 0) 0 else curHp
    log.info(s"${d.name} HP: ${prevHp} -> ${d.hp}")
    Command(otName, o.name, dtName, d.name, CommandType.UPDATE_HP, "hp", d.hp)
  }

  /*
  def guard(selfName: String, selfStatus: Status) = {
    log.info("s${selfName} is trying to guard[Guard]")
    val prevDefense = selfStatus.defense
    val curDefense = PercentUtils.percentage(prevDefense, 15)
    selfStatus.defense = curDefense
    log.info(s"${selfName} Defense: ${prevDefense} -> ${curDefense}")
    Command()
  }
  */
}
