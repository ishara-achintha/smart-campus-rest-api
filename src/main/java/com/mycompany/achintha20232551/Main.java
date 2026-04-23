package com.mycompany.achintha20232551;

import com.mycompany.achintha20232551.config.ApplicationConfig;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

public class Main {

    public static final String BASE_URI = "http://localhost:9090/api/v1/";

    public static HttpServer startServer() {
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new ApplicationConfig());
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        System.out.println("Smart Campus API started.");
        System.out.println("Base URI: " + BASE_URI);
        System.out.println("Press Enter to stop the server");
        System.in.read();
        server.shutdownNow();
    }
}
