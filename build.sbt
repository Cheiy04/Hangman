ThisBuild / scalaVersion := "3.5.2"
ThisBuild / scalacOptions ++= Seq("-encoding", "UTF-8")

lazy val root = (project in file("."))
  .settings(
    name := "Hangman",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % Test
  )
