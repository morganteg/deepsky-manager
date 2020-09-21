package it.attsd.deepsky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;

@EnableConfigurationProperties
@SpringBootApplication
//(scanBasePackages = {
//        "it.attsd"
//})
@EnableJpaRepositories("it.attsd.deepsky.model")
//@EntityScan(basePackages = {
//        "it.attsd",
//})
//@EnableJpaRepositories
public class DeepskyAppApplication {
//	@Autowired
//	private static ConstellationRepository constellationRepository;

	public static void main(String[] args) {
		SpringApplication.run(DeepskyAppApplication.class, args);
		
//		Constellation orionToSave = new Constellation("orion");
//		try {
//			Constellation orionSaved = constellationRepository.save(orionToSave);
//		} catch (RepositoryException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
