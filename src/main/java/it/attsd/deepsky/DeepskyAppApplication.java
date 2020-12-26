package it.attsd.deepsky;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeepskyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeepskyAppApplication.class, args);
        Logger logger = LoggerFactory.getLogger(DeepskyAppApplication.class);
        logger.info("Application started!");
	}

}
