package org.tanuneko.battle.session

class LegacyRunner {

  /*
 def main(args: Array[String]): Unit = {
   implicit val timeout = Timeout(5, TimeUnit.SECONDS)
   val sys = ActorSystem("BattleSim")
   val f1 = sys.actorOf(Props(Entity("Ryu")), "Ryu")
   val f2 = sys.actorOf(Props(Entity("Sagat")), "Sagat")
   val b = sys.actorOf(Props[BattleSession])
   val rez = b ? Join("Blue", f1, sampleEntity("Ryu"))
   val rez2 = b ? Join("Red", f2, sampleEntity("Sagat", 60, Jobs.Magician))
   val rezB1 = Await.result(rez, Duration("3s")).asInstanceOf[Boolean]
   val rezB2 = Await.result(rez, Duration("5s")).asInstanceOf[Boolean]
   val ready = b ? Ready
   val readyFlag = Await.result(ready, Duration("5s")).asInstanceOf[Boolean]

   if(readyFlag && rezB1 && rezB2) {
     import sys.dispatcher
     val scheduler = sys.scheduler.schedule(0 seconds, 1 seconds, b, Proceed)

     //b ! Dump
     Thread.sleep(60000)
     if(scheduler.cancel) {
       println("Scheduler has canceled. Sending poison pills to all actors..")
       f1 ! PoisonPill
       f2 ! PoisonPill
       b ! PoisonPill
       println("All actors has taken the poison pills.")
     } else {
       println("Scheduler cancellation has failed!!!")
     }
   } else {
     println("Initial registration has failed.")
   }

   Await.ready(sys.terminate, Duration.Inf)
   println("Session has ended.")
 }
 */

}
