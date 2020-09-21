package it.attsd.deepsky.mock;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ConstellationService.class)
public class ConstellationServiceTest {
	
	@Mock
	private ConstellationRepository constellationRepository;

	@InjectMocks
	private ConstellationService constellationService;

	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void testGetAll() {
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(new Constellation(1, "orion"));
		when(constellationRepository.findAll()).thenReturn(constellations);
		
		assertThat(constellationService.getAll()).isEqualTo(constellations);
	}
	
	@Test
	public void testGetById() {
		Constellation orion = new Constellation(1, "orion");
		when(constellationRepository.findById(1)).thenReturn(orion);
		
		assertThat(constellationService.getById(1)).isEqualTo(orion);
	}
}
