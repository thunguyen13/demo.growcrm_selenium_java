package testcases;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.BaseSetup;
import base.DataProviderFactory;
import base.Helpers;
import base.TestListener;
import pages.DashboardPage;
import pages.SignInPage;
import pages.SignUpPage;

@Listeners(TestListener.class)
public class SignUpTest extends BaseSetup {

	private WebDriver driver;
	private SignUpPage signUpPage;
	private SignInPage signInPage;
	private Helpers helper;
	private DashboardPage dashboardPage;

	private String firstRequired = "first name is required";
	private String lastRequired = "last name is required";
	private String companyNameRequired = "company name is required";
	private String emailRequired = "email address is required";
	private String emailInvalid = "email address is not a valid email address";
	private String emailExisted = "email address is already taken";
	private String passwordRequired = "password is required";
	private String confirmPasswordInvalid = "confirm password does not match";
	private String successSignUp = "Welcome to your new project dashboard";

	private static int testCaseCounter = 0;

	@BeforeClass(alwaysRun = true)
	public void setUpForTest() {
		driver = getDriver();
		System.out.println("Sign Up driver: " + driver);
		helper = new Helpers(driver);
		signUpPage = new SignUpPage(driver);
	}

//	@BeforeGroups(groups = { "invalidPwd" })
//	public void beforeGroups() {
//		System.out.println("Before groups run");
//	}

	@BeforeMethod(alwaysRun = true)
	public void beforeStart1Case() {
		driver.navigate().to("https://demo.growcrm.io/signup");
	}

	@Test(priority = 1)
	public void verifySignUpPage() throws InterruptedException {

		helper.waitForPageLoaded();

		Assert.assertTrue(signUpPage.verifyPageTitle(), "Sign Up Page's title does not match");
		Assert.assertTrue(signUpPage.verifyPageHeader(), "Sign Up Page's header does not macth");
		Assert.assertTrue(signUpPage.verifySignInLink(), "Sign In Link does not match");

		Thread.sleep(2000);
	}

	@Test(priority = 2)
	public void redirectToSignInPage() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage = signUpPage.goToSignInPage();

		Assert.assertTrue(signInPage.verifyPageTitle(), "Sign In page's title is not as expected");
		Assert.assertTrue(signInPage.verifyPageHeader(), "Sign In page's header does not match - Wrong redirect");

		Thread.sleep(2000);
	}

	@Test(priority = 3, groups = { "invalid" })
	public void f_blankAllField() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("", "", "", "", "", "");

		Assert.assertTrue(signUpPage.verifyErrorFirstname(), "Blank Field - First Name field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorLastname(), "Blank Field - Last Name field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorCompanyname(),
				"Blank Field - Company Name field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorEmail(), "Blank Field - Email field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorPassword(), "Blank Field - Password field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorConfirmPwd(),
				"Blank Field - Confirm Password field does not change to Error");

		Thread.sleep(2000);
	}

	@Test(priority = 3, groups = { "invalid" })
	public void f_blankSpaceFirstLastCompanyName() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp(" ", " ", " ", "client_test@demo.com", "123456", "123456");

		// String[] firstLastCompanyRequired =
		// {firstRequired,lastRequired,companyNameRequired};
		Set<String> firstLastCompanyRequired = new HashSet<String>();
		firstLastCompanyRequired.add(firstRequired);
		firstLastCompanyRequired.add(lastRequired);
		firstLastCompanyRequired.add(companyNameRequired);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(firstLastCompanyRequired),
				"All error messages are not found!");

		Thread.sleep(2000);
	}

	@Test(priority = 4, groups = { "invalid" })
	public void f_blankFirstLastCompanyName() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("", "", "", "client_test@demo.com", "123456", "123456");

		Assert.assertTrue(signUpPage.verifyErrorFirstname(), "Blank Field - First Name field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorLastname(), "Blank Field - Last Name field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorCompanyname(),
				"Blank Field - Company Name field does not change to Error");

		Thread.sleep(2000);
	}

	@Test(priority = 5, groups = { "invalid", "invalidEmail" })
	public void f_lackofDomainEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@", "123456", "123456");

		// String[] invalidEmail = {emailInvalid};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailInvalid);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		Thread.sleep(2000);
	}

	@Test(priority = 6, groups = { "invalid", "invalidEmail" })
	public void f_lackofNameEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "@example.com", "123456", "123456");

		// String[] invalidEmail = {emailInvalid};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailInvalid);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		Thread.sleep(2000);
	}

	@Test(priority = 7, groups = { "invalid", "invalidEmail" })
	public void f_blankspaceEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", " ", "123456", "123456");

		// String[] invalidEmail = {emailInvalid,emailRequired};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailInvalid);
		invalidEmail.add(emailRequired);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		Thread.sleep(2000);
	}

	@Test(priority = 8, groups = { "invalid", "invalidEmail" })
	public void f_invalidEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client.com", "123456", "123456");

		// String[] invalidEmail = {emailInvalid};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailInvalid);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		Thread.sleep(2000);
	}

	@Test(priority = 9, groups = { "invalid", "invalidEmail" })
	public void f_blankEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "", "123456", "123456");

		// String[] invalidEmail = {emailRequired};
		// Set<String> invalidEmail = new HashSet<String>();
		// invalidEmail.add(emailRequired);

		Assert.assertTrue(signUpPage.verifyErrorEmail(), "Blank Email");

		Thread.sleep(2000);
	}

	@Test(priority = 10, groups = { "invalid", "invalidEmail" })
	public void f_existedEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "mike@example.com", "123456", "123456");

		// String[] invalidEmail = {emailExisted};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailExisted);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Existed Email");

		Thread.sleep(2000);
	}

	@Test(priority = 11, groups = { "invalid", "invalidPwd", "invalidConfirmPwd" })
	public void f_blankPasswordConfirmPwd() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "", "");

		Assert.assertTrue(signUpPage.verifyErrorPassword(), "Invalid Password");

		Thread.sleep(2000);
	}

	@Test(priority = 12, groups = { "invalid", "invalidPwd" })
	public void f_passwordLessThan6Chars() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "12345", "12345");

		Assert.assertTrue(signUpPage.verifyErrorPassword(), "Invalid Password");

		Thread.sleep(2000);
	}

	@Test(priority = 13, groups = { "invalid", "invalidPwd" })
	public void f_password6BlankSpace() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "      ", "      ");

		Set<String> invalidPwd = new HashSet<String>();
		invalidPwd.add(passwordRequired);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidPwd), "Existed Email");

		Thread.sleep(2000);
	}

	@Test(priority = 14, groups = { "invalid", "invalidConfirmPwd" })
	public void f_notMatchPasswordConfirmPwd() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "123456", "123457");

		// Assert.assertTrue(signUpPage.verifyErrorConfirmPwd(), "Invalid Confirm
		// Password");
		Set<String> invalidConfirmPwd = new HashSet<String>();
		invalidConfirmPwd.add(confirmPasswordInvalid);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidConfirmPwd), "Invalid Confirm Password!");

		Thread.sleep(2000);
	}

	@Test(priority = 15, groups = { "invalid", "invalidConfirmPwd" })
	public void f_blankConfirmPwd() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "123456", "");

		Assert.assertTrue(signUpPage.verifyErrorConfirmPwd(), "Invalid Confirm Password");

		Thread.sleep(2000);
	}

	@Test(priority = 16, groups = { "valid" })
	public void p_minimunValidData() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("A", "B", "D", "t@e.c", "123456", "123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test(priority = 17, groups = { "valid" })
	public void p_blankspaceBeforeAfterEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("Tester", "Demo", "Learning AutoTest", " client_test@example.com ", "123456",
				"123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test(priority = 18, groups = { "valid" })
	public void p_specialChars() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("Tester!", "Demo@", "Learning# AutoTest$", "client_test_1@example.com",
				"%123456", "%123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test(priority = 19, groups = { "valid" })
	public void p_validAllFields() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test_2@example.com", "123456",
				"123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test(priority = 101, groups = {
			"invalid_excel" }, dataProvider = "signUpData_invalid", dataProviderClass = DataProviderFactory.class)
	public void p_invalid_excel(String first_name, String last_name, String company_name, String email, String password,
			String confirm_password, String expected_msg) throws InterruptedException {

		System.out.println("expected msg: " + expected_msg);
		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp(first_name, last_name, company_name, email, password, confirm_password);

		if (expected_msg.isEmpty()) {

			if (first_name.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorFirstname(),
						"Blank Field - First Name field does not change to Error");
			}
			if (last_name.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorLastname(),
						"Blank Field - Last Name field does not change to Error");
			}
			if (company_name.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorCompanyname(),
						"Blank Field - Company Name field does not change to Error");
			}
			if (email.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorEmail(), "Blank Field - Email field does not change to Error");
			}
			if (password.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorPassword(),
						"Blank Field - Password field does not change to Error");
			}
			if (confirm_password.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorConfirmPwd(),
						"Blank Field - Confirm Password field does not change to Error");
			}
		} else {
			String[] message = expected_msg.split(",");
			Set<String> expected_message = new HashSet<String>(Arrays.asList(message));
			Assert.assertTrue(signUpPage.verifyAllAlertMessage(expected_message), "Alerts does not match! ");
		}

		Thread.sleep(2000);
	}

	@Test(priority = 102, groups = {
			"invalid_excel" }, dataProvider = "signUpData_invalid_i", dataProviderClass = DataProviderFactory.class)
	public void p_invalid_excel_i(String first_name, String last_name, String company_name, String email,
			String password, String confirm_password, String expected_msg) throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp(first_name, last_name, company_name, email, password, confirm_password);

		if (expected_msg.isEmpty()) {

			if (first_name.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorFirstname(),
						"Blank Field - First Name field does not change to Error");
			}
			if (last_name.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorLastname(),
						"Blank Field - Last Name field does not change to Error");
			}
			if (company_name.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorCompanyname(),
						"Blank Field - Company Name field does not change to Error");
			}
			if (email.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorEmail(), "Blank Field - Email field does not change to Error");
			}
			if (password.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorPassword(),
						"Blank Field - Password field does not change to Error");
			}
			if (confirm_password.isEmpty()) {
				Assert.assertTrue(signUpPage.verifyErrorConfirmPwd(),
						"Blank Field - Confirm Password field does not change to Error");
			}
		} else {
			String[] message = expected_msg.split(",");
			Set<String> expected_message = new HashSet<String>(Arrays.asList(message));
			Assert.assertTrue(signUpPage.verifyAllAlertMessage(expected_message), "Alerts does not match! ");
		}

		Thread.sleep(2000);
	}

	@AfterMethod(alwaysRun = true)
	public void afterFinish1Case(ITestResult result) {
		System.out.println("Finish 1 case and refresh page. Total: " + ++testCaseCounter);
	}

}
