package testcases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseSetup;
import base.DataProviderFactory;
import base.Helpers;
import pages.DashboardPage;
import pages.ForgotPasswordPage;
import pages.SignInPage;
import pages.SignUpPage;

public class SignInTest extends BaseSetup {

	private WebDriver driver;
	private SignInPage signInPage;
	private SignUpPage signUpPage;
	private ForgotPasswordPage forgotPwdPage;
	private Helpers helper;
	private DashboardPage dashboardPage;
	
	private String InvalidErrorMsg = "Invalid login details";
	
	private static int testCaseCounter = 0;

	@BeforeClass
	public void setUpForTest() {
		driver = getDriver();
		System.out.println("Sign In driver: " + driver);
		helper = new Helpers(driver);
		signInPage = new SignInPage(driver);
	}
	
	@BeforeMethod
	public void beforeStart1Case() {
		if(testCaseCounter != 0) {
			driver.navigate().to("https://demo.growcrm.io/login");
		}
	}

	@Test(priority = 1)
	public void verifySignInPage() throws InterruptedException {

		helper.waitForPageLoaded();

		Assert.assertTrue(signInPage.verifyPageTitle(), "Sign In page's title is not as expected");
		Assert.assertTrue(signInPage.verifyPageHeader(), "Sign In page's header does not match");
		Assert.assertTrue(signInPage.verifyForgotPasswordLink(), "Fotgot Password text does not exist");
		Assert.assertTrue(signInPage.verifySignUpText(), "Sign Up text does not exist");

		Thread.sleep(2000);

	}
	
	@Test(priority = 2)
	public void redirectToSignUpPage() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage = signInPage.goToSignUpPage();

		Assert.assertTrue(signUpPage.verifyPageTitle(), "Sign Up page's title is not as expected");
		Assert.assertTrue(signUpPage.verifyPageHeader(), "Sign Up page's header does not match - Wrong redirect");

		Thread.sleep(2000);

	}
	
	@Test(priority = 3)
	public void redirectToForgotPasswordPage() throws InterruptedException {

		helper.waitForPageLoaded();

		forgotPwdPage = signInPage.goToForgotPasswordPage();

		Assert.assertTrue(forgotPwdPage.verifyPageTitle(), "Forgot Password Page's title is not as expected");
		Assert.assertTrue(forgotPwdPage.verifyPageHeader(), "Forgot Password Page's header does not match");

		Thread.sleep(2000);

	}

	@Test(priority = 4)
	public void f_validUserWrongPass_rememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage.signIn_remember("admin@example.com", "growcrm123");

		Assert.assertTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		Thread.sleep(2000);

	}

	@Test(priority = 5)
	public void f_validUserBlankPass_noRememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage.signIn_noRemember("admin@example.com", "");

		Assert.assertFalse(signInPage.verifyErrorEmail(), "Email - change to error state");
		Assert.assertTrue(signInPage.verifyErrorPassword(), "Password - not change to error state");

		Thread.sleep(2000);

	}
	
	@Test(priority = 6)
	public void f_invalidUserValidPass_rememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage.signIn_remember("admin1@example.com", "growcrm");

		Assert.assertTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		Thread.sleep(2000);

	}
	
	@Test(priority = 7)
	public void f_invalidUserInvalidPass_noRememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage.signIn_noRemember("admin1@example.com", "growcrm123");

		Assert.assertTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		Thread.sleep(2000);

	}
	
	@Test(priority = 8)
	public void f_blankUserValidPass_rememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage.signIn_remember("", "growcrm");

		Assert.assertTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		Assert.assertFalse(signInPage.verifyErrorPassword(), "Password - change to error state");

		Thread.sleep(2000);

	}
	
	@Test(priority = 9)
	public void f_blankUserInvalidPass_noRememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage.signIn_noRemember("", "growcrm123");

		Assert.assertTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		Assert.assertFalse(signInPage.verifyErrorPassword(), "Password - change to error state");

		Thread.sleep(2000);

	}
	
	@Test(priority = 10)
	public void f_blankUserBlankPass_rememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage.signIn_remember("", "");

		Assert.assertTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		Assert.assertTrue(signInPage.verifyErrorPassword(), "Password - not change to error state");

		Thread.sleep(2000);

	}
	
	
	@Test(priority = 11)
	public void p_validUserValidPass_noRememberMe() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signInPage.signIn_noRemember("admin@example.com", "growcrm");

		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page' title does not match ");
		Assert.assertTrue(dashboardPage.verifyPageSelected(), "Dashboard item is not selected");

		Thread.sleep(2000);

	}
	
	@Test(priority = 97, dataProvider = "signInData_blank", dataProviderClass = DataProviderFactory.class)
	public void f_blankCase(String email, String password, String rememberMe) throws InterruptedException {

		helper.waitForPageLoaded();

		switch (rememberMe) {
		case "remember":
			signInPage.signIn_remember(email, password);
			break;
		case "noRemember":
			signInPage.signIn_noRemember(email, password);
			break;
		default:
			System.out.println("No choose Remember Me or not, thus, we choose no =))");
			signInPage.signIn_noRemember(email, password);
			break;
		}
		
		if(email.isEmpty()) {
			Assert.assertTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		} else {
			Assert.assertFalse(signInPage.verifyErrorEmail(), "Email - change to error state");
		}
		
		if(password.isEmpty()) {
			Assert.assertTrue(signInPage.verifyErrorPassword(), "Password - not change to error state");
		} else {
			Assert.assertFalse(signInPage.verifyErrorPassword(), "Password - change to error state");
		}

		Thread.sleep(2000);

	}
	
	@Test(priority = 98, dataProvider = "signInData_invalid", dataProviderClass = DataProviderFactory.class)
	public void f_invalidCase(String email, String password, String rememberMe) throws InterruptedException {

		helper.waitForPageLoaded();

		switch (rememberMe) {
		case "remember":
			signInPage.signIn_remember(email, password);
			break;
		case "noRemember":
			signInPage.signIn_noRemember(email, password);
			break;
		default:
			System.out.println("No choose Remember Me or not, thus, we choose no =))");
			signInPage.signIn_noRemember(email, password);
			break;
		}
		

		Assert.assertTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		Thread.sleep(2000);

	}
	
	@Test(priority = 99, dataProvider = "signInData_valid", dataProviderClass = DataProviderFactory.class)
	public void p_validCase(String email, String password, String rememberMe) throws InterruptedException {

		helper.waitForPageLoaded();

		switch (rememberMe) {
		case "remember":
			dashboardPage = signInPage.signIn_remember(email, password);
			break;
		case "noRemember":
			dashboardPage = signInPage.signIn_noRemember(email, password);
			break;
		default:
			System.out.println("No choose Remember Me or not, thus, we choose no =))");
			dashboardPage = signInPage.signIn_noRemember(email, password);
			break;
		}
		
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page' title does not match " );
		Assert.assertTrue(dashboardPage.verifyPageSelected(), "Dashboard item is not selected");

		//Sign out
		
		Thread.sleep(2000);

	}
	
	@Test(priority = 100)
	public void quickSignIn() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signInPage.signIn_quick();

		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page' title does not match");
		Assert.assertTrue(dashboardPage.verifyPageSelected(), "Dashboard item is not selected");

		Thread.sleep(2000);

	}
	
	@AfterMethod
	public void afterFinish1Case() {
		//driver.navigate().refresh();
		System.out.println("Finish 1 case and refresh page. Total: " + ++testCaseCounter);
	}

}
