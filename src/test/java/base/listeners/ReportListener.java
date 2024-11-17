package base.listeners;

import java.awt.AWTException;
import java.io.IOException;
import java.util.Arrays;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

import base.helpers.CaptureHelper;
import base.reports.AllureReportManager;
import base.reports.ExtentReportManager;
import base.reports.ExtentTestManager;
import base.setup.DriverManager;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;

public class ReportListener implements ITestListener, TestLifecycleListener{
	
	private static ThreadLocal<CaptureHelper> screenRecorder = new ThreadLocal<>();
	
	private void stopRecorder() {
		try {
			screenRecorder.get().stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Retrieve name of each case
	private String getTestName(ITestResult result) {
		return result.getTestName() != null ? result.getTestName() : result.getMethod().getConstructorOrMethod().getName();
	}
	
	// Retrieve description of each case)
	private String getTestDescription(ITestResult result) {
		return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
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
		try {
			CaptureHelper getScreenRecorder = CaptureHelper.getScreenRecorder(result.getMethod());
			screenRecorder.set(getScreenRecorder);
			screenRecorder.get().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestFailure(result);
		
		String arrayData = Arrays.toString(result.getParameters());
		String message = String.format("%s %s %s <br> %s", getTestName(result),arrayData," is failed. <br> Reason: ",result.getThrowable().toString());
		
		//Extent Report
		ExtentTestManager.addScreenshot(Status.FAIL, message, DriverManager.getDriver());
		
		//Allure report
		AllureReportManager.saveTextLog(message);
		AllureReportManager.saveScreenshot(DriverManager.getDriver());
		
		stopRecorder();
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestSkipped(result);
		String message = getTestName(result) + " is skipped.";
		
		//Extent Report
		ExtentTestManager.logMessage(Status.SKIP, message, DriverManager.getDriver());
		
		//Allure report
		AllureReportManager.saveTextLog(message);
		
		stopRecorder();
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestSuccess(result);
		String arrayData = Arrays.toString(result.getParameters());
		String message = String.format("%s %s %s", getTestName(result),arrayData," is passed.");
		
		//Extent Report
		ExtentTestManager.addScreenshot(Status.PASS, message, DriverManager.getDriver());
		
		//Allure report
		AllureReportManager.saveTextLog(message);
		AllureReportManager.saveScreenshot(DriverManager.getDriver());
		
		stopRecorder();
	}
	
	@Override
	public void afterTestStop(TestResult result) {
		// TODO Auto-generated method stub
		//TestLifecycleListener.super.beforeTestStop(result);
		if(result.getStatus() == io.qameta.allure.model.Status.BROKEN) {
			if(result.getStatusDetails().getMessage().contains("Expected condition failed: waiting for")) {
				System.out.println("change status from Broken to Failed");
				result.setStatus(io.qameta.allure.model.Status.FAILED);
			}
		}
	}
	
}
