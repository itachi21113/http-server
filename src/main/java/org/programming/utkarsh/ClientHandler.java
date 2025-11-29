
package org.programming.utkarsh;
import java.io.*; // Import everything for File handling
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
            // 1. Prepare Input/Output
            InputStream input = client.getInputStream();
            OutputStream output = client.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // 2. Read the Request Line
            String requestLine = reader.readLine();
            if (requestLine == null) return; // Handle empty requests

            System.out.println("Thread " + Thread.currentThread().getId() + " requesting: " + requestLine);

            // 3. Parse the Request to find the FILE NAME
            // Request line looks like: "GET /index.html HTTP/1.1"
            // We split by " " (space) to get the middle part
            String[] parts = requestLine.split(" ");
            String path = parts[1];

            // Default to index.html if they ask for "/"
            if (path.equals("/")) {
                path = "/index.html";
            }

            // 4. Locate the file on your Hard Drive
            // "." means "Current Directory"
            File file = new File("." + path);

            // 5. Check if file exists and send response
            if (file.exists() && !file.isDirectory()) {
                // --- 200 OK (File Found) ---

                // Read file bytes
                byte[] fileBytes = Files.readAllBytes(file.toPath());

                // Determine Content-Type (MIME Type)
                String contentType = guessContentType(path);

                // Send Headers
                String headers = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + fileBytes.length + "\r\n" +
                        "\r\n"; // End of headers

                output.write(headers.getBytes());
                // Send the actual file data
                output.write(fileBytes);

                System.out.println("Served: " + path);

            } else {
                // --- 404 Not Found ---
                String notFoundMsg = "<h1>404 Not Found</h1><p>The file " + path + " does not exist.</p>";
                String headers = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + notFoundMsg.length() + "\r\n" +
                        "\r\n";

                output.write(headers.getBytes());
                output.write(notFoundMsg.getBytes());
                System.out.println("404 Error: " + path);
            }

            output.flush();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to tell the browser what kind of file this is
    private String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }
}