package it.attsd.deepsky.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import it.attsd.deepsky.dto.ConstellationSaveRequest;
import it.attsd.deepsky.dto.ConstellationUpdateRequest;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ConstellationRestITTest {
	@LocalServerPort
	private int port;

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private ConstellationService constellationService;

	String ORION = "orion";
	String SCORPIUS = "scorpius";

	@BeforeClass
	public static void init() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
	}

	@Before
	public void setup() {
		RestAssured.port = port;
		constellationRepository.emptyTable();
	}

	@Test
	public void testGetAllConstellations() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		Constellation scorpius = constellationService.save(new Constellation(SCORPIUS));
		assertNotNull(scorpius);

		given().accept(ContentType.JSON).when().get("/api/constellation").then().statusCode(200).body("size()", is(2));
	}

	@Test
	public void testGetConstellationById() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		given().accept(ContentType.JSON).when().get("/api/constellation/" + orion.getId()).then().statusCode(200)
				.body("id", is((int) orion.getId()));
	}

	@Test
	public void testSaveConstellation() throws Exception {
		ConstellationSaveRequest constellationSaveRequest = new ConstellationSaveRequest();
		constellationSaveRequest.setName(ORION);

		String payload = new Gson().toJson(constellationSaveRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).post("/api/constellation").then()
				.statusCode(200).extract().response();
		assertNotNull(response.getBody());
		
		long addedConstellationId = response.jsonPath().getLong("id");
		String addedConstellationName = response.jsonPath().getString("name");
		assertEquals(addedConstellationName, ORION);

		Constellation orion = constellationService.findById(addedConstellationId);
		assertNotNull(orion);
	}
	
	@Test
	public void testUpdateConstellation() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);
		
		String orionNameChanged = orion.getName() + " changed";
		
		ConstellationUpdateRequest constellationUpdateRequest = new ConstellationUpdateRequest();
		constellationUpdateRequest.setId(orion.getId());
		constellationUpdateRequest.setName(orionNameChanged);

		String payload = new Gson().toJson(constellationUpdateRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).put("/api/constellation").then()
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
		
		given().contentType(ContentType.JSON).delete("/api/constellation/" + orion.getId()).then()
				.statusCode(200);
		
		Constellation orionDeleted = constellationService.findById(orion.getId());
		assertNull(orionDeleted);
	}

//	@Test
//	public void testRestAssured() {
//		JsonObject jsonObject = given().baseUri("http://dummy.restapiexample.com/").basePath("api/v1/employees").get()
//				.as(JsonObject.class);
//		System.out.println(jsonObject.toString());
//	}
//
//	@Test
//	public void testRestAssured2() {
//		given().log().all().when().get("http://www.google.com").then().statusCode(200);
//	}

}
