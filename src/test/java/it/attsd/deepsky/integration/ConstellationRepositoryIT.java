package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstellationRepositoryIT {
	private Logger logger = LoggerFactory.getLogger(ConstellationRepositoryIT.class);

	@Autowired
	private ConstellationRepository constellationRepository;

	private long constellationId;

	@Test
	@Order(1)
	public void testAAddConstellationWhenNotExists() throws RepositoryException {
		constellationId = 1;
		String constellationName = "orion";

		Constellation existingConstellation = constellationRepository.findByName(constellationName);
		assertNull(existingConstellation);

		Constellation constellationToSave = new Constellation(constellationName);
		Constellation constellationSaved = constellationRepository.save(constellationToSave);
		
		assertNotNull(constellationSaved);
		assertThat(constellationSaved.getId() > 0);

		constellationId = constellationSaved.getId();
		
	}

	@Test
	@Order(2)
	public void testBFindAll() {
		List<Constellation> constellations = constellationRepository.findAll();
		logger.info("constellations: " + constellations);

		assertThat(constellations.size()).isEqualTo(1);
	}

	@Test
	@Order(3)
	public void testCFindById() throws RepositoryException {
		Constellation constellation = constellationRepository.findById(constellationId);
		assertNotNull(constellation);
	}

	@Test
	@Order(4)
	public void testDDeleteById() throws RepositoryException {
		Constellation constellationBefore = constellationRepository.findById(constellationId);
		assertNotNull(constellationBefore);

		constellationRepository.remove(constellationId);

		Constellation constellationAfter = constellationRepository.findById(constellationId);
		assertNull(constellationAfter);
	}

}
