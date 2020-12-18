package it.attsd.deepsky.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@InjectMocks
	private ConstellationService constellationService;

	private String ORION = "orion";
	private String SCORPIUS = "scorpius";

	private Constellation orion = new Constellation(1L, ORION);
	private Constellation scorpius = new Constellation(2L, SCORPIUS);

	@Test
	public void testFindAllConstellationsWhenDbIsEmpty() {
		when(constellationRepository.findAll()).thenReturn(new ArrayList<Constellation>());

		assertThat(constellationService.findAll()).isEmpty();
	}

	@Test
	public void testFindAllConstellationsWhenDbHasTwo() {
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(orion);
		constellations.add(scorpius);

		when(constellationRepository.findAll()).thenReturn(constellations);

		assertThat(constellationService.findAll()).containsExactly(orion, scorpius);
	}

	@Test
	public void testFindConstellationByIdWhenIsPresent() {
		when(constellationRepository.findById(1L)).thenReturn(Optional.of(orion));

		assertThat(constellationService.findById(1L)).isSameAs(orion);
	}

	@Test
	public void testFindConstellationByIdWhenIsNotPresent() {
		when(constellationRepository.findById(1L)).thenReturn(Optional.empty());

		assertThat(constellationService.findById(1L)).isNull();
	}

	@Test
	public void testFindConstellationByNameWhenIsPresent() {
		when(constellationRepository.findByName(ORION)).thenReturn(orion);

		assertThat(constellationService.findByName(ORION)).isSameAs(orion);
	}

	@Test
	public void testFindConstellationByNameWhenIsNotPresent() {
		when(constellationRepository.findByName(ORION)).thenReturn(null);

		assertThat(constellationService.findByName(ORION)).isNull();
	}

	@Test
	public void testSaveConstellation() {
		Constellation orionToSave = spy(new Constellation(100L, ORION));

		when(constellationRepository.save(orionToSave)).thenReturn(orion);

		Constellation orionSaved = constellationService.save(orionToSave);
		assertThat(orionSaved).isSameAs(orion);

		InOrder inOrder = inOrder(orionToSave, constellationRepository);
		inOrder.verify(orionToSave).setId(null);
		inOrder.verify(constellationRepository).save(orionToSave);
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
	public void testDeleteConstellationWhenExists() {
//		when(constellationRepository.findById(orion.getId())).thenReturn(orion);

		constellationService.deleteById(1l);

//		verify(constellationRepository).deleteById(orion.getId());
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
