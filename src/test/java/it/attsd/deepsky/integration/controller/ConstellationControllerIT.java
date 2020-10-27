package it.attsd.deepsky.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConstellationControllerIT {
	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private ConstellationService constellationService;

	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;

	private final String ORION = "orion";
	private final String SCORPIUS = "scorpius";

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();

		deepSkyObjectRepository.emptyTable();
		constellationRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testGetConstellations() throws ConstellationAlreadyExistsException {
		constellationService.save(new Constellation(ORION));
		constellationService.save(new Constellation(SCORPIUS));

		driver.get(baseUrl + "/constellation");

		int tdSize = driver.findElement(By.id("constellations")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(2);
	}
	
	@Test
	public void testSaveConstellationIfNotExists() throws ConstellationAlreadyExistsException {
		driver.get(baseUrl + "/constellation");
		
		WebElement nameElement = driver.findElement(By.id("name"));
		nameElement.sendKeys(ORION);
		
		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();
		
		int tdSize = driver.findElement(By.id("constellations")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(1);
	}
	
	@Test
	public void testSaveConstellationIfAlreadyExists() throws ConstellationAlreadyExistsException {
		constellationService.save(new Constellation(ORION));
		
		driver.get(baseUrl + "/constellation");
		
		WebElement nameElement = driver.findElement(By.id("name"));
		nameElement.sendKeys(ORION);
		
		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();
		
		int tdSize = driver.findElement(By.id("errorMessage")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(driver.findElement(By.id("errorMessage")).getText()).isEqualToIgnoringCase("constellation already exists"); // TODO centralize constant
	}
}
