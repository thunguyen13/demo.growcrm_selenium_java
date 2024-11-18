package testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import base.helpers.ActionKeys;
import base.listeners.ReportListener;
import base.setup.BaseSetup;
import base.setup.DataProviderFactory;
import base.setup.DriverManager;
import pages.DashboardPage;
import pages.ForgotPasswordPage;
import pages.SignInPage;
import pages.SignUpPage;

@Listeners(ReportListener.class)
public class SignInTest extends BaseSetup {

	private SignInPage signInPage;
	private SignUpPage signUpPage;
	private ForgotPasswordPage forgotPwdPage;
	private DashboardPage dashboardPage;
	
	private String InvalidErrorMsg = "Invalid login details";
	
	private static int testCaseCounter = 0;

	@Parameters({ "isParallel" })
	@BeforeClass(alwaysRun = true)
	public void setUpForTest(@Optional("true") boolean isParallel) {
		if (!isParallel) {
			System.out.println("Sign In driver (no parallel): " + DriverManager.getDriver());
			signInPage = new SignInPage();
		}
	}
	
	@Parameters({ "isParallel" })
	@BeforeMethod(alwaysRun = true)
	public void beforeStart1Case(@Optional("true") boolean isParallel) {
		if (isParallel) {
			System.out.println("Sign In driver (parallel): " + DriverManager.getDriver());
			signInPage = new SignInPage();
		}
		
		ActionKeys.openURL("https://demo.growcrm.io");
	}

	@Test(priority = 1)
	public void verifySignInPage() throws InterruptedException {

		ActionKeys.verifyTrue(signInPage.verifyPageTitle(), "Sign In page's title is not as expected");
		ActionKeys.verifyTrue(signInPage.verifyPageHeader(), "Sign In page's header does not match");
		ActionKeys.verifyTrue(signInPage.verifyForgotPasswordLink(), "Fotgot Password text does not exist");
		ActionKeys.verifyTrue(signInPage.verifySignUpText(), "Sign Up text does not exist");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 2)
	public void redirectToSignUpPage() throws InterruptedException {

		signUpPage = signInPage.goToSignUpPage();

		ActionKeys.verifyTrue(signUpPage.verifyPageTitle(), "Sign Up page's title is not as expected");
		ActionKeys.verifyTrue(signUpPage.verifyPageHeader(), "Sign Up page's header does not match - Wrong redirect");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 3)
	public void redirectToForgotPasswordPage() throws InterruptedException {
		
		forgotPwdPage = signInPage.goToForgotPasswordPage();

		ActionKeys.verifyTrue(forgotPwdPage.verifyPageTitle(), "Forgot Password Page's title is not as expected");
		ActionKeys.verifyTrue(forgotPwdPage.verifyPageHeader(), "Forgot Password Page's header does not match");

		ActionKeys.sleep(2);
	}

	@Test(priority = 4)
	public void f_validUserWrongPass_rememberMe() throws InterruptedException {

		signInPage.signIn_remember("admin@example.com", "growcrm123");

		ActionKeys.verifyTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		ActionKeys.sleep(2);
	}

	@Test(priority = 5)
	public void f_validUserBlankPass_noRememberMe() throws InterruptedException {

		signInPage.signIn_noRemember("admin@example.com", "");

		Assert.assertFalse(signInPage.verifyErrorEmail(), "Email - change to error state");
		ActionKeys.verifyTrue(signInPage.verifyErrorPassword(), "Password - not change to error state");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 6)
	public void f_invalidUserValidPass_rememberMe() throws InterruptedException {

		signInPage.signIn_remember("admin1@example.com", "growcrm");

		ActionKeys.verifyTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 7)
	public void f_invalidUserInvalidPass_noRememberMe() throws InterruptedException {

		signInPage.signIn_noRemember("admin1@example.com", "growcrm123");

		ActionKeys.verifyTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 8)
	public void f_blankUserValidPass_rememberMe() throws InterruptedException {

		signInPage.signIn_remember("", "growcrm");

		ActionKeys.verifyTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		Assert.assertFalse(signInPage.verifyErrorPassword(), "Password - change to error state");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 9)
	public void f_blankUserInvalidPass_noRememberMe() throws InterruptedException {

		signInPage.signIn_noRemember("", "growcrm123");

		ActionKeys.verifyTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		Assert.assertFalse(signInPage.verifyErrorPassword(), "Password - change to error state");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 10)
	public void f_blankUserBlankPass_rememberMe() throws InterruptedException {

		signInPage.signIn_remember("", "");

		ActionKeys.verifyTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		ActionKeys.verifyTrue(signInPage.verifyErrorPassword(), "Password - not change to error state");

		ActionKeys.sleep(2);
	}
	
	
	@Test(priority = 11)
	public void p_validUserValidPass_noRememberMe() throws InterruptedException {

		dashboardPage = signInPage.signIn_noRemember("admin@example.com", "growcrm");

		ActionKeys.verifyTrue(dashboardPage.verifyPageTitle(), "Dashboard Page' title does not match ");
		ActionKeys.verifyTrue(dashboardPage.verifyPageSelected(), "Dashboard item is not selected");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 97, dataProvider = "signInData_blank", dataProviderClass = DataProviderFactory.class)
	public void f_blankCase(String email, String password, String rememberMe) throws InterruptedException {

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
			ActionKeys.verifyTrue(signInPage.verifyErrorEmail(), "Email - not change to error state");
		} else {
			Assert.assertFalse(signInPage.verifyErrorEmail(), "Email - change to error state");
		}
		
		if(password.isEmpty()) {
			ActionKeys.verifyTrue(signInPage.verifyErrorPassword(), "Password - not change to error state");
		} else {
			Assert.assertFalse(signInPage.verifyErrorPassword(), "Password - change to error state");
		}

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 98, dataProvider = "signInData_invalid", dataProviderClass = DataProviderFactory.class)
	public void f_invalidCase(String email, String password, String rememberMe) throws InterruptedException {
		
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
		
		ActionKeys.verifyTrue(signInPage.verifyErrorAlert(InvalidErrorMsg), "Invalid Message does not match");

		ActionKeys.sleep(2);
	}
	
	@Test(priority = 99, dataProvider = "signInData_valid", dataProviderClass = DataProviderFactory.class)
	public void p_validCase(String email, String password, String rememberMe) throws InterruptedException {

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
		
		ActionKeys.verifyTrue(dashboardPage.verifyPageTitle(), "Dashboard Page' title does not match " );
		ActionKeys.verifyTrue(dashboardPage.verifyPageSelected(), "Dashboard item is not selected");

		//Sign out
		
		ActionKeys.sleep(2);
	}
	
	@Test(priority = 100)
	public void quickSignIn() throws InterruptedException {

		dashboardPage = signInPage.signIn_quick();

		ActionKeys.verifyTrue(dashboardPage.verifyPageTitle(), "Dashboard Page' title does not match");
		ActionKeys.verifyTrue(dashboardPage.verifyPageSelected(), "Dashboard item is not selected");

		ActionKeys.sleep(2);
	}
	
	@AfterMethod
	public void afterFinish1Case() {
		//driver.navigate().refresh();
		System.out.println("Finish 1 case and refresh page. Total: " + ++testCaseCounter);
	}

}
