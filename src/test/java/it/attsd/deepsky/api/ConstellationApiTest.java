package it.attsd.deepsky.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringBootTest
@AutoConfigureTestDatabase
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstellationApiTest {
//	@LocalServerPort
//	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String baseUrl = "http://localhost:8080/api/constellation";

	@Test
	public void test1GetAllConstellations() throws Exception {
		List<Constellation> constellations = restTemplate.getForObject(baseUrl, List.class);

		assertNotNull(constellations);
		assertThat(constellations.size()).isGreaterThan(0);
	}

}
