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

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;

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
	public void testEmptyDeepSkyObjectTypeTable() {
		String queryString = String.format("DELETE FROM %s", DeepSkyObjectType.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		deepSkyObjectTypeRepository.emptyTable();

		verify(entityManager).createQuery(queryString);
		verify(query).executeUpdate();
		verify(entityManager).flush();
	}
	
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
	public void testGetDeepSkyObjectTypeByIdWhenExists() {
		DeepSkyObjectType orion = new DeepSkyObjectType(1L, GALAXY);

		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(orion);

		DeepSkyObjectType deepSkyObjectTypeFound = deepSkyObjectTypeRepository.findById(1L);

		verify(entityManager).find(DeepSkyObjectType.class, 1L);

		assertNotNull(deepSkyObjectTypeFound);
	}

	@Test
	public void testGetDeepSkyObjectTypeByIdWhenNotExists() {
		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(null);

		DeepSkyObjectType deepSkyObjectTypeFound = deepSkyObjectTypeRepository.findById(1L);

		verify(entityManager).find(DeepSkyObjectType.class, 1L);

		assertNull(deepSkyObjectTypeFound);
	}
	
	@Test
	public void testGetDeepSkyObjectTypeByTypeWhenExists() {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(1L, GALAXY);

		String queryString = String.format("SELECT t FROM %s t WHERE t.type=:type", DeepSkyObjectType.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.setParameter("type", GALAXY)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(galaxy);

		DeepSkyObjectType galaxyFound = deepSkyObjectTypeRepository.findByType(GALAXY);

		verify(entityManager).createQuery(queryString);
		verify(query).setParameter("type", GALAXY);
		verify(query).getSingleResult();

		assertNotNull(galaxyFound);
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenNotExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(GALAXY);
		
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				DeepSkyObjectType galaxySaved = (DeepSkyObjectType) invocation.getArguments()[0];
				galaxySaved.setId(1L);
				return null;
			}
		}).when(entityManager).persist(galaxy);

		DeepSkyObjectType galaxySaved = deepSkyObjectTypeRepository.save(galaxy);
		assertNotNull(galaxySaved);
		assertThat(galaxySaved.getId()).isPositive();
		
		verify(entityManager).persist(galaxy);
		verify(entityManager).flush();
	}

	@Test
	public void testAddDeepSkyObjectTypeWhenAlreadyExists() {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(GALAXY);
		
		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).persist(galaxy);

		assertThrows(IllegalStateException.class, () -> deepSkyObjectTypeRepository.save(galaxy));
	}
	
	@Test
	public void testAddDeepSkyObjectTypeWithPersistenceException() throws ConstellationAlreadyExistsException {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(GALAXY);

		PersistenceException exc = new PersistenceException();
		doThrow(exc).when(entityManager).persist(galaxy);

		assertThrows(PersistenceException.class, () -> deepSkyObjectTypeRepository.save(galaxy));
	}
	
	@Test
	public void testUpdateDeepSkyObjectTypeWhenExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(1L, GALAXY);

		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(galaxy);

		DeepSkyObjectType galaxyFound = deepSkyObjectTypeRepository.findById(1L);

		verify(entityManager).find(DeepSkyObjectType.class, 1L);

		String typeChanged = galaxyFound.getType() + " changed";
		galaxyFound.setType(typeChanged);

		DeepSkyObjectType galaxyChangedMock = new DeepSkyObjectType(1L, typeChanged);

		when(entityManager.merge(galaxyFound)).thenReturn(galaxyChangedMock);

		DeepSkyObjectType galaxyChanged = deepSkyObjectTypeRepository.update(galaxyFound);
		assertNotNull(galaxyChanged);
		verify(entityManager).merge(galaxyFound);
		verify(entityManager).flush();
	}

	@Test
	public void testDeleteDeepSkyObjectTypeWhenNotExists() {
		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(null);

		deepSkyObjectTypeRepository.delete(1L);
		
		verify(entityManager).find(DeepSkyObjectType.class, 1L);
		verify(entityManager, times(0)).remove(1L);
	}

	@Test
	public void testDeleteDeepSkyObjectTypeWhenExists() {
		DeepSkyObjectType galaxy = new DeepSkyObjectType(1L, GALAXY);
		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(galaxy);

		deepSkyObjectTypeRepository.delete(1L);

		verify(entityManager).find(DeepSkyObjectType.class, 1L);
		verify(entityManager).remove(galaxy);
		verify(entityManager).flush();
	}

}
