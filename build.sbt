name := "CatShitHandle"

version := "1.0"

scalaVersion := "2.12.3"
libraryDependencies ++= Seq(
  //  "net.debasishg" %% "redisclient" % "3.4",
  "redis.clients" % "jedis" % "2.7.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.8",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.8"
)

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "ShitHandle",
  scalaVersion := "2.12.3",
  test in assembly := {}
)

lazy val app = (project in file("app")).
  settings(commonSettings: _*).
  settings(
    mainClass in assembly := Some("shitHandle")
  )