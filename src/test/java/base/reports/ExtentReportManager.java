package base.reports;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import base.helpers.PropertiesHelper;
import base.utils.StampUtil;
import base.utils.SystemUtil;

public class ExtentReportManager {
	private static final ExtentReports extentReports = new ExtentReports();
	private static final String datetime = StampUtil.getCurrentDateTimestamp();

	public synchronized static ExtentReports getExtentReports() {
		String filePath = SystemUtil.createFilePath(PropertiesHelper.getValue("extent.report.path"))
						  + File.separator + "extentReports_" + datetime + ".html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(filePath); // ./././output/reports/extentReports_" + datetime + ".html
		// Set the name of the report
		reporter.config().setReportName("Extent Reports");
		reporter.config().setTheme(Theme.DARK);
		
		// Attach reporter into extentReports to extentReports knows that it needs to
		// write the report in the format provided by ExtentSparkReporter.
		extentReports.attachReporter(reporter);
		
		// Add system info
		extentReports.setSystemInfo("Framework", "Selenium Java");
		extentReports.setSystemInfo("Author", "Ng.M.Thu");

		return extentReports;
	}

}
