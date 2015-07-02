import sbt.Keys._
import sbt._
import BuildKeys._

object Testing {

  import Configs._

  private lazy val testSettings = Seq(
    fork in Test := false,
    parallelExecution in Test := true
  )

  private lazy val itSettings = inConfig(IntegrationTest)(Defaults.testSettings) ++ Seq(
    fork in IntegrationTest := false,
    parallelExecution in IntegrationTest := true,
    scalaSource in IntegrationTest := baseDirectory.value / "src/it/scala"
  )

  private lazy val e2eSettings = inConfig(EndToEndTest)(Defaults.testSettings) ++ Seq(
    fork in EndToEndTest := false,
    parallelExecution in EndToEndTest := true,
    scalaSource in EndToEndTest := baseDirectory.value / "src/e2e/scala"
  )

  lazy val settings = testSettings ++ itSettings ++ e2eSettings ++ Seq(
    testAll := (),
    testAll <<= testAll.dependsOn(test in EndToEndTest),
    testAll <<= testAll.dependsOn(test in IntegrationTest),
    testAll <<= testAll.dependsOn(test in Test)
  )
}
