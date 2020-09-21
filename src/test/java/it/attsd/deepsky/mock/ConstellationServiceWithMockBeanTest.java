package it.attsd.deepsky.mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ConstellationService.class)
public class ConstellationServiceWithMockBeanTest {
	
	@MockBean
	private ConstellationRepository constellationRepository;

	@Autowired
	private ConstellationService constellationService;
	
	@Test
	public void testGetAll() {
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(new Constellation(1, "orion"));
		constellations.add(new Constellation(2, "scorpion"));
		when(constellationRepository.findAll()).thenReturn(constellations);
		
		assertThat(constellationService.getAll()).isEqualTo(constellations);
	}
	
	@Test
	public void testGetById() throws RepositoryException {
		Constellation orion = new Constellation(1, "orion");
		when(constellationRepository.findById(1)).thenReturn(orion);
		
		assertThat(constellationService.getById(1)).isEqualTo(orion);
	}
}
