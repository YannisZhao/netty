package io.netty.example.demo.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

    private Executor workers = new ThreadPoolExecutor(1, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(16));

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (
                ServerSocket server = new ServerSocket(port)
        ) {
            System.out.println("Server started at port " + port);
            while (true) {
                Socket socket = server.accept();
                System.out.println(socket.getRemoteSocketAddress().toString() + " connected.");
                workers.execute(new RequestHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final class RequestHandler implements Runnable {

        private Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }

        private void handle() throws IOException {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
            ) {
                // request
                StringBuilder received = new StringBuilder();
                String line;
                while (!"#~#".equals(line = reader.readLine())) {
                    received.append(line);
                }

                // response
                writer.print("Response:");
                writer.println(received.toString());
                writer.flush();
            }

        }

        @Override
        public void run() {
            try {
                handle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server(9999).start();
    }

}
