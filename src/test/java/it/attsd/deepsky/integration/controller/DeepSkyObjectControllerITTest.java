package it.attsd.deepsky.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DeepSkyObjectControllerITTest {
	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	@Autowired
	private ConstellationService constellationService;

	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;

	private final String ORION = "orion";

	private final String NEBULA = "nebula";

	private final String M42 = "m42";
	private final String M43 = "m43";

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

	private WebElement getInputName() {
		return driver.findElement(By.id("deepSkyObjectName"));
	}

	private WebElement getInputConstellationId() {
		return driver.findElement(By.id("constellationId"));
	}

	private WebElement getInputDeepSkyObjectTypeId() {
		return driver.findElement(By.id("deepSkyObjectTypeId"));
	}

	@Test
	public void testGetDeepSkyObjects() throws ConstellationAlreadyExistsException,
			DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(M42, orionSaved, nebulaSaved));
		assertNotNull(m42);

		DeepSkyObject m43 = deepSkyObjectService.save(new DeepSkyObject(M43, orionSaved, nebulaSaved));
		assertNotNull(m43);

		driver.get(baseUrl + "/deepskyobject");

		int tdSize = driver.findElement(By.id("deepSkyObjects")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(2);
	}

	@Test
	public void testSaveDeepSkyObjectWhenNotExists()
			throws ConstellationAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);

		driver.get(baseUrl + "/deepskyobject");

		WebElement nameElement = getInputName();
		nameElement.sendKeys(M42);

		Select constellationSelect = new Select(getInputConstellationId());
		constellationSelect.selectByVisibleText(ORION);

		Select deepSkyObjectTypeSelect = new Select(getInputDeepSkyObjectTypeId());
		deepSkyObjectTypeSelect.selectByVisibleText(NEBULA);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		int tdSize = driver.findElement(By.id("deepSkyObjects")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(1);
	}

	@Test
	public void testSaveDeepSkyObjectWhenAlreadyExists() throws ConstellationAlreadyExistsException,
			DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(M42, orionSaved, nebulaSaved));
		assertNotNull(m42);

		driver.get(baseUrl + "/deepskyobject");

		WebElement nameElement = getInputName();
		nameElement.clear();
		nameElement.sendKeys(M42);

		Select constellationSelect = new Select(getInputConstellationId());
		constellationSelect.selectByVisibleText(ORION);

		Select deepSkyObjectTypeSelect = new Select(getInputDeepSkyObjectTypeId());
		deepSkyObjectTypeSelect.selectByVisibleText(NEBULA);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		assertThat(driver.findElement(By.id("errorMessage")).getText())
				.isEqualToIgnoringCase("DeepSkyObject already exists"); // TODO centralize constant
	}

	@Test
	public void testUpdateDeepSkyObjectWhenExists() throws ConstellationAlreadyExistsException,
			DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(M42, orionSaved, nebulaSaved));
		assertNotNull(m42);

		driver.get(baseUrl + "/deepskyobject/modify/" + m42.getId());

		String nameChanged = m42.getName() + " mod";

		WebElement nameElement = getInputName();
		nameElement.clear();
		nameElement.sendKeys(nameChanged);

		Select constellationSelect = new Select(getInputConstellationId());
		constellationSelect.selectByVisibleText(ORION);

		Select deepSkyObjectTypeSelect = new Select(getInputDeepSkyObjectTypeId());
		deepSkyObjectTypeSelect.selectByVisibleText(NEBULA);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		assertThat(driver.findElement(By.id("deepskyobject-name-" + m42.getId())).getText())
				.isEqualToIgnoringCase(nameChanged);
	}

	@Test
	public void testDeleteDeepSkyObjectIfExists() throws ConstellationAlreadyExistsException,
			DeepSkyObjectTypeAlreadyExistsException, DeepSkyObjectAlreadyExistsException {
		Constellation orionSaved = constellationService.save(new Constellation(ORION));
		assertNotNull(orionSaved);

		DeepSkyObjectType nebulaSaved = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebulaSaved);

		DeepSkyObject m42 = deepSkyObjectService.save(new DeepSkyObject(M42, orionSaved, nebulaSaved));
		assertNotNull(m42);

		driver.get(baseUrl + "/deepskyobject/delete/" + m42.getId());

		assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("deepskyobject-id-" + m42.getId())));
	}
}
