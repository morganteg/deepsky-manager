package it.attsd.deepsky.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import it.attsd.deepsky.exceptions.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exceptions.ConstellationIsStillUsedException;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(MockitoJUnitRunner.class)
public class ConstellationServiceWithMockBeanTest {

	@Mock
	private ConstellationRepository constellationRepository;

	@Mock
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@InjectMocks
	private ConstellationService constellationService;

	private String ORION = "orion";
	private String SCORPIUS = "scorpius";

	private String M42 = "m42";

	private Constellation orionSaved = new Constellation(1L, ORION);
	private Constellation scorpiusSaved = new Constellation(2L, SCORPIUS);

	private DeepSkyObject deepSkyObjectSaved = new DeepSkyObject(1L, M42, orionSaved);

	@Test
	public void testFindAllConstellationsWhenDbIsEmpty() {
		when(constellationRepository.findAll()).thenReturn(new ArrayList<Constellation>());

		assertThat(constellationService.findAll()).isEmpty();
	}

	@Test
	public void testFindAllConstellationsWhenDbHasTwo() {
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(orionSaved);
		constellations.add(scorpiusSaved);

		when(constellationRepository.findAll()).thenReturn(constellations);

		assertThat(constellationService.findAll()).containsExactly(orionSaved, scorpiusSaved);
	}

	@Test
	public void testFindConstellationByIdWhenIsPresent() {
		when(constellationRepository.findById(1L)).thenReturn(Optional.of(orionSaved));

		assertThat(constellationService.findById(1L)).isSameAs(orionSaved);
	}

	@Test
	public void testFindConstellationByIdWhenIsNotPresent() {
		when(constellationRepository.findById(1L)).thenReturn(Optional.empty());

		assertThat(constellationService.findById(1L)).isNull();
	}

	@Test
	public void testFindConstellationByNameWhenIsPresent() {
		when(constellationRepository.findByName(ORION)).thenReturn(orionSaved);

		assertThat(constellationService.findByName(ORION)).isSameAs(orionSaved);
	}

	@Test
	public void testFindConstellationByNameWhenIsNotPresent() {
		when(constellationRepository.findByName(ORION)).thenReturn(null);

		assertThat(constellationService.findByName(ORION)).isNull();
	}

	@Test
	public void testSaveConstellationIfNotExists() throws ConstellationAlreadyExistsException {
		Constellation orionToSave = spy(new Constellation(100L, ORION));

		when(constellationRepository.save(orionToSave)).thenReturn(orionSaved);

		Constellation orionSaved = constellationService.save(orionToSave);
		assertThat(orionSaved).isSameAs(this.orionSaved);

		InOrder inOrder = inOrder(orionToSave, constellationRepository);
		inOrder.verify(orionToSave).setId(null);
		inOrder.verify(constellationRepository).save(orionToSave);
	}

	@Test
	public void testSaveConstellationIfAlreadyExists() {
		when(constellationRepository.findByName(ORION)).thenReturn(orionSaved);

		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationService.save(new Constellation(ORION)));

		verify(constellationRepository).findByName(ORION);
		verify(constellationRepository, times(0)).save(new Constellation(ORION));
	}

//	@Test
//	public void testSaveConstellationWhenAlreadyExists() throws ConstellationAlreadyExistsException {
//		doThrow(new ConstellationAlreadyExistsException()).when(constellationRepository).save(libra);
//
//		assertThrows(ConstellationAlreadyExistsException.class, () -> constellationService.save(libra));
//	}

	@Test
	public void testUpdateConstellation() {
		Constellation orionToUpdate = spy(new Constellation(null, ORION));
		Constellation orionUpdated = new Constellation(1L, ORION);

		when(constellationRepository.save(any(Constellation.class))).thenReturn(orionUpdated);

		Constellation result = constellationService.updateById(1L, orionToUpdate);
		assertThat(result).isSameAs(orionUpdated);
		
		InOrder inOrder = inOrder(orionToUpdate, constellationRepository);
		inOrder.verify(orionToUpdate).setId(1L);
		inOrder.verify(constellationRepository).save(orionToUpdate);
	}

	@Test
	public void testDeleteConstellationWhenExistsAndNotUsed() throws ConstellationIsStillUsedException {
		when(constellationRepository.findById(orionSaved.getId())).thenReturn(Optional.of(orionSaved));

		constellationService.deleteById(1l);

		InOrder inOrder = inOrder(constellationRepository);
		inOrder.verify(constellationRepository).findById(orionSaved.getId());
		inOrder.verify(constellationRepository).deleteById(orionSaved.getId());
	}

	@Test
	public void testDeleteConstellationWhenExistsAndUsed() {
		when(constellationRepository.findById(orionSaved.getId())).thenReturn(Optional.of(orionSaved));
		when(deepSkyObjectRepository.findByConstellation(orionSaved)).thenReturn(Arrays.asList(deepSkyObjectSaved));

		assertThrows(ConstellationIsStillUsedException.class, () -> constellationService.deleteById(1l));

		verify(constellationRepository).findById(orionSaved.getId());
		verify(deepSkyObjectRepository).findByConstellation(orionSaved);
		verify(constellationRepository, times(0)).deleteById(orionSaved.getId());

		InOrder inOrder = inOrder(constellationRepository, deepSkyObjectRepository);
		inOrder.verify(constellationRepository).findById(orionSaved.getId());
		inOrder.verify(deepSkyObjectRepository).findByConstellation(orionSaved);
		inOrder.verify(constellationRepository, times(0)).deleteById(orionSaved.getId());
	}

//	@Test
//	public void testDeleteConstellationWhenNotExists() {
//		when(constellationRepository.findById(orion.getId())).thenReturn(null);
//
//		constellationService.delete(orion.getId());
//
//		verify(constellationRepository).delete(orion.getId());
//	}
}
