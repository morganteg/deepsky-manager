package it.attsd.deepsky.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties
public class ConstellationRepositoryUnitTest {
	private Logger logger = LoggerFactory.getLogger(ConstellationRepositoryUnitTest.class);
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testAddConstellationWhenNotExists() throws RepositoryException {
		Constellation orionToSave = new Constellation("orion");
		Constellation orionSaved = constellationRepository.save(orionToSave);
		assertNotNull(orionSaved);
		assertThat(orionSaved.getId() > 0);
	}
	
//	@Test
//	public void testFindAll() {
//		List<Constellation> constellations = constellationRepository.findAll();
//		logger.info("constellations: " + constellations);
//		
//		assertThat(constellations.size()).isEqualTo(2);
//	}
//	
//	@Test
//	public void testFindById() {
//		Constellation orion = new Constellation(1, "orion");
//		when(constellationRepository.findById(1)).thenReturn(orion);
//		
//		assertThat(constellationRepository.findById(1)).isEqualTo(orion);
//		assertThat(constellationRepository.findById(2)).isNull();
//	}
	
}
