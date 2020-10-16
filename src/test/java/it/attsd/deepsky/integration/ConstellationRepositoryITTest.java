package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.GenericRepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstellationRepositoryITTest {
	private Logger logger = LoggerFactory.getLogger(ConstellationRepositoryITTest.class);
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	String ORION = "orion";
	String LIBRA = "libra";
	
	@Before
	public void setup() {
		constellationRepository.emptyTable();
	}

	@Test
	public void testBFindAll() throws GenericRepositoryException, ConstellationAlreadyExistsException {
//		Constellation orionExisting = constellationRepository.findByName(ORION);
//		assertNull(orionExisting);
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
	public void testCFindByIdWhenIsPresent() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		Constellation constellationFound = constellationRepository.findById(orionSaved.getId());
		assertNotNull(constellationFound);
	}
	
	@Test
	public void testFindByIdWhenIsNotPresent() throws GenericRepositoryException {
		Constellation constellationFound = constellationRepository.findById(1L);
		assertNull(constellationFound);
	}
	
	@Test
	public void testFindByNameWhenIsPresent() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		Constellation constellationFound = constellationRepository.findByName(ORION);
		assertNotNull(constellationFound);
	}
	
	@Test
	public void testFindByNameWhenIsNotPresent() throws GenericRepositoryException {
		Constellation constellationFound = constellationRepository.findByName(ORION);
		assertNull(constellationFound);
	}
	
	@Test
	public void testAddConstellationWhenNotExists() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orionExisting = constellationRepository.findByName(ORION);
		assertNull(orionExisting);
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		assertThat(orionSaved.getId() > 0);
	}
	
	@Test
	public void testUpdateConstellation() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation constellationExisting = constellationRepository.findById(1L);
		assertNull(constellationExisting);
		Constellation constellationSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(constellationSaved);
		
		String nameUpdated = "orion changed";
		constellationSaved.setName(nameUpdated);
		constellationRepository.update(constellationSaved);
		
		Constellation constellationUpdated = constellationRepository.findById(constellationSaved.getId());
		assertNotNull(constellationUpdated);
		assertThat(constellationUpdated.getName().equalsIgnoreCase(nameUpdated));
	}
	
	@Test
	public void testDDeleteById() throws GenericRepositoryException, ConstellationAlreadyExistsException {
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
