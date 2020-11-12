package it.attsd.deepsky.unit.model;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ConstellationRepositoryTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private Query query;

	@InjectMocks
	private ConstellationRepository constellationRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEmptyConstellationTable() {
		String queryString = String.format("DELETE FROM %s", Constellation.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		constellationRepository.emptyTable();

		verify(entityManager).createQuery(queryString);
		verify(query).executeUpdate();
		verify(entityManager).flush();
	}

	@Test
	public void testGetAllConstellationsWhenDBIsEmpty() {
		String queryString = String.format("SELECT t FROM %s t", Constellation.class.getName());
		List<Constellation> constellations = new ArrayList<Constellation>();

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(constellations);
		List<Constellation> constellationsResult = constellationRepository.findAll();

		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();

		assertThat(constellationsResult.isEmpty()).isTrue();
	}

	@Test
	public void testGetAllConstellationsWhenContainsTwo() {
		Constellation orion = new Constellation(1, "orion");
		Constellation scorpion = new Constellation(1, "scorpion");

		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(orion);
		constellations.add(scorpion);

		String queryString = String.format("SELECT t FROM %s t", Constellation.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(constellations);
		List<Constellation> constellationsResult = constellationRepository.findAll();

		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();

		assertEquals(2, constellationsResult.size());
	}

	@Test
	public void testGetConstellationByIdWhenIdIsPresent() {
		Constellation orion = new Constellation(1L, "orion");

		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);

		Constellation constellationFound = constellationRepository.findById(1L);

		verify(entityManager).find(Constellation.class, 1L);

		assertNotNull(constellationFound);
	}

	@Test
	public void testGetConstellationByIdWhenIdIsNotPresent() {
		when(entityManager.find(Constellation.class, 1L)).thenReturn(null);

		Constellation constellationFound = constellationRepository.findById(1L);

		verify(entityManager).find(Constellation.class, 1L);

		assertNull(constellationFound);
	}

	@Test
	public void testGetConstellationByNameWhenIsPresent() {
		String name = "orion";
		Constellation orion = new Constellation(1L, name);

		String queryString = String.format("SELECT t FROM %s t WHERE t.name=:name", Constellation.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.setParameter("name", name)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(orion);

		Constellation constellationFound = constellationRepository.findByName(name);

		verify(entityManager).createQuery(queryString);
		verify(query).setParameter("name", name);
		verify(query).getSingleResult();

		assertNotNull(constellationFound);
	}

	@Test
	public void testGetConstellationByNameWhenIsNotPresent() {
		String name = "orion";
		String queryString = String.format("SELECT t FROM %s t WHERE t.name=:name", Constellation.class.getName());

		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.setParameter("name", name)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(null);

		Constellation constellationFound = constellationRepository.findByName(name);

		verify(entityManager).createQuery(queryString);
		verify(query).setParameter("name", name);
		verify(query).getSingleResult();

		assertNull(constellationFound);
	}

	@Test
	public void testAddConstellationWhenNotExists() throws ConstellationAlreadyExistsException {
		Constellation orion = new Constellation("orion");

		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				Constellation orionSaved = (Constellation) invocation.getArguments()[0];
				orionSaved.setId(1L);
				return null;
			}
		}).when(entityManager).persist(orion);

		Constellation orionSaved = constellationRepository.save(orion);
		assertNotNull(orionSaved);
		assertThat(orionSaved.getId()).isPositive();
		
		verify(entityManager).persist(orion);
		verify(entityManager).flush();
	}

	@Test
	public void testAddConstellationWhenAlreadyExists() throws ConstellationAlreadyExistsException {
		Constellation orion = new Constellation("orion");

		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).persist(orion);

		assertThrows(IllegalStateException.class, () -> constellationRepository.save(orion));
	}
	
	@Test
	public void testAddConstellationWithPersistenceException() throws ConstellationAlreadyExistsException {
		Constellation orion = new Constellation("orion");

		PersistenceException exc = new PersistenceException();
		doThrow(exc).when(entityManager).persist(orion);

		assertThrows(PersistenceException.class, () -> constellationRepository.save(orion));
	}

	@Test
	public void testUpdateConstellationWhenExists() throws ConstellationAlreadyExistsException {
		Constellation orion = new Constellation(1L, "orion");

		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);

		Constellation orionFound = constellationRepository.findById(1L);

		verify(entityManager).find(Constellation.class, 1L);

		String nameChanged = orionFound.getName() + " changed";
		orionFound.setName(nameChanged);

		Constellation orionChangedMock = new Constellation(1L, nameChanged);

		when(entityManager.merge(orionFound)).thenReturn(orionChangedMock);

		Constellation orionChanged = constellationRepository.update(orionFound);
		assertNotNull(orionChanged);
		verify(entityManager).merge(orionFound);
		verify(entityManager).flush();
	}

	@Test
	public void testDeleteConstellationWhenNotExists() {
		when(entityManager.find(Constellation.class, 1L)).thenReturn(null);

		constellationRepository.delete(1L);

		verify(entityManager).find(Constellation.class, 1L);
		verify(entityManager, times(0)).remove(1L);
	}

	@Test
	public void testDeleteConstellationWhenExists() {
		Constellation orion = new Constellation(1L, "orion");
		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);

		constellationRepository.delete(1L);

		verify(entityManager).find(Constellation.class, 1L);
		verify(entityManager).remove(orion);
		verify(entityManager).flush();
	}

}
