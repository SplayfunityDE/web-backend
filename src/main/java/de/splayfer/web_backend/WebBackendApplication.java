package de.splayfer.web_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class WebBackendApplication {

	public static String PATH = "";

	@Value("${MONGO_HOST}")
	private String test;

	public static void main(String[] args) {

		WebBackendApplication app = new WebBackendApplication();
		app.printTest();

//		if (envFile.exists())
//			dotenv = Dotenv.configure().directory(jarDir).load();
//		else
//			dotenv = Dotenv.configure().load();
//		if (dotenv.get("MEDIA_PATH").equals("user.dir"))
//			PATH = System.getProperty("user.dir");
//		else
//			PATH = dotenv.get("MEDIA_PATH");
//
//		SpringApplication.run(WebBackendApplication.class, args);
	}

	public void printTest() {
		System.out.println(test);
	}
}
