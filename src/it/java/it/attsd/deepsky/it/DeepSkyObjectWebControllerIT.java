package it.attsd.deepsky.it;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DeepSkyObjectWebControllerIT {

    @Autowired
    private ConstellationRepository constellationRepository;

    @Autowired
    private DeepSkyObjectRepository deepSkyObjectRepository;

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String baseUrl;

    String ORION = "orion";
    String SCORPIUS = "scorpius";

    String M42 = "m42";

    @Before
    public void setup() {
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
    public void testDeepSkyObjects() {
        Constellation orion = constellationRepository.save(new Constellation(ORION));
        DeepSkyObject testM42 = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

        driver.get(baseUrl + "/deepskyobject");

        List<WebElement> deepSkyObjectsRows = driver.findElement(By.id("deepSkyObjects"))
                .findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        assertThat(deepSkyObjectsRows.size()).isEqualTo(1);
        assertThat(deepSkyObjectsRows.get(0).findElement(By.className("deepskyobject-id")).getText()).isNotEmpty();
        assertThat(deepSkyObjectsRows.get(0).findElement(By.className("deepskyobject-name")).getText()).isEqualTo(M42);
        assertThat(deepSkyObjectsRows.get(0).findElement(By.className("deepskyobject-constellationname")).getText()).isEqualTo(ORION);
        deepSkyObjectsRows.get(0).findElement(By.cssSelector("a[href*='deepskyobject/modify/" + testM42.getId() + "']"));
        deepSkyObjectsRows.get(0).findElement(By.cssSelector("a[href*='deepskyobject/delete/" + testM42.getId() + "']"));
    }

    @Test
    public void testSaveDeepSkyObject() {
        constellationRepository.save(new Constellation(ORION));

        driver.get(baseUrl + "/deepskyobject");

        WebElement nameElement = driver.findElement(By.id("deepSkyObjectName"));
        nameElement.sendKeys(M42);

        Select constellationElement = new Select(driver.findElement(By.id("constellationId")));
        constellationElement.selectByVisibleText(ORION);

        WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
        submitButtonElement.click();

        assertThat(deepSkyObjectRepository.findByName(M42)).isNotNull();
    }

    @Test
    public void testUpdateDeepSkyObjectIfExists() {
        Constellation orion = constellationRepository.save(new Constellation(ORION));
        constellationRepository.save(new Constellation(SCORPIUS));
        DeepSkyObject testM42 = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

        driver.get(baseUrl + "/deepskyobject/modify/" + testM42.getId());

        String nameChanged = M42 + " changed";

        WebElement nameElement = driver.findElement(By.id("deepSkyObjectName"));
        nameElement.clear();
        nameElement.sendKeys(nameChanged);

        Select constellationElement = new Select(driver.findElement(By.id("constellationId")));
        constellationElement.selectByVisibleText(SCORPIUS);

        driver.findElement(By.id("submitButton")).click();

        DeepSkyObject deepSkyObjectFound = deepSkyObjectRepository.findByName(nameChanged);
        assertThat(deepSkyObjectFound).isNotNull();
        assertThat(deepSkyObjectFound.getConstellation().getName()).isEqualTo(SCORPIUS);
    }

    @Test
    public void testDeleteDeepSkyObject() {
        Constellation orion = constellationRepository.save(new Constellation(ORION));
        DeepSkyObject testM42 = deepSkyObjectRepository.save(new DeepSkyObject(M42, orion));

        driver.get(baseUrl + "/deepskyobject/delete/" + testM42.getId());

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("deepSkyObjects")));
    }

}
