package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

	String orionName = "orion";
	String nebulaType = "nebula";
	String m42Name = "m42";
	
	String scorpiusName = "scorpius";
	String antaresName = "antares";
	String starName = "star";

	@Before
	public void setup() {
		deepSkyObjectRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
		constellationRepository.emptyTable();
	}

	/**
	 * Transactional test with success
	 */
	@Test
	public void testAAddConstellationAndDeepSkyObjectWithSuccess() {
		try {
			DeepSkyObject deepSkyObjectSaved = deepSkyObjectService.saveConstellationAndDeepSkyObject(
					orionName, m42Name, nebulaType);
			assertThat(deepSkyObjectSaved.getId() > 1);
		} catch (ConstellationAlreadyExistsException e) {
			logger.error(e.getMessage());
		} catch (RepositoryException e) {
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
	public void testBAddConstellationAndDeepSkyObjectWithRollback() {
		try {
//			1 - Create constellation
			DeepSkyObjectType star = new DeepSkyObjectType(starName);
			DeepSkyObjectType starSaved = deepSkyObjectTypeService.save(star);
			assertNotNull(starSaved);
			assertThat(starSaved.getId() > 0);

//			2 - Create constellation, deepskyobject and deepskyobjecttype (previously created)
			DeepSkyObject deepSkyObjectSaved = deepSkyObjectService.saveConstellationAndDeepSkyObject(scorpiusName,
					antaresName, starName);
			assertThat(deepSkyObjectSaved.getId() == 1);
		} catch (ConstellationAlreadyExistsException e) {
			logger.error(e.getMessage());
		} catch (RepositoryException e) {
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
