package it.attsd.deepsky.it;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ConstellationRestControllerIT {
	String ORION = "orion";
	String LIBRA = "libra";

	@Autowired
	private ConstellationRepository constellationRepository;

	@LocalServerPort
	private int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}

	@Test
	public void testSaveConstellation() throws Exception {
		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(new Constellation(ORION)).when()
				.post("/api/constellation");

		Constellation orionSaved = response.getBody().as(Constellation.class);

		assertThat(constellationRepository.findById(orionSaved.getId()).get()).isEqualTo(orionSaved);
	}

	@Test
	public void testUpdateConstellation() throws Exception {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

		String nameChanged = ORION + " changed";
		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(new Constellation(nameChanged)).when()
				.put("/api/constellation/" + orionSaved.getId()).then().statusCode(200).body(
						"id", equalTo(orionSaved.getId().intValue()), "name", equalTo(nameChanged));
	}

}
