name := "wiki_analyzer"

version := "0.1"

scalaVersion := "2.13.3"

idePackagePrefix := Some("ucu.functional.programing")


lazy val akkaHttpVersion = "10.2.2"
lazy val akkaVersion    = "2.6.10"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "ucu.functional.programing",
      scalaVersion    := "2.13.3"
    )),
    name := "wiki_analyzer",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",


      "org.scala-lang.modules" %% "scala-async" % "0.10.0" ,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided ,

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  )
