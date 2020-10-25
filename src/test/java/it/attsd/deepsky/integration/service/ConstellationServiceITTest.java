package it.attsd.deepsky.integration.service;

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
import it.attsd.deepsky.exception.GenericRepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ConstellationServiceITTest {
	private Logger logger = LoggerFactory.getLogger(ConstellationServiceITTest.class);

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private ConstellationService constellationService;

	String ORION = "orion";
	String LIBRA = "libra";

	@Before
	public void setup() {
		constellationRepository.emptyTable();
	}

	@Test
	public void testFindAll() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		Constellation libraSaved = constellationService.save(new Constellation(LIBRA));
		assertNotNull(libraSaved);

		List<Constellation> constellations = constellationService.findAll();
		logger.info("constellations: " + constellations);

		assertThat(constellations.size()).isEqualTo(2);
	}

	@Test
	public void testAddConstellationWhenNotExists() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation existingConstellation = constellationService.findByName(ORION);
		assertNull(existingConstellation);

		Constellation constellationSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(constellationSaved);
		assertThat(constellationSaved.getName()).isEqualToIgnoringCase(ORION);
	}

	@Test
	public void testAddConstellationWhenAlreadyExists() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		// Save first Constellation entity
		Constellation constellationSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(constellationSaved);

		// Save second Constellation entity
		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationService.save(new Constellation(ORION)));
	}

	@Test
	public void testUpdateConstellationWhenIsPresent() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		String nameUpdated = ORION + " changed";
		orionSaved.setName(nameUpdated);

		constellationService.update(orionSaved);

		Constellation orionFound = constellationService.findById(orionSaved.getId());
		assertNotNull(orionFound);

		assertThat(orionFound.getName()).isEqualToIgnoringCase(nameUpdated);
	}

	@Test
	public void testDeleteConstellationWhenIsPresent() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		constellationService.delete(orionSaved.getId());

		Constellation orionFound = constellationService.findById(orionSaved.getId());
		assertNull(orionFound);
	}

}
