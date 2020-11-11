package io.netty.example.demo.bio;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (
                Socket socket = new Socket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {

            writer.println("Hello, I am " + Thread.currentThread().getName());
            writer.println("#~#");
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(Thread.currentThread().getName() + " received: " + line);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(() -> new Client("127.0.0.1", 9999).start(), "Client A").start();
        new Thread(() -> new Client("127.0.0.1", 9999).start(), "Client B").start();
        System.in.read();
    }

}
