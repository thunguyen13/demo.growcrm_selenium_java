package base.listeners;

import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

import base.helpers.CaptureHelper;
import base.reports.ExtentReportManager;
import base.reports.ExtentTestManager;
import base.setup.BaseSetup;

public class ReportListener implements ITestListener{
	
	// Retrieve name of each case
	private String getTestName(ITestResult result) {
		return result.getTestName() != null ? result.getTestName() : result.getMethod().getConstructorOrMethod().getName();
	}
	
	// Retrieve description of each case)
	private String getTestDescription(ITestResult result) {
		return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
	}
	
	// Retrieve driver
	private WebDriver getCurrentDriver(ITestResult result) {
		Object testClass = result.getInstance();
		WebDriver driver = null;
		try {
			driver = ((BaseSetup) testClass).getDriver();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Fail to get driver!");
			e.printStackTrace();
		}
		return driver;
	}
	
	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		//ITestListener.super.onFinish(context);
		ExtentReportManager.getExtentReports().flush();
	}
	
	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestStart(result);
		ExtentTestManager.saveToReport(getTestName(result), getTestDescription(result));
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestFailure(result);
		String arrayData = Arrays.toString(result.getParameters());
		String message = String.format("%s %s %s <br> %s", getTestName(result),arrayData," is failed. <br> Reason: ",result.getThrowable().toString());
		WebDriver driver = getCurrentDriver(result);
		ExtentTestManager.addScreenshot(Status.FAIL, message, driver);
		
		CaptureHelper.screenshotCapture(driver, result);
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestSkipped(result);
		String message = getTestName(result) + " is skipped.";
		WebDriver driver = getCurrentDriver(result);
		ExtentTestManager.logMessage(Status.SKIP, message, driver);
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestSuccess(result);
		String arrayData = Arrays.toString(result.getParameters());
		String message = String.format("%s %s %s", getTestName(result),arrayData," is passed.");
		WebDriver driver = getCurrentDriver(result);
		ExtentTestManager.addScreenshot(Status.PASS, message, driver);
	}
	
}
