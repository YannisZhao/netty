package io.netty.example.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        RequestEncoder requestEncoder = new RequestEncoder();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LengthFieldBasedFrameDecoder(50000, 0, 4));
                            p.addLast(new ProtocolDecoder());
                            p.addLast(new ProtocolEncoder());
                            p.addLast(new ResponseDecoder());
                            p.addLast(requestEncoder);
                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast(new ClientHandler());
                        }
                    });

            ChannelFuture f = b.connect("127.0.0.1", 8080).sync();

            Request request = new Request();
            request.setId(ThreadLocalRandom.current().nextLong());
            request.setMessage("hello");
            f.channel().writeAndFlush(request);

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
