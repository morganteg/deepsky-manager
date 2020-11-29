package it.attsd.deepsky.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class DeepSkyObjectTypeServiceIT {

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
	public void testFindAll() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxySaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxySaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);

		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();

		assertThat(deepSkyObjectTypes.size()).isEqualTo(2);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenNotExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));

		assertNotNull(typeSaved);
		assertThat(typeSaved.getType()).isEqualToIgnoringCase(GALAXY);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenAlreadyExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);
		
		DeepSkyObjectType deepSkyObjectType = new DeepSkyObjectType(GALAXY);

		assertThrows(DeepSkyObjectTypeAlreadyExistsException.class,
				() -> deepSkyObjectTypeService.save(deepSkyObjectType));
	}

	@Test
	public void testUpdateDeepSkyObjectTypeWhenIsPresent() throws DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectTypeNotFoundException {
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
	public void testDeleteDeepSkyObjectTypeWhenIsPresent() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType typeSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(typeSaved);

		deepSkyObjectTypeService.delete(typeSaved.getId());
		
		assertThrows(DeepSkyObjectTypeNotFoundException.class,
				() -> deepSkyObjectTypeService.findById(typeSaved.getId()));
	}

}
