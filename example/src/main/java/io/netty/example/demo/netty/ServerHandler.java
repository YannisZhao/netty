package io.netty.example.demo.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        System.out.println(msg);

        /**
         * write:
         * AbstractChannelHandlerContext#invokeWriteAndFlush  invokeWrite0
         * MessageToMessageEncoder#write
         * StringEncoder#encode
         * AbstractChannelHandlerContext#write
         * LoggingHandler#write
         * AbstractChannelHandlerContext#write
         * HeadContext#write
         * AbstractChannel.AbstractUnsafe#write
         * ChannelOutboundBuffer#addMessage
         *
         *
         * flush:
         * AbstractChannelHandlerContext#invokeFlush0
         * ChannelOutboundHandlerAdapter#flush
         * AbstractChannelHandlerContext#flush
         *
         * AbstractChannelHandlerContext#invokeFlush  invokeFlush0
         * LoggingHandler#flush
         * AbstractChannelHandlerContext#flush
         * AbstractChannel.AbstractUnsafe#flush
         * ChannelOutboundBuffer#addFlush
         * AbstractNioUnsafe#flush0
         * AbstractUnsafe#flush0  doWrite
         * NioSocketChannel#doWrite
         * SocketChannel#write
         */
        System.out.println(msg);
        ctx.writeAndFlush("Hi, now is " + new Date());
    }
}
