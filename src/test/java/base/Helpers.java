package base;

//import java.awt.AWTException;
//import java.awt.Robot;
//import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Helpers {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	public Helpers(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		js = (JavascriptExecutor) driver;
	}

	public void setText(By locator, String value) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		scrollIntoViewElement(element);
		element.clear();
		element.sendKeys(value);
	}

	public void clickElement(By locator) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		scrollIntoViewElement(element);
		element.click();
	}
	
	public void clickElementByJS(By locator) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		scrollIntoViewElement(element);
		js.executeScript("arguments[0].click();", element);
	}
	
	public void toggleOption(By locator, Boolean shouldBe) {
		
		// Check if the element is selected
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		Boolean isSelected = element.isSelected();
		
		// Toggle option based on shouldBe parameter
		if(shouldBe && !isSelected){
			scrollIntoViewElement(element);
			element.click();
		} else if (!shouldBe && isSelected){
			scrollIntoViewElement(element);
			element.click();
		}
	}
	
	public void chooseOneOptionFromDropdown(By dropdownLocator, By optionLocator) {
		WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownLocator));
		dropdownElement.click();
		WebElement optionChoice = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
		optionChoice.click();
	}
	
	public Boolean isDisplayed(By locator) {
		
		//Check if the element is displayed
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		
		if(element.isDisplayed()) {
			return true;
		} else {
			return false;
		}

	}

	public void waitForPageLoaded() {
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
			WebDriverWait waitPageLoad = new WebDriverWait(driver, Duration.ofSeconds(30));
			waitPageLoad.until(expectationForJQuery);
			waitPageLoad.until(expectationForJS);
			By preloaderDiv = By.xpath("//div[@class='preloader']");
			waitPageLoad.until(ExpectedConditions.attributeContains(preloaderDiv, "style", "display: none;"));
		} catch (Throwable error) {
			// TODO: handle exception
			//org.testng.Assert.fail("Timeout for wait page load");
			System.out.println("Timeout for wait page load " + error.getMessage());
			//Assert.fail("Timeout for wait page load");
			
		}
	}

	public Boolean verifyPageTitle(String expectedTitle) {
		String pageTitle = driver.getTitle().toLowerCase();
		expectedTitle = expectedTitle.toLowerCase();
		return pageTitle.contains(expectedTitle);
	}

	public Boolean verifyText(By locator, String expectedText) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		scrollIntoViewElement(element);
		String text = element.getText();
		return text.equals(expectedText);
	}

	public Boolean verifyMsgInAlert(By locator, String expectedErrorMsg) {
		WebElement alertMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		//scrollIntoViewElement(errorMsg);
		String strAlertMsg = alertMsg.getText();
		//System.out.println("alert mesage: " + strAlertMsg);
		if (strAlertMsg.equals(expectedErrorMsg)) {
			return true;
		}
		return false;
	}
	
	public Boolean verifyMsgsInAlert(By locator, Set<String> expectedMessages) {
		//Get list of element for message
		List<WebElement> listAlertMsg = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
		
		//Create a set containing the messages to be verified Set<String>
		/* 
		 * expectedMsgSet = new HashSet<String>(); 
		 * for (String expectedMsg : expectedMessages) { expectedMsgSet.add(expectedMsg); }
		 */
		
		// Compare the size of list messages to expected messages
		boolean sameSize = listAlertMsg.size() == expectedMessages.size();
		//Verify all message in the Error list -> easy to understand and use
		for (WebElement msgInList : listAlertMsg) {
			String msgInListStr = msgInList.getText();
			//System.out.println(msgInListStr);
			expectedMessages.remove(msgInListStr);
		}
		
		// Another way with Stream
		/* boolean sameSize = listAlertMsg.size() == expectedMessages.size();
		 * boolean allMessagesPresent = listAlertMsg.stream() 
		 * .map(WebElement::getText) or .map(element -> element.getText()) 
		 * .allMatch(expectedMessages::remove); or .allMatch(msg -> expectedMessages.remove(msg));
		 */
		// Another way with compare as Set -> still need to compare for the case: list of messages has duplicate messages
		/* boolean sameSize = listAlertMsg.size() == expectedMessages.size();
		 * boolean allMatch = new HashSet<>(listAlertMsg).equals(expectedMessages);
		 */

		//Verify the all message in Set is remove => all message are displayed
		return sameSize && expectedMessages.isEmpty(); //sameSize && allMatch;
		
	}

	public Boolean verifyErrorField(By locator) {
		WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		scrollIntoViewElement(errorElement);
		String classValue = errorElement.getAttribute("class");
		if (classValue.contains("error")) {
			return true;
		}
		return false;
	}
	
	public void scrollIntoViewElement(WebElement element) {
		
		// Check if the element is in a scrollable viewport
		Boolean isElementInView = isElementInViewPort(element);

		// Scroll to the element if it is not in view
		if (!isElementInView) {

			// Find the preceding sibling element
			Wait<WebElement> waitF = new FluentWait<>(element).withTimeout(Duration.ofSeconds(5)) // Thời gian chờ tối đa là 5 giây
					.pollingEvery(Duration.ofSeconds(1)) // Kiểm tra mỗi 1 giây
					.ignoring(NoSuchElementException.class); // Bỏ qua ngoại lệ NoSuchElementException

			WebElement preSiblingElement = waitF.until(new Function<WebElement, WebElement>() {
				public WebElement apply(WebElement element) {
					
					try {
						JavascriptExecutor js = (JavascriptExecutor) driver;
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
		}

	}
	
/*	
    private Boolean isElementInScrollablePage(WebElement element, WebElement scrollablePage) {
		return (Boolean) js.executeScript("var eRect = arguments[0].getBoundingClientRect();"
				+ "var pageRect = arguments[1].getBoundingClientRect();"
				+ "return (eRect.top >= pageRect.top && eRect.left >= pageRect.left && eRect.bottom <= pageRect.bottom && eRect.right <= pageRect.right);",
				element, scrollablePage);
	}
*/
	
	private Boolean isElementInViewPort(WebElement element) {
		return (Boolean) js.executeScript("var e = arguments[0]," + "eRect = e.getBoundingClientRect(),"
				+ "windowHeight = (document.documentElement.clientHeight || window.innerHeight),"
				+ "windowWidth = (document.documentElement.clientWidth || window.innerWidth);"
				+ "return (eRect.top >= 0 && eRect.left >= 0 && eRect.bottom <= windowHeight && eRect.right <= windowWidth);",
				element);
	}
	

}
