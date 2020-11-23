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
import it.attsd.deepsky.pojo.ConstellationPojo;
import it.attsd.deepsky.pojo.DeepSkyObjectTypePojo;
import it.attsd.deepsky.pojo.DeepSkyObjectPojo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class DeepSkyObjectRestE2E {
	@LocalServerPort
	private int port;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	private final String BASE_URL_CONSTELLATION = "/api/constellation";
	private final String BASE_URL_DEEPSKYOBJECTTYPE = "/api/deepskyobjecttype";
	private final String BASE_URL_DEEPSKYOBJECT = "/api/deepskyobject";
	private final String ORION = "orion";
	private final String NEBULA = "nebula";
	private final String M42 = "m42";

	@BeforeClass
	public static void init() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
	}

	@Before
	public void setup() {
		RestAssured.port = port;
		deepSkyObjectRepository.emptyTable();
	}

	@Test
	public void testRestEndPoints() throws Exception {
		// Save new Constellation
		ConstellationPojo constellationSaveRequest = new ConstellationPojo();
		constellationSaveRequest.setName(ORION);

		String saveConstellationPayload = new Gson().toJson(constellationSaveRequest);

		Response saveConstellationResponse = given().contentType(ContentType.JSON).body(saveConstellationPayload)
				.post(BASE_URL_CONSTELLATION).then().statusCode(200).extract().response();

		int orionId = saveConstellationResponse.body().path("id");
		assertThat(orionId).isPositive();

		// Read saved Constellation
		given().accept(ContentType.JSON).when().get(BASE_URL_CONSTELLATION + "/" + orionId).then().statusCode(200)
				.assertThat().body("id", equalTo(orionId), "name", equalToIgnoringCase(ORION));

		// Save new DeepSkyObjectType
		DeepSkyObjectTypePojo deepSkyObjectTypeSaveRequest = new DeepSkyObjectTypePojo();
		deepSkyObjectTypeSaveRequest.setType(NEBULA);

		String saveDeepSkyObjectTypePayload = new Gson().toJson(deepSkyObjectTypeSaveRequest);

		Response saveDeepSkyObjectTypeResponse = given().contentType(ContentType.JSON)
				.body(saveDeepSkyObjectTypePayload).post(BASE_URL_DEEPSKYOBJECTTYPE).then().statusCode(200).extract()
				.response();

		int nebulaId = saveDeepSkyObjectTypeResponse.body().path("id");
		assertThat(nebulaId).isPositive();

		// Read saved DeepSkyObjectType
		given().accept(ContentType.JSON).when().get(BASE_URL_DEEPSKYOBJECTTYPE + "/" + nebulaId).then().statusCode(200)
				.assertThat().body("id", equalTo(nebulaId), "type", equalToIgnoringCase(NEBULA));

		// Save new DeepSkyObject
		DeepSkyObjectPojo deepSkyObjectSaveRequest = new DeepSkyObjectPojo();
		deepSkyObjectSaveRequest.setConstellationId(orionId);
		deepSkyObjectSaveRequest.setDeepSkyObjectTypeId(nebulaId);
		deepSkyObjectSaveRequest.setName(M42);

		String saveDeepSkyObjectPayload = new Gson().toJson(deepSkyObjectSaveRequest);

		Response savedeepSkyObjectResponse = given().contentType(ContentType.JSON)
				.body(saveDeepSkyObjectPayload).post(BASE_URL_DEEPSKYOBJECT).then().statusCode(200).extract()
				.response();

		int m42Id = savedeepSkyObjectResponse.body().path("id");
		assertThat(nebulaId).isPositive();

		// Read saved DeepSkyObject
		given().accept(ContentType.JSON).when().get(BASE_URL_DEEPSKYOBJECT + "/" + m42Id).then().statusCode(200)
				.assertThat().body("id", equalTo(m42Id), "name", equalToIgnoringCase(M42));

		// Update DeepSkyObject
		String m42Name = savedeepSkyObjectResponse.body().path("name");
		String m42NameChanged = m42Name + " changed";

		DeepSkyObjectPojo deepSkyObjectUpdateRequest = new DeepSkyObjectPojo();
		deepSkyObjectUpdateRequest.setId(m42Id);
		deepSkyObjectUpdateRequest.setConstellationId(orionId);
		deepSkyObjectUpdateRequest.setDeepSkyObjectTypeId(nebulaId);
		deepSkyObjectUpdateRequest.setName(m42NameChanged);

		String updateDeepSkyObjectPayload = new Gson().toJson(deepSkyObjectUpdateRequest);
		given().contentType(ContentType.JSON).body(updateDeepSkyObjectPayload).put(BASE_URL_DEEPSKYOBJECT).then().statusCode(200).assertThat()
				.body("id", equalTo(m42Id), "name", equalToIgnoringCase(m42NameChanged));

		// Delete DeepSkyObjectType
		given().contentType(ContentType.JSON).delete(BASE_URL_DEEPSKYOBJECT + "/" + m42Id).then().statusCode(200);

		// Check DeepSkyObjectType is deleted
		given().accept(ContentType.JSON).when().get(BASE_URL_DEEPSKYOBJECT + "/" + m42Id).then().statusCode(404);
	}

}
