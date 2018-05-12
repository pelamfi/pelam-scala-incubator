// Basic facts
name := "pelam-scala-incubator"

organization := "fi.pelam"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.11", "2.12.4")

scalacOptions ++= Seq()

javacOptions ++= Seq(
  "-source", "1.8",
  "-target", "1.8"
)

scalacOptions ++= (
  if (scalaVersion.value.startsWith("2.12")) {
    Seq.empty
  } else {
    // Explicitly target 1.6 for scala < 2.12
    Seq("-target:jvm-1.8")
  }
)


libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.beachape" %% "enumeratum" % "1.5.12",
  "com.typesafe.akka" %% "akka-actor" % "2.5.8",
  "com.google.code.findbugs" % "jsr305" % "3.0.0",
  "com.googlecode.java-diff-utils" % "diffutils" % "1.3.0",
  "com.google.guava" % "guava" % "23.0",
  "org.clapper" %% "grizzled-slf4j" % "1.3.2",
  "ch.qos.logback" % "logback-classic" % "1.1.2" % Test,
  "org.scalatest" %% "scalatest" % "3.0.0" % Test,
  "junit" % "junit" % "4.12" % Test,
  // https://stackoverflow.com/a/28051194/1148030
  "com.novocode" % "junit-interface" % "0.11" % Test exclude("junit", "junit-dep")
)

val printTests = taskKey[Unit]("something")

printTests := {
  val tests = (definedTests in Test).value
  println("Tests!")
  tests map { t =>
    println(t.name)
  }
}

/*
git.remoteRepo := "git@github.com:pelamfi/pelam-scala-incubator.git"
*/