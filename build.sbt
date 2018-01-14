
name := "BattleSim"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += "AWS DynamoDB" at "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"

libraryDependencies += "org.scalactic" % "scalactic_2.12" % "3.0.3"
libraryDependencies += "org.scalaz" % "scalaz-core_2.12" % "7.3.0-M15"
libraryDependencies += "org.scalaz" % "scalaz-effect_2.12" % "7.3.0-M15"
libraryDependencies += "org.scalaz" % "scalaz-scalacheck-binding_2.12" % "7.3.0-M15"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.4"
libraryDependencies += "com.google.inject" % "guice" % "4.1.0"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.0-MF"
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.192"
libraryDependencies += "com.gu" %% "scanamo" % "0.9.5"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.8"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.3" % "test"
