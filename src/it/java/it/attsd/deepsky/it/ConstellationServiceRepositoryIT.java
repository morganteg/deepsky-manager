package it.attsd.deepsky.it;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(ConstellationService.class)
public class ConstellationServiceRepositoryIT {
	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private ConstellationService constellationService;

	String ORION = "orion";
	String LIBRA = "libra";

	@Test
	public void testServiceCanSaveIntoRepository() {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));

		assertThat(constellationRepository.findById(orionSaved.getId())).isPresent();
	}

	@Test
	public void testServiceCanUpdateIntoRepository() {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));

		String nameChanged = orionSaved.getName() + " changed";
		Constellation orionModified = constellationService.updateById(orionSaved.getId(), new Constellation(orionSaved.getId(), nameChanged));

		assertThat(constellationRepository.findById(orionSaved.getId()).get()).isEqualTo(orionModified);
	}

}
