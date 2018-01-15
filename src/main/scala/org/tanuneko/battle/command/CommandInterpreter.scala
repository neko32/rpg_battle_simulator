package org.tanuneko.battle.command

import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.model.{CommandType, StatusType, Team}
import org.tanuneko.battle.util.ReflectionUtils

trait CommandInterpreter {

  private lazy val log = Logger(getClass)

  def exec(cmd: Command, roster: Map[String, Team], currentTurn: Int): Map[String, Team] = {
    val updateTarget = roster(cmd.toTeamName).members.find(_._1 == cmd.toEntName)
    if(updateTarget.isEmpty) roster
    else {
      val newStat = updateTarget.get._2.status
      newStat.evalExpiry(currentTurn)
      cmd.cmdType match {
        case CommandType.UPDATE_HP =>
          val newHp = cmd.value.asInstanceOf[Int]
          newStat.hp = newHp
          if(newStat.hp <= 0) {
            newStat.statusTypes = Seq(StatusType.Dying)
          }
          roster(cmd.toTeamName).members(cmd.toEntName).status = newStat
          roster

        case CommandType.UPDATE_INT_STATUS =>
          val newVal = cmd.value.asInstanceOf[Int]
          val result = ReflectionUtils.updateVariableViaReflection(newStat, Some(newVal))(cmd.attrName)
          result match {
            case Right(_) =>
              newStat.overrideExpiry += (currentTurn + cmd.expiry -> cmd.attrName)
              roster(cmd.toTeamName).members(cmd.toEntName).status = newStat
          }
          roster

        case CommandType.DYING =>
          newStat.statusTypes = Seq(StatusType.Dying)
          roster(cmd.toTeamName).members(cmd.toEntName).status = newStat
          roster

        case _ => roster
      }
    }
  }

}
