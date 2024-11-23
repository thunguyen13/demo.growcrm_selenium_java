package base.setup;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import base.helpers.ActionKeys;
import base.helpers.PropertiesHelper;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseSetup {

	private WebDriver driver;
	private static volatile boolean isMethodsParallel = false;
	private static volatile boolean isClassesParallel = false;
	private static volatile boolean isTestsParallel = false;
	private static volatile boolean isSequential =  false;

	// Choose driver of browser
	public void setDriver(String browserName, String appUrl) {
		// System.out.println("Start setup driver: ");
		switch (browserName) {
			case "chrome":
				driver = initChromeDriver(appUrl);
				break;
			case "firefox":
				driver = initFirefoxDriver(appUrl);
				break;
			case "edge":
				driver = initEdgeDriver(appUrl);
				break;
			default:
				System.out.println("Broswer Type: " + browserName + " is invalid. Launching Chrome browser as default.");
				driver = initChromeDriver(appUrl);
				break;
		}

		DriverManager.setDriver(driver);
		
		driver.manage().window().maximize();
		driver.navigate().to(appUrl);
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		
	}

	// Init driver of browser
	private WebDriver initChromeDriver(String appUrl) {
		// TODO Auto-generated method stub
		WebDriver _driver;
		System.out.println("Lauching Chrome browser...");
		System.setProperty("webdriver.chrome.driver", "D:\\automation\\driver\\chromedriver-win64\\chromedriver.exe");
		_driver = new ChromeDriver();
		return _driver;
	}

	private WebDriver initFirefoxDriver(String appUrl) {
		WebDriver _driver;
		System.out.println("Lauching Firefox browser...");
		WebDriverManager.firefoxdriver().setup();
		_driver = new FirefoxDriver();
		return _driver;
	}

	private WebDriver initEdgeDriver(String appUrl) {
		WebDriver _driver;
		System.out.println("Lauching Edge browser...");
		_driver = new EdgeDriver();
		return _driver;
	}

	// Run before each @Test
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browserName", "appUrl" })
	public void initMethod(@Optional("chrome") String browserName, String appUrl) {
		if (isMethodsParallel) {
			try {
				// set driver before test
				setDriver(browserName, appUrl);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error: " + e.getStackTrace());
			}

		}
	}

	// Run after each @Test
	@AfterMethod(alwaysRun = true)
	public void teardownMethod() {
		if (isMethodsParallel) {
			ActionKeys.sleep(2);
			DriverManager.quit();
		}
	}
	
	// Run before each Class
	@BeforeClass(alwaysRun = true)
	@Parameters({ "browserName", "appUrl" })
	public void initClass(@Optional("chrome") String browserName, String appUrl) {
		if (isClassesParallel) {
			try {
				// set driver before test
				setDriver(browserName, appUrl);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error: " + e.getStackTrace());
			}

		}
	}

	// Run after each Class
	@AfterClass(alwaysRun = true)
	public void teardownClass() {
		if (isClassesParallel) {
			ActionKeys.sleep(2);
			DriverManager.quit();
		}
	}
		
	// Run before each Test
	@BeforeTest(alwaysRun = true)
	@Parameters({ "parallelMode", "browserName", "appUrl" })
	public void initTest(@Optional("none") String parallelMode, @Optional("chrome") String browserName, String appUrl) {
		
		// check parallel mode
		isClassesParallel = "classes".equals(parallelMode);
		isMethodsParallel = "methods".equals(parallelMode);
		
		if (isTestsParallel) {
			try {
				// set driver before test
				setDriver(browserName, appUrl);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error: " + e.getStackTrace());
			}
		}
	}

	// Run after each Test
	@AfterTest(alwaysRun = true)
	public void teardownTest() throws InterruptedException {
		if (isTestsParallel) {
			ActionKeys.sleep(2);
			DriverManager.quit();
		}
	}
	
	// Run before each Suite
	@BeforeSuite(alwaysRun = true)
	@Parameters({ "parallelMode", "browserName", "appUrl" })
	public void initSuite(@Optional("none") String parallelMode, @Optional("chrome") String browserName, String appUrl) {

		// check parallel mode
		isSequential = "none".equals(parallelMode);
		isTestsParallel = "tests".equals(parallelMode);
		
		PropertiesHelper.loadAllProperties();
		
		// Set sequential driver
		if (isSequential) {
			try {
				// set driver before test
				setDriver(browserName, appUrl);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error: " + e.getStackTrace());
			}
		}
	}

	// Run after each Suite
	@AfterSuite(alwaysRun = true)
	public void teardownSuite() throws InterruptedException {
		if (isSequential) {
			ActionKeys.sleep(2);
			DriverManager.quit();
		}
		System.out.println("Location of Allure: " + System.getProperty("allure.results.directory"));
	}

}
