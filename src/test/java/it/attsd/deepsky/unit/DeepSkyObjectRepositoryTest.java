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

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.model.DeepSkyObjectRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeepSkyObjectRepositoryTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private Query query;

	@InjectMocks
	private DeepSkyObjectRepository deepSkyObjectRepository;
	
	Constellation orion = new Constellation(1, "orion");
	DeepSkyObjectType nebula = new DeepSkyObjectType("nebula");
	DeepSkyObject m42 = new DeepSkyObject(1L, "m42", orion, nebula);
	DeepSkyObject m43 = new DeepSkyObject(2, "m43", orion, nebula);

	@Test
	public void test1GetAllDeepSkyObjectsWhenDBIsEmpty() {
		String queryString = String.format("SELECT t FROM %s t", DeepSkyObject.class.getName());
		List<DeepSkyObject> deepSkyObjects = new ArrayList<DeepSkyObject>();

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(deepSkyObjects);
		List<DeepSkyObject> deepSkyObjectsResult = deepSkyObjectRepository.findAll();

		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();

		assertThat(deepSkyObjectsResult.isEmpty()).isTrue();
	}

	@Test
	public void test2GetAllDeepSkyObjectsWhenContainsTwo() {
		List<DeepSkyObject> deepSkyObjects = new ArrayList<DeepSkyObject>();
		deepSkyObjects.add(m42);
		deepSkyObjects.add(m43);

		String queryString = String.format("SELECT t FROM %s t", DeepSkyObject.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(deepSkyObjects);
		List<DeepSkyObject> deepSkyObjectsResult = deepSkyObjectRepository.findAll();

		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();

		assertEquals(2, deepSkyObjectsResult.size());
	}

	@Test
	public void test3GetDeepSkyObjectByIdWhenIdIsPresent() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(m42);

		DeepSkyObject deepSkyObjectFound = deepSkyObjectRepository.findById(1L);

		verify(entityManager, times(1)).find(DeepSkyObject.class, 1L);

		assertNotNull(deepSkyObjectFound);
	}

	@Test
	public void test4GetDeepSkyObjectByIdWhenIdIsNotPresent() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(null);

		DeepSkyObject deepSkyObjectFound = deepSkyObjectRepository.findById(1L);

		verify(entityManager, times(1)).find(DeepSkyObject.class, 1L);

		assertNull(deepSkyObjectFound);
	}

	@Test
	public void test5AddDeepSkyObjectWhenIsNotPresent() throws DeepSkyObjectAlreadyExistsException {
		deepSkyObjectRepository.save(m42);
		verify(entityManager, times(1)).persist(m42);
	}

	@Test
	public void testAddDeepSkyObjectWhenIsAlreadyPresent() throws DeepSkyObjectAlreadyExistsException {
		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).persist(m42);
		
		assertThrows(IllegalStateException.class, () -> deepSkyObjectRepository.save(m42));
	}

	@Test
	public void testDeleteDeepSkyObjectWhenIsNotPresent() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(null);
		
		deepSkyObjectRepository.delete(1L);
		
		verify(entityManager, times(1)).find(DeepSkyObject.class, 1L);
		verify(entityManager, times(0)).remove(m42);
	}
	
	@Test
	public void testDeleteDeepSkyObject() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(m42);
		
		deepSkyObjectRepository.delete(1L);
		
		verify(entityManager, times(1)).find(DeepSkyObject.class, 1L);
		verify(entityManager, times(1)).remove(m42);
	}

	@Test
	public void testDeleteDeepSkyObjectWhenIsNull() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(null);
		
		deepSkyObjectRepository.delete(1L);
		
		verify(entityManager, times(1)).find(DeepSkyObject.class, 1L);
		verify(entityManager, times(0)).remove(m42);
	}

}
