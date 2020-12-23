package it.attsd.deepsky.it;

import it.attsd.deepsky.exceptions.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import it.attsd.deepsky.service.DeepSkyObjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
	String M43 = "m43";

	@Test
	public void testServiceCanFindAllFromRepository() {
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));
		DeepSkyObject m43Saved = deepSkyObjectRepository.save(new DeepSkyObject(M43, orion));
		List<DeepSkyObject> deepSkyObjectsSaved = Arrays.asList(m42Saved, m43Saved);

		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();

		assertThat(deepSkyObjects).containsAll(deepSkyObjectsSaved);
	}

	@Test
	public void testServiceCanFindByIdFromRepository() {
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

		assertThat(deepSkyObjectService.findById(m42Saved.getId())).isNotNull();
	}

	@Test
	public void testServiceCanFindByNameFromRepository() {
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

		assertThat(deepSkyObjectService.findByName(M42)).isNotNull();
	}

	@Test
	public void testServiceCanSaveIntoRepository() throws DeepSkyObjectAlreadyExistsException {
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		DeepSkyObject m42Saved = deepSkyObjectService.save(new DeepSkyObject(M42, orion));

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

	@Test
	public void testServiceCanDeleteByIdFromRepository() {
		Constellation orion = constellationRepository.save(new Constellation(ORION));
		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

		deepSkyObjectService.deleteById(m42Saved.getId());

		assertThat(deepSkyObjectRepository.findById(m42Saved.getId())).isNotPresent();
	}

}
