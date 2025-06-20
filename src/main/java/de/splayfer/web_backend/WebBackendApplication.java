package de.splayfer.web_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class WebBackendApplication {

	public static String PATH = "";

	public static void main(String[] args) {
		MongoDBDatabase.connect();
		SpringApplication.run(WebBackendApplication.class, args);
	}
}
