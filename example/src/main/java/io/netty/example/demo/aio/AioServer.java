package io.netty.example.demo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class AioServer {

    public AioServer(int port) throws IOException {
        init(port);
    }

    private void init(int port) throws IOException {
        AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel
                .open()
                .bind(new InetSocketAddress(port));

        System.out.println("Server started at port " + port);

        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Void attachment) {
                serverChannel.accept(null, this);
                handle(result);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });

        while (true) {

        }
    }

    private void handle(AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                buffer.flip();
                StringBuilder req = new StringBuilder();
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                while (charBuffer.hasRemaining()) {
                    req.append(charBuffer.get());
                }
                channel.write(ByteBuffer.wrap(("Response: " + req.toString()).getBytes()), null,
                        new CompletionHandler<Integer, Object>() {
                            @Override
                            public void completed(Integer result, Object attachment) {
                                try {
                                    System.out.println(channel.getRemoteAddress().toString() + " processed");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, Object attachment) {
                                exc.printStackTrace();
                            }
                        });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        new AioServer(8080);
    }
}
