ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "AkkaTemplate",
    idePackagePrefix := Some("com.knoldus")
  )



libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-http" % "10.5.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.5.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.8.0",
  "com.typesafe.akka" %% "akka-stream" % "2.8.0",
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.5.4",
  "com.github.pureconfig" %% "pureconfig" % "0.17.4",
  "com.github.tminglei" %% "slick-pg" % "0.21.1",
  "com.github.tminglei" %% "slick-pg_circe-json" % "0.21.1",
  "io.circe" %% "circe-core" % "0.14.5",
  "io.circe" %% "circe-generic" % "0.14.5",
  "io.circe" %% "circe-parser" % "0.14.5",
  "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",
  "net.liftweb" %% "lift-json" % "3.5.0",
  "org.flywaydb" % "flyway-core" % "3.2.1"
)

