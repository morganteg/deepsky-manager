package it.attsd.deepsky.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ConstellationRestITTest {
	@LocalServerPort
	private int port;

	@Autowired
	private ConstellationRepository constellationRepository;

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
		List<Constellation> constellations = new ArrayList<Constellation>();
		constellations.add(new Constellation("orion"));
		constellations.add(new Constellation("scorpius"));
		
		when(constellationRepository.findAll()).thenReturn(constellations);

//		Response response = get("/api/constellation");
//		response.then().body("size()", is(2));

//		Response response = given().log().all().accept(ContentType.JSON).when().get("/api/constellation");
//		response.then().assertThat().statusCode(200);
//		
//		System.out.println(response.getBody().toString());
//
//		List<Constellation> tmp = response.getBody().as(List.class);
//		System.out.println(tmp);
//		        body("size()", is(2));

		given().accept(ContentType.JSON).when().get("/api/constellation").then().statusCode(200).body("size()", is(2));

//		Response response = given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
//				.get("/api/constellation").then().contentType(ContentType.JSON).extract().response();
//		System.out.println(response.getBody().toString());
	}

	@Test
	public void test1() {
//		when().get("https://dgaonline.regione.lazio.it/dgaonline/wp-json/api/tipologiegioco/").then()
//				.statusCode(200);

//		Response response = get("https://dgaonline.regione.lazio.it/dgaonline/wp-json/api/tipologiegioco/");
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		Response response = get("/api/constellation");

//		RestAssured.requestSpecification = new RequestSpecBuilder().
//		        setBaseUri("https://dgaonline.regione.lazio.it/").
//		        setContentType(ContentType.JSON).
//		        build().
//		        log().all();

//		JsonArray jsonArray = given()
//				.baseUri("https://dgaonline.regione.lazio.it/")
//				.basePath("dgaonline/wp-json/api/tipologiegioco/")
//				.get()
//				.as(JsonArray.class);
//		System.out.println(jsonArray.toString());
	}

	@Test
	public void testRestAssured() {
		JsonObject jsonObject = given().baseUri("http://dummy.restapiexample.com/").basePath("api/v1/employees").get()
				.as(JsonObject.class);
		System.out.println(jsonObject.toString());
	}

	@Test
	public void testRestAssured2() {
		given().log().all().when().get("http://www.google.com").then().statusCode(200);
	}

}
