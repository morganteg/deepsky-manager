package it.attsd.deepsky.it;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(DeepSkyObjectService.class)
public class DeepSkyObjectServiceRepositoryIT {
	@Autowired
	private ConstellationRepository constellationRepository;
	
	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	String ORION = "orion";
	
	String M42 = "m42";

	@Test
	public void testServiceCanSaveIntoRepository() {
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

		assertThat(deepSkyObjectRepository.findById(m42Saved.getId())).isPresent();
	}

	@Test
	public void testServiceCanUpdateIntoRepository() {
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

		String nameChanged = m42Saved.getName() + " changed";
		DeepSkyObject m42Modified = deepSkyObjectService.updateById(m42Saved.getId(), new DeepSkyObject(nameChanged, orion));

		assertThat(deepSkyObjectRepository.findById(m42Saved.getId()).get()).isEqualTo(m42Modified);
	}

}
