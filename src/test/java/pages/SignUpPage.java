package pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import base.helpers.ActionKeys;

public class SignUpPage {

	private By headerText = By.xpath("//h3[@class='box-title m-t-10 text-center']");
	private static By firstnameBox = By.xpath("//input[@id='first_name']");
	private static By lastnameBox = By.xpath("//input[@id='last_name']");
	private static By companyNameBox  = By.xpath("//input[@id='client_company_name']");
	private static By emailBox = By.xpath("//input[@id='email']");
	private static By passwordBox = By.xpath("//input[@id='password']");
	private static By confirmPasswordBox = By.xpath("//input[@id='password_confirmation']");
	private static By signUpBtn = By.xpath("//button[@id='signupButton']");
	
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
	
	public SignUpPage() {
		// TODO Auto-generated constructor stub
	}
	
	public DashboardPage signUp(String firstname, String lastname, String companyName, String email, String password, String confirmPwd) {
		ActionKeys.setText(firstnameBox, firstname);
		ActionKeys.setText(lastnameBox, lastname);
		ActionKeys.setText(companyNameBox, companyName);
		ActionKeys.setText(emailBox, email);
		ActionKeys.setText(passwordBox, password);
		ActionKeys.setText(confirmPasswordBox, confirmPwd);
		ActionKeys.clickElement(signUpBtn);
		
		return new DashboardPage();
	}

	public Boolean verifyPageTitle() {
		String titleContains = "ABC Inc";
		return ActionKeys.verifyPageTitle(titleContains);
	}

	public Boolean verifyPageHeader() {
		String headerExpected = "Create New Account";
		return ActionKeys.verifyText(headerText,headerExpected);
	}
	
	public Boolean verifyAlertMessage(String msg) {
		String expectedErrorMsg = msg;
		return ActionKeys.verifyMsgInAlert(errorAlertMessage, expectedErrorMsg);
	}
	
	public Boolean verifyAllAlertMessage(List<String> expectedMsgs) {
		return ActionKeys.verifyMsgsInAlertUseList(errorAlertMessage, expectedMsgs);
	}
	
	public Boolean verifyErrorFirstname() {
		return ActionKeys.verifyErrorField(firstnameBox);
	}
	
	public Boolean verifyErrorLastname() {
		return ActionKeys.verifyErrorField(lastnameBox);
	}
	
	public Boolean verifyErrorCompanyname() {
		return ActionKeys.verifyErrorField(companyNameBox);
	}
	
	public Boolean verifyErrorEmail() {
		return ActionKeys.verifyErrorField(emailBox);
	}
	
	public Boolean verifyErrorPassword() {
		return ActionKeys.verifyErrorField(passwordBox);
	}
	
	public Boolean verifyErrorConfirmPwd() {
		return ActionKeys.verifyErrorField(confirmPasswordBox);
	}
	
	public Boolean verifyErrorField(String fieldName) {
		By locator = FIELD_LOCATORS.get(fieldName);
		return locator != null && ActionKeys.verifyErrorField(locator);
	}
	
	public Boolean verifySignInLink() {
		String expectedTextLink = "Sign In";
		return ActionKeys.verifyText(signInLink, expectedTextLink);
	}
	
	public SignInPage goToSignInPage() {
		ActionKeys.clickElement(signInLink);
		return new SignInPage();
	}

}
