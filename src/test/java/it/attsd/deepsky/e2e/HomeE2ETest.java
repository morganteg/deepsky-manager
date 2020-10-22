package it.attsd.deepsky.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class HomeE2ETest {
	private static int port = 8080;
	private static String baseUrl = "http://localhost:" + port;
	private WebDriver driver;

//	@BeforeClass
//	public static void setupClass() {
//		WebDriverManager.chromedriver().setup();
//	}
//
//	@Before
//	public void setup() {
//		baseUrl = "http://localhost:" + port;
//		driver = new ChromeDriver();
//	}
//
//	@After
//	public void teardown() {
//		driver.quit();
//	}
//
//	@Test
//	public void testHomePage() {
//		driver.get(baseUrl);
//		WebElement element = driver.findElement(By.cssSelector("h1[id*='title"));
//		
//		assertThat(element.getText().equalsIgnoreCase("home"));
//	}
}