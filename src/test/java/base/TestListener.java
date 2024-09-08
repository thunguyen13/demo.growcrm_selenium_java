package base;

import java.awt.AWTException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener{
	
	private CaptureHelper screenRecorder;

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		screenshot(result);
		stopRecorder();
	}
	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		screenshot(result);
		stopRecorder();
	}
	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		screenshot(result);
		stopRecorder();
	}
	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		try {
			screenRecorder = CaptureHelper.getScreenRecorder(result.getMethod());
			screenRecorder.start();
		} catch (IOException | AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void screenshot(ITestResult result) {
		//Get the test class instance currently being executed
		Object testClass = result.getInstance();
		//Get driver of the current class
		WebDriver driver = null;
		try {
			driver = ((BaseSetup) testClass).getDriver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Perform screenshot
		CaptureHelper.screenshotCapture(driver, result);
	}
	
	private void stopRecorder() {
		try {
			screenRecorder.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
