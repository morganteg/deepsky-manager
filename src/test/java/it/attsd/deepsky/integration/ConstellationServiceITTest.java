package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
public class ConstellationServiceITTest {
	private Logger logger = LoggerFactory.getLogger(ConstellationServiceITTest.class);
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	@Autowired
	private ConstellationService constellationService;

	String constellationName = "orion";
	
	@Before
	public void setup() {
		constellationRepository.emptyTable();
	}

	@Test
	public void testAAddConstellationWhenNotExists() throws RepositoryException {
		Constellation existingConstellation = constellationService.findByName(constellationName);
		assertNull(existingConstellation);

		Constellation constellationToSave = new Constellation(constellationName);
		Constellation constellationSaved = constellationService.save(constellationToSave);
		
		assertNotNull(constellationSaved);
		assertThat(constellationSaved.getId() == 1);
	}

//	@Test
//	public void testBFindAll() {
//		List<Constellation> constellations = constellationService.findAll();
//		logger.info("constellations: " + constellations);
//
//		assertThat(constellations.size()).isEqualTo(1);
//	}

//	@Test
//	public void testCFindById() throws RepositoryException {
//		Constellation constellation = constellationService.findById(1);
//		assertNotNull(constellation);
//	}

//	@Test
//	public void testDDeleteById() throws RepositoryException {
//		Constellation constellationBefore = constellationService.findById(1);
//		assertNotNull(constellationBefore);
//
//		constellationService.delete(1);
//
//		Constellation constellationAfter = constellationService.findById(1);
//		assertNull(constellationAfter);
//	}

}
