package it.attsd.deepsky.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=ConstellationService.class)
public class ConstellationServiceWithMockBeanTest {

	@MockBean
	private ConstellationRepository constellationRepository;

	@Autowired
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

		assertThat(constellationService.findAll()).containsAll(constellations);
	}

	@Test
	public void testFindConstellationByIdWhenIsPresent() throws ConstellationNotFoundException {
		when(constellationRepository.findById(1)).thenReturn(orion);

		assertThat(constellationService.findById(1)).isSameAs(orion);
	}

	@Test
	public void testFindConstellationByNameWhenIsPresent() {
		when(constellationRepository.findByName(ORION)).thenReturn(orion);
		
		assertThat(constellationService.findByName(ORION)).isSameAs(orion);
	}
	
	@Test
	public void testFindConstellationByNameWhenIsNotPresent() {
		when(constellationRepository.findByName(ORION)).thenReturn(null);

		Constellation orionFound = constellationService.findByName(ORION);

		assertNull(orionFound);
	}

	@Test
	public void testSaveConstellationWhenNotExists()
			throws ConstellationAlreadyExistsException {
		Constellation libraSaved = new Constellation(1L, LIBRA);

		when(constellationRepository.save(libra)).thenReturn(libraSaved);
		Constellation libraSavedFromService = constellationService.save(libra);

		verify(constellationRepository).save(libra);
		assertNotNull(libraSavedFromService);
		assertThat(libraSavedFromService.getId()).isPositive();
	}

	@Test
	public void testSaveConstellationWhenAlreadyExists()
			throws ConstellationAlreadyExistsException {
		doThrow(new ConstellationAlreadyExistsException()).when(constellationRepository).save(libra);

		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationService.save(libra));
	}
	
	@Test
	public void testUpdateConstellationWhenExists()
			throws ConstellationAlreadyExistsException {
		String libraNameUpdated = LIBRA + " changed";
		Constellation libraUpdated = new Constellation(1L, libraNameUpdated);

		when(constellationRepository.update(libra)).thenReturn(libraUpdated);
		libra.setName(libraNameUpdated);
		Constellation libraUpdatedFromService = constellationService.update(libra);

		verify(constellationRepository).update(libra);
		assertNotNull(libraUpdatedFromService);
		assertThat(libraUpdatedFromService.getId()).isPositive();
	}

	@Test
	public void testDeleteConstellationWhenExists() throws ConstellationNotFoundException {
		when(constellationRepository.findById(orion.getId())).thenReturn(orion);

		constellationService.delete(orion.getId());

		verify(constellationRepository).delete(orion.getId());
	}

	@Test
	public void testDeleteConstellationWhenNotExists() {
		when(constellationRepository.findById(orion.getId())).thenReturn(null);

		constellationService.delete(orion.getId());

		verify(constellationRepository).delete(orion.getId());
	}
}
