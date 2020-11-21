package it.attsd.deepsky.integration.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.pojo.ConstellationPojo;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ConstellationRestIT {
	@LocalServerPort
	private int port;

	@Autowired
	private ConstellationRepository constellationRepository;
	
	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private ConstellationService constellationService;
	
	private final String BASE_URL = "/api/constellation";

	private final String ORION = "orion";
	private final String SCORPIUS = "scorpius";

	@BeforeClass
	public static void init() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
	}

	@Before
	public void setup() {
		RestAssured.port = port;
		deepSkyObjectRepository.emptyTable();
		constellationRepository.emptyTable();
	}

	@Test
	public void testGetAllConstellations() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		Constellation scorpius = constellationService.save(new Constellation(SCORPIUS));
		assertNotNull(scorpius);

		given().accept(ContentType.JSON).when().get(BASE_URL).then().statusCode(200).body("size()", is(2));
	}

	@Test
	public void testGetConstellationById() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		given().accept(ContentType.JSON).when().get(BASE_URL + "/" + orion.getId()).then().statusCode(200)
				.body("id", is((int) orion.getId()));
	}

	@Test
	public void testSaveConstellationWhenNotExists() throws Exception {
		ConstellationPojo constellationSaveRequest = new ConstellationPojo();
		constellationSaveRequest.setName(ORION);

		String payload = new Gson().toJson(constellationSaveRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).post(BASE_URL).then()
				.statusCode(200).extract().response();
		assertNotNull(response.getBody());
		
		long addedConstellationId = response.jsonPath().getLong("id");
		String addedConstellationName = response.jsonPath().getString("name");
		assertEquals(addedConstellationName, ORION);

		Constellation orion = constellationService.findById(addedConstellationId);
		assertNotNull(orion);
	}
	
	@Test
	public void testSaveConstellationWhenAlreadyExists() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);
		
		ConstellationPojo constellationSaveRequest = new ConstellationPojo();
		constellationSaveRequest.setName(ORION);

		String payload = new Gson().toJson(constellationSaveRequest);

		given().contentType(ContentType.JSON).body(payload).post(BASE_URL).then()
				.statusCode(500).extract().response();
	}
	
	@Test
	public void testUpdateConstellation() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);
		
		String orionNameChanged = orion.getName() + " changed";
		
		ConstellationPojo constellationUpdateRequest = new ConstellationPojo();
		constellationUpdateRequest.setId(orion.getId());
		constellationUpdateRequest.setName(orionNameChanged);

		String payload = new Gson().toJson(constellationUpdateRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).put(BASE_URL).then()
				.statusCode(200).extract().response();
		assertNotNull(response.getBody());
		
		long updatedConstellationId = response.jsonPath().getLong("id");
		String updatedConstellationName = response.jsonPath().getString("name");
		assertEquals(updatedConstellationName, orionNameChanged);

		Constellation orionUpdated = constellationService.findById(updatedConstellationId);
		assertNotNull(orionUpdated);
		assertEquals(orionUpdated.getName(), orionNameChanged);
	}
	
	@Test
	public void testDeleteConstellation() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);
		
		given().contentType(ContentType.JSON).delete(BASE_URL + "/" + orion.getId()).then()
				.statusCode(200);
		
		assertThrows(ConstellationNotFoundException.class, () -> constellationService.findById(orion.getId()));
	}

}
