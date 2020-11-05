package it.attsd.deepsky.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConstellationServiceWithMockBeanTest {

	@Mock
	private ConstellationRepository constellationRepository;

	@InjectMocks
	private ConstellationService constellationService;

	private String ORION = "orion";
	private String SCORPION = "scorpion";
	private String LIBRA = "libra";
	
	private Constellation orion = new Constellation(1L, ORION);
	private Constellation scorpion = new Constellation(2L, SCORPION);
	private Constellation libra = new Constellation(LIBRA);

	@Test
	public void testFindAllConstellationsWhenDbIsEmpty() {
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(orion);
		constellations.add(scorpion);
		when(constellationRepository.findAll()).thenReturn(constellations);

		List<Constellation> constellationsFound = constellationService.findAll();

		assertThat(constellationsFound).isEqualTo(constellations);
	}

	@Test
	public void testFindConstellationByIdWhenIsPresent() {
		when(constellationRepository.findById(1)).thenReturn(orion);

		Constellation constellationFound = constellationService.findById(1);

		assertThat(constellationFound).isEqualTo(orion);
	}

	@Test
	public void testFindConstellationByTypeWhenIsPresent() {
		when(constellationRepository.findByName(ORION)).thenReturn(orion);

		Constellation orionFound = constellationRepository.findByName(ORION);

		assertThat(orionFound).isEqualTo(orion);
	}

	@Test
	public void testSaveConstellationWhenNotExists()
			throws ConstellationAlreadyExistsException {
		Constellation libraSaved = new Constellation(1L, LIBRA);

		when(constellationRepository.save(libra)).thenReturn(libraSaved);
		Constellation libraSavedFromService = constellationRepository.save(libra);

		verify(constellationRepository, times(1)).save(libra);
		assertNotNull(libraSavedFromService);
		assertThat(libraSavedFromService.getId()).isPositive();
	}

	@Test
	public void testSaveConstellationWhenAlreadyExists()
			throws ConstellationAlreadyExistsException {
		doThrow(new ConstellationAlreadyExistsException()).when(constellationRepository).save(libra);

		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationRepository.save(libra));
	}
	
	@Test
	public void testUpdateConstellationWhenExists()
			throws ConstellationAlreadyExistsException {
		String libraNameUpdated = LIBRA + " changed";
		Constellation libraUpdated = new Constellation(1L, libraNameUpdated);

		when(constellationRepository.update(libra)).thenReturn(libraUpdated);
		libra.setName(libraNameUpdated);
		Constellation libraUpdatedFromService = constellationRepository.update(libra);

		verify(constellationRepository, times(1)).update(libra);
		assertNotNull(libraUpdatedFromService);
		assertThat(libraUpdatedFromService.getId()).isPositive();
	}

	@Test
	public void testDeleteConstellationWhenExists() {
		when(constellationRepository.findById(1L)).thenReturn(null);

		constellationRepository.delete(1L);
		Constellation libraDeleted = constellationRepository.findById(1L);

		verify(constellationRepository, times(1)).delete(1L);
		assertNull(libraDeleted);
	}

	@Test
	public void testDeleteConstellationWhenNotExists() {
		long constellationId = 1L;
		when(constellationRepository.findById(constellationId)).thenReturn(null);

		constellationRepository.delete(constellationId);

		verify(constellationRepository, times(1)).delete(constellationId);
	}
}
