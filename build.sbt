name := "history"

version := "1.0"

lazy val `history` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.4", "2.11.0")

libraryDependencies ++= Seq( jdbc , anorm , cache , ws )

fork := true

parallelExecution in Test := false

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "compile"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.4" % "compile"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )