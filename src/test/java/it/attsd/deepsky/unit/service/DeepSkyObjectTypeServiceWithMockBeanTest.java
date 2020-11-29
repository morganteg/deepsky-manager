package it.attsd.deepsky.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeepSkyObjectTypeServiceWithMockBeanTest {

	@Mock
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@InjectMocks
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	private String NEBULA = "nebula";
	private String GALAXY = "galaxy";

	private DeepSkyObjectType nebula = new DeepSkyObjectType(1L, NEBULA);
	private DeepSkyObjectType galaxy = new DeepSkyObjectType(2L, GALAXY);

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFindAllDeepSkyObjectTypesWhenDbIsEmpty() {
		List<DeepSkyObjectType> deepSkyObjectTypes = new ArrayList<DeepSkyObjectType>();
		deepSkyObjectTypes.add(nebula);
		deepSkyObjectTypes.add(galaxy);
		when(deepSkyObjectTypeRepository.findAll()).thenReturn(deepSkyObjectTypes);

		List<DeepSkyObjectType> deepSkyObjectTypesFound = deepSkyObjectTypeService.findAll();

		assertThat(deepSkyObjectTypesFound).isEqualTo(deepSkyObjectTypes);
	}

	@Test
	public void testFindDeepSkyObjectTypeByIdWhenIsPresent() throws DeepSkyObjectTypeNotFoundException {
		when(deepSkyObjectTypeRepository.findById(1L)).thenReturn(nebula);

		DeepSkyObjectType nebulaFound = deepSkyObjectTypeService.findById(1L);

		assertThat(nebulaFound).isEqualTo(nebula);
	}

	@Test
	public void testFindDeepSkyObjectTypeByTypeWhenIsPresent() {
		when(deepSkyObjectTypeRepository.findByType(NEBULA)).thenReturn(nebula);

		DeepSkyObjectType nebulaFound = deepSkyObjectTypeService.findByType(NEBULA);

		assertThat(nebulaFound).isEqualTo(nebula);
	}

	@Test
	public void testSaveDeepSkyObjectTypeWhenNotExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType cluster = new DeepSkyObjectType("cluster");
		DeepSkyObjectType clusterSaved = new DeepSkyObjectType(1L, "cluster");

		when(deepSkyObjectTypeRepository.save(cluster)).thenReturn(clusterSaved);
		DeepSkyObjectType clusterSavedFromService = deepSkyObjectTypeService.save(cluster);

		verify(deepSkyObjectTypeRepository, times(1)).save(cluster);
		assertNotNull(clusterSavedFromService);
		assertThat(clusterSavedFromService.getId()).isPositive();
	}

	@Test
	public void testSaveDeepSkyObjectTypeWhenAlreadyExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(GALAXY);

		doThrow(new DeepSkyObjectTypeAlreadyExistsException()).when(deepSkyObjectTypeRepository).save(galaxy);

		assertThrows(DeepSkyObjectTypeAlreadyExistsException.class, () -> deepSkyObjectTypeService.save(galaxy));
	}

	@Test
	public void testUpdateDeepSkyObjectTypeWhenExists() throws ConstellationAlreadyExistsException {
		String galaxyNameUpdated = GALAXY + " changed";
		DeepSkyObjectType galaxyUpdated = new DeepSkyObjectType(1L, galaxyNameUpdated);

		when(deepSkyObjectTypeRepository.update(galaxy)).thenReturn(galaxyUpdated);
		galaxy.setType(galaxyNameUpdated);
		DeepSkyObjectType deepSkyObjectTypeUpdatedFromService = deepSkyObjectTypeRepository.update(galaxy);

		verify(deepSkyObjectTypeRepository, times(1)).update(galaxy);
		assertNotNull(deepSkyObjectTypeUpdatedFromService);
		assertThat(deepSkyObjectTypeUpdatedFromService.getId()).isPositive();
	}

	@Test
	public void testDeleteDeepSkyObjectTypeWhenExists() throws DeepSkyObjectTypeNotFoundException {
		when(deepSkyObjectTypeRepository.findById(1L)).thenReturn(null);

		deepSkyObjectTypeService.delete(1L);
		
		assertThrows(DeepSkyObjectTypeNotFoundException.class, () -> deepSkyObjectTypeService.findById(1L));

		verify(deepSkyObjectTypeRepository, times(1)).delete(1L);
	}

}
