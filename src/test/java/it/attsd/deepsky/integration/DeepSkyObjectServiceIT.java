package it.attsd.deepsky.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

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
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectServiceIT {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectServiceIT.class);

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	@Autowired
	private ConstellationService constellationService;

	String constellationOrionName = "orion";
	String deepSkyObjectM42Type = "nebula";
	String deepSkyObjectM42Name = "m42";

	@Before
	public void setup() {
		deepSkyObjectRepository.emptyTable("deepskyobject");
		constellationRepository.emptyTable("constellation");
	}

	@Test
	public void testAAddConstellationAndDeepSkyObjectWhenNotExist() {
		try {
			DeepSkyObject deepSkyObjectSaved = deepSkyObjectService.saveConstellationAndDeepSkyObject(
					constellationOrionName, deepSkyObjectM42Name, deepSkyObjectM42Type);
			assertThat(deepSkyObjectSaved.getId() == 1);
		} catch (ConstellationAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeepSkyObjectAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeepSkyObjectTypeAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create Constellation if not exists
		Constellation existingConstellation = constellationService.findByName(constellationOrionName);
		assertNotNull(existingConstellation);

		DeepSkyObject existingDeepSkyObject = deepSkyObjectService.findByName(deepSkyObjectM42Name);
		assertNotNull(existingDeepSkyObject);
	}

}
