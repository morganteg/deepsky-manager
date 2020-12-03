package it.attsd.deepsky.integration.repository;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(ConstellationService.class)
public class ConstellationServiceRepositoryIT {
	private Logger logger = LoggerFactory.getLogger(ConstellationServiceRepositoryIT.class);

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private ConstellationService constellationService;

	String ORION = "orion";
	String LIBRA = "libra";

//	@Before
//	public void setup() {
//		deepSkyObjectRepository.emptyTable();
//		deepSkyObjectTypeRepository.emptyTable();
////		constellationRepository.emptyTable();
//	}

	@Test
	public void testSaveIntoRepository() throws ConstellationAlreadyExistsException {
		Constellation saved = constellationService.save(new Constellation(ORION));
		constellationRepository.findById(saved.getId()).isPresent();

		assertThat(constellationRepository.findById(saved.getId()).get()).isEqualTo(saved);
	}

//	@Test
//	public void testFindAll() throws ConstellationAlreadyExistsException {
//		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		Constellation libraSaved = constellationRepository.save(new Constellation(LIBRA));
//		assertNotNull(libraSaved);
//
//		List<Constellation> constellations = constellationRepository.findAll();
//		logger.info("constellations: " + constellations);
//
//		assertThat(constellations.size()).isEqualTo(2);
//	}
//
////	@Test
////	public void testFindByIdWhenIsPresent() throws ConstellationAlreadyExistsException {
////		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
////		assertNotNull(orionSaved);
////
////		Constellation orionFound = constellationRepository.findById(orionSaved.getId());
////		assertNotNull(orionFound);
////	}
////
////	@Test
////	public void testFindByIdWhenIsNotPresent() {
////		Constellation orionFound = constellationRepository.findById(1L);
////		assertNull(orionFound);
////	}
//
//	@Test
//	public void testFindByNameWhenIsPresent() throws ConstellationAlreadyExistsException {
//		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		Constellation orionFound = constellationRepository.findByName(ORION);
//		assertNotNull(orionFound);
//	}
//
//	@Test
//	public void testFindByNameWhenIsNotPresent() {
//		Constellation orionFound = constellationRepository.findByName(ORION);
//		assertNull(orionFound);
//	}
//
//	@Test
//	public void testAddConstellationWhenNotExists() throws ConstellationAlreadyExistsException {
//		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		assertThat(orionSaved.getId()).isPositive();
//	}
//
//	@Test
//	public void testAddConstellationWhenAlreadyExists() throws ConstellationAlreadyExistsException {
//		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		Constellation orion = new Constellation(ORION);
//
//		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationRepository.save(orion));
//	}
//
////	@Test
////	public void testUpdateConstellation() throws ConstellationAlreadyExistsException {
////		Constellation constellationSaved = constellationRepository.save(new Constellation(ORION));
////		assertNotNull(constellationSaved);
////
////		String nameUpdated = constellationSaved.getName() + " changed";
////		constellationSaved.setName(nameUpdated);
////		constellationRepository.update(constellationSaved);
////
////		Constellation constellationUpdated = constellationRepository.findById(constellationSaved.getId());
////		assertNotNull(constellationUpdated);
////		assertThat(constellationUpdated.getName()).isEqualToIgnoringCase(nameUpdated);
////	}
////
////	@Test
////	public void testDeleteById() throws ConstellationAlreadyExistsException {
////		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
////		assertNotNull(orionSaved);
////		assertThat(orionSaved.getId()).isPositive();
////
////		long orionId = orionSaved.getId();
////
////		constellationRepository.delete(orionId);
////
////		Constellation constellationAfterDelete = constellationRepository.findById(orionId);
////		assertNull(constellationAfterDelete);
////	}

}
