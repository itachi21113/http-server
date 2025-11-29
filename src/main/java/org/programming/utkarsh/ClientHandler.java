package org.programming.utkarsh;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ClientHandler implements Runnable {
    private Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // 1. Setup Input/Output
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            ResponseWriter writer = new ResponseWriter(client.getOutputStream());

            // 2. Read Request
            String requestLine = reader.readLine();
            if (requestLine == null) return;

            // USE OUR NEW CLASS: Parse the raw string into an object
            HTTPRequest request = new HTTPRequest(requestLine);
            System.out.println("Requested: " + request.getPath());

            // 3. Handle File Logic
            String path = request.getPath();
            if (path.equals("/")) path = "/index.html";

            File file = new File("." + path);

            if (file.exists() && !file.isDirectory()) {
                // Success: Delegate to ResponseWriter
                byte[] content = Files.readAllBytes(file.toPath());
                String type = guessContentType(path);

                writer.sendSuccess(content, type);

            } else {
                // Failure: Delegate to ResponseWriter
                writer.sendNotFound();
            }

            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".jpg")) return "image/jpg";
        return "text/plain";
    }
}