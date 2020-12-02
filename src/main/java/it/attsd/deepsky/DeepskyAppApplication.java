package it.attsd.deepsky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableConfigurationProperties
@SpringBootApplication
public class DeepskyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeepskyAppApplication.class, args);
	}

}
