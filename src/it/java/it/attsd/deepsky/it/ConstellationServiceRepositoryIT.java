package it.attsd.deepsky.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import it.attsd.deepsky.exceptions.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exceptions.ConstellationIsStillUsedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.unit.repository.ConstellationRepository;
import it.attsd.deepsky.unit.service.ConstellationService;

import java.util.Arrays;
import java.util.List;

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
	public void testServiceCanFindAllFromRepository() {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		Constellation libraSaved = constellationRepository.save(new Constellation(LIBRA));
		List<Constellation> constellationsSaved = Arrays.asList(orionSaved, libraSaved);

		List<Constellation> constellations = constellationService.findAll();

		assertThat(constellations).containsAll(constellationsSaved);
	}

	@Test
	public void testServiceCanFindByIdFromRepository() {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

		assertThat(constellationService.findById(orionSaved.getId())).isNotNull();
	}

	@Test
	public void testServiceCanFindByNameFromRepository() {
		constellationRepository.save(new Constellation(ORION));

		assertThat(constellationService.findByName(ORION)).isNotNull();
	}

	@Test
	public void testServiceCanSaveIntoRepository() throws ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));

		assertThat(constellationRepository.findById(orionSaved.getId())).isPresent();
	}

	@Test
	public void testServiceCannotSaveIntoRepository() throws ConstellationAlreadyExistsException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationService.save(new Constellation(ORION)));
	}

	@Test
	public void testServiceCanUpdateIntoRepository() {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

		String nameChanged = orionSaved.getName() + " changed";
		Constellation orionModified = constellationService.updateById(orionSaved.getId(), new Constellation(orionSaved.getId(), nameChanged));

		Constellation orionFound = constellationRepository.findById(orionSaved.getId()).get();
		assertThat(orionFound).isEqualTo(orionModified);
	}

	@Test
	public void testServiceCanDeleteByIdFromRepository() throws ConstellationIsStillUsedException {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

		constellationService.deleteById(orionSaved.getId());

		assertThat(constellationRepository.findById(orionSaved.getId())).isNotPresent();
	}

}
