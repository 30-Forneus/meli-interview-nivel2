package com.meli.api;

import static com.meli.api.rest.Constant.APP_PORT;
import static com.meli.api.rest.Constant.DEFULT_BACKLOG;
import static com.meli.api.rest.Constant.GET;
import static com.meli.api.rest.Constant.HTTP_200;
import static com.meli.api.rest.Constant.HTTP_NOT_ALLOWED;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.meli.api.rest.Constant;
import com.meli.api.rest.Parser;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

@SpringBootApplication
public class Nivel2Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Nivel2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//System.out.println("[MAIN] Nivel2Application already started. Press Ctrl + C to shutdown.");
        HttpServer server = HttpServer.create(new InetSocketAddress(APP_PORT), DEFULT_BACKLOG);
        HttpContext context = server.createContext("/mutant/", (exchange -> {

			if (GET.equals(exchange.getRequestMethod())) {
				Map<String, List<String>> params = Parser.INSTANCE.splitQuery(exchange.getRequestURI().getRawQuery());
				String noNameText = "Anonymous";
				String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);
				String respText = String.format("Hello %s!", name);
				exchange.sendResponseHeaders(HTTP_200, respText.getBytes().length);
				OutputStream output = exchange.getResponseBody();
				output.write(respText.getBytes());
				output.flush();
			}  
			else if (Constant.POST.equals(exchange.getRequestMethod())) {
				
			}  
			else {
				exchange.sendResponseHeaders(HTTP_NOT_ALLOWED, -1);// 405 Method Not Allowed
			}
			exchange.close();
            
        }));
        
        context.setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("admin") && pwd.equals("admin");
            }
        });

        server.setExecutor(null); // creates a default executor
        server.start();
	}
	
}
