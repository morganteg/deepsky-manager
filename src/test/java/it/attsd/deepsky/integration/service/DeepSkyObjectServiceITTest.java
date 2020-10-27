package it.attsd.deepsky.integration.service;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectServiceITTest {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectServiceITTest.class);

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	@Autowired
	private ConstellationService constellationService;

	String orionName = "orion";
	String nebulaType = "nebula";
	String m42Name = "m42";
	String m43Name = "m43";

	String scorpiusName = "scorpius";
	String antaresName = "antares";
	String starName = "star";

	@Before
	public void setup() {
		deepSkyObjectRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
		constellationRepository.emptyTable();
	}

	@Test
	public void testFindAll() throws ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42 = new DeepSkyObject(m42Name, orionSaved, nebulaSaved);
		DeepSkyObject m42Saved = deepSkyObjectService.save(m42);
		assertNotNull(m42Saved);

		DeepSkyObject m43 = new DeepSkyObject(m43Name, orionSaved, nebulaSaved);
		DeepSkyObject m43Saved = deepSkyObjectService.save(m43);
		assertNotNull(m43Saved);

		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();

		assertThat(deepSkyObjects.size()).isEqualTo(2);
	}

	@Test
	public void testFindById() throws ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42 = new DeepSkyObject(m42Name, orionSaved, nebulaSaved);
		DeepSkyObject m42Saved = deepSkyObjectService.save(m42);
		assertNotNull(m42Saved);

		DeepSkyObject m42Found = deepSkyObjectService.findById(m42Saved.getId());

		assertNotNull(m42Found);
	}

	@Test
	public void testAddDeepSkyObjectWhenNotExists() throws ConstellationNotFoundException,
			DeepSkyObjectTypeNotFoundException, ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42Saved = deepSkyObjectService.save(orionSaved.getId(), nebulaSaved.getId(), m42Name);
		assertNotNull(m42Saved);
		assertThat(m42Saved.getId()).isPositive();
	}
	
	@Test
	public void testAddDeepSkyObjectWhenAlreadyExists() throws ConstellationNotFoundException,
			DeepSkyObjectTypeNotFoundException, ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42Saved = deepSkyObjectService.save(orionSaved.getId(), nebulaSaved.getId(), m42Name);
		assertNotNull(m42Saved);
		assertThat(m42Saved.getId()).isPositive();
		
		assertThrows(DeepSkyObjectAlreadyExistsException.class, () -> deepSkyObjectService.save(orionSaved.getId(), nebulaSaved.getId(), m42Name));
	}

	@Test
	public void testAddDeepSkyObjectIfNotPresentAndConstellationNotExists() throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findByName(m42Name);
		assertNull(deepSkyObjectFound);

		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		long orionId = orionSaved.getId();

		constellationService.delete(orionId);

		Constellation orionFound = constellationService.findById(orionId);
		assertNull(orionFound);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		assertThrows(ConstellationNotFoundException.class,
				() -> deepSkyObjectService.save(orionId, nebulaSaved.getId(), m42Name));
	}

	@Test
	public void testAddDeepSkyObjectIfNotPresentAndConstellationNotExists1()
			throws ConstellationAlreadyExistsException {
		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findByName(m42Name);
		assertNull(deepSkyObjectFound);

		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		assertThrows(ConstellationAlreadyExistsException.class,
				() -> deepSkyObjectService.saveConstellationAndDeepSkyObject(orionName, m42Name, nebulaType));
	}

	@Test
	public void testAddDeepSkyObjectIfNotPresentAndDeepSkyObjectTypeNotExists() throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findByName(m42Name);
		assertNull(deepSkyObjectFound);

		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		long nebulaId = nebulaSaved.getId();

		deepSkyObjectTypeService.delete(nebulaId);

		DeepSkyObjectType nebulaFound = deepSkyObjectTypeService.findById(nebulaId);
		assertNull(nebulaFound);

		assertThrows(DeepSkyObjectTypeNotFoundException.class,
				() -> deepSkyObjectService.save(orionSaved.getId(), nebulaId, m42Name));
	}

	@Test
	public void testAddDeepSkyObjectIfNotPresentAndDeepSkyObjectTypeNotExists1() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findByName(m42Name);
		assertNull(deepSkyObjectFound);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		assertThrows(DeepSkyObjectTypeAlreadyExistsException.class,
				() -> deepSkyObjectService.saveConstellationAndDeepSkyObject(orionName, m42Name, nebulaType));
	}

	@Test
	public void testUpdateDeepSkyObjectIfPresent() throws ConstellationNotFoundException,
			DeepSkyObjectTypeNotFoundException, ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findByName(orionName);
		assertNull(deepSkyObjectFound);

		Constellation orionSaved = constellationService.save(new Constellation(orionName));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(nebulaType));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42Saved = deepSkyObjectService.save(orionSaved.getId(), nebulaSaved.getId(), m42Name);
		assertNotNull(m42Saved);
		assertThat(m42Saved.getId()).isPositive();

		String m42NameChanged = m42Name + " changed";
		m42Saved.setName(m42NameChanged);
		
		DeepSkyObject m42Changed = deepSkyObjectService.update(m42Saved);
		assertNotNull(m42Changed);
		assertThat(m42Saved.getId()).isEqualTo(m42Changed.getId());
		assertThat(m42Changed.getName()).isEqualTo(m42NameChanged);
	}

	/**
	 * Transactional test with success
	 */
	@Test
	public void testAddConstellationAndDeepSkyObjectWithSuccess() {
		try {
			DeepSkyObject deepSkyObjectSaved = deepSkyObjectService.saveConstellationAndDeepSkyObject(orionName,
					m42Name, nebulaType);
			assertThat(deepSkyObjectSaved.getId()).isPositive();
		} catch (ConstellationAlreadyExistsException e) {
			logger.error(e.getMessage());
		} catch (DeepSkyObjectAlreadyExistsException e) {
			logger.error(e.getMessage());
		} catch (DeepSkyObjectTypeAlreadyExistsException e) {
			logger.error(e.getMessage());
		}

		// Check Constellation is created
		Constellation existingConstellation = constellationService.findByName(orionName);
		assertNotNull(existingConstellation);

		// Check DeepSkyObject is created
		DeepSkyObject existingDeepSkyObject = deepSkyObjectService.findByName(m42Name);
		assertNotNull(existingDeepSkyObject);
	}

	/**
	 * Transactional test with rollback
	 */
	@Test
	public void testAddConstellationAndDeepSkyObjectWithRollback() {
		try {
//			1 - Create constellation
			DeepSkyObjectType star = new DeepSkyObjectType(starName);
			DeepSkyObjectType starSaved = deepSkyObjectTypeService.save(star);
			assertNotNull(starSaved);
			assertThat(starSaved.getId()).isPositive();

//			2 - Create constellation, deepskyobject and deepskyobjecttype (previously created)
			DeepSkyObject deepSkyObjectSaved = deepSkyObjectService.saveConstellationAndDeepSkyObject(scorpiusName,
					antaresName, starName);
			assertThat(deepSkyObjectSaved.getId()).isEqualTo(1L);
		} catch (ConstellationAlreadyExistsException e) {
			logger.error(e.getMessage());
		} catch (DeepSkyObjectAlreadyExistsException e) {
			logger.error(e.getMessage());
		} catch (DeepSkyObjectTypeAlreadyExistsException e) {
			logger.error(e.getMessage());
		}

//		Check Constellation is not created
		Constellation scorpius = constellationService.findByName(scorpiusName);
		assertNull(scorpius);

//		Check DeepSkyObjectType is not created
		DeepSkyObject antares = deepSkyObjectService.findByName(antaresName);
		assertNull(antares);

//		Check DeepSkyObject is not created
		DeepSkyObjectType existingDeepSkyObject = deepSkyObjectTypeService.findByType(m42Name);
		assertNull(existingDeepSkyObject);
	}

}
