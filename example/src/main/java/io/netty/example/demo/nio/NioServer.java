package io.netty.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    private Selector selector;

    public NioServer(int port) throws IOException {
        init(port);
    }

    private void init(int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        System.out.println("Server started at port " + port);
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException {
        while (true) {
            int selectCount = selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socket = serverChannel.accept();
                    socket.configureBlocking(false);
                    socket.write(ByteBuffer.wrap("Welcome\t".getBytes()));
                    socket.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    StringBuilder request = new StringBuilder();
                    Charset charset = Charset.forName("UTF-8");
                    CharsetDecoder decoder = charset.newDecoder();
                    while (socketChannel.read(buf) != 0) {
                        buf.flip();
                        CharBuffer charBuffer = decoder.decode(buf);
                        while (charBuffer.hasRemaining()) {
                            request.append(charBuffer.get());
                        }
                        buf.clear();
                    }

                    socketChannel.write(ByteBuffer.wrap(("Response: " + request.toString()).getBytes()));
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new NioServer(8888).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
