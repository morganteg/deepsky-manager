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
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.pojo.DeepSkyObjectPojo;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class DeepSkyObjectRestITTest {
	@LocalServerPort
	private int port;

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private ConstellationService constellationService;

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	private final String BASE_URL = "/api/deepskyobject";

	private final String ORION = "orion";
	private final String NEBULA = "nebula";
	private final String M42 = "m42";
	private final String M43 = "m43";

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
		constellationRepository.emptyTable();
	}

	private DeepSkyObject saveDeepSkyObject(String deepSkyObjectName)
			throws DeepSkyObjectAlreadyExistsException, ConstellationAlreadyExistsException,
			DeepSkyObjectTypeAlreadyExistsException {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(deepSkyObjectName, orion, nebula));
		assertNotNull(m42);

		return m42;
	}

	@Test
	public void testGetAllDeepSkyObjects() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(M42, orion, nebula));
		assertNotNull(m42);

		DeepSkyObject m43 = deepSkyObjectService.save(new DeepSkyObject(M43, orion, nebula));
		assertNotNull(m43);

		given().accept(ContentType.JSON).when().get(BASE_URL).then().statusCode(200).body("size()", is(2));
	}

	@Test
	public void testGetDeepSkyObjectById() throws Exception {
		DeepSkyObject m42 = saveDeepSkyObject(M42);

		given().accept(ContentType.JSON).when().get(BASE_URL + "/" + m42.getId()).then().statusCode(200).body("id",
				is((int) m42.getId()));
	}

	@Test
	public void testSaveDeepSkyObjectWhenNotExists() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		DeepSkyObjectPojo deepSkyObjectSaveRequest = new DeepSkyObjectPojo();
		deepSkyObjectSaveRequest.setName(M42);
		deepSkyObjectSaveRequest.setConstellationId(orion.getId());
		deepSkyObjectSaveRequest.setDeepSkyObjectTypeId(nebula.getId());

		String payload = new Gson().toJson(deepSkyObjectSaveRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).post(BASE_URL).then().statusCode(200)
				.extract().response();
		assertNotNull(response.getBody());

		long addedDeepSkyObjectId = response.jsonPath().getLong("id");
		String addedDeepSkyObjectName = response.jsonPath().getString("name");
		assertEquals(addedDeepSkyObjectName, M42);

		DeepSkyObject m42Found = deepSkyObjectService.findById(addedDeepSkyObjectId);
		assertNotNull(m42Found);
	}
	
	@Test
	public void testSaveDeepSkyObjectWhenAlreadyExists() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(M42, orion, nebula));
		assertNotNull(m42);
		
		DeepSkyObjectPojo deepSkyObjectSaveRequest = new DeepSkyObjectPojo();
		deepSkyObjectSaveRequest.setName(M42);
		deepSkyObjectSaveRequest.setConstellationId(orion.getId());
		deepSkyObjectSaveRequest.setDeepSkyObjectTypeId(nebula.getId());

		String payload = new Gson().toJson(deepSkyObjectSaveRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).post(BASE_URL).then().statusCode(500)
				.extract().response();
		assertNotNull(response.getBody());
	}

	@Test
	public void testUpdateDeepSkyObject() throws Exception {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(M42, orion, nebula));
		assertNotNull(m42);

		String m42NameChanged = m42.getName() + " changed";

		DeepSkyObjectPojo deepSkyObjectUpdateRequest = new DeepSkyObjectPojo();
		deepSkyObjectUpdateRequest.setId(m42.getId());
		deepSkyObjectUpdateRequest.setName(m42NameChanged);
		deepSkyObjectUpdateRequest.setConstellationId(orion.getId());
		deepSkyObjectUpdateRequest.setDeepSkyObjectTypeId(nebula.getId());

		String payload = new Gson().toJson(deepSkyObjectUpdateRequest);

		Response response = given().contentType(ContentType.JSON).body(payload).put(BASE_URL).then().statusCode(200)
				.extract().response();
		assertNotNull(response.getBody());

		long updatedDeepSkyObjectId = response.jsonPath().getLong("id");
		String updatedDeepSkyObjectName = response.jsonPath().getString("name");
		assertEquals(updatedDeepSkyObjectName, m42NameChanged);

		DeepSkyObject deepSkyObjectUpdated = deepSkyObjectService.findById(updatedDeepSkyObjectId);
		assertNotNull(deepSkyObjectUpdated);
		assertEquals(deepSkyObjectUpdated.getName(), m42NameChanged);
	}

	@Test
	public void testDeleteDeepSkyObject() throws Exception {
		DeepSkyObject m42 = saveDeepSkyObject(M42);

		given().contentType(ContentType.JSON).delete(BASE_URL + "/" + m42.getId()).then().statusCode(200);

		DeepSkyObject orionDeleted = deepSkyObjectService.findById(m42.getId());
		assertNull(orionDeleted);
	}

}
