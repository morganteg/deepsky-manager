package it.attsd.deepsky.integration.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectTypeRepositoryIT {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectTypeRepositoryIT.class);
	
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
	public void testFindAll() throws DeepSkyObjectTypeAlreadyExistsException {
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
	public void testFindByIdWhenIsPresent() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);
		
		DeepSkyObjectType typeFound = deepSkyObjectTypeRepository.findById(galaxySaved.getId());
		assertNotNull(typeFound);
	}
	
	@Test
	public void testFindByIdWhenIsNotPresent() {
		DeepSkyObjectType typeFound = deepSkyObjectTypeRepository.findById(1L);
		assertNull(typeFound);
	}
	
	@Test
	public void testFindByTypeWhenIsPresent() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);
		
		DeepSkyObjectType galaxyFound = deepSkyObjectTypeRepository.findByType(GALAXY);
		assertNotNull(galaxyFound);
	}
	
	@Test
	public void testFindByTypeWhenIsNotPresent() {
		DeepSkyObjectType typeFound = deepSkyObjectTypeRepository.findByType(GALAXY);
		assertNull(typeFound);
	}
	
	@Test
	public void testAddDeepSkyObjectTypeWhenNotExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);

		assertThat(galaxySaved.getId()).isPositive();
	}
	
	@Test
	public void testAddDeepSkyObjectTypeWhenAlreadyExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);
		
		DeepSkyObjectType deepSkyObjectType = new DeepSkyObjectType(GALAXY);

		assertThrows(DeepSkyObjectTypeAlreadyExistsException.class,
				() -> deepSkyObjectTypeRepository.save(deepSkyObjectType));
	}
	
	@Test
	public void testUpdateDeepSkyObjectType() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType typeExisting = deepSkyObjectTypeRepository.findById(1L);
		assertNull(typeExisting);
		DeepSkyObjectType typeSaved = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);
		
		String typeStringUpdated = "orion changed";
		typeSaved.setType(typeStringUpdated);
		deepSkyObjectTypeRepository.update(typeSaved);
		
		DeepSkyObjectType typeUpdated = deepSkyObjectTypeRepository.findById(typeSaved.getId());
		assertNotNull(typeUpdated);
		assertThat(typeUpdated.getType()).isEqualToIgnoringCase(typeStringUpdated);
	}
	
	@Test
	public void testDDeleteById() throws DeepSkyObjectTypeAlreadyExistsException {
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
