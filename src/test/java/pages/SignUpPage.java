package pages;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.helpers.Helpers;

public class SignUpPage {

	private WebDriver driver;
	private Helpers helper;

	private By headerText = By.xpath("//h3[@class='box-title m-t-10 text-center']");
	private static By firstnameBox = By.xpath("//input[@id='first_name']");
	private static By lastnameBox = By.xpath("//input[@id='last_name']");
	private static By companyNameBox  = By.xpath("//input[@id='client_company_name']");
	private static By emailBox = By.xpath("//input[@id='email']");
	private static By passwordBox = By.xpath("//input[@id='password']");
	private static By confirmPasswordBox = By.xpath("//input[@id='password_confirmation']");
	private By signUpBtn = By.xpath("//button[@id='signupButton']");
	
	private By signInLink = By.xpath("//b[normalize-space()='Sign In']");
	
	private By errorAlertMessage = By.xpath("//span[@class='noty_text']/li");
	
	private static final Map<String, By> FIELD_LOCATORS = new HashMap<>();
	static {
	    FIELD_LOCATORS.put("First Name", firstnameBox);
	    FIELD_LOCATORS.put("Last Name", lastnameBox);
	    FIELD_LOCATORS.put("Company Name", companyNameBox);
	    FIELD_LOCATORS.put("Email", emailBox);
	    FIELD_LOCATORS.put("Password", passwordBox);
	    FIELD_LOCATORS.put("Confirm Password", confirmPasswordBox);
	}

	public SignUpPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		helper = new Helpers(driver);
	}
	
	public DashboardPage signUp(String firstname, String lastname, String companyName, String email, String password, String confirmPwd) {
		helper.setText(firstnameBox, firstname);
		helper.setText(lastnameBox, lastname);
		helper.setText(companyNameBox, companyName);
		helper.setText(emailBox, email);
		helper.setText(passwordBox, password);
		helper.setText(confirmPasswordBox, confirmPwd);
		helper.clickElement(signUpBtn);
		
		return new DashboardPage(driver);
	}

	public Boolean verifyPageTitle() {
		String titleContains = "ABC Inc";
		return helper.verifyPageTitle(titleContains);
	}

	public Boolean verifyPageHeader() {
		String headerExpected = "Create New Account";
		return helper.verifyText(headerText,headerExpected);
	}
	
	public Boolean verifyAlertMessage(String msg) {
		String expectedErrorMsg = msg;
		return helper.verifyMsgInAlert(errorAlertMessage, expectedErrorMsg);
	}
	
	public Boolean verifyAllAlertMessage(Set<String> expectedMsgs) {
		return helper.verifyMsgsInAlert(errorAlertMessage, expectedMsgs);
	}
	
	public Boolean verifyErrorFirstname() {
		return helper.verifyErrorField(firstnameBox);
	}
	
	public Boolean verifyErrorLastname() {
		return helper.verifyErrorField(lastnameBox);
	}
	
	public Boolean verifyErrorCompanyname() {
		return helper.verifyErrorField(companyNameBox);
	}
	
	public Boolean verifyErrorEmail() {
		return helper.verifyErrorField(emailBox);
	}
	
	public Boolean verifyErrorPassword() {
		return helper.verifyErrorField(passwordBox);
	}
	
	public Boolean verifyErrorConfirmPwd() {
		return helper.verifyErrorField(confirmPasswordBox);
	}
	
	public Boolean verifyErrorField(String fieldName) {
		By locator = FIELD_LOCATORS.get(fieldName);
		return locator!= null && helper.verifyErrorField(locator);
	}
	
	public Boolean verifySignInLink() {
		String expectedTextLink = "Sign In";
		return helper.verifyText(signInLink, expectedTextLink);
	}
	
	public SignInPage goToSignInPage() {
		helper.clickElement(signInLink);
		return new SignInPage(driver);
	}

}
