import sbt._
import sbt.Keys._

object Dependencies {

  object Netty {
    private val version = "4.0.27.Final"
    val all = "io.netty" % "netty-all" % version
    val epoll = "io.netty" % "netty-transport-native-epoll" % version
  }

  private val config = "com.typesafe" % "config" % "1.3.0"

  val microservice = dependencies(Netty.all, Netty.epoll, config)

  private def dependencies(modules: ModuleID*): Seq[Setting[_]] = Seq(libraryDependencies ++= modules)
}
