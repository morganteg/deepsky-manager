package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
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
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstellationRepositoryIT {
	private Logger logger = LoggerFactory.getLogger(ConstellationRepositoryIT.class);
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	String ORION = "orion";
	String LIBRA = "libra";
	
	@Before
	public void setup() {
		constellationRepository.emptyTable();
	}

	@Test
	public void testAAddConstellationWhenNotExists() throws RepositoryException {
		Constellation orionExisting = constellationRepository.findByName(ORION);
		assertNull(orionExisting);
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		assertThat(orionSaved.getId() > 0);
	}

	@Test
	public void testBFindAll() throws RepositoryException {
		Constellation orionExisting = constellationRepository.findByName(ORION);
		assertNull(orionExisting);
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		Constellation libraExisting = constellationRepository.findByName(LIBRA);
		assertNull(libraExisting);
		Constellation libraSaved = constellationRepository.save(new Constellation(LIBRA));
		assertNotNull(libraSaved);
		
		List<Constellation> constellations = constellationRepository.findAll();
		logger.info("constellations: " + constellations);

		assertThat(constellations.size()).isEqualTo(2);
	}

	@Test
	public void testCFindById() throws RepositoryException {
		Constellation orionExisting = constellationRepository.findByName(ORION);
		assertNull(orionExisting);
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		Constellation constellationFound = constellationRepository.findById(orionSaved.getId());
		assertNotNull(constellationFound);
	}

	@Test
	public void testDDeleteById() throws RepositoryException {
		Constellation orionExisting = constellationRepository.findByName(ORION);
		assertNull(orionExisting);
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		long orionId = orionSaved.getId();
		
		Constellation constellationBeforeDelete = constellationRepository.findById(orionId);
		assertNotNull(constellationBeforeDelete);

		constellationRepository.delete(orionId);

		Constellation constellationAfterDelete = constellationRepository.findById(orionId);
		assertNull(constellationAfterDelete);
	}

}
