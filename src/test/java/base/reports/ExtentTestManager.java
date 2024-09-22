package base.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class ExtentTestManager {
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	static ExtentReports extentReport = ExtentReportManager.getExtentReports();
	
	public synchronized static ExtentTest saveToReport(String testName, String desc) {
		// Get id of the current thread
		Integer id = (int) Thread.currentThread().getId();
		// Create a test for the current test case
		ExtentTest test = extentReport.createTest("Thread " + id + "_" + testName, desc);
		// Put the current test into map
		extentTestMap.put(id, test);
		return test;
	}
	
	public static ExtentTest getTest() {
		// Get test of the current thread in map, if null throw a soft exception
		return Optional.ofNullable(extentTestMap.get((int) Thread.currentThread().getId())).orElseThrow(() -> new RuntimeException("Test not initialized for this thread"));
	}
	
	public static void addScreenshot(String message, WebDriver driver) {
		try {
			ExtentTest test = getTest();
			if(test != null) {
				// Screenshot in base64 type
				String base64Image = "data:image/png;base64," + ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
				// Attach the screenshot into the test
				test.log(Status.INFO, message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while taking screenshot: " + e.getMessage());
		}
	}
	
	public static void addScreenshot(Status status, String message, WebDriver driver) {
		try {
			ExtentTest test = getTest();
			if(test != null) {
				// Screenshot in base64 type
				String base64Image = "data:image/png;base64," + ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
				//System.out.println("base64Image: " + base64Image);
				// Attach the screenshot into the test
				test.log(status, message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while taking screenshot: " + e.getMessage());
		}
	}
	
	public static void logMessage(String message, WebDriver driver) {
		try {
			ExtentTest test = getTest();
			if(test != null) {
				// Add log status and message
				test.log(Status.INFO, message);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void logMessage(Status status, String message, WebDriver driver) {
		try {
			ExtentTest test = getTest();
			if(test != null) {
				// Add log status and message
				test.log(status, message);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
