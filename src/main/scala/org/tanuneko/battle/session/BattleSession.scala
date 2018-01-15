package org.tanuneko.battle.session

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import org.tanuneko.battle.model._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.command.CommandInterpreter

class BattleSession extends Actor with CommandInterpreter {

  private lazy val log = Logger(getClass)
  var roster: Map[String, Team] = Map.empty
  var winnerTeam: Option[String] = None
  val onlyOnceWinnerNotice = new AtomicInteger(0)
  var turn: Int = 0

  override def receive = entry

  def entry: Receive = {
    case Join(teamName, ref, status) =>
      log.info(s"joining ${status.name} to team[${teamName}] @Entry::Join")
      roster = add(teamName, status.name, status, ref)
      ref ! Init(teamName, status, self)
      sender() ! true

    case Ready =>
      // broadcast finalized roster
      import context.dispatcher
      val rez = broadcast[Boolean](allRefs, RosterSync(roster))
      if(rez.forall(_ == true)) {
        context.become(inGame)
        log.info("Ready to start the session! @Entry::Ready")
        log.info("*Session state has changed from entry to inGame @Entry::Ready")
        sender() ! true
      } else {
        log.error("Roster sync failed @Entry::Ready")
        sender() ! false
      }

    case Dump =>
      log.info("**** DUMPING **** @Entry::Dump")
      log.info(roster.toString)
  }

  def inGame: Receive = {

    case Proceed =>
      if(roster.size < 2) {
        log.warn("Not ready to start the session.. @InGame::Proceed")
      } else {
        val winner = getWinner
        if(winner.isEmpty) {
          broadcastAsync(allAliveRefs, Proceed)
        } else {
          winnerTeam = winner
          showWinner(winnerTeam.get)
          log.info("Game is over. Changing context from inGame -> gameOver")
          context.become(gameOver)
          context.parent ! GameOver
        }
        turn += 1
      }

    case ExecCommand(cmds) =>
      cmds.foreach(exec(_, roster, turn))

    case Dump =>
      log.info("**** DUMPING **** @InGame::Dump")
      log.info(roster.toString)
  }

  def gameOver: Receive = {

    case Proceed =>
      if(onlyOnceWinnerNotice.getAndAdd(1) == 0) {
        log.debug("Game is over..")
        if(winnerTeam.isEmpty) {
          log.info("***** Draw game.. ")
        }
      }

    case Dump =>
      log.info("**** DUMPING **** @InGame::Dump")
      log.info(roster.toString)
  }

  private def add(teamName: String, entName: String, status: Status, ref: ActorRef): Map[String, Team] = {
    val o = roster.get(teamName)
    if(o.isDefined) {
      val t = o.get
      t.join(entName, Profile(status, ref))
      roster += (teamName -> t)
    } else {
      val n = Team(teamName)
      n.join(entName, Profile(status, ref))
      roster += (teamName -> n)
    }
    roster
  }

  private def allRefs = {
    for {
      t <- roster.valuesIterator.toSeq
      m <- t.members
    } yield m._2.ref
  }

  private def allAliveRefs = {
    for {
      t <- roster.valuesIterator.toSeq
      m <- t.members if m._2.status.statusTypes.contains(StatusType.Active)
    } yield m._2.ref
  }

  private def broadcastAsync(refs: Seq[ActorRef], msg: Message) = refs.foreach(_ ! msg)

  private def broadcast[A](refs: Seq[ActorRef], msg: Message)
                          (implicit ec: ExecutionContext): Seq[A] = {
    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val rez = Future.traverse(refs) { ref =>
      log.debug(s"${ref.path} --- updating roster @func::broadcast")
      ref ? msg
    }
    val x = Await.result(rez, Duration("5s"))
    x.map(_.asInstanceOf[A])
  }

  private def getWinner: Option[String] = {
    // roster("Red").members.valuesIterator.map(_.status).foreach { x => log.debug(x.toString) }
    val rez = roster.filterNot(_._2.members.forall(_._2.status.statusTypes.contains(StatusType.Dying)))
    rez match {
      case x if rez.size == 1 => Some(rez.keysIterator.next)
      case _ => None
    }
  }

  private def showWinner(winnerTeam: String): Unit = {
    log.info(s"***** WINNER!!! Team - ${winnerTeam}")
    log.info(s"***** Team Members - {${roster(winnerTeam).members.map(_._1).mkString(",")}}")
  }

}
