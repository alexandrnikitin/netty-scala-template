import io.netty.channel.socket.SocketChannel
import io.netty.channel.{ChannelInitializer, ChannelPipeline}
import io.netty.handler.codec.http.{HttpRequestDecoder, HttpResponseEncoder}

class ServerInitializer extends ChannelInitializer[SocketChannel] {
  override def initChannel(s: SocketChannel): Unit = {
    val p: ChannelPipeline = s.pipeline
    p.addLast("encoder", new HttpResponseEncoder)
    p.addLast("decoder", new HttpRequestDecoder)
    p.addLast("handler", new ServerHandler())
  }
}
