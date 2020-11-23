package it.attsd.deepsky.e2e.rest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

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
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.pojo.DeepSkyObjectTypePojo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class DeepSkyObjectTypeRestE2E {
	@LocalServerPort
	private int port;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	private final String BASE_URL = "/api/deepskyobjecttype";
	private final String NEBULA = "nebula";

	@BeforeClass
	public static void init() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
	}

	@Before
	public void setup() {
		RestAssured.port = port;
		deepSkyObjectRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
	}

	@Test
	public void testRestEndPoints() throws Exception {
		// Save new DeepSkyObjectType
		DeepSkyObjectTypePojo deepSkyObjectTypeSaveRequest = new DeepSkyObjectTypePojo();
		deepSkyObjectTypeSaveRequest.setType(NEBULA);

		String savePayload = new Gson().toJson(deepSkyObjectTypeSaveRequest);

		Response saveResponse = given().contentType(ContentType.JSON).body(savePayload).post(BASE_URL).then()
				.statusCode(200).extract().response();

		int nebulaId = saveResponse.body().path("id");
		assertThat(nebulaId).isPositive();

		// Read saved DeepSkyObjectType
		given().accept(ContentType.JSON).when().get(BASE_URL + "/" + nebulaId).then().statusCode(200)
				.assertThat().body("id", equalTo(nebulaId), "type", equalToIgnoringCase(NEBULA));
		
		// Update DeepSkyObjectType
		String nebulaType = saveResponse.body().path("type");
		String nebulaTypeChanged = nebulaType + " changed";

		DeepSkyObjectTypePojo deepSkyObjectTypeUpdateRequest = new DeepSkyObjectTypePojo();
		deepSkyObjectTypeUpdateRequest.setId(nebulaId);
		deepSkyObjectTypeUpdateRequest.setType(nebulaTypeChanged);

		String updatePayload = new Gson().toJson(deepSkyObjectTypeUpdateRequest);
		given().contentType(ContentType.JSON).body(updatePayload).put(BASE_URL).then().statusCode(200)
				.assertThat().body("id", equalTo(nebulaId), "type", equalToIgnoringCase(nebulaTypeChanged));
		
		// Delete DeepSkyObjectType
		given().contentType(ContentType.JSON).delete(BASE_URL + "/" + nebulaId).then().statusCode(200);
		
		// Check DeepSkyObjectType is deleted
		given().accept(ContentType.JSON).when().get(BASE_URL + "/" + nebulaId).then().statusCode(404);
	}

}
