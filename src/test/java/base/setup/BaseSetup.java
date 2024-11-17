package base.setup;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import base.helpers.ActionKeys;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseSetup {

	private WebDriver driver;

	// Return driver of this class
//	public WebDriver getDriver(boolean isParallel) {
//		driver = DriverManager.getDriver(isParallel);
//		System.out.println("Current base setup driver: " + driver);
//		return driver;
//	}

	// Choose driver of browser
	public void setDriver(boolean isParallel, String browserType, String appUrl) {
		// System.out.println("Start setup driver: ");
		switch (browserType) {
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
				System.out.println("Broswer Type: " + browserType + " is invalid. Launching Chrome browser as default.");
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
	@Parameters({ "isParallel", "browserType", "appUrl" })
	@BeforeMethod(alwaysRun = true)
	public void initMethod(@Optional("true") boolean isParallel, @Optional("chrome") String browserType, String appUrl) {
		if (isParallel) {
			try {
				// set driver before test
				setDriver(isParallel, browserType, appUrl);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error: " + e.getStackTrace());
			}
		}
	}

	// Run after each @Test
	@Parameters({ "isParallel" })
	@AfterMethod(alwaysRun = true)
	public void teardownMethod(@Optional("true") boolean isParallel) {
		if (isParallel) {
			ActionKeys.sleep(2);
			DriverManager.quit();
		}
	}
	
	// Run before each Class
	@Parameters({ "isParallel", "browserType", "appUrl" })
	@BeforeSuite(alwaysRun = true)
	public void initClass(@Optional("true") boolean isParallel, @Optional("chrome") String browserType, String appUrl) {
		if (!isParallel) {
			try {
				// set driver before test
				setDriver(isParallel, browserType, appUrl);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error: " + e.getStackTrace());
			}
		}
	}

	// Run after each Class
	@Parameters({ "isParallel" })
	@AfterSuite(alwaysRun = true)
	public void teardownClass(@Optional("true") boolean isParallel) throws InterruptedException {
		if (!isParallel) {
			ActionKeys.sleep(2);
			DriverManager.quit();
		}
		System.out.println("Location of Allure: " + System.getProperty("allure.results.directory"));
	}

}
