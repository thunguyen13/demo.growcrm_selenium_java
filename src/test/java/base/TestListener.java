package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener{
	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		screenshot(result);
	}
	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		screenshot(result);
	}
	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		screenshot(result);
	}
	
	
	private void screenshot(ITestResult result) {
		//Get driver of the current class
		WebDriver driver = BaseSetup.getDriver();
		//Perform screenshot
		CaptureHelper.screenShot(driver, result);
		
	}

}
