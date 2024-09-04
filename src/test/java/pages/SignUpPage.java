package pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.Helpers;

public class SignUpPage {

	private WebDriver driver;
	private Helpers helper;

	private By headerText = By.xpath("//h3[@class='box-title m-t-10 text-center']");
	private By firstnameBox = By.xpath("//input[@id='first_name']");
	private By lastnameBox = By.xpath("//input[@id='last_name']");
	private By companyNameBox  = By.xpath("//input[@id='client_company_name']");
	private By emailBox = By.xpath("//input[@id='email']");
	private By passwordBox = By.xpath("//input[@id='password']");
	private By confirmPasswordBox = By.xpath("//input[@id='password_confirmation']");
	private By signUpBtn = By.xpath("//button[@id='signupButton']");
	
	private By signInLink = By.xpath("//b[normalize-space()='Sign In']");
	
	private By errorAlertMessage = By.xpath("//span[@class='noty_text']/li");

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
	
	public Boolean verifySignInLink() {
		String expectedTextLink = "Sign In";
		return helper.verifyText(signInLink, expectedTextLink);
	}
	
	public SignInPage goToSignInPage() {
		helper.clickElement(signInLink);
		return new SignInPage(driver);
	}

}
