package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.GenericRepositoryException;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class DeepSkyObjectTypeServiceITTest {

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
	public void testFindAll() throws GenericRepositoryException, DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);

		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();

		assertThat(deepSkyObjectTypes.size()).isEqualTo(2);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenNotExists() throws GenericRepositoryException, DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType existingType = deepSkyObjectTypeService.findByType(GALAXY);
		assertNull(existingType);

		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));

		assertNotNull(typeSaved);
		assertThat(typeSaved.getType()).isEqualToIgnoringCase(GALAXY);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenExists() throws GenericRepositoryException, DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);

		assertThrows(GenericRepositoryException.class,
				() -> deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY)));
	}

	@Test
	public void testUpdateDeepSkyObjectTypeWhenIsPresent() throws GenericRepositoryException, DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);

		String typeUpdated = GALAXY + " changed";
		typeSaved.setType(typeUpdated);

		deepSkyObjectTypeService.update(typeSaved);

		DeepSkyObjectType typeFound = deepSkyObjectTypeService.findById(typeSaved.getId());
		assertNotNull(typeFound);

		assertThat(typeFound.getType()).isEqualToIgnoringCase(typeUpdated);
	}

	@Test
	public void testDeleteDeepSkyObjectTypeWhenIsPresent() throws GenericRepositoryException, DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);

		deepSkyObjectTypeService.delete(typeSaved.getId());

		DeepSkyObjectType typeFound = deepSkyObjectTypeService.findById(typeSaved.getId());
		assertNull(typeFound);
	}

}
