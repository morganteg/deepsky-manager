package it.attsd.deepsky.it;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConstellationRestControllerIT {
    String ORION = "orion";
    String LIBRA = "libra";

    @Autowired
    private ConstellationRepository constellationRepository;

    @Autowired
    private DeepSkyObjectRepository deepSkyObjectRepository;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        RestAssured.port = port;
        deepSkyObjectRepository.deleteAll();
        deepSkyObjectRepository.flush();
        constellationRepository.deleteAll();
        constellationRepository.flush();
    }

    @Test
    public void testGetAllConstellations() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
        Constellation libraSaved = constellationRepository.save(new Constellation(LIBRA));
        List<Constellation> constellationsSaved = Arrays.asList(orionSaved, libraSaved);

        List<Constellation> constellationsRetrieved = Arrays.asList(given().when().get("/api/constellation")
                .as(Constellation[].class));

        assertThat(constellationsRetrieved).containsAll(constellationsSaved);
    }

    @Test
    public void testGetConstellationById() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

        Constellation constellationRetrieved = given().when()
                .get("/api/constellation/" + orionSaved.getId())
                .as(Constellation.class);

        assertThat(constellationRetrieved).isEqualTo(orionSaved);
    }

    @Test
    public void testGetConstellationByName() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

        Constellation constellationRetrieved = given().when()
                .get("/api/constellation/name/" + ORION)
                .as(Constellation.class);

        assertThat(constellationRetrieved).isEqualTo(orionSaved);
    }

    @Test
    public void testSaveConstellation() {
        Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(new Constellation(ORION)).when()
                .post("/api/constellation");

        Constellation orionSaved = response.getBody().as(Constellation.class);

        Constellation orionFound = constellationRepository.findById(orionSaved.getId()).get();
        assertThat(orionFound).isEqualTo(orionSaved);
    }

    @Test
    public void testUpdateConstellation() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

        String nameChanged = ORION + " changed";
        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(new Constellation(nameChanged)).when()
                .put("/api/constellation/" + orionSaved.getId())
                .then()
                .statusCode(200)
                .body(
                        "id", equalTo(orionSaved.getId().intValue()),
                        "name", equalTo(nameChanged)
                );
    }

    @Test
    public void testDeleteConstellationById() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

        given().when()
                .delete("/api/constellation/" + orionSaved.getId())
                .then()
                .statusCode(200);

        assertThat(constellationRepository.findById(orionSaved.getId())).isNotPresent();
    }

}
