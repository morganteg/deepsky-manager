package it.attsd.deepsky.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.GenericRepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
	public void test1GetAllConstellationsWhenDBIsEmpty() {
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
	public void test2GetAllConstellationsWhenContainsTwo() {
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
	public void test3GetConstellationByIdWhenIdIsPresent() throws GenericRepositoryException {
		Constellation orion = new Constellation(1L, "orion");

		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);

		Constellation constellationFound = constellationRepository.findById(1L);

		verify(entityManager, times(1)).find(Constellation.class, 1L);

		assertNotNull(constellationFound);
	}

	@Test
	public void test4GetConstellationByIdWhenIdIsNotPresent() throws GenericRepositoryException {
		when(entityManager.find(Constellation.class, 1L)).thenReturn(null);
		
//		NoResultException noResultException = new NoResultException();
//		doThrow(noResultException).when(entityManager).find(Constellation.class, 1L);

		Constellation constellationFound = constellationRepository.findById(1L);

		verify(entityManager, times(1)).find(Constellation.class, 1L);

		assertNull(constellationFound);
	}

	@Test
	public void testGetConstellationByNameWhenIsPresent() throws GenericRepositoryException {
		String name = "orion";
		Constellation orion = new Constellation(1L, name);
		
		String queryString = String.format("SELECT t FROM %s t WHERE t.name=:name", Constellation.class.getName());
		
		when(entityManager
				.createQuery(queryString))
						.thenReturn(query);
		when(query.setParameter("name", name)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(orion);

		Constellation constellationFound = constellationRepository.findByName(name);

		verify(entityManager).createQuery(queryString);
		verify(query).setParameter("name", name);
		verify(query).getSingleResult();

		assertNotNull(constellationFound);
	}
	
	@Test
	public void testGetConstellationByNameWhenIsNotPresent() throws GenericRepositoryException {
		String name = "orion";
		String queryString = String.format("SELECT t FROM %s t WHERE t.name=:name", Constellation.class.getName());
		
		when(entityManager
				.createQuery(queryString))
						.thenReturn(query);
		when(query.setParameter("name", name)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(null);

		Constellation constellationFound = constellationRepository.findByName(name);

		verify(entityManager).createQuery(queryString);
		verify(query).setParameter("name", name);
		verify(query).getSingleResult();

		assertNull(constellationFound);
	}

	@Test
	public void test6AddConstellationWhenIsNotPresent() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orion = new Constellation(1L, "orion");

		constellationRepository.save(orion);
		verify(entityManager, times(1)).persist(orion);
	}

	@Test(expected = GenericRepositoryException.class)
	public void test7AddConstellationWhenIsAlreadyPresent() throws GenericRepositoryException, ConstellationAlreadyExistsException {
		Constellation orion = new Constellation(1L, "orion");

		IllegalStateException exc = new IllegalStateException();
		doThrow(exc).when(entityManager).persist(orion);

		constellationRepository.save(orion);
//		verify(entityManager, times(1)).persist(orion);
	}

	@Test
	public void testDeleteConstellationWhenIsNotPresent() throws GenericRepositoryException {
		when(entityManager.find(Constellation.class, 1L)).thenReturn(null);

//		IllegalStateException exc = new IllegalStateException();
//		doThrow(exc).when(entityManager).remove(null);

		constellationRepository.delete(1L);
		
		verify(entityManager, times(1)).find(Constellation.class, 1L);
		verify(entityManager, times(0)).remove(1L);
	}

	@Test
	public void test9RemoveConstellationWhenIsPresent() throws GenericRepositoryException {
		Constellation orion = new Constellation(1L, "orion");
		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);

		constellationRepository.delete(1L);

		verify(entityManager, times(1)).find(Constellation.class, 1L);
		verify(entityManager, times(1)).remove(orion);
	}

//	@Test(expected = GenericRepositoryException.class)
//	public void testDeleteConstellationWhenIsNull() throws GenericRepositoryException {
//		IllegalStateException exc = new IllegalStateException();
//		doThrow(exc).when(entityManager).remove(null);
//
//		constellationRepository.delete(0);
//	}

}
