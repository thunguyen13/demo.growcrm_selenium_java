package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseSetup {

	private WebDriver driver;
	
	// Return driver of this class
	public WebDriver getDriver() {
		System.out.println("Current base setup driver: " + driver);
		return driver;
	}
	
	// Choose driver of browser
	public void setDriver(String browserType, String appUrl) {
		//System.out.println("Start setup driver: ");
		switch (browserType) {
		case "chrome":
			driver = initChromeDriver(appUrl);
			break;
		case "firefox":
			driver = initFirefoxDriver(appUrl);
			break;
		default:
			System.out.println("Broswer Type: " + browserType + " is invalid. Launching Chrome browser as default.");
			driver = initChromeDriver(appUrl);
			break;
		}
		
	}

	
	// Init driver of browser
	private WebDriver initChromeDriver(String appUrl) {
		// TODO Auto-generated method stub
		WebDriver _driver;
		System.out.println("Lauching Chrome browser...");
		System.setProperty("webdriver.chrome.driver", "D:\\automation\\driver\\chromedriver-win64\\chromedriver.exe");
		_driver = new ChromeDriver();
		_driver.manage().window().maximize();
		_driver.navigate().to(appUrl);
		_driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		_driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		return _driver;
	}

	private WebDriver initFirefoxDriver(String appUrl) {
		WebDriver _driver;
		System.out.println("Lauching Firefox browser...");
		//System.setProperty("webdriver.geckodriver.driver", "D:\\automation\\driver\\geckodriver-win64\\geckodriver.exe");
		WebDriverManager.firefoxdriver().setup();
		_driver = new FirefoxDriver();
		_driver.manage().window().maximize();
		_driver.navigate().to(appUrl);
		_driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		_driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		return _driver;
	}
	
	// Run before this suite case
	@Parameters({"browserType","appUrl"})
	@BeforeSuite(alwaysRun = true)
	public void initTestBaseSetup(String browserType, String appUrl) {
		try {
			// set driver before test
			setDriver(browserType, appUrl);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error: " + e.getStackTrace());
		}
	}
	
	// Run after this suite case
	@AfterSuite(alwaysRun = true)
	public void tearDown() throws InterruptedException {
		Thread.sleep(2000);
		driver.quit();
	}
	
}
