scalaVersion := "3.9.0"

lazy val root = rootProject
  .settings(
    name := "Hangman",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    )
  )
