package it.attsd.deepsky.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.dto.ConstellationList;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {TestRestTemplate.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstellationApiTest {
//	@LocalServerPort
//	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

//	private String baseUrl = "http://localhost:8080/api/constellation";

//	@Mock
//	private EntityManager entityManager;
//
//	@InjectMocks
//	private ConstellationRepository constellationRepository;

	@Test
	public void test1GetAllConstellations() throws Exception {
		// Add two constellations
//		List<Constellation> constellations = new ArrayList<Constellation>();
//		constellations.add(new Constellation("orion"));
//		constellations.add(new Constellation("scorpius"));
//
////		when(constellationRepository.findAll()).thenReturn(constellations);
//
//		ConstellationList constellationsFound = restTemplate.getForObject("/api/constellation", ConstellationList.class);
//
////		when().get("/api/constellation").then().statusCode(200).assertThat().body(equalTo("" + employees.size()));
//
////		assertNotNull(constellationsFound);
////		assertNotNull(constellationsFound.getConstellations());
////		assertThat(constellationsFound.getConstellations().size()).isEqualTo(2);
	}

}
