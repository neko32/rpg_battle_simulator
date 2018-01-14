package org.tanuneko.battle.entity

import java.time.{Duration, Instant}

import akka.actor.{Actor, ActorRef}
import com.typesafe.scalalogging.Logger
import org.scalacheck.commands.Commands
import org.tanuneko.battle.alg.actionstrategy.ActionStrategy
import org.tanuneko.battle.command.{Command, CommandInterpreter}
import org.tanuneko.battle.model._

case class Entity(name: String) extends Actor with ActionStrategy with CommandInterpreter {

  private lazy val log = Logger(getClass)

  private var teamName: String = ""
  private var status: Option[Status] = None
  private var session: Option[ActorRef] = None
  private var roster: Map[String, Team] = Map.empty
  private var lastActionTime: Instant = Instant.now.minusSeconds(100)
  private var turn = 0

  override def receive = alive

  def alive: Receive = {
    case Init(myTeamName, initStat, sessionRef) =>
      teamName = myTeamName
      status = Some(initStat)
      session = Some(sessionRef)
      log.info(s"${name}'s status and roster are set - ${status} @Alive::Init")

    case RosterSync(newRoster) =>
      roster = newRoster
      log.info("Updated roster from master @Alive::RosterSync")
      sender() ! true

    case ExecCommand(cmds) =>
      cmds.foreach(exec(_, roster))
      log.debug(s"***ROSTER UPDATED*** ${roster}")

    case Proceed =>
      if(status.get.statusTypes == StatusType.Dying) {
        log.info(s"${status.get.name} has been dead..")
      } else {
        val curTime = Instant.now
        val time = Duration.between(lastActionTime, Instant.now).getSeconds
        log.debug(s"curTime - ${curTime}, lastAction - ${lastActionTime} - diff - ${time} @Alive::Proceed")
        if (time < status.get.job.waitTime) {
          log.debug(s"${name}: not active yet - elapsed from last action is ${time} while waitTime[${status.get.job.waitTime}] @Alive::Proceed")
        } else {
          action
          lastActionTime = Instant.now
        }
      }

  }

  def dead: Receive = {
    case Attack => log.warn(s"${name} has been dead. Noop..")
  }

  private def action = {
    // expiry check

    // then action
    val cmd = doAction(status.get.name, teamName, roster, status.get, false)
    roster = exec(cmd, roster)
    broadcastAsync(cmd)
    turn += 1
  }

  private def broadcastAsync(command: Command) = {
    val cmds = Seq(command)
    session.get ! ExecCommand(Seq(command))
    for {
      v <- roster.filterNot(_._1 == status.get.name).valuesIterator
      w <- v.members
    } {
      w._2.ref ! ExecCommand(cmds)
    }
  }

}
