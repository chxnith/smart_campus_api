package edu.university.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public final class Main {

    private static final URI BASE_URI = URI.create("http://0.0.0.0:8080/api/v1/");

    private Main() {
    }

    public static HttpServer startServer() {
        ResourceConfig config = ResourceConfig.forApplication(new SmartCampusApplication());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, config);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        System.out.println("Smart Campus API is running on http://localhost:8080/api/v1");
        System.out.println("Press Enter to stop...");
        System.in.read();
        server.shutdownNow();
    }
}
