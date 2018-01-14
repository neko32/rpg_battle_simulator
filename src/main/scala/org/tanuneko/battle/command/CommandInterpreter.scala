package org.tanuneko.battle.command

import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.model.{CommandType, StatusType, Team}
import org.tanuneko.battle.util.ReflectionUtils

trait CommandInterpreter {

  private lazy val log = Logger(getClass)

  def exec(cmd: Command, roster: Map[String, Team]): Map[String, Team] = {
    val updateTarget = roster(cmd.toTeamName).members.find(_._1 == cmd.toEntName)
    if(updateTarget.isEmpty) roster
    else {
      val newStat = updateTarget.get._2.status
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
          ReflectionUtils.updateVariableViaReflection(newStat, newVal)(cmd.attrName) match {
            case Right(_) => log.debug(s"${cmd.attrName} was updated via reflection util")
            case Left(e) => log.error(e.getMessage, e)
          }
          roster(cmd.toTeamName).members(cmd.toEntName).status = newStat
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
