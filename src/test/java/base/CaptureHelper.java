package base;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

public class CaptureHelper {

	public static void screenShot(WebDriver driver, ITestResult result){
		
		//Convert webdriver object to takescreenshot
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		
		//Call getScreenshotAs method to create image file
		File scrFile = screenshot.getScreenshotAs(OutputType.FILE);
		
		//Define the path to where the file is saved
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		String directoryPath = "././output/screenshot/" + date;
		Path directory = Paths.get(directoryPath);
		String methodName = result.getMethod().getMethodName() + result.getMethod().getCurrentInvocationCount();
		String fileName = methodName + "_" + timestamp + ".png";
		Path desFile = directory.resolve(fileName);
		
		//Copy screenshot file to destination
		try {
			//Create folder if it does not exist
			if(Files.notExists(directory)) {
				Files.createDirectories(directory);
			}
			//Save screenshot file to desFile path
			Files.copy(scrFile.toPath(), desFile);
			System.out.println("Screenshot has saved successfully at: " + desFile);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Failed to save screenshot " + e.getMessage());
		}
	}
}
