package it.attsd.deepsky.unit.repository;

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
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class DeepSkyObjectRepositoryTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private Query query;

	@InjectMocks
	private DeepSkyObjectRepository deepSkyObjectRepository;
	
	private final String M42 = "m42";
	private final String M43 = "m43";
	
	private final Constellation orion = new Constellation(1, "orion");
	private final DeepSkyObjectType nebula = new DeepSkyObjectType("nebula");
	private final DeepSkyObject m42 = new DeepSkyObject(1L, M42, orion, nebula);
	private final DeepSkyObject m43 = new DeepSkyObject(2, M43, orion, nebula);

	@Test
	public void testEmptyDeepSkyObjectTable() {
		String queryString = String.format("DELETE FROM %s", DeepSkyObject.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		deepSkyObjectRepository.emptyTable();

		verify(entityManager).createQuery(queryString);
		verify(query).executeUpdate();
		verify(entityManager).flush();
	}
	
	@Test
	public void testGetAllDeepSkyObjectsWhenDBIsEmpty() {
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
	public void testGetAllDeepSkyObjectsWhenContainsTwo() {
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
	public void testGetDeepSkyObjectByIdWhenIdIsPresent() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(m42);

		DeepSkyObject deepSkyObjectFound = deepSkyObjectRepository.findById(1L);

		verify(entityManager).find(DeepSkyObject.class, 1L);

		assertNotNull(deepSkyObjectFound);
	}

	@Test
	public void testGetDeepSkyObjectByIdWhenIdIsNotPresent() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(null);

		DeepSkyObject deepSkyObjectFound = deepSkyObjectRepository.findById(1L);

		verify(entityManager).find(DeepSkyObject.class, 1L);
		assertNull(deepSkyObjectFound);
	}
	
	@Test
	public void testGetDeepSkyObjectByNameWhenIsPresent() {
		String name = M42;

		String queryString = String.format("SELECT t FROM %s t WHERE t.name=:name", DeepSkyObject.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.setParameter("name", name)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(m42);

		DeepSkyObject m42Found = deepSkyObjectRepository.findByName(name);

		verify(entityManager).createQuery(queryString);
		verify(query).setParameter("name", name);
		verify(query).getSingleResult();

		assertNotNull(m42Found);
	}

	@Test
	public void testAddDeepSkyObjectWhenIsNotPresent() throws DeepSkyObjectAlreadyExistsException {
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				DeepSkyObject m42Saved = (DeepSkyObject) invocation.getArguments()[0];
				m42Saved.setId(1L);
				return null;
			}
		}).when(entityManager).persist(m42);

		DeepSkyObject m42Saved = deepSkyObjectRepository.save(m42);
		assertNotNull(m42Saved);
		assertThat(m42Saved.getId()).isPositive();
		
		verify(entityManager).persist(m42);
		verify(entityManager).flush();
	}

	@Test
	public void testAddDeepSkyObjectWhenIsAlreadyPresent() throws DeepSkyObjectAlreadyExistsException {
		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).persist(m42);
		
		assertThrows(IllegalStateException.class, () -> deepSkyObjectRepository.save(m42));
	}
	
	@Test
	public void testAddDeepSkyObjectWithPersistenceException() throws ConstellationAlreadyExistsException {
		PersistenceException exc = new PersistenceException();
		doThrow(exc).when(entityManager).persist(m42);

		assertThrows(PersistenceException.class, () -> deepSkyObjectRepository.save(m42));
	}
	
	@Test
	public void testUpdateDeepSkyObjectWhenExists() throws ConstellationAlreadyExistsException {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(m42);

		DeepSkyObject m42Found = deepSkyObjectRepository.findById(1L);

		verify(entityManager).find(DeepSkyObject.class, 1L);

		String nameChanged = m42Found.getName() + " changed";
		m42Found.setName(nameChanged);

		DeepSkyObject m42ChangedMock = new DeepSkyObject(1L, nameChanged, m42.getConstellation(), m42.getType());

		when(entityManager.merge(m42Found)).thenReturn(m42ChangedMock);

		DeepSkyObject m42Changed = deepSkyObjectRepository.update(m42Found);
		assertNotNull(m42Changed);
		verify(entityManager).merge(m42Found);
		verify(entityManager).flush();
	}

	@Test
	public void testDeleteDeepSkyObjectWhenIsNotPresent() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(null);
		
		deepSkyObjectRepository.delete(1L);
		
		verify(entityManager).find(DeepSkyObject.class, 1L);
		verify(entityManager, times(0)).remove(m42);
	}
	
	@Test
	public void testDeleteDeepSkyObjectWhenIsPresent() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(m42);
		
		deepSkyObjectRepository.delete(1L);
		
		verify(entityManager).find(DeepSkyObject.class, 1L);
		verify(entityManager).remove(m42);
		verify(entityManager).flush();
	}

	@Test
	public void testDeleteDeepSkyObjectWhenIsNull() {
		when(entityManager.find(DeepSkyObject.class, 1L)).thenReturn(null);
		
		deepSkyObjectRepository.delete(1L);
		
		verify(entityManager).find(DeepSkyObject.class, 1L);
		verify(entityManager, times(0)).remove(m42);
	}

}
