package org.tanuneko.battle.model

import akka.actor.ActorRef

case class Team(name: String, var members: Map[String, Profile] = Map.empty) {

  def join(n: String, prof: Profile):Unit = {
    members = members + (n -> prof)
  }

  def remove(n: String):Unit = {
    members = members.filterNot(_._1 == n)
  }
}

case class Profile(var status: Status, ref: ActorRef)

