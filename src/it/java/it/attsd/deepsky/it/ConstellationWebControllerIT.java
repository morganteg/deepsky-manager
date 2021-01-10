package it.attsd.deepsky.it;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConstellationWebControllerIT {

    @ClassRule
    public static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withExposedPorts(3316);

    @Autowired
    private ConstellationRepository constellationRepository;

    @Autowired
    private DeepSkyObjectRepository deepSkyObjectRepository;

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String baseUrl;

    String ORION = "orion";

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Before
    public void setup() {
//        assertTrue(mySQLContainer.isRunning());
        baseUrl = "http://localhost:" + port;
        driver = new HtmlUnitDriver();
        deepSkyObjectRepository.deleteAll();
        deepSkyObjectRepository.flush();
        constellationRepository.deleteAll();
        constellationRepository.flush();
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testConstellations() {
        Constellation testConstellation = constellationRepository.save(new Constellation(ORION));

        driver.get(baseUrl + "/constellation");

        List<WebElement> constellationRows = driver.findElement(By.id("constellations"))
                .findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        assertThat(constellationRows.size()).isEqualTo(1);
        assertThat(constellationRows.get(0).findElement(By.className("constellation-id")).getText()).isNotEmpty();
        assertThat(constellationRows.get(0).findElement(By.className("constellation-name")).getText()).isEqualTo(ORION);
        constellationRows.get(0).findElement(By.cssSelector("a[href*='constellation/modify/" + testConstellation.getId() + "']"));
        constellationRows.get(0).findElement(By.cssSelector("a[href*='constellation/delete/" + testConstellation.getId() + "']"));
    }

    @Test
    public void testSaveConstellation() {
        driver.get(baseUrl + "/constellation");

        WebElement nameElement = driver.findElement(By.id("constellationName"));
        nameElement.sendKeys(ORION);

        WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
        submitButtonElement.click();

        assertThat(constellationRepository.findByName(ORION)).isNotNull();
    }

    @Test
    public void testUpdateConstellationIfExists() {
        Constellation testConstellation = constellationRepository.save(new Constellation(ORION));

        driver.get(baseUrl + "/constellation/modify/" + testConstellation.getId());

        String nameChanged = ORION + " changed";

        WebElement nameElement = driver.findElement(By.id("constellationName"));
        nameElement.clear();
        nameElement.sendKeys(nameChanged);
        driver.findElement(By.id("submitButton")).click();

        assertThat(constellationRepository.findByName(nameChanged)).isNotNull();
    }

    @Test
    public void testDeleteConstellation() {
        Constellation testConstellation = constellationRepository.save(new Constellation(ORION));

        driver.get(baseUrl + "/constellation/delete/" + testConstellation.getId());

//        By byConstellations = By.id("constellations");
//        assertThrows(NoSuchElementException.class, () -> driver.findElement(byConstellations));
        assertThat(driver.findElement(By.id("constellations")).getText()).doesNotContain(ORION);
    }

}
