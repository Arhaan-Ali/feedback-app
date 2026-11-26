package com.example.feedback;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

public class FeedbackApplication {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/", exchange -> {
            byte[] page;
            try (InputStream input = FeedbackApplication.class.getResourceAsStream("/index.html")) {
                if (input == null) {
                    exchange.sendResponseHeaders(404, -1);
                    exchange.close();
                    return;
                }
                page = input.readAllBytes();
            }
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, page.length);
            exchange.getResponseBody().write(page);
            exchange.close();
        });
        server.start();
        System.out.println("Feedback application running at http://localhost:" + port);
    }
}
