package pages;


import org.openqa.selenium.By;

import base.helpers.ActionKeys;

public class SignInPage {

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

	public SignInPage() {
		// TODO Auto-generated constructor stub
	}
	
	public DashboardPage signIn_noRemember(String email, String password) throws InterruptedException {
		ActionKeys.setText(emailBox, email);
		ActionKeys.setText(passwordBox, password);
		ActionKeys.clickElementByJS(rememberMeBox);
		ActionKeys.clickElement(loginBtn);
		
		return new DashboardPage();
	}
	
	public DashboardPage signIn_remember(String email, String password) throws InterruptedException {
		ActionKeys.setText(emailBox, email);
		ActionKeys.setText(passwordBox, password);
		ActionKeys.clickElement(loginBtn);
		
		return new DashboardPage();
	}

	public DashboardPage signIn_quick() throws InterruptedException {
		ActionKeys.clickElement(loginBtn);
		
		return new DashboardPage();
	}
	
	public Boolean verifyPageTitle() {
		String titleContains = "ABC Inc";
		return ActionKeys.verifyPageTitle(titleContains);
	}

	public Boolean verifyPageHeader() {
		String headerExpected = "Sign in to your account";
		return ActionKeys.verifyText(headerText,headerExpected);
	}
	
	public Boolean verifyErrorPassword() {
		return ActionKeys.verifyErrorField(passwordBox);
	}
	
	public Boolean verifyErrorEmail() {
		return ActionKeys.verifyErrorField(emailBox);
	}
	
	public Boolean verifyForgotPasswordLink() {
		String expectedTextLink = "Forgot Password";
		return ActionKeys.verifyText(forgotPwdLink, expectedTextLink);
	}
	
	public Boolean verifyErrorAlert(String errorMsg) {
		return ActionKeys.verifyText(errorAlert, errorMsg);
	}
	
	public Boolean verifySignUpText() {
		String expectedTextLink = "Don't have an account? Sign Up";
		return ActionKeys.verifyText(textSignUp, expectedTextLink);
	}
	
	public ForgotPasswordPage goToForgotPasswordPage() {
		ActionKeys.clickElement(forgotPwdLink);
		return new ForgotPasswordPage();
	}
	
	public SignUpPage goToSignUpPage() {
		ActionKeys.clickElement(signUpLink);
		return new SignUpPage();
	}
}
