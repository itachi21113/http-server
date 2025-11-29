
package org.programming.utkarsh;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHTTPServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server started! Listening on port: " + port);

        while (true) {
            // 1. Accept Connection
            Socket client = server.accept();
            System.out.println("New client connected: " + client.getInetAddress());

            // 2. Spawn Thread (The Waiter)
            // We pass the "client" socket to the handler so it knows who to talk to
            ClientHandler handler = new ClientHandler(client);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }
}