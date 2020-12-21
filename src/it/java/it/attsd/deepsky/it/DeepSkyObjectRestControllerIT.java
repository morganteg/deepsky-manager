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
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DeepSkyObjectRestControllerIT {
//	String ORION = "orion";
//	String M42 = "m42";
//
//	@Autowired
//	private ConstellationRepository constellationRepository;
//
//	@Autowired
//	private DeepSkyObjectRepository deepSkyObjectRepository;
//
//	@LocalServerPort
//	private int port;
//
//	@Before
//	public void setup() {
//		RestAssured.port = port;
//	}
//
//	@Test
//	public void testSaveDeepSkyObject() throws Exception {
//		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
//
//		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(new DeepSkyObject(M42, orionSaved)).when()
//				.post("/api/deepskyobject");
//
//		DeepSkyObject m42Saved = response.getBody().as(DeepSkyObject.class);
//
//		assertThat(deepSkyObjectRepository.findById(m42Saved.getId()).get()).isEqualTo(m42Saved);
//	}
//
//	@Test
//	public void testUpdateDeepSkyObject() throws Exception {
//		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
//		DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orionSaved));
//
//		String nameChanged = M42 + " changed";
//		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(new DeepSkyObject(nameChanged, orionSaved)).when()
//				.put("/api/deepskyobject/" + m42Saved.getId()).then().statusCode(200).body(
//						"id", equalTo(m42Saved.getId().intValue()), "name", equalTo(nameChanged), "constellation.id", equalTo(orionSaved.getId().intValue()));
//	}

}
