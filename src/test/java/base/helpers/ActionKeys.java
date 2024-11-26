package base.helpers;

//import java.awt.AWTException;
//import java.awt.Robot;
//import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.Status;

import base.reports.ExtentTestManager;
import base.setup.DriverManager;
import base.utils.LogUtils;
import io.qameta.allure.Step;

public class ActionKeys {

	private final static int TIME_OUT = Integer.parseInt(PropertiesHelper.getValue("implicy.wait.timeout"));
	private final static int PAGELOAD_TIME_OUT = Integer.parseInt(PropertiesHelper.getValue("page.load.timeout"));
	//private static double SLEEP_TIME = 0;

	private ActionKeys() {
	}

	public static void logConsole(String msg) {
		System.out.println(msg);
	}
	
	public static void sleep(double second) {
		try {
			Thread.sleep((long) (second * 1000));
			LogUtils.info("Sleep " + second + " s.");
			ExtentTestManager.logMessage("Sleep " + second + " s.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			new RuntimeException(e);
		}
	}

	@Step("Open URL: {0}")
	public static void openURL(String url) {
		DriverManager.getDriver().get(url);
		LogUtils.info("Open URL: " + url);
		ExtentTestManager.logMessage("Open URL: " + url);
		waitForPageLoaded();
	}
	
	@Step("Verify true: {1}")
	public static void verifyTrue(Boolean booleanVal, String purpose, String msg) {
		Assert.assertTrue(booleanVal, msg);
		LogUtils.info("Verify true: " + purpose);
		ExtentTestManager.logMessage(Status.PASS,"Verify true: " + purpose);
	}
	
	@Step("Verify true")
	public static void verifyTrue(Boolean booleanVal, String msg) {
		Assert.assertTrue(booleanVal, msg);
		//ExtentTestManager.logMessage(Status.PASS,"Verify true");
	}
	
	public static WebElement getVisibleElement(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIME_OUT));
			return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.error("Timeout waiting for the element is visible: " + locator.toString());
			ExtentTestManager.logMessage(Status.FAIL,"Timeout waiting for the element is visible: " + locator.toString());
			Assert.fail(e.getMessage());
			return null;
		}
	}

	public static WebElement getClickableElement(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIME_OUT));
			return wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.error("Timeout waiting for the element is clickable: " + locator.toString());
			ExtentTestManager.logMessage(Status.FAIL,"Timeout waiting for the element is clickable: " + locator.toString());
			Assert.fail(e.getMessage());
			return null;
		}
	}

	public static WebElement getPresenceElement(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIME_OUT));
			return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.error("Timeout waiting for the element is presenced: " + locator.toString());
			ExtentTestManager.logMessage(Status.FAIL,"Timeout waiting for the element is presenced: " + locator.toString());
			Assert.fail(e.getMessage());
			return null;
		}
	}

	public static List<WebElement> getVisibleElements(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIME_OUT));
			List<WebElement> listElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			return listElement;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.error("Timeout waiting for elements are visible: " + locator.toString());
			ExtentTestManager.logMessage(Status.FAIL,"Timeout waiting for elements are visible: " + locator.toString());
			Assert.fail(e.getMessage());
			return null;
		}
	}

	@Step("Set text: '{1}' into element with locator: {0}")
	public static void setText(By locator, String value) {
		WebElement element = getClickableElement(locator);
		scrollIntoViewElement(element);
		element.clear();
		element.sendKeys(value);
		LogUtils.info("Set text: " + value + " into element with locator: " + locator);
		ExtentTestManager.logMessage("Set text: '" + value + "' into element with locator: " + locator);
	}

	@Step("Click on element with locator: {0}")
	public static void clickElement(By locator) {
		WebElement element = getClickableElement(locator);
		scrollIntoViewElement(element);
		element.click();
		LogUtils.info("Click on element with locator: " + locator);
		ExtentTestManager.logMessage("Click on element with locator: " + locator);
	}

	@Step("Click on element with locator: {0}")
	public static void clickElementByJS(By locator) {
		WebElement element = getPresenceElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
		scrollIntoViewElement(element);
		js.executeScript("arguments[0].click();", element);
		LogUtils.info("Click on element with locator: " + locator);
		ExtentTestManager.logMessage("Click on element with locator: " + locator);
	}

	@Step("Choose element with locator: {0} to be: {1}")
	public void toggleOption(By locator, Boolean shouldBe) {
		// Check if the element is selected
		WebElement element = getClickableElement(locator);
		Boolean isSelected = element.isSelected();

		// Toggle option based on shouldBe parameter
		if (shouldBe != isSelected) {
			scrollIntoViewElement(element);
			element.click();
		}
		LogUtils.info("Choose element with locator: " + locator + "to be: " + shouldBe);
		ExtentTestManager.logMessage("Choose element with locator: " + locator + "to be: " + shouldBe);
	}

	@Step("Choose element with locator: {2} in dropdown: {1}")
	public static void chooseOneOptionFromDropdown(By preDropdownLocator, By dropdownLocator, By optionLocator) {
		WebElement preDropdownElement = getVisibleElement(preDropdownLocator);
		preDropdownElement.click();
		if(checkElementIsDisplayed(dropdownLocator)) {
			WebElement optionChoice = getClickableElement(optionLocator);
			scrollIntoViewElement(optionChoice);
			optionChoice.click();
		} else {
			LogUtils.error("Can not choose the option because the dropdown: " + dropdownLocator + "is not displayed.");
			ExtentTestManager.logMessage("Can not choose the option because the dropdown: " + dropdownLocator + "is not displayed.");
			Assert.fail("Can not choose the option because the dropdown is not displayed.");
		}
		LogUtils.info("Choose element with locator: " + optionLocator + "in dropdown: " + dropdownLocator);
		ExtentTestManager.logMessage("Choose element with locator: " + optionLocator + "in dropdown: " + dropdownLocator);

	}

	public static Boolean checkElementIsDisplayed(By locator) {
		// Check if the element is displayed
		WebElement element = getVisibleElement(locator);
		if (element.isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}

	@Step("Wait for page load")
	public static void waitForPageLoaded() {
		// Condition wait for JS load
		ExpectedCondition<Boolean> expectationForJS = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				// TODO Auto-generated method stub
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};

		// Condition wait for JQuery load
		ExpectedCondition<Boolean> expectationForJQuery = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				// TODO Auto-generated method stub
				try {
					return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					// TODO: handle exception
					// jQuery is not defined on the page, assuming it's not used
					return true;
				}
			}
		};

		try {
			WebDriverWait waitPageLoad = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(PAGELOAD_TIME_OUT));
			waitPageLoad.until(expectationForJQuery);
			waitPageLoad.until(expectationForJS);
			LogUtils.info("Wait for page load in: " + PAGELOAD_TIME_OUT + " s");
			ExtentTestManager.logMessage("Wait for page load in: " + PAGELOAD_TIME_OUT + " s");
			
			// Especially on this web page, needing to ensure that the preloader div was disabled
			By preloaderDiv = By.xpath("//div[@class='preloader']");
			waitPageLoad.until(ExpectedConditions.attributeContains(preloaderDiv, "style", "display: none;"));
		} catch (Throwable error) {
			// TODO: handle exception
			LogUtils.info("Timeout for wait page load. " + error.getMessage());
			ExtentTestManager.logMessage(Status.FAIL,"Timeout for wait page load. " + error.getMessage());
			Assert.fail("PAGE LOAD TIMEOUT. " + error.getMessage());
		}
	}

	public static Boolean verifyPageTitle(String expectedTitle) {
		String pageTitle = DriverManager.getDriver().getTitle().toLowerCase();
		expectedTitle = expectedTitle.toLowerCase();
		return pageTitle.contains(expectedTitle);
	}

	public static Boolean verifyText(By locator, String expectedText) {
		WebElement element = getVisibleElement(locator);
		scrollIntoViewElement(element);
		String text = element.getText();
		return text.equals(expectedText);
	}

	public static Boolean verifyMsgInAlert(By locator, String expectedErrorMsg) {
		WebElement alertMsg = getVisibleElement(locator);
		String strAlertMsg = alertMsg.getText();
		if (strAlertMsg.equals(expectedErrorMsg)) {
			return true;
		}
		return false;
	}

	public static Boolean verifyMsgsInAlertUseSet(By locator, Set<String> expectedMessages) {
		// Get list of element for message
		List<WebElement> listAlertMsgs = getVisibleElements(locator);

		// Compare the size of list messages to expected messages
		boolean sameSize = listAlertMsgs.size() == expectedMessages.size();
		
		// Verify all message in the Error list -> easy to understand and use
		for (WebElement msgInList : listAlertMsgs) {
			String msgInListStr = msgInList.getText();
			expectedMessages.remove(msgInListStr);
		}

		// Another way with compare as Set & because convert from list to set -> still need to compare for the case: list of messages has duplicate messages
		/*
		 * boolean allMatch = new HashSet<>(listAlertMsgs).equals(expectedMessages);
		 */

		// Verify the all message in Set is remove => all message are displayed
		return sameSize && expectedMessages.isEmpty(); // sameSize && allMatch;
		
		// Another way with Stream, no needing to compare the size
		/*
		* boolean allMessagesPresent = listAlertMsgs.stream().map(WebElement::getText).allMatch(expectedMessages::remove); 
		* or 												 .map(element -> element.getText()).allMatch(msg -> expectedMessages.remove(msg));
		*/
	}

	public static Boolean verifyMsgsInAlertUseList(By locator, List<String> expectedMessages) {
		// Get list of elements for message
		List<WebElement> listAlertMsgs = getVisibleElements(locator);
		
		// Get list of actual messages of each element
		List<String> actualMessages = listAlertMsgs.stream().map(WebElement::getText).collect(Collectors.toList());
		
		return expectedMessages.equals(actualMessages); // same size, match messages in order;
	}
	
	public static Boolean verifyErrorField(By locator) {
		WebElement errorElement = getVisibleElement(locator);
		scrollIntoViewElement(errorElement);
		
		// Verify 'error' exists in class value
		String classValue = errorElement.getAttribute("class");
		if (classValue.contains("error")) {
			return true;
		}
		return false;
	}

	@Step("Scroll element: {0] into view")
	public static void scrollIntoViewElement(WebElement element) {

		// Check if the element is in a scrollable viewport
		Boolean isElementInView = isElementInViewPort(element);

		// Scroll to the element if it is not in view
		if (!isElementInView) {
			JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

			// Find the preceding sibling element
			Wait<WebElement> waitF = new FluentWait<>(element).withTimeout(Duration.ofSeconds(5)) // Thời gian chờ tối đa là 5 giây
					.pollingEvery(Duration.ofSeconds(1)) // Kiểm tra mỗi 1 giây
					.ignoring(NoSuchElementException.class); // Bỏ qua ngoại lệ NoSuchElementException
			WebElement preSiblingElement = waitF.until(new Function<WebElement, WebElement>() {
				public WebElement apply(WebElement element) {
					try {
						String script = "var preSibling = document.evaluate('./parent::div/preceding-sibling::label', arguments[0], null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
								+ "return preSibling;";
						Object preSiblingNode = js.executeScript(script, element);
						if (preSiblingNode != null) {
							return (WebElement) js.executeScript("return arguments[0];", preSiblingNode);
						} else {
							return element;
						}
					} catch (Exception e) {
						return null;
					}
				}
			});

			// Perform scrolling
			js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'start' });", preSiblingElement);
			LogUtils.info("Scroll element: " + element + " into view");
			ExtentTestManager.logMessage("Scroll element: " + element + " into view");
		}

	}

	private static Boolean isElementInViewPort(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
		return (Boolean) js.executeScript("var e = arguments[0]," 
				+ "eRect = e.getBoundingClientRect(),"
				+ "windowHeight = (document.documentElement.clientHeight || window.innerHeight),"
				+ "windowWidth = (document.documentElement.clientWidth || window.innerWidth);"
				+ "return (eRect.top >= 0 && eRect.left >= 0 && eRect.bottom <= windowHeight && eRect.right <= windowWidth);",
				element);
	}

}
