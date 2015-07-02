import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.{ChannelFutureListener, ChannelHandlerContext, SimpleChannelInboundHandler}
import io.netty.handler.codec.http._
import io.netty.util.CharsetUtil

class ServerHandler extends SimpleChannelInboundHandler[HttpRequest] {

  import ServerHandler._

  override def channelRead0(ctx: ChannelHandlerContext, r: HttpRequest): Unit =
    writeResponse(ctx, r, okContentBuffer.duplicate, typePlain, okContentLength)

  override def exceptionCaught(ctx: ChannelHandlerContext, e: Throwable): Unit =
    ctx.close()

}

object ServerHandler {

  private val contentTypeEntity = HttpHeaders.newEntity(HttpHeaders.Names.CONTENT_TYPE)
  private val contentLengthEntity = HttpHeaders.newEntity(HttpHeaders.Names.CONTENT_LENGTH)

  private val okContentBuffer = Unpooled.unreleasableBuffer(Unpooled.directBuffer.writeBytes("OK".getBytes(CharsetUtil.UTF_8)))
  private val okContentLength = HttpHeaders.newEntity(String.valueOf(okContentBuffer.readableBytes))
  private val typePlain = HttpHeaders.newEntity("text/plain; charset=UTF-8")

  private def writeResponse(ctx: ChannelHandlerContext, request: HttpRequest, buf: ByteBuf, contentType: CharSequence, contentLength: CharSequence) {
    val keepAlive = HttpHeaders.isKeepAlive(request)
    val response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf, false)
    val headers = response.headers
    headers.set(contentTypeEntity, contentType)
    headers.set(contentLengthEntity, contentLength)

    if (!keepAlive) {
      ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }
    else {
      ctx.writeAndFlush(response, ctx.voidPromise)
    }
  }

}
