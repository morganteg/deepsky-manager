package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class DeepSkyObjectTypeServiceITTest {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectTypeServiceITTest.class);
	
	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;
	
	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;
	
	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	String GALAXY = "galaxy";
	String NEBULA = "nebula";
	
	@Before
	public void setup() {
		deepSkyObjectRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
	}
	
	@Test
	public void testFindAll() throws RepositoryException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);
		
		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);
		
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		logger.info("constellations: " + deepSkyObjectTypes);

		assertThat(deepSkyObjectTypes.size()).isEqualTo(2);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenNotExists() throws RepositoryException {
		DeepSkyObjectType existingType = deepSkyObjectTypeService.findByType(GALAXY);
		assertNull(existingType);

		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		
		assertNotNull(typeSaved);
		assertThat(typeSaved.getId() == 1);
	}
	
	@Test(expected = RepositoryException.class)
	public void testAddDeepSkyObjectTypeWhenExists() throws RepositoryException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);
		
		deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
	}
	
	@Test
	public void testUpdateDeepSkyObjectTypeWhenIsPresent() throws RepositoryException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);
		
		String typeUpdated = GALAXY + " changed";
		typeSaved.setType(typeUpdated);
		
		deepSkyObjectTypeService.update(typeSaved);
		
		DeepSkyObjectType typeFound = deepSkyObjectTypeService.findById(typeSaved.getId());
		assertNotNull(typeFound);
		
		assertThat(typeFound.getType().equalsIgnoreCase(typeUpdated));
	}
	
	@Test
	public void testDeleteDeepSkyObjectTypeWhenIsPresent() throws RepositoryException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);
		
		deepSkyObjectTypeService.delete(typeSaved.getId());
		
		DeepSkyObjectType typeFound = deepSkyObjectTypeService.findById(typeSaved.getId());
		assertNull(typeFound);
	}

}
