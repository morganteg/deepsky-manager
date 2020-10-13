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

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectTypeRepositoryITTest {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectTypeRepositoryITTest.class);
	
	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;
	
	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;
	
	String GALAXY = "galaxy";
	String NEBULA = "nebula";
	
	@Before
	public void setup() {
		deepSkyObjectRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
	}

	@Test
	public void testFindAll() throws RepositoryException {
		DeepSkyObjectType galaxyExisting = deepSkyObjectTypeRepository.findByType(GALAXY);
		assertNull(galaxyExisting);
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);
		
		DeepSkyObjectType nebulaExisting = deepSkyObjectTypeRepository.findByType(NEBULA);
		assertNull(nebulaExisting);
		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);
		
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeRepository.findAll();
		logger.info("deepSkyObjectTypes: " + deepSkyObjectTypes);

		assertThat(deepSkyObjectTypes.size()).isEqualTo(2);
	}

	@Test
	public void testFindByIdWhenIsPresent() throws RepositoryException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);
		
		DeepSkyObjectType typeFound = deepSkyObjectTypeRepository.findById(galaxySaved.getId());
		assertNotNull(typeFound);
	}
	
	@Test
	public void testFindByIdWhenIsNotPresent() throws RepositoryException {
		DeepSkyObjectType typeFound = deepSkyObjectTypeRepository.findById(1L);
		assertNull(typeFound);
	}
	
	@Test
	public void testFindByTypeWhenIsPresent() throws RepositoryException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);
		
		DeepSkyObjectType galaxyFound = deepSkyObjectTypeRepository.findByType(GALAXY);
		assertNotNull(galaxyFound);
	}
	
	@Test
	public void testFindByTypeWhenIsNotPresent() throws RepositoryException {
		DeepSkyObjectType typeFound = deepSkyObjectTypeRepository.findByType(GALAXY);
		assertNull(typeFound);
	}
	
	@Test
	public void testAAddDeepSkyObjectTypeWhenNotExists() throws RepositoryException {
		DeepSkyObjectType galaxyExisting = deepSkyObjectTypeRepository.findByType(GALAXY);
		assertNull(galaxyExisting);
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);

		assertThat(galaxySaved.getId() > 0);
	}
	
	@Test
	public void testUpdateDeepSkyObjectType() throws RepositoryException {
		DeepSkyObjectType typeExisting = deepSkyObjectTypeRepository.findById(1L);
		assertNull(typeExisting);
		DeepSkyObjectType typeSaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);
		
		String typeStringUpdated = "orion changed";
		typeSaved.setType(typeStringUpdated);
		deepSkyObjectTypeRepository.update(typeSaved);
		
		DeepSkyObjectType typeUpdated = deepSkyObjectTypeRepository.findById(typeSaved.getId());
		assertNotNull(typeUpdated);
		assertThat(typeUpdated.getType().equalsIgnoreCase(typeStringUpdated));
	}
	
	@Test
	public void testDDeleteById() throws RepositoryException {
		DeepSkyObjectType typeExisting = deepSkyObjectTypeRepository.findByType(GALAXY);
		assertNull(typeExisting);
		DeepSkyObjectType typeSaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);
		
		long orionId = typeSaved.getId();
		
		DeepSkyObjectType typeBeforeDelete = deepSkyObjectTypeRepository.findById(orionId);
		assertNotNull(typeBeforeDelete);

		deepSkyObjectTypeRepository.delete(orionId);

		DeepSkyObjectType typeAfterDelete = deepSkyObjectTypeRepository.findById(orionId);
		assertNull(typeAfterDelete);
	}

}
