
lazy val settings = Seq (
  name := "explorer",
  version := "0.1",
  scalaVersion := "2.12.10"
)

autoCompilerPlugins := true

resolvers ++= Seq("Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "SonaType" at "https://oss.sonatype.org/content/groups/public",
  "Typesafe maven releases" at "https://repo.typesafe.com/typesafe/maven-releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

lazy val doobieVersion = "0.8.4"
lazy val http4sVersion = "0.20.12"

libraryDependencies ++= Seq(
  "io.estatico" %% "newtype" % "0.4.3",
  "eu.timepit" %% "refined"  % "0.9.10",
  "org.typelevel" %% "cats-effect" % "2.0.0",
  "org.flywaydb" % "flyway-sbt" % "3.0",
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2"   % doobieVersion
)

addCompilerPlugin("org.typelevel"   % "kind-projector" % "0.11.0" cross CrossVersion.full)
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

scalacOptions ++= Seq(
  "-Ypartial-unification",
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds")

val explorer = (project in file(".")).settings(settings: _*)