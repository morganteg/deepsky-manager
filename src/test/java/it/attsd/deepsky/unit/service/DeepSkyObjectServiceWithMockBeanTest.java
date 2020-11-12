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
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeepSkyObjectServiceWithMockBeanTest {

	@Mock
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@InjectMocks
	private DeepSkyObjectService deepSkyObjectService;

	private String ORION = "orion";
	private String NEBULA = "nebula";
	private String M42 = "m42";
	private String M43 = "m43";

	private Constellation orion = new Constellation(1L, ORION);
	private DeepSkyObjectType nebula = new DeepSkyObjectType(1L, NEBULA);

	private DeepSkyObject m42 = new DeepSkyObject(1L, M42, orion, nebula);
	private DeepSkyObject m43 = new DeepSkyObject(1L, M43, orion, nebula);

	@Test
	public void testFindAllDeepSkyObjectsWhenDbIsEmpty() {
		List<DeepSkyObject> deepSkyObjects = new ArrayList<DeepSkyObject>();
		deepSkyObjects.add(m42);
		deepSkyObjects.add(m43);
		when(deepSkyObjectRepository.findAll()).thenReturn(deepSkyObjects);

		List<DeepSkyObject> deepSkyObjectsFound = deepSkyObjectService.findAll();

		assertThat(deepSkyObjectsFound).isEqualTo(deepSkyObjects);
	}

	@Test
	public void testFindDeepSkyObjectByIdWhenIsPresent() throws DeepSkyObjectNotFoundException {
		when(deepSkyObjectRepository.findById(1L)).thenReturn(m42);

		DeepSkyObject m42Found = deepSkyObjectService.findById(1L);

		assertThat(m42Found).isEqualTo(m42);
	}

	@Test
	public void testFindDeepSkyObjectByNameWhenIsPresent() {
		when(deepSkyObjectRepository.findByName(ORION)).thenReturn(m42);

		DeepSkyObject m42Found = deepSkyObjectService.findByName(ORION);

		assertThat(m42Found).isEqualTo(m42);
	}

	@Test
	public void testSaveDeepSkyObjectWhenNotExists() throws DeepSkyObjectAlreadyExistsException {
		Constellation orion = new Constellation(1L, ORION);
		DeepSkyObjectType nebula = new DeepSkyObjectType(1L, NEBULA);
		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orion, nebula);

		when(deepSkyObjectRepository.save(m42)).thenReturn(m42Saved);
		DeepSkyObject m42SavedFromService = deepSkyObjectService.save(m42);

		verify(deepSkyObjectRepository, times(1)).save(m42);
		assertNotNull(m42SavedFromService);
		assertThat(m42SavedFromService.getId()).isPositive();
	}

	@Test
	public void testSaveDeepSkyObjectWhenAlreadyExists() throws DeepSkyObjectAlreadyExistsException {
		doThrow(new DeepSkyObjectAlreadyExistsException()).when(deepSkyObjectRepository).save(m42);

		assertThrows(DeepSkyObjectAlreadyExistsException.class, () -> deepSkyObjectService.save(m42));
	}

	@Test
	public void testDeleteDeepSkyObjectWhenExists() throws DeepSkyObjectNotFoundException {
		when(deepSkyObjectRepository.findById(1L)).thenReturn(null);

		deepSkyObjectService.delete(1L);
		verify(deepSkyObjectRepository, times(1)).delete(1L);
		
		assertThrows(DeepSkyObjectNotFoundException.class, () -> deepSkyObjectService.findById(1L));
	}

}
