package base.listeners;

import java.awt.AWTException;
import java.io.IOException;

import org.testng.ITestListener;
import org.testng.ITestResult;

import base.helpers.CaptureHelper;
import base.setup.DriverManager;

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
		CaptureHelper.screenshotCapture(DriverManager.getDriver(), result);
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
