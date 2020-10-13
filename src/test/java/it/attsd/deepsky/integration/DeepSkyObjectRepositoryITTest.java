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
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectRepositoryITTest {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectRepositoryITTest.class);
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;
	
	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;
	
	String ORION = "orion";
	
	String NEBULA = "nebula";
	
	String M42 = "m42";
	String M43 = "m43";
	
	@Before
	public void setup() {
		deepSkyObjectRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
		constellationRepository.emptyTable();
	}

	@Test
	public void testFindAll() throws RepositoryException {
		DeepSkyObjectType type = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(type);
		
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orion);
		
		DeepSkyObject m42Existing = deepSkyObjectRepository.findByName(M42);
		assertNull(m42Existing);
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion, type));
		assertNotNull(m42Saved);
		
		DeepSkyObject m43Existing = deepSkyObjectRepository.findByName(M43);
		assertNull(m43Existing);
		DeepSkyObject m43Saved = deepSkyObjectRepository.save(new DeepSkyObject(M43, orion, type));
		assertNotNull(m43Saved);
		
		List<DeepSkyObject> deepSkyObjectTypes = deepSkyObjectRepository.findAll();
		logger.info("deepSkyObjectTypes: " + deepSkyObjectTypes);

		assertThat(deepSkyObjectTypes.size()).isEqualTo(2);
	}

	@Test
	public void testFindByIdWhenIsPresent() throws RepositoryException {
		DeepSkyObjectType type = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(type);
		
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orion);
		
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion, type));
		assertNotNull(m42Saved);
		
		DeepSkyObject objectFound = deepSkyObjectRepository.findById(m42Saved.getId());
		assertNotNull(objectFound);
	}
	
	@Test
	public void testFindByIdWhenIsNotPresent() throws RepositoryException {
		DeepSkyObject objectFound = deepSkyObjectRepository.findById(1L);
		assertNull(objectFound);
	}
	
	@Test
	public void testFindByTypeWhenIsPresent() throws RepositoryException {
		DeepSkyObjectType type = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(type);
		
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orion);
		
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion, type));
		assertNotNull(m42Saved);
		
		DeepSkyObject m42Found = deepSkyObjectRepository.findByName(M42);
		assertNotNull(m42Found);
	}
	
	@Test
	public void testFindByTypeWhenIsNotPresent() throws RepositoryException {
		DeepSkyObject m42Found = deepSkyObjectRepository.findByName(M42);
		assertNull(m42Found);
	}
	
	@Test
	public void testAAddDeepSkyObjectTypeWhenNotExists() throws RepositoryException {
		DeepSkyObjectType type = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(type);
		
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orion);
		
		DeepSkyObject m42Existing = deepSkyObjectRepository.findByName(M42);
		assertNull(m42Existing);
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion, type));
		assertNotNull(m42Saved);
		
		assertThat(m42Saved.getId() > 0);
	}
	
	@Test
	public void testUpdateDeepSkyObjectType() throws RepositoryException {
		DeepSkyObjectType type = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(type);
		
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orion);
		
		DeepSkyObject m42Existing = deepSkyObjectRepository.findById(1L);
		assertNull(m42Existing);
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion, type));
		assertNotNull(m42Saved);
		
		String nameStringUpdated = "m42 changed";
		m42Saved.setName(nameStringUpdated);
		deepSkyObjectRepository.update(m42Saved);
		
		DeepSkyObject m42Updated = deepSkyObjectRepository.findById(m42Saved.getId());
		assertNotNull(m42Updated);
		assertThat(m42Updated.getName().equalsIgnoreCase(nameStringUpdated));
	}
	
	@Test
	public void testDDeleteById() throws RepositoryException {
		DeepSkyObjectType type = deepSkyObjectTypeRepository.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(type);
		
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orion);
		
		DeepSkyObject m42Existing = deepSkyObjectRepository.findByName(M42);
		assertNull(m42Existing);
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion, type));
		assertNotNull(m42Saved);
		
		long orionId = m42Saved.getId();
		
		DeepSkyObject m42BeforeDelete = deepSkyObjectRepository.findById(orionId);
		assertNotNull(m42BeforeDelete);

		deepSkyObjectRepository.delete(orionId);

		DeepSkyObject m42AfterDelete = deepSkyObjectRepository.findById(orionId);
		assertNull(m42AfterDelete);
	}

}
