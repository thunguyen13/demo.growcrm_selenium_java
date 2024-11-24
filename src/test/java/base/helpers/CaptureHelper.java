package base.helpers;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_QUICKTIME;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_QUICKTIME_JPEG;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import base.utils.StampUtils;

public class CaptureHelper extends ScreenRecorder{
	
	String name;
	int count;
	
	// Constructor: Initializes the screen recorder with specified parameters - adding a new parameter 
	public CaptureHelper(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat, Format screenFormat,
			Format mouseFormat, Format audioFormat, File movieFolder, ITestNGMethod method) throws IOException, AWTException {
		super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
		// TODO Auto-generated constructor stub
		this.name = method.getMethodName();
		this.count = method.getCurrentInvocationCount();
	}
	
	@Override
	protected File createMovieFile(Format fileFormat) throws IOException {
		// TODO Auto-generated method stub
		// Create folder if it is not exist
		if(!movieFolder.exists()) {
			movieFolder.mkdirs();
		} else if(!movieFolder.isDirectory()) {
			throw new IOException("\"" + movieFolder + "\" is not a directory!");
		}
		
		//Generate file name with date, time, extension
		String dateTime = StampUtils.getCurrentDateTimestamp();
		String extension = Registry.getInstance().getExtension(fileFormat);
		String fileName = String.format("%s_%d_%s.%s", name, count,dateTime,extension);
		return new File(movieFolder,fileName);
	}
	
	public static CaptureHelper getScreenRecorder(ITestNGMethod method) throws IOException, AWTException {
		//Set up folder for recorded video
		String folderPath = "././output/record/" + StampUtils.getCurrentDatestamp();
		File folder = new File(folderPath);
		
		//Get screen dimensions to define screen size for the record
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		
		//Define area to record (full screen)
		Rectangle recordArea = new Rectangle(0,0,width,height);
		
		// Get default graphics configuration
		GraphicsConfiguration gpc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		// Create and return RecordHelper instance with specified settings
		return new CaptureHelper(gpc, recordArea,
								new Format(MediaTypeKey,MediaType.FILE,MimeTypeKey,MIME_QUICKTIME),
								new Format(MediaTypeKey,MediaType.VIDEO,EncodingKey,ENCODING_QUICKTIME_JPEG,CompressorNameKey,ENCODING_QUICKTIME_JPEG,DepthKey,24,FrameRateKey,Rational.valueOf(15),QualityKey,1.0f,KeyFrameIntervalKey,15*60),
								new Format(MediaTypeKey,MediaType.VIDEO,EncodingKey,"black",FrameRateKey,Rational.valueOf(30)),
								null,
								folder, method);
	}

	public static void screenshotCapture(WebDriver driver, ITestResult result){
		
		//Convert webdriver object to takescreenshot
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		
		//Call getScreenshotAs method to create image file
		File scrFile = screenshot.getScreenshotAs(OutputType.FILE);
		
		//Define the path to where the file is saved
		String date = StampUtils.getCurrentDatestamp();
		String directoryPath = "././output/screenshot/" + date;
		Path directory = Paths.get(directoryPath);
		
		String timestamp = StampUtils.getCurrentTimeStamp();
		String methodNameAndInvocationCount = result.getMethod().getMethodName() + "_" + result.getMethod().getCurrentInvocationCount();
		String fileName = methodNameAndInvocationCount + "_" + timestamp + ".png";
		Path desFile = directory.resolve(fileName);
		
		//Copy screenshot file to destination
		try {
			//Create folder if it does not exist
			if(Files.notExists(directory)) {
				Files.createDirectories(directory);
			}
			//Save screenshot file to desFile path
			Files.copy(scrFile.toPath(), desFile);
			//System.out.println("Screenshot has saved successfully at: " + desFile);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Failed to save screenshot " + e.getMessage());
		}
	}

}
