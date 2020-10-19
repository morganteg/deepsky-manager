package it.attsd.deepsky.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
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
		
		Response response = get("/api/constellation");
//		response.then().body("size()", is(2));

		
//		Response response = given().accept("application/json; charset=UTF-8").when().get("/api/constellation");
//		response.then().assertThat().statusCode(200);
		
		List<Constellation> tmp = response.getBody().as(List.class);
		System.out.println(tmp);
//		        body("size()", is(2));
	}

}
