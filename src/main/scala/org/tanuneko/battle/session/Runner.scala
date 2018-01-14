package org.tanuneko.battle.session

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, PoisonPill, Props}
import org.tanuneko.battle.model._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import akka.pattern.ask
import akka.util.Timeout
import cats.data.EitherT
import com.typesafe.scalalogging.Logger
import org.tanuneko.battle.entity.Entity

import scala.util.Random
import scala.concurrent.duration._

/**
  * [TODO] introduce scalastyle
  * [TODO] unit testing
  * [TODO] log util
  * [TODO] add guard action
  * [TODO] hp no negative
  */

object Runner {

  lazy val log = Logger(getClass)
  val sys = ActorSystem("BattleSim")
  val runner = sys.actorOf(Props(new GameRunner(sys)), "GameRunner")
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  def main(args: Array[String]): Unit = {
    register("Blue", "Ryu", sampleEntity("Ryu")) match {
      case Left(e) => throw e
      case _ => log.debug("Registration done")
    }
    register("Red", "Sagat", sampleEntity("Sagat", 60, Jobs.Magician)) match {
      case Left(e) => throw e
      case _ => log.debug("Registration done")
    }
    Await.ready(runner ? Proceed, Duration("5s"))

    waitTillGameover

    runner ! PoisonPill
    val r = Await.result(sys.terminate, Duration.Inf)
    log.info(r.toString)
    log.info("Session has ended.")
  }

  def waitTillGameover: Unit = {
    var overFlag = false
    while(overFlag == false) {
      Thread.sleep(1000)
      overFlag = isOver
    }
  }

  def isOver: Boolean = {
    import sys.dispatcher
    val f = runner ? QueryStatus
    Await.result(f, Duration("5s")).asInstanceOf[String] == "over"
  }

  def register(team: String, name: String, status: Status): Either[Exception, Integer] = {
    val f = runner ? Entry(team, name, status)
    val result = Await.result(f, Duration("5s")).asInstanceOf[SessionEntryResponse]
    result match {
      case x if x.result => Right(x.numOfRegisteredEntities)
      case _ => Left(new Exception(s"user ${name}[team:${team}] registration failure"))
    }
  }

  def sampleEntity(name: String, hp: Int = 100, job: Jobs = Jobs.Fighter): Status = {
    Status(
    name = name,
    job = job,
    hp = hp,
    mp = 0,
    offense = 76,
    defense = 104,
    power = 20,
    agility = 10,
    inteligence = 10,
    luck = 5,
    exp = 0L,
    gold = 0L
    )
  }

}
