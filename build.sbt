ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val http4sVersion = "0.23.16"
val catsVersion = "3.3.14"
val circeVersion = "0.14.2"

lazy val root = (project in file("."))
  .settings(
    name := "test",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.typelevel" %% "cats-effect" % catsVersion,
      "ch.qos.logback" % "logback-classic" % "1.4.0",
      "org.typelevel" %% "log4cats-slf4j"   % "2.5.0",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic"% circeVersion,
      "io.circe" %% "circe-parser"% circeVersion
    ),
  )
