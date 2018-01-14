package org.tanuneko.battle.model

import akka.actor.ActorRef
import org.tanuneko.battle.command.Command

sealed trait Message

final case object Init
final case class Entry(teamName: String, name: String, status: Status)
final case class Join(teamName: String, ref: ActorRef, status: Status)
final case class Init(myTeamName: String, status: Status, session: ActorRef) extends Message
final case class RosterSync(roster: Map[String, Team]) extends Message
final case object Ready extends Message
final case object Proceed extends Message
final case class Attack(offense: Profile) extends Message
final case class ExecCommand(command: Seq[Command])
final case object GameOver
final case object QueryStatus

case object Entry extends Message
case object Gone extends Message
case object Dump extends Message
