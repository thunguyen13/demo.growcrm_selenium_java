package base.setup;

import org.openqa.selenium.WebDriver;

public class DriverManager {
	
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	
	private DriverManager() {	
	}
	
	//Method to initialize WebDriver for both parallel and sequential use cases
	public static void setDriver(WebDriver _driver) {
		if(driver.get() == null) {
			//Parallel execution: create driver for each thread
			driver.set(_driver);
			System.out.println("Created new driver: " + _driver + " for thread: " + Thread.currentThread().getId());
		}
	}
	
	//Get the current driver base on mode
	public static WebDriver getDriver() {
		return driver.get();
	}
	
	//Quit and clean up driver
	public static void quit() {
		if(driver.get() != null) {
			driver.get().quit();
			driver.remove();
		}
	}

}
