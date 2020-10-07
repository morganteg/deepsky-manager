package it.attsd.deepsky.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeepSkyObjectTypeServiceWithMockBeanTest {

	@Mock
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@InjectMocks
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	DeepSkyObjectType nebula = new DeepSkyObjectType(1L, "nebula");
	DeepSkyObjectType galaxy = new DeepSkyObjectType(2L, "galaxy");

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
	public void testFindDeepSkyObjectByIdWhenIsPresent() throws RepositoryException {
		when(deepSkyObjectTypeRepository.findById(1L)).thenReturn(nebula);

		DeepSkyObjectType nebulaFound = deepSkyObjectTypeService.findById(1L);

		assertThat(nebulaFound).isEqualTo(nebula);
	}

	@Test
	public void testFindDeepSkyObjectByTypeWhenIsPresent() throws RepositoryException {
		when(deepSkyObjectTypeRepository.findByType("nebula")).thenReturn(nebula);

		DeepSkyObjectType nebulaFound = deepSkyObjectTypeService.findByType("nebula");

		assertThat(nebulaFound).isEqualTo(nebula);
	}

	@Test
	public void testSaveDeepSkyObjectWhenNotExists() throws RepositoryException {
		DeepSkyObjectType cluster = new DeepSkyObjectType("cluster");
		DeepSkyObjectType clusterSaved = new DeepSkyObjectType(1L, "cluster");

		when(deepSkyObjectTypeRepository.save(cluster)).thenReturn(clusterSaved);
		DeepSkyObjectType clusterSavedFromService = deepSkyObjectTypeService.save(cluster);

		verify(deepSkyObjectTypeRepository, times(1)).save(cluster);
		assertNotNull(clusterSavedFromService);
		assertThat(clusterSavedFromService.getId()).isGreaterThan(0);
	}

	@Test(expected = RepositoryException.class)
	public void testSaveDeepSkyObjectWhenAlreadyExists() throws RepositoryException {
		DeepSkyObjectType cluster = new DeepSkyObjectType("cluster");

		doThrow(new RepositoryException("Error during DeepSkyObjectType saving...")).when(deepSkyObjectTypeRepository)
				.save(cluster);

		DeepSkyObjectType clusterSavedFromService = deepSkyObjectTypeService.save(cluster);

		verify(deepSkyObjectTypeRepository, times(1)).save(cluster);
		assertNull(clusterSavedFromService);
	}

	@Test
	public void testDeleteDeepSkyObjectWhenExists() throws RepositoryException {
		when(deepSkyObjectTypeRepository.findById(1L)).thenReturn(null);

		deepSkyObjectTypeService.delete(1L);
		DeepSkyObjectType clusterDeleted = deepSkyObjectTypeService.findById(1L);

		verify(deepSkyObjectTypeRepository, times(1)).delete(1L);
		assertNull(clusterDeleted);
	}

	@Test(expected = RepositoryException.class)
	public void testDeleteDeepSkyObjectWhenNotExists() throws RepositoryException {
		doThrow(new RepositoryException("DeepSkyObjectType not found")).when(deepSkyObjectTypeRepository).delete(1L);

		deepSkyObjectTypeService.delete(1L);

		verify(deepSkyObjectTypeRepository, times(1)).delete(1L);
	}
}
