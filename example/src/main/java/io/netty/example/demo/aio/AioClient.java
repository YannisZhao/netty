package io.netty.example.demo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AioClient {

    private AsynchronousSocketChannel clientChannel;

    private volatile boolean isConnected = false;

    public AioClient(String host, int port) {
        try {
            init(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(String host, int port) throws IOException {
        clientChannel = AsynchronousSocketChannel.open();
        clientChannel.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                isConnected = true;
                System.out.println(String.format("Connected to %s:%d", host, port));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }

    public void start() {
        for (; ; ) {
            if (isConnected) {
                break;
            }
        }
        // greeting
        clientChannel.write(ByteBuffer.wrap(("Hi, this is " + Thread.currentThread().getName()).getBytes()),
                Thread.currentThread().getName(),
                new CompletionHandler<Integer, String>() {
                    @Override
                    public void completed(Integer result, String attachment) {
                        System.out.println(String.format("[%s] Request sent", attachment));
                    }

                    @Override
                    public void failed(Throwable exc, String attachment) {
                        exc.printStackTrace();
                    }
                });

        ByteBuffer response = ByteBuffer.allocate(1024);
        clientChannel.read(response, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                response.flip();
                Charset charset = StandardCharsets.UTF_8;
                CharBuffer plainResponse = charset.decode(response);
                StringBuilder ret = new StringBuilder();
                while (plainResponse.hasRemaining()) {
                    ret.append(plainResponse.get());
                }

                System.out.println(ret.toString());
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        new Thread(() -> new AioClient("127.0.0.1", 8080).start(), "Client A").start();
        new Thread(() -> new AioClient("127.0.0.1", 8080).start(), "Client B").start();
        System.in.read();
    }

}
