package main.java;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        // 1. Create a server instance bound to port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 2. Link the "/" URL path to a specific request handler
        server.createContext("/", new RootHandler());

        // 3. Set the default multi-threaded executor
        server.setExecutor(null);

        // 4. Start the server
        System.out.println("Server started on http://localhost:8080/");
        server.start();
    }

    // This block handles incoming HTTP requests
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello, World! This is a simple Java server.";
            
            // Send HTTP Status 200 (OK) and define the response length
            exchange.sendResponseHeaders(200, response.length());
            
            // Write the text to the client's output stream
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            
            // Clean up and close the streams
            os.close();
        }
    }
}

