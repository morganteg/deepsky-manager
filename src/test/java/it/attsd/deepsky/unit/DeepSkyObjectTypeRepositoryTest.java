package it.attsd.deepsky.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.GenericRepositoryException;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectTypeRepositoryTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private Query query;

	@InjectMocks
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;
	
	private final String GALAXY = "galaxy";
	private final String NEBULA = "nebula";

	@Test
	public void testGetAllDeepSkyObjectTypesWhenDBIsEmpty() {
		String queryString = String.format("SELECT t FROM %s t", DeepSkyObjectType.class.getName());
		List<DeepSkyObjectType> deepSkyObjectTypes = new ArrayList<DeepSkyObjectType>();

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(deepSkyObjectTypes);
		List<DeepSkyObjectType> deepSkyObjectTypesResult = deepSkyObjectTypeRepository.findAll();

		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();

		assertThat(deepSkyObjectTypesResult.isEmpty()).isTrue();
	}

	@Test
	public void testGetAllDeepSkyObjectTypesWhenContainsTwo() {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(1L, GALAXY);
		DeepSkyObjectType nebula = new DeepSkyObjectType(2L, NEBULA);

		List<DeepSkyObjectType> deepSkyObjectTypes = new ArrayList<DeepSkyObjectType>();
		deepSkyObjectTypes.add(galaxy);
		deepSkyObjectTypes.add(nebula);

		String queryString = String.format("SELECT t FROM %s t", DeepSkyObjectType.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(deepSkyObjectTypes);
		List<DeepSkyObjectType> deepSkyObjectTypesResult = deepSkyObjectTypeRepository.findAll();

		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();

		assertEquals(2, deepSkyObjectTypesResult.size());
	}

	@Test
	public void testGetDeepSkyObjectTypeByIdWhenExists() throws GenericRepositoryException {
		DeepSkyObjectType orion = new DeepSkyObjectType(1L, GALAXY);

		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(orion);

		DeepSkyObjectType deepSkyObjectTypeFound = deepSkyObjectTypeRepository.findById(1L);

		verify(entityManager, times(1)).find(DeepSkyObjectType.class, 1L);

		assertNotNull(deepSkyObjectTypeFound);
	}

	@Test
	public void testGetDeepSkyObjectTypeByIdWhenNotExists() throws GenericRepositoryException {
		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(null);

		DeepSkyObjectType deepSkyObjectTypeFound = deepSkyObjectTypeRepository.findById(1L);

		verify(entityManager, times(1)).find(DeepSkyObjectType.class, 1L);

		assertNull(deepSkyObjectTypeFound);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenNotExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(GALAXY);

		deepSkyObjectTypeRepository.save(galaxy);
		verify(entityManager, times(1)).persist(galaxy);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenAlreadyExists() {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(GALAXY);
		
		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).persist(galaxy);

		assertThrows(IllegalStateException.class, () -> deepSkyObjectTypeRepository.save(galaxy));
	}
	
	@Test
	public void testUpdateDeepSkyObjectTypeWhenExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(1L, GALAXY + " changed");

		deepSkyObjectTypeRepository.update(galaxy);
		verify(entityManager, times(1)).merge(galaxy);
	}

	@Test
	public void testDeleteDeepSkyObjectTypeWhenNotExists() {
		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(null);

		deepSkyObjectTypeRepository.delete(1L);
		
		verify(entityManager, times(1)).find(DeepSkyObjectType.class, 1L);
		verify(entityManager, times(0)).remove(1L);
	}

	@Test
	public void testDeleteDeepSkyObjectTypeWhenExists() {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(1L, GALAXY);
		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(galaxy);

		deepSkyObjectTypeRepository.delete(1L);

		verify(entityManager, times(1)).find(DeepSkyObjectType.class, 1L);
		verify(entityManager, times(1)).remove(galaxy);
	}

}
