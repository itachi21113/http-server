
package org.programming.utkarsh;
public class HTTPRequest {
    private String method;
    private String path;

    // We pass the raw request line (e.g., "GET /index.html HTTP/1.1")
    public HTTPRequest(String requestLine) {
        String[] parts = requestLine.split(" ");
        this.method = parts[0]; // GET
        this.path = parts[1];   // /index.html
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
}