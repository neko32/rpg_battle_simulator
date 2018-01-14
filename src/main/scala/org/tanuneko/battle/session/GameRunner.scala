package org.tanuneko.battle.session

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.util.Timeout
import org.tanuneko.battle.entity.Entity
import org.tanuneko.battle.model._
import akka.pattern.ask
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameRunner(sys: ActorSystem) extends Actor {

  import sys.dispatcher
  lazy val log = Logger(getClass)
  override def receive = entry
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)
  lazy val session: ActorRef = context.actorOf(Props[BattleSession])
  lazy val sessionScheduler = sys.scheduler.schedule(0 seconds, 1 seconds, session, Proceed)
  var entities: List[ActorRef] = List.empty[ActorRef]
  val entryCounter = new AtomicInteger(0)

  def entry: Receive = {

    case Entry(teamName, name, status) =>
      val newJoiner = context.actorOf(Props(Entity(name)), name)
      entities = newJoiner :: entities
      val rezF = session ? Join(teamName, newJoiner, status)
      val rez = Await.result(rezF, Duration("3s")).asInstanceOf[Boolean]
      if(rez) {
        entryCounter.incrementAndGet()
      } else {
        log.warn("Failed to register a new joiner")
      }
      sender() ! SessionEntryResponse(rez, entryCounter.get)

    case Proceed =>
      if(entryCounter.get() >= 2) {
        log.info(s"Confirmed entry counter is enough(${entryCounter.get}) @Entry::Proceed")
        if(Await.result(session ? Ready, Duration("5s")).asInstanceOf[Boolean]) {
          sessionScheduler
          context.become(running)
          sender() ! true
        }
      } else {
        log.warn("To start session, at least 2 entities should be registered")
        sender() ! false
      }

    case QueryStatus => sender() ! "entry"
  }

  def running: Receive = {
    case GameOver =>
      if(!sessionScheduler.cancel) {
        log.error("Session scheduler cancel has failed!")
      }
      session ! PoisonPill
      entities.foreach(_ ! PoisonPill)
      context.become(over)

    case QueryStatus => sender() ! "running"
  }

  def over: Receive = {
    case Dump =>
      log.info("Game is over.")

    case QueryStatus => sender() ! "over"
  }
}
