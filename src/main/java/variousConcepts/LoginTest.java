package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginTest {

	WebDriver driver;
	String browser;
	String url;

	// Element List
	By USER_NAME_FIELD = By.xpath("//*[@id=\"username\"]");
	By PASSWORD_FIELD = By.xpath("//*[@id=\"password\"]");
	By SIGNIN_BUTTON_FIELD = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By DASHBOARD_HEADER_FIELD = By.xpath("//*[@id=\"page-wrapper\"]/div[2]/div/h2");
	By CUSTOMER_MENU_LOCATOR = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By ADD_CUSTOMER_MENU_LOCATOR = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By ADD_CONTACT_HEADER_LOCATOR = By.xpath("//*[@id=\"page-wrapper\"]/div[3]/div[1]/div/div/div/div[1]/h5");
	By FULL_NAME_LOCATOR = By.xpath("//*[@id=\"account\"]");
	By COMPANY_DROPDOWN_LOCATOR = By.xpath("//select[@id=\"cid\"]");
	By EMAIL_LOCATOR = By.xpath("//*[@id=\"email\"]");
	By COUNTRY_LOCATOR = By.xpath("//select[@id=\"country\"]");

	// Test Data
	String userName = "demo@techfios.com";
	String password = "abc123";
	
	@BeforeSuite
	public void readConfig() {
		
		// FileReader //Scanner //InputStream //BufferedReader
		
		try{
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser used: " + browser);
			url = prop.getProperty("url");
			
			
		}catch(IOException e) {
			e.getStackTrace();
		}
		
	}

	@BeforeTest
	public void init() {
		
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}else if(browser.equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}

		
		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

//	@Test(priority=2)
	public void loginTest() {

		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();

		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), "Dashboard", "Wrong page!!!");

	}

	//@Test(priority=1)
	public void negLoginTest() {

		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();

		// Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(),
		// "Dashboard", "Wrong page!!!");

	}
	
	@Test
	public void addCustomerTest() {
			
		loginTest();
		
		driver.findElement(CUSTOMER_MENU_LOCATOR).click();
		
		waitForElement(driver, 5, ADD_CUSTOMER_MENU_LOCATOR);
		
		driver.findElement(ADD_CUSTOMER_MENU_LOCATOR).click();
		Assert.assertEquals(driver.findElement(ADD_CONTACT_HEADER_LOCATOR).getText(), "Add Contact", "Wrong page!!!");
		
		driver.findElement(FULL_NAME_LOCATOR).sendKeys("December Selenium" + generateRandomNo(999));
		selectFromDropdown(COMPANY_DROPDOWN_LOCATOR, "Techfios");
		driver.findElement(EMAIL_LOCATOR).sendKeys(generateRandomNo(9999) + "abc@techfios.com");
		
		selectFromDropdown(COUNTRY_LOCATOR, "Algeria");
		
		
	}
	

	private int generateRandomNo(int boundaryNo) {
		
		Random rnd = new Random();
		int generatedNo = rnd.nextInt(boundaryNo);
		return generatedNo;
		
	}

	private void selectFromDropdown(By locator, String visibleText) {
		
		Select sel = new Select(driver.findElement(locator));
		sel.selectByVisibleText(visibleText);
		
	}

	private void waitForElement(WebDriver driver, int timeInSeconds, By locator) {
		
		WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	
//	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
