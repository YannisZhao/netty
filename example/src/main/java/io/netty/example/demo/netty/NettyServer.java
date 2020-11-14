package io.netty.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.List;

public class NettyServer {

    private static ServerHandler serverHandler = new ServerHandler();

    public static void main(String[] args) throws InterruptedException {

        ResponseEncoder responseEncoder = new ResponseEncoder();

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler("logger", LogLevel.DEBUG))
                    .option(ChannelOption.SO_BACKLOG, 512)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler("logger", LogLevel.DEBUG));
                            p.addLast(new LengthFieldBasedFrameDecoder(50000, 0, 4));
                            p.addLast(new ProtocolDecoder());
                            p.addLast(new ProtocolEncoder());
                            p.addLast(new RequestDecoder());
                            p.addLast(responseEncoder);
                            p.addLast(serverHandler);
                        }
                    });

            ChannelFuture f = b.bind(8080).sync();

            f.channel().closeFuture().sync();

        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
