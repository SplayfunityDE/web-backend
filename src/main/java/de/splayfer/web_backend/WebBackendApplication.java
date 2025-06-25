package de.splayfer.web_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebBackendApplication {

	public static String PATH = "";

	public static void main(String[] args) {
		MongoDBDatabase.connect();
		SpringApplication.run(WebBackendApplication.class, args);
	}
}
