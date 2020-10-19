package it.attsd.deepsky.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;

@Deprecated
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ConstellationApiTest {
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test1GetAllConstellations() throws Exception {
		// Add two constellations
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(new Constellation("orion"));
		constellations.add(new Constellation("scorpius"));

		// GET ALL constellations
		Constellation[] constellationsFound = restTemplate.getForObject("/api/constellation", Constellation[].class);

		assertNotNull(constellationsFound);
		assertThat(constellationsFound.length).isEqualTo(2);
	}

}
