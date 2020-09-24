package it.attsd.deepsky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
