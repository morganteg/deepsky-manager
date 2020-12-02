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

import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectServiceIT {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectServiceIT.class);

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

	String ORION = "orion";
	String NEBULA = "nebula";
	String M42 = "m42";
	String M43 = "m43";

	String SCORPIUS = "scorpius";
	String ANTARES = "antares";
	String STAR = "star";

//	@Before
//	public void setup() {
//		deepSkyObjectRepository.emptyTable();
//		deepSkyObjectTypeRepository.emptyTable();
////		constellationRepository.emptyTable();
//	}
//
//	@Test
//	public void testFindAll() throws ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException,
//			DeepSkyObjectAlreadyExistsException {
//		Constellation orionSaved = constellationService.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebulaSaved);
//
//		DeepSkyObject m42 = new DeepSkyObject(M42, orionSaved, nebulaSaved);
//		DeepSkyObject m42Saved = deepSkyObjectService.save(m42);
//		assertNotNull(m42Saved);
//
//		DeepSkyObject m43 = new DeepSkyObject(M43, orionSaved, nebulaSaved);
//		DeepSkyObject m43Saved = deepSkyObjectService.save(m43);
//		assertNotNull(m43Saved);
//
//		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
//
//		assertThat(deepSkyObjects.size()).isEqualTo(2);
//	}
//
//	@Test
//	public void testFindById() throws ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException,
//			DeepSkyObjectAlreadyExistsException, DeepSkyObjectNotFoundException {
//		Constellation orionSaved = constellationService.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebulaSaved);
//
//		DeepSkyObject m42 = new DeepSkyObject(M42, orionSaved, nebulaSaved);
//		DeepSkyObject m42Saved = deepSkyObjectService.save(m42);
//		assertNotNull(m42Saved);
//
//		DeepSkyObject m42Found = deepSkyObjectService.findById(m42Saved.getId());
//
//		assertNotNull(m42Found);
//	}
//
//	@Test
//	public void testAddDeepSkyObjectWhenNotExists() throws ConstellationNotFoundException,
//			DeepSkyObjectTypeNotFoundException, ConstellationAlreadyExistsException,
//			DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
//		Constellation orionSaved = constellationService.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebulaSaved);
//
//		DeepSkyObject m42Saved = deepSkyObjectService.save(orionSaved.getId(), nebulaSaved.getId(), M42);
//		assertNotNull(m42Saved);
//		assertThat(m42Saved.getId()).isPositive();
//	}
//
//	@Test
//	public void testAddDeepSkyObjectWhenAlreadyExists() throws ConstellationNotFoundException,
//			DeepSkyObjectTypeNotFoundException, ConstellationAlreadyExistsException,
//			DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
//		Constellation orionSaved = constellationService.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebulaSaved);
//
//		DeepSkyObject m42Saved = deepSkyObjectService.save(orionSaved.getId(), nebulaSaved.getId(), M42);
//		assertNotNull(m42Saved);
//		assertThat(m42Saved.getId()).isPositive();
//		
//		long orionSavedId = orionSaved.getId();
//		long nebulaSavedId = nebulaSaved.getId();
//
//		assertThrows(DeepSkyObjectAlreadyExistsException.class,
//				() -> deepSkyObjectService.save(orionSavedId, nebulaSavedId, M42));
//	}
//
////	@Test
////	public void testAddDeepSkyObjectIfNotPresentAndConstellationNotExists()
////			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException,
////			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
////		Constellation orionSaved = constellationService.save(new Constellation(ORION));
////		assertNotNull(orionSaved);
////
////		long orionId = orionSaved.getId();
////
////		constellationService.delete(orionId);
////		
////		assertThrows(ConstellationNotFoundException.class,
////				() -> constellationService.findById(orionId));
////
////		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
////		assertNotNull(nebulaSaved);
////		
////		long nebulaSavedId = nebulaSaved.getId();
////
////		assertThrows(ConstellationNotFoundException.class,
////				() -> deepSkyObjectService.save(orionId, nebulaSavedId, M42));
////	}
//
//	@Test
//	public void testAddDeepSkyObjectIfNotPresentAndConstellationNotExists1()
//			throws ConstellationAlreadyExistsException {
//		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findByName(M42);
//		assertNull(deepSkyObjectFound);
//
//		Constellation orionSaved = constellationService.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		assertThrows(ConstellationAlreadyExistsException.class,
//				() -> deepSkyObjectService.saveConstellationAndDeepSkyObject(ORION, M42, NEBULA));
//	}
//
//	@Test
//	public void testAddDeepSkyObjectIfNotPresentAndDeepSkyObjectTypeNotExists()
//			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException,
//			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
//		Constellation orionSaved = constellationService.save(new Constellation(ORION));
//		assertNotNull(orionSaved);
//
//		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebulaSaved);
//
//		long nebulaId = nebulaSaved.getId();
//
//		deepSkyObjectTypeService.delete(nebulaId);
//		
//		assertThrows(DeepSkyObjectTypeNotFoundException.class,
//				() -> deepSkyObjectTypeService.findById(nebulaId));
//		
//		long orionSavedId = orionSaved.getId();
//
//		assertThrows(DeepSkyObjectTypeNotFoundException.class,
//				() -> deepSkyObjectService.save(orionSavedId, nebulaId, M42));
//	}
//
//	@Test
//	public void testAddDeepSkyObjectIfNotPresentAndDeepSkyObjectTypeNotExists1()
//			throws DeepSkyObjectTypeAlreadyExistsException {
//		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findByName(M42);
//		assertNull(deepSkyObjectFound);
//
//		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebulaSaved);
//
//		assertThrows(DeepSkyObjectTypeAlreadyExistsException.class,
//				() -> deepSkyObjectService.saveConstellationAndDeepSkyObject(ORION, M42, NEBULA));
//	}
//
//	/**
//	 * Transactional test with success
//	 */
//	@Test
//	public void testAddConstellationAndDeepSkyObjectWithSuccess() {
//		try {
//			DeepSkyObject deepSkyObjectSaved = deepSkyObjectService.saveConstellationAndDeepSkyObject(ORION, M42,
//					NEBULA);
//			assertThat(deepSkyObjectSaved.getId()).isPositive();
//		} catch (ConstellationAlreadyExistsException e) {
//			logger.error(e.getMessage());
//		} catch (DeepSkyObjectAlreadyExistsException e) {
//			logger.error(e.getMessage());
//		} catch (DeepSkyObjectTypeAlreadyExistsException e) {
//			logger.error(e.getMessage());
//		}
//
//		// Check Constellation is created
//		Constellation existingConstellation = constellationService.findByName(ORION);
//		assertNotNull(existingConstellation);
//
//		// Check DeepSkyObject is created
//		DeepSkyObject existingDeepSkyObject = deepSkyObjectService.findByName(M42);
//		assertNotNull(existingDeepSkyObject);
//	}
//
//	/**
//	 * Transactional test with rollback
//	 */
//	@Test
//	public void testAddConstellationAndDeepSkyObjectWithRollback() {
//		try {
////			1 - Create constellation
//			DeepSkyObjectType star = new DeepSkyObjectType(STAR);
//			DeepSkyObjectType starSaved = deepSkyObjectTypeService.save(star);
//			assertNotNull(starSaved);
//			assertThat(starSaved.getId()).isPositive();
//
////			2 - Create constellation, deepskyobject and deepskyobjecttype (previously created)
//			DeepSkyObject deepSkyObjectSaved = deepSkyObjectService.saveConstellationAndDeepSkyObject(SCORPIUS, ANTARES,
//					STAR);
//			assertThat(deepSkyObjectSaved.getId()).isEqualTo(1L);
//		} catch (ConstellationAlreadyExistsException e) {
//			logger.error(e.getMessage());
//		} catch (DeepSkyObjectAlreadyExistsException e) {
//			logger.error(e.getMessage());
//		} catch (DeepSkyObjectTypeAlreadyExistsException e) {
//			logger.error(e.getMessage());
//		}
//
////		Check Constellation is not created
//		Constellation scorpius = constellationService.findByName(SCORPIUS);
//		assertNull(scorpius);
//
////		Check DeepSkyObjectType is not created
//		DeepSkyObject antares = deepSkyObjectService.findByName(ANTARES);
//		assertNull(antares);
//
////		Check DeepSkyObject is not created
//		DeepSkyObjectType existingDeepSkyObject = deepSkyObjectTypeService.findByType(M42);
//		assertNull(existingDeepSkyObject);
//	}
//
//	/**
//	 * Update a DeepSkyObject if exists, successfully.
//	 * 
//	 * @throws DeepSkyObjectNotFoundException
//	 * @throws ConstellationAlreadyExistsException
//	 * @throws DeepSkyObjectTypeAlreadyExistsException
//	 * @throws ConstellationNotFoundException
//	 * @throws DeepSkyObjectTypeNotFoundException
//	 * @throws DeepSkyObjectAlreadyExistsException
//	 */
//	@Test
//	public void testUpdateDeepSkyObjectWhenExists() throws DeepSkyObjectNotFoundException,
//			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException,
//			ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectAlreadyExistsException {
//		Constellation orion = constellationService.save(new Constellation(ORION));
//		assertNotNull(orion);
//
//		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebula);
//
//		DeepSkyObject m42 = deepSkyObjectService.save(orion.getId(), nebula.getId(), M42);
//		assertNotNull(m42);
//		assertThat(m42.getId()).isPositive();
//
//		String m42NameChanged = M42 + " changed";
//		m42.setName(m42NameChanged);
//
//		DeepSkyObject m42Changed = deepSkyObjectService.update(m42.getId(), m42NameChanged, orion.getId(),
//				nebula.getId());
//		assertNotNull(m42Changed);
//		assertThat(m42.getId()).isEqualTo(m42Changed.getId());
//		assertThat(m42Changed.getName()).isEqualTo(m42NameChanged);
//	}
//
//	@Test
//	public void testUpdateDeepSkyObjectWhenNotExists() throws DeepSkyObjectNotFoundException,
//			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException,
//			ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectAlreadyExistsException {
//		Constellation orion = constellationService.save(new Constellation(ORION));
//		assertNotNull(orion);
//
//		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebula);
//
//		String m42NameChanged = M42 + " changed";
//		
//		long orionId = orion.getId();
//		long nebulaId = nebula.getId();
//
//		assertThrows(DeepSkyObjectNotFoundException.class,
//				() -> deepSkyObjectService.update(1L, m42NameChanged, orionId, nebulaId));
//	}
//
//	@Test
//	public void testUpdateDeepSkyObjectIfPresentWithAndConstellationNotExists() throws DeepSkyObjectNotFoundException,
//			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException,
//			ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectAlreadyExistsException {
//		Constellation orion = constellationService.save(new Constellation(ORION));
//		assertNotNull(orion);
//
//		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebula);
//
//		DeepSkyObject m42 = deepSkyObjectService.save(orion.getId(), nebula.getId(), M42);
//		assertNotNull(m42);
//		assertThat(m42.getId()).isPositive();
//
//		String m42NameChanged = M42 + " changed";
//		m42.setName(m42NameChanged);
//
//		long notExistingConstellationId = 2L;
//		
//		long nebulaId = nebula.getId();
//
//		assertThrows(ConstellationNotFoundException.class, () -> deepSkyObjectService.update(m42.getId(),
//				m42NameChanged, notExistingConstellationId, nebulaId));
//
//		DeepSkyObject m42Found = deepSkyObjectService.findById(m42.getId());
//		assertNotNull(m42Found);
//		assertThat(m42Found.getName()).isNotEqualToIgnoringCase(m42NameChanged);
//	}
//	
//	@Test
//	public void testUpdateDeepSkyObjectWhenExistsAndDeepSkyObjectTypeNotExists() throws DeepSkyObjectNotFoundException,
//			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException,
//			ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectAlreadyExistsException {
//		Constellation orion = constellationService.save(new Constellation(ORION));
//		assertNotNull(orion);
//
//		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebula);
//
//		DeepSkyObject m42 = deepSkyObjectService.save(orion.getId(), nebula.getId(), M42);
//		assertNotNull(m42);
//		assertThat(m42.getId()).isPositive();
//
//		String m42NameChanged = M42 + " changed";
//		m42.setName(m42NameChanged);
//
//		long notExistingDeepSkyObjectTypeId = 2L;
//		
//		long orionId = orion.getId();
//
//		assertThrows(DeepSkyObjectTypeNotFoundException.class, () -> deepSkyObjectService.update(m42.getId(),
//				m42NameChanged, orionId, notExistingDeepSkyObjectTypeId));
//
//		DeepSkyObject m42Found = deepSkyObjectService.findById(m42.getId());
//		assertNotNull(m42Found);
//		assertThat(m42Found.getName()).isNotEqualToIgnoringCase(m42NameChanged);
//	}
//
//	@Test
//	public void testDeleteDeepSkyObjectIfPresent()
//			throws ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException,
//			ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectAlreadyExistsException {
//		Constellation orion = constellationService.save(new Constellation(ORION));
//		assertNotNull(orion);
//
//		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
//		assertNotNull(nebula);
//
//		DeepSkyObject m42 = deepSkyObjectService.save(orion.getId(), nebula.getId(), M42);
//		assertNotNull(m42);
//		assertThat(m42.getId()).isPositive();
//
//		deepSkyObjectService.delete(m42.getId());
//		
//		assertThrows(DeepSkyObjectNotFoundException.class,
//				() -> deepSkyObjectService.findById(m42.getId()));
//	}

}
