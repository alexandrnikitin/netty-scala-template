import com.typesafe.config.{Config, ConfigFactory}

abstract class Context(val config: Config) {
  def this() {
    this {
      Option(System.getenv().get("ENV")) map { e =>
        ConfigFactory.load(s"application.$e")
      } getOrElse {
        ConfigFactory.load()
      }
    }
  }
}

class MicroserviceContext extends Context {
  val endpoint = new EndpointSettings(config)
}

class EndpointSettings(config: Config) {
  config.checkValid(ConfigFactory.defaultReference(), "endpoint")

  val port = config.getInt("endpoint.port")
}