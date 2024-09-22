package base.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import base.utils.StampUtil;

public class ExtentReportManager {
	private static final ExtentReports extentReports = new ExtentReports();
	private static final String datetime = StampUtil.getCurrentDateTimestamp();
	
	public synchronized static ExtentReports getExtentReports() {
		ExtentSparkReporter reporter = new ExtentSparkReporter("./././output/reports/extentReports_" + datetime + ".html");
		// Set the name of the report
		reporter.config().setReportName("Extent Reports");
		reporter.config().setTheme(Theme.DARK);
		// Attach reporter into extentReports to extentReports knows that it needs to write the report in the format provided by ExtentSparkReporter.
		extentReports.attachReporter(reporter);
		// Add system info
		extentReports.setSystemInfo("Framework", "Selenium Java");
		extentReports.setSystemInfo("Author", "Ng.M.Thu");
		
		return extentReports;
	}

}
