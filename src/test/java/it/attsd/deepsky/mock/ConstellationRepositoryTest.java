package it.attsd.deepsky.mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration(classes = ConstellationService.class)
@DataJpaTest
public class ConstellationRepositoryTest {
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private Query query;
	
	@InjectMocks
	private ConstellationRepository constellationRepository;
	
	@Test
	public void testGetAllConstellationsWhenDBIsEmpty() {
		String queryString = String.format("SELECT c FROM %s c", Constellation.TABLE_NAME);
		List<Constellation> constellations = new ArrayList<Constellation>();
		
		when(entityManager.createQuery(queryString)).thenReturn(query);
		when(query.getResultList()).thenReturn(constellations);
		List<Constellation> constellationsResult = constellationRepository.findAll();
		
		verify(entityManager).createQuery(queryString);
		verify(query).getResultList();
		
		assertThat(constellationsResult.isEmpty()).isTrue();
	}
	
	@Test
	public void testFindAll() {
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(new Constellation(1, "orion"));
		when(constellationRepository.findAll()).thenReturn(constellations);
		
		assertThat(constellationRepository.findAll()).isEqualTo(constellations);
	}
	
	@Test
	public void testFindById() {
		Constellation orion = new Constellation(1, "orion");
		when(constellationRepository.findById(1)).thenReturn(orion);
		
		assertThat(constellationRepository.findById(1)).isEqualTo(orion);
		assertThat(constellationRepository.findById(2)).isNull();
	}
	
}
