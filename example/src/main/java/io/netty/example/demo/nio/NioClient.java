package io.netty.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class NioClient {

    private Selector selector;

    public NioClient(String host, int port) {
        try {
            init(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(String host, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        selector = Selector.open();
        channel.connect(new InetSocketAddress(host, port));
        channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
    }

    public void start() {
        while (true) {
            try {
                int select = selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isConnectable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                        }
                        channel.configureBlocking(false);
                        channel.write(ByteBuffer.wrap(("Hi, i'm client "
                                + Thread.currentThread().getName()).getBytes()));
                    } else if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buf = ByteBuffer.allocate(8);
                        StringBuilder response = new StringBuilder();
                        Charset charset = Charset.forName("UTF-8");
                        CharsetDecoder decoder = charset.newDecoder();
                        while (channel.read(buf) != 0) {
                            buf.flip();
                            CharBuffer charBuffer = decoder.decode(buf);
                            while (charBuffer.hasRemaining()) {
                                response.append(charBuffer.get());
                            }
                            buf.clear();
                        }
                        System.out.println(response.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(() -> new NioClient("127.0.0.1", 8888).start(), "Client A").start();
        new Thread(() -> new NioClient("127.0.0.1", 8888).start(), "Client B").start();
        System.in.read();
    }

}