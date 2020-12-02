package it.attsd.deepsky.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.DeepSkyObjectService;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DeepSkyObjectServiceWithMockBeanTest {
	@Mock
	private ConstellationRepository constellationRepository;

	@Mock
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Mock
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@InjectMocks
	private DeepSkyObjectService deepSkyObjectService;

	private String ORION = "orion";
	private String NEBULA = "nebula";
	private String M42 = "m42";
	private String M43 = "m43";

	private Constellation orion = new Constellation(ORION);
	private DeepSkyObjectType nebula = new DeepSkyObjectType(NEBULA);

	private DeepSkyObject m42 = new DeepSkyObject(M42, orion, nebula);
	private DeepSkyObject m43 = new DeepSkyObject(M43, orion, nebula);

//	@Before
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//	}
//
//	@Test
//	public void testFindAllDeepSkyObjectsWhenDbIsEmpty() {
//		List<DeepSkyObject> deepSkyObjects = new ArrayList<DeepSkyObject>();
//		deepSkyObjects.add(m42);
//		deepSkyObjects.add(m43);
//		when(deepSkyObjectRepository.findAll()).thenReturn(deepSkyObjects);
//
//		List<DeepSkyObject> deepSkyObjectsFound = deepSkyObjectService.findAll();
//
//		assertThat(deepSkyObjectsFound).isEqualTo(deepSkyObjects);
//	}
//
//	@Test
//	public void testFindDeepSkyObjectByIdWhenIsPresent() throws DeepSkyObjectNotFoundException {
//		when(deepSkyObjectRepository.findById(1L)).thenReturn(m42);
//
//		DeepSkyObject m42Found = deepSkyObjectService.findById(1L);
//
//		assertThat(m42Found).isEqualTo(m42);
//	}
//
//	@Test
//	public void testFindDeepSkyObjectByNameWhenIsPresent() {
//		when(deepSkyObjectRepository.findByName(ORION)).thenReturn(m42);
//
//		DeepSkyObject m42Found = deepSkyObjectService.findByName(ORION);
//
//		assertThat(m42Found).isEqualTo(m42);
//	}
//
//	@Test
//	public void testSaveDeepSkyObjectWhenNotExists() throws DeepSkyObjectAlreadyExistsException {
//		Constellation orionSaved = new Constellation(1L, ORION);
//		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
//		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orionSaved, nebulaSaved);
//
//		when(deepSkyObjectRepository.save(m42)).thenReturn(m42Saved);
//		DeepSkyObject m42SavedFromService = deepSkyObjectService.save(m42);
//
//		verify(deepSkyObjectRepository).save(m42);
//		assertNotNull(m42SavedFromService);
//		assertThat(m42SavedFromService.getId()).isPositive();
//	}
//
//	@Test
//	public void testSaveDeepSkyObjectWhenAlreadyExists() throws DeepSkyObjectAlreadyExistsException {
//		doThrow(new DeepSkyObjectAlreadyExistsException()).when(deepSkyObjectRepository).save(m42);
//
//		assertThrows(DeepSkyObjectAlreadyExistsException.class, () -> deepSkyObjectService.save(m42));
//	}
//	
//	@Test
//	public void testSaveDeepSkyObjectSimpleWhenNotExistsWithNullReturn() throws DeepSkyObjectAlreadyExistsException {
//		when(deepSkyObjectRepository.save(m42)).thenReturn(null);
//		DeepSkyObject m42SavedFromService = deepSkyObjectService.save(m42);
//
//		verify(deepSkyObjectRepository).save(m42);
//		assertNull(m42SavedFromService);
//	}
//
//	@Test
//	public void testSaveDeepSkyObjectWhenConstellationNotExists() throws DeepSkyObjectAlreadyExistsException {
//		long constellationId = 1L;
//		when(constellationRepository.findById(constellationId)).thenReturn(null);
//
//		long deepSkyObjectTypeId = 1L;
//
//		assertThrows(ConstellationNotFoundException.class,
//				() -> deepSkyObjectService.save(constellationId, deepSkyObjectTypeId, ORION));
//
//		verify(deepSkyObjectRepository, times(0)).save(m42);
//	}
//
////	@Test
////	public void testSaveDeepSkyObjectWhenDeepSkyObjectTypeNotExists() throws DeepSkyObjectAlreadyExistsException {
////		long constellationId = 1L;
////		when(constellationRepository.findById(constellationId)).thenReturn(orion);
////
////		long deepSkyObjectTypeId = 1L;
////		when(deepSkyObjectTypeRepository.findById(deepSkyObjectTypeId)).thenReturn(null);
////
////		assertThrows(DeepSkyObjectTypeNotFoundException.class,
////				() -> deepSkyObjectService.save(constellationId, deepSkyObjectTypeId, ORION));
////
////		verify(deepSkyObjectRepository, times(0)).save(m42);
////	}
////
////	@Test
////	public void testSaveDeepSkyObjectWhenNotExistsWithNullReturn() throws DeepSkyObjectAlreadyExistsException,
////			ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException {
////		Constellation orionSaved = new Constellation(1L, ORION);
////		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
////
////		when(constellationRepository.findById(orionSaved.getId())).thenReturn(orionSaved);
////		when(deepSkyObjectTypeRepository.findById(nebulaSaved.getId())).thenReturn(nebulaSaved);
////		when(deepSkyObjectRepository.save(any(DeepSkyObject.class))).thenReturn(null);
////
////		DeepSkyObject m42Saved = deepSkyObjectService.save(orionSaved.getId(), nebulaSaved.getId(), ORION);
////
////		verify(deepSkyObjectRepository).save(any(DeepSkyObject.class));
////		assertNull(m42Saved);
////	}
//
//	@Test
//	public void testSaveConstellationAndDeepSkyObjectWhenNotExists() throws DeepSkyObjectAlreadyExistsException,
//			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
//		Constellation orionSaved = new Constellation(1L, ORION);
//		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
//		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orionSaved, nebulaSaved);
//
//		when(constellationRepository.findByName(ORION)).thenReturn(null);
//		when(constellationRepository.save(any(Constellation.class))).thenReturn(orionSaved);
//
//		when(deepSkyObjectTypeRepository.findByType(NEBULA)).thenReturn(null);
//		when(deepSkyObjectTypeRepository.save(any(DeepSkyObjectType.class))).thenReturn(nebulaSaved);
//
//		when(deepSkyObjectRepository.save(any(DeepSkyObject.class))).thenReturn(m42Saved);
//
//		DeepSkyObject m42SavedFromService = deepSkyObjectService.saveConstellationAndDeepSkyObject(ORION, M42, NEBULA);
//
//		verify(deepSkyObjectRepository).save(any(DeepSkyObject.class));
//		assertNotNull(m42SavedFromService);
//		assertThat(m42SavedFromService.getId()).isPositive();
//	}
//
//	@Test
//	public void testSaveConstellationAndDeepSkyObjectWhenConstellationExists()
//			throws DeepSkyObjectAlreadyExistsException {
//		when(constellationRepository.findByName(ORION)).thenReturn(orion);
//
//		assertThrows(ConstellationAlreadyExistsException.class,
//				() -> deepSkyObjectService.saveConstellationAndDeepSkyObject(ORION, M42, NEBULA));
//
//		verify(deepSkyObjectRepository, times(0)).save(m42);
//	}
//
//	@Test
//	public void testSaveConstellationAndDeepSkyObjectWhenDeepSkyObjectTypeExists()
//			throws DeepSkyObjectAlreadyExistsException {
//		when(constellationRepository.findByName(ORION)).thenReturn(null);
//		when(deepSkyObjectTypeRepository.findByType(NEBULA)).thenReturn(nebula);
//
//		assertThrows(DeepSkyObjectTypeAlreadyExistsException.class,
//				() -> deepSkyObjectService.saveConstellationAndDeepSkyObject(ORION, M42, NEBULA));
//
//		verify(deepSkyObjectRepository, times(0)).save(m42);
//	}
//
//	@Test
//	public void testSaveConstellationAndDeepSkyObjectWithNullReturn() throws DeepSkyObjectAlreadyExistsException,
//			ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
//		Constellation orionSaved = new Constellation(1L, ORION);
//		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
//
//		when(constellationRepository.findByName(ORION)).thenReturn(null);
//		when(constellationRepository.save(any(Constellation.class))).thenReturn(orionSaved);
//
//		when(deepSkyObjectTypeRepository.findByType(NEBULA)).thenReturn(null);
//		when(deepSkyObjectTypeRepository.save(any(DeepSkyObjectType.class))).thenReturn(nebulaSaved);
//
//		when(deepSkyObjectRepository.save(any(DeepSkyObject.class))).thenReturn(null);
//
//		DeepSkyObject m42SavedFromService = deepSkyObjectService.saveConstellationAndDeepSkyObject(ORION, M42, NEBULA);
//
//		verify(deepSkyObjectRepository).save(any(DeepSkyObject.class));
//		assertNull(m42SavedFromService);
//	}
//
////	@Test
////	public void testUpdateDeepSkyObjectName() throws ConstellationAlreadyExistsException,
////			DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException,
////			ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectNotFoundException {
////		String m42NameChanged = M42 + " changed";
////
////		Constellation orionSaved = new Constellation(1L, ORION);
////		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
////		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orionSaved, nebulaSaved);
////		DeepSkyObject m42Updated = new DeepSkyObject(1L, m42NameChanged, orionSaved, nebulaSaved);
////
////		when(constellationRepository.findById(orionSaved.getId())).thenReturn(orionSaved);
////		when(deepSkyObjectTypeRepository.findById(nebulaSaved.getId())).thenReturn(nebulaSaved);
////		when(deepSkyObjectRepository.findById(m42Saved.getId())).thenReturn(m42Saved);
////		when(deepSkyObjectRepository.update(any(DeepSkyObject.class))).thenReturn(m42Updated);
////
////		DeepSkyObject m42SavedFromService = deepSkyObjectService.update(1L, m42NameChanged, orionSaved.getId(),
////				nebulaSaved.getId());
////
////		verify(deepSkyObjectRepository).update(any(DeepSkyObject.class));
////		assertNotNull(m42SavedFromService);
////	}
//	
//	@Test
//	public void testUpdateDeepSkyObjectWhenDeepSkyObjectNotExists() {
//		String m42NameChanged = M42 + " changed";
//
//		Constellation orionSaved = new Constellation(1L, ORION);
//		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
//		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orionSaved, nebulaSaved);
//
//		when(deepSkyObjectRepository.findById(m42Saved.getId())).thenReturn(null);
//
//		assertThrows(DeepSkyObjectNotFoundException.class,
//				() -> deepSkyObjectService.update(1L, m42NameChanged, orionSaved.getId(), nebulaSaved.getId()));
//
//		verify(deepSkyObjectRepository, times(0)).update(any(DeepSkyObject.class));
//	}
//
//	@Test
//	public void testUpdateDeepSkyObjectWhenConstellationNotExists() {
//		String m42NameChanged = M42 + " changed";
//
//		Constellation orionSaved = new Constellation(1L, ORION);
//		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
//		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orionSaved, nebulaSaved);
//
//		when(deepSkyObjectRepository.findById(m42Saved.getId())).thenReturn(m42Saved);
//		when(constellationRepository.findById(orionSaved.getId())).thenReturn(null);
//
//		assertThrows(ConstellationNotFoundException.class,
//				() -> deepSkyObjectService.update(1L, m42NameChanged, orionSaved.getId(), nebulaSaved.getId()));
//
//		verify(deepSkyObjectRepository, times(0)).update(any(DeepSkyObject.class));
//	}
//	
////	@Test
////	public void testUpdateDeepSkyObjectWhenDeepSkyObjectTypeNotExists() {
////		String m42NameChanged = M42 + " changed";
////
////		Constellation orionSaved = new Constellation(1L, ORION);
////		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
////		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orionSaved, nebulaSaved);
////
////		when(deepSkyObjectRepository.findById(m42Saved.getId())).thenReturn(m42Saved);
////		when(constellationRepository.findById(orionSaved.getId())).thenReturn(orionSaved);
////		when(deepSkyObjectTypeRepository.findById(nebulaSaved.getId())).thenReturn(null);
////
////		assertThrows(DeepSkyObjectTypeNotFoundException.class,
////				() -> deepSkyObjectService.update(1L, m42NameChanged, orionSaved.getId(), nebulaSaved.getId()));
////
////		verify(deepSkyObjectRepository, times(0)).update(any(DeepSkyObject.class));
////	}
//	
////	@Test
////	public void testUpdateDeepSkyObjectWhenNameIsEmpty() {
////		String m42NameChanged = null;
////
////		Constellation orionSaved = new Constellation(1L, ORION);
////		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
////		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, orionSaved, nebulaSaved);
////
////		when(constellationRepository.findById(orionSaved.getId())).thenReturn(orionSaved);
////		when(deepSkyObjectTypeRepository.findById(nebulaSaved.getId())).thenReturn(nebulaSaved);
////		when(deepSkyObjectRepository.findById(m42Saved.getId())).thenReturn(m42Saved);
////
////		assertThrows(DeepSkyObjectEmptyAttributeException.class,
////				() -> deepSkyObjectService.update(1L, m42NameChanged, orionSaved.getId(), nebulaSaved.getId()));
////
////		verify(deepSkyObjectRepository, times(0)).update(any(DeepSkyObject.class));
////	}
////	
////	@Test
////	public void testUpdateDeepSkyObjectWhenConstellationIsNull() {
////		String m42NameChanged = M42 + " changed";
////
////		Constellation orionSaved = new Constellation(1L, ORION);
////		DeepSkyObjectType nebulaSaved = new DeepSkyObjectType(1L, NEBULA);
////		DeepSkyObject m42Saved = new DeepSkyObject(1L, M42, null, nebulaSaved);
////
////		when(constellationRepository.findById(orionSaved.getId())).thenReturn(orionSaved);
////		when(deepSkyObjectTypeRepository.findById(nebulaSaved.getId())).thenReturn(nebulaSaved);
////		when(deepSkyObjectRepository.findById(m42Saved.getId())).thenReturn(m42Saved);
////
////		assertThrows(DeepSkyObjectEmptyAttributeException.class,
////				() -> deepSkyObjectService.update(1L, m42NameChanged, orionSaved.getId(), nebulaSaved.getId()));
////
////		verify(deepSkyObjectRepository, times(0)).update(any(DeepSkyObject.class));
////	}
//
//	@Test
//	public void testDeleteDeepSkyObjectWhenExists() throws DeepSkyObjectNotFoundException {
//		when(deepSkyObjectRepository.findById(1L)).thenReturn(null);
//
//		deepSkyObjectService.delete(1L);
//		verify(deepSkyObjectRepository).delete(1L);
//
//		assertThrows(DeepSkyObjectNotFoundException.class, () -> deepSkyObjectService.findById(1L));
//	}

}
