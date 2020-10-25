package it.attsd.deepsky.integration.rest;

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
import it.attsd.deepsky.dto.DeepSkyObjectTypeSaveRequest;
import it.attsd.deepsky.dto.DeepSkyObjectTypeUpdateRequest;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class DeepSkyObjectTypeRestITTest {
	@LocalServerPort
	private int port;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;
	
	String baseUrl = "/api/deepskyobjecttype";

	String GALAXY = "galaxy";
	String NEBULA = "nebula";

	@BeforeClass
	public static void init() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
	}

	@Before
	public void setup() {
		RestAssured.port = port;
		deepSkyObjectTypeRepository.emptyTable();
	}

	@Test
	public void testGetAllDeepSkyObjectTypes() throws Exception {
		DeepSkyObjectType galaxy = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxy);

		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		given().accept(ContentType.JSON).when().get(baseUrl).then().statusCode(200).body("size()", is(2));
	}

	@Test
	public void testGetDeepSkyObjectTypeById() throws Exception {
		DeepSkyObjectType galaxy = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxy);

		given().accept(ContentType.JSON).when().get(baseUrl + "/" + galaxy.getId()).then().statusCode(200)
				.body("id", is((int) galaxy.getId()));
	}

	@Test
	public void testSaveDeepSkyObjectType() throws Exception {
		DeepSkyObjectTypeSaveRequest deepSkyObjectTypeSaveRequest = new DeepSkyObjectTypeSaveRequest();
		deepSkyObjectTypeSaveRequest.setType(GALAXY);

		String payload = new Gson().toJson(deepSkyObjectTypeSaveRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).post(baseUrl).then()
				.statusCode(200).extract().response();
		assertNotNull(response.getBody());
		
		long addedDeepSkyObjectTypeId = response.jsonPath().getLong("id");
		String addedDeepSkyObjectTypeName = response.jsonPath().getString("type");
		assertEquals(addedDeepSkyObjectTypeName, GALAXY);

		DeepSkyObjectType deepSkyObjectTypeFound = deepSkyObjectTypeService.findById(addedDeepSkyObjectTypeId);
		assertNotNull(deepSkyObjectTypeFound);
	}
	
	@Test
	public void testUpdateDeepSkyObjectType() throws Exception {
		DeepSkyObjectType galaxy = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxy);
		
		String galaxyTypeChanged = galaxy.getType() + " changed";
		
		DeepSkyObjectTypeUpdateRequest deepSkyObjectTypeUpdateRequest = new DeepSkyObjectTypeUpdateRequest();
		deepSkyObjectTypeUpdateRequest.setId(galaxy.getId());
		deepSkyObjectTypeUpdateRequest.setType(galaxyTypeChanged);

		String payload = new Gson().toJson(deepSkyObjectTypeUpdateRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).put(baseUrl).then()
				.statusCode(200).extract().response();
		assertNotNull(response.getBody());
		
		long updatedDeepSkyObjectTypeId = response.jsonPath().getLong("id");
		String updatedDeepSkyObjectTypeType = response.jsonPath().getString("type");
		assertEquals(updatedDeepSkyObjectTypeType, galaxyTypeChanged);

		DeepSkyObjectType galaxyUpdated = deepSkyObjectTypeService.findById(updatedDeepSkyObjectTypeId);
		assertNotNull(galaxyUpdated);
		assertEquals(galaxyUpdated.getType(), galaxyTypeChanged);
	}
	
	@Test
	public void testDeleteDeepSkyObjectType() throws Exception {
		DeepSkyObjectType galaxy = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxy);
		
		given().contentType(ContentType.JSON).delete(baseUrl + "/" + galaxy.getId()).then()
				.statusCode(200);
		
		DeepSkyObjectType galaxyDeleted = deepSkyObjectTypeService.findById(galaxy.getId());
		assertNull(galaxyDeleted);
	}

}
