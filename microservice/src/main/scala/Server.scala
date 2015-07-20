import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.epoll.{Epoll, EpollEventLoopGroup, EpollServerSocketChannel}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelOption, EventLoopGroup, ServerChannel}

class Server(context: MicroserviceContext) {

  private def doRun(group: EventLoopGroup, clazz: Class[_ <: ServerChannel]): Unit = {
    try {
      val inet: InetSocketAddress = new InetSocketAddress(context.endpoint.port)
      val bootstrap = new ServerBootstrap()
      bootstrap.group(group).channel(clazz).childHandler(new ServerInitializer())
      bootstrap.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
      bootstrap.bind(inet).sync.channel.closeFuture.sync
    } finally {
      group.shutdownGracefully.sync
    }
  }

  def run(): Unit ={
    if (Epoll.isAvailable) {
      doRun(new EpollEventLoopGroup, classOf[EpollServerSocketChannel])
    }
    else {
      doRun(new NioEventLoopGroup, classOf[NioServerSocketChannel])
    }
  }
}
