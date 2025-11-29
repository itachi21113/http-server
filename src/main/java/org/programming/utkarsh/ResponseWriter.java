package org.programming.utkarsh;
import java.io.OutputStream;
import java.io.IOException;

public class ResponseWriter {
    private OutputStream output;

    public ResponseWriter(OutputStream output) {
        this.output = output;
    }

    public void sendSuccess(byte[] content, String contentType) throws IOException {
        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + content.length + "\r\n" +
                "\r\n";

        output.write(headers.getBytes());
        output.write(content);
        output.flush();
    }

    public void sendNotFound() throws IOException {
        String msg = "<h1>404 Not Found</h1>";
        String headers = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + msg.length() + "\r\n" +
                "\r\n";
        output.write(headers.getBytes());
        output.write(msg.getBytes());
        output.flush();
    }
}