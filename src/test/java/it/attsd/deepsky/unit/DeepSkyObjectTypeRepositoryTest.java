package it.attsd.deepsky.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

	@Test
	public void test1GetAllDeepSkyObjectTypesWhenDBIsEmpty() {
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
	public void test2GetAllDeepSkyObjectTypesWhenContainsTwo() {
		DeepSkyObjectType star = new DeepSkyObjectType(1, "star");
		DeepSkyObjectType nebula = new DeepSkyObjectType(1, "nebula");

		List<DeepSkyObjectType> deepSkyObjectTypes = new ArrayList<DeepSkyObjectType>();
		deepSkyObjectTypes.add(star);
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
	public void test3DeepSkyObjectTypeByIdWhenIdIsPresent() throws GenericRepositoryException {
		DeepSkyObjectType orion = new DeepSkyObjectType(1L, "star");

		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(orion);

		DeepSkyObjectType deepSkyObjectTypeFound = deepSkyObjectTypeRepository.findById(1L);

		verify(entityManager, times(1)).find(DeepSkyObjectType.class, 1L);

		assertNotNull(deepSkyObjectTypeFound);
	}

	@Test
	public void test4DeepSkyObjectTypeByIdWhenIdIsNotPresent() throws GenericRepositoryException {
		when(entityManager.find(DeepSkyObjectType.class, 1L)).thenReturn(null);

		DeepSkyObjectType deepSkyObjectTypeFound = deepSkyObjectTypeRepository.findById(1L);

		verify(entityManager, times(1)).find(DeepSkyObjectType.class, 1L);

		assertNull(deepSkyObjectTypeFound);
	}

//	@Test
//	public void test5AddDeepSkyObjectTypeWhenIsNotPresent() {
//
//	}
//
//	@Test
//	public void test6AddDeepSkyObjectTypeWhenIsAlreadyPresent() {
//
//	}
//
//	@Test
//	public void test7RemoveDeepSkyObjectTypeWhenIsPresent() {
//
//	}
//
//	@Test
//	public void test8RemoveDeepSkyObjectTypeWhenIsNotPresent() {
//
//	}
//
//	@Test
//	public void test9RemoveDeepSkyObjectTypeWhenIsNull() {
//
//	}

}
