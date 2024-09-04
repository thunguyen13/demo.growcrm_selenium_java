package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.Helpers;

public class SignInPage {

	private WebDriver driver;
	private Helpers helper;

	private By emailBox = By.xpath("//input[@id='email']");
	private By passwordBox = By.xpath("//input[@id='password']");
	private By rememberMeBox = By.xpath("//span[@class='custom-control-indicator']"); 
	private By loginBtn = By.xpath("//button[@id='loginSubmitButton']");
	private By headerText = By.xpath("//h4[@class='box-title m-t-10 text-center']");
	
	private By errorAlert = By.xpath("//span[@class='noty_text']");
	
	//private By forgotPwdLink = By.xpath("//a[@id='to-recover']");
	private By forgotPwdLink = By.linkText("Forgot Password");
	//private By signUpLink = By.xpath("//b[normalize-space()='Sign Up']");
	private By signUpLink = By.linkText("Sign Up");
	private By textSignUp = By.xpath("//div[@class='col-sm-12 text-center']");

	public SignInPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		helper = new Helpers(driver);
	}
	
	public DashboardPage signIn_noRemember(String email, String password) throws InterruptedException {
		helper.setText(emailBox, email);
		helper.setText(passwordBox, password);
		helper.clickElementByJS(rememberMeBox);
		helper.clickElement(loginBtn);
		
		return new DashboardPage(driver);
	}
	
	public DashboardPage signIn_remember(String email, String password) throws InterruptedException {
		helper.setText(emailBox, email);
		helper.setText(passwordBox, password);
		helper.clickElement(loginBtn);
		
		return new DashboardPage(driver);
	}

	public DashboardPage signIn_quick() throws InterruptedException {
		helper.clickElement(loginBtn);
		
		return new DashboardPage(driver);
	}
	
	public Boolean verifyPageTitle() {
		String titleContains = "ABC Inc";
		return helper.verifyPageTitle(titleContains);
	}

	public Boolean verifyPageHeader() {
		String headerExpected = "Sign in to your account";
		return helper.verifyText(headerText,headerExpected);
	}
	
	public Boolean verifyErrorPassword() {
		return helper.verifyErrorField(passwordBox);
	}
	
	public Boolean verifyErrorEmail() {
		return helper.verifyErrorField(emailBox);
	}
	
	public Boolean verifyForgotPasswordLink() {
		String expectedTextLink = "Forgot Password";
		return helper.verifyText(forgotPwdLink, expectedTextLink);
	}
	
	public Boolean verifyErrorAlert(String errorMsg) {
		return helper.verifyText(errorAlert, errorMsg);
	}
	
	public Boolean verifySignUpText() {
		String expectedTextLink = "Don't have an account? Sign Up";
		return helper.verifyText(textSignUp, expectedTextLink);
	}
	
	public ForgotPasswordPage goToForgotPasswordPage() {
		helper.clickElement(forgotPwdLink);
		return new ForgotPasswordPage(driver);
	}
	
	public SignUpPage goToSignUpPage() {
		helper.clickElement(signUpLink);
		return new SignUpPage(driver);
	}
}
