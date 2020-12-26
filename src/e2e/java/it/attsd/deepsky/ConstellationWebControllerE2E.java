package it.attsd.deepsky;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ConstellationWebControllerE2E {
    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
    private static String baseUrl = "http://localhost:" + port;
    private WebDriver driver;

    private final String ORION = "orion";

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port;
        driver = new ChromeDriver();
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testHomePage() {
        driver.get(baseUrl);
        WebElement urlElement = driver.findElement(By.cssSelector("a[href*='/constellation"));

        assertThat(urlElement).isNotNull();
    }

    @Test
    public void testCreateNewConstellationWhenNotExists() {
        driver.get(baseUrl);

        driver.findElement(By.cssSelector("a[href*='/constellation")).click();

        String name = generateConstellationName();
        driver.findElement(By.id("constellationName")).sendKeys(name);
        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("constellations")).getText()).contains(name);
    }

    @Test
    public void testCreateNewConstellationWhenAlreadyExists() throws JSONException {
        driver.get(baseUrl);

        // Create Constellation through REST
        String name = generateConstellationName();
        postConstellation(name);

        driver.findElement(By.cssSelector("a[href*='/constellation")).click();

        driver.findElement(By.id("constellationName")).sendKeys(name);
        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("errorMessage")).getText()).isEqualTo("A Constellation with the same name already exists");
    }

    @Test
    public void testUpdateConstellation() throws JSONException {
        // Create Constellation through REST
        String name = generateConstellationName();
        Integer constellationId = postConstellation(name);

        driver.get(baseUrl);

        // Go to /constellation page
        driver.findElement(By.cssSelector("a[href*='/constellation")).click();

        // Go to /constellation/modify/<id> page
        driver.findElement(By.cssSelector("a[href*='/constellation/modify/" + constellationId.intValue())).click();

        String nameChanged = name + " changed";

        WebElement constellationNameElement = driver.findElement(By.id("constellationName"));
        constellationNameElement.clear();
        constellationNameElement.sendKeys(nameChanged);

        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("constellations")).getText()).contains(nameChanged);
    }

    @Test
    public void testDeleteConstellation() throws JSONException {
        driver.get(baseUrl);

        // Create Constellation through REST
        String name = generateConstellationName();
        Integer constellationId = postConstellation(name);

        driver.findElement(By.cssSelector("a[href*='/constellation")).click();
        driver.findElement(By.cssSelector("a[href*='/constellation/delete/" + constellationId.intValue())).click();

        assertThat(driver.findElement(By.id("constellations")).getText()).doesNotContain(name);
    }

    private String generateConstellationName() {
        return ORION + "-" + Math.random();
    }

    private Integer postConstellation(String name) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("name", name);

        return given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body.toString()).when()
                .post("/api/constellation")
                .then()
                .statusCode(200)
                .extract().path("id");
    }

}
