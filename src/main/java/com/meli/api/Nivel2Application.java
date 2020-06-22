package com.meli.api;

import static com.meli.api.rest.Constant.APP_PORT;
import static com.meli.api.rest.Constant.DEFULT_BACKLOG;
import static com.meli.api.rest.Constant.GET;
import static com.meli.api.rest.Constant.HTTP_200;
import static com.meli.api.rest.Constant.HTTP_NOT_ALLOWED;

import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sun.net.httpserver.HttpServer;




@SpringBootApplication
public class Nivel2Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Nivel2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("[MAIN] Nivel2Application already started. Press Ctrl + C to shutdown.");
        HttpServer server = HttpServer.create(new InetSocketAddress(APP_PORT), DEFULT_BACKLOG);
        server.createContext("/api/hello", (exchange -> {

            if (GET.equals(exchange.getRequestMethod())) {
                String respText = "Hello!";
                exchange.sendResponseHeaders(HTTP_200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(HTTP_NOT_ALLOWED, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.setExecutor(null); // creates a default executor
        server.start();
	}
	
}
