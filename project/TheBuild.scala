import sbt._

object TheBuild extends Build {
  lazy val root = Project("root", file("."))
      .dependsOn(microservice)
      .settings(Settings.root: _*)

  lazy val microservice = Project("microservice", file("microservice"))
      .configs(Configs.all: _*)
      .settings(Settings.microservice: _*)
}
