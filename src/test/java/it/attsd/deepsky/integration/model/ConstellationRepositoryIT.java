package it.attsd.deepsky.integration.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ConstellationRepositoryIT {
	private Logger logger = LoggerFactory.getLogger(ConstellationRepositoryIT.class);

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	String ORION = "orion";
	String LIBRA = "libra";

	@Before
	public void setup() {
		deepSkyObjectRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
		constellationRepository.emptyTable();
	}

	@Test
	public void testFindAll() throws ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		Constellation libraSaved = constellationRepository.save(new Constellation(LIBRA));
		assertNotNull(libraSaved);

		List<Constellation> constellations = constellationRepository.findAll();
		logger.info("constellations: " + constellations);

		assertThat(constellations.size()).isEqualTo(2);
	}

	@Test
	public void testFindByIdWhenIsPresent() throws ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		Constellation orionFound = constellationRepository.findById(orionSaved.getId());
		assertNotNull(orionFound);
	}

	@Test
	public void testFindByIdWhenIsNotPresent() {
		Constellation orionFound = constellationRepository.findById(1L);
		assertNull(orionFound);
	}

	@Test
	public void testFindByNameWhenIsPresent() throws ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		Constellation orionFound = constellationRepository.findByName(ORION);
		assertNotNull(orionFound);
	}

	@Test
	public void testFindByNameWhenIsNotPresent() {
		Constellation orionFound = constellationRepository.findByName(ORION);
		assertNull(orionFound);
	}

	@Test
	public void testAddConstellationWhenNotExists() throws ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		assertThat(orionSaved.getId()).isPositive();
	}

	@Test
	public void testAddConstellationWhenAlreadyExists() throws ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		Constellation orion = new Constellation(ORION);

		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationRepository.save(orion));
	}

	@Test
	public void testUpdateConstellation() throws ConstellationAlreadyExistsException {
		Constellation constellationSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(constellationSaved);

		String nameUpdated = "orion changed";
		constellationSaved.setName(nameUpdated);
		constellationRepository.update(constellationSaved);

		Constellation constellationUpdated = constellationRepository.findById(constellationSaved.getId());
		assertNotNull(constellationUpdated);
		assertThat(constellationUpdated.getName()).isEqualToIgnoringCase(nameUpdated);
	}

	@Test
	public void testDeleteById() throws ConstellationAlreadyExistsException {
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
