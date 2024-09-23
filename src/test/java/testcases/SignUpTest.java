package testcases;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;

import base.helpers.Helpers;
import base.listeners.ReportListener;
import base.setup.BaseSetup;
import base.setup.DataProviderFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import pages.DashboardPage;
import pages.SignInPage;
import pages.SignUpPage;

//@Listeners(base.listeners.TestListener.class)
@Listeners(ReportListener.class)
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

	@Test
	public void verifySignUpPage() throws InterruptedException {

		helper.waitForPageLoaded();

		Assert.assertTrue(signUpPage.verifyPageTitle(), "Sign Up Page's title does not match");
		Assert.assertTrue(signUpPage.verifyPageHeader(), "Sign Up Page's header does not macth");
		Assert.assertTrue(signUpPage.verifySignInLink(), "Sign In Link does not match");

		Thread.sleep(2000);
	}

	@Test
	public void redirectToSignInPage() throws InterruptedException {

		helper.waitForPageLoaded();

		signInPage = signUpPage.goToSignInPage();

		Assert.assertTrue(signInPage.verifyPageTitle(), "Sign In page's title is not as expected");
		Assert.assertTrue(signInPage.verifyPageHeader(), "Sign In page's header does not match - Wrong redirect");

		Thread.sleep(2000);
	}

	@Test
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

	@Test
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

	@Test
	public void f_blankFirstLastCompanyName() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("", "", "", "client_test@demo.com", "123456", "123456");

		Assert.assertTrue(signUpPage.verifyErrorFirstname(), "Blank Field - First Name field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorLastname(), "Blank Field - Last Name field does not change to Error");
		Assert.assertTrue(signUpPage.verifyErrorCompanyname(),
				"Blank Field - Company Name field does not change to Error");

		Thread.sleep(2000);
	}

	@Test
	public void f_lackofDomainEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@", "123456", "123456");

		// String[] invalidEmail = {emailInvalid};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailInvalid);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		Thread.sleep(2000);
	}

	@Test
	public void f_lackofNameEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "@example.com", "123456", "123456");

		// String[] invalidEmail = {emailInvalid};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailInvalid);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		Thread.sleep(2000);
	}

	@Test
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

	@Test
	public void f_invalidEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client.com", "123456", "123456");

		// String[] invalidEmail = {emailInvalid};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailInvalid);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		Thread.sleep(2000);
	}

	@Test
	public void f_blankEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "", "123456", "123456");

		// String[] invalidEmail = {emailRequired};
		// Set<String> invalidEmail = new HashSet<String>();
		// invalidEmail.add(emailRequired);

		Assert.assertTrue(signUpPage.verifyErrorEmail(), "Blank Email");

		Thread.sleep(2000);
	}

	@Test
	public void f_existedEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "mike@example.com", "123456", "123456");

		// String[] invalidEmail = {emailExisted};
		Set<String> invalidEmail = new HashSet<String>();
		invalidEmail.add(emailExisted);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Existed Email");

		Thread.sleep(2000);
	}

	@Test
	public void f_blankPasswordConfirmPwd() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "", "");

		Assert.assertTrue(signUpPage.verifyErrorPassword(), "Invalid Password");

		Thread.sleep(2000);
	}

	@Test
	public void f_passwordLessThan6Chars() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "12345", "12345");

		Assert.assertTrue(signUpPage.verifyErrorPassword(), "Invalid Password");

		Thread.sleep(2000);
	}

	@Test
	public void f_password6BlankSpace() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "      ", "      ");

		Set<String> invalidPwd = new HashSet<String>();
		invalidPwd.add(passwordRequired);

		Assert.assertTrue(signUpPage.verifyAllAlertMessage(invalidPwd), "Existed Email");

		Thread.sleep(2000);
	}

	@Description("Test to verify error message appears with invalid data")
	@Story("Invalid data")
	@Test
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

	@Test
	public void f_blankConfirmPwd() throws InterruptedException {

		helper.waitForPageLoaded();

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "123456", "");

		Assert.assertTrue(signUpPage.verifyErrorConfirmPwd(), "Invalid Confirm Password");

		Thread.sleep(2000);
	}

	@Test
	public void p_minimunValidData() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("A", "B", "D", "t@e.c", "123456", "123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test
	public void p_blankspaceBeforeAfterEmail() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("Tester", "Demo", "Learning AutoTest", " client_test@example.com ", "123456",
				"123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test
	public void p_specialChars() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("Tester!", "Demo@", "Learning# AutoTest$", "client_test_1@example.com",
				"%123456", "%123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test
	public void p_validAllFields() throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test_2@example.com", "123456",
				"123456");

		Assert.assertTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		Assert.assertTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		Thread.sleep(2000);
	}

	@Test(dataProvider = "signUpData_invalid", dataProviderClass = DataProviderFactory.class)
	@Description("Test to verify error message appears with invalid data in array")
	@Story("Invalid data")
	public void f_invalid_excel(String first_name, String last_name, String company_name, String email, String password,
			String confirm_password, String expected_msg) throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp(first_name, last_name, company_name, email, password, confirm_password);

		if (expected_msg.isEmpty()) {
			//Using Map for validate blank field
			Map<String, String> checkFields = new HashMap<String,String>();
			checkFields.put("First Name", first_name);
			checkFields.put("Last Name", last_name);
			checkFields.put("Company Name", company_name);
			checkFields.put("Email", email);
			checkFields.put("Password", password);
			checkFields.put("Confirm Password", confirm_password);
			for (Map.Entry<String, String> entry : checkFields.entrySet()) {
				String fieldName = entry.getKey();
				String fieldValue = entry.getValue();
				if(fieldValue.isEmpty()) {
					Assert.assertTrue(signUpPage.verifyErrorField(fieldName),
							"Blank Field - " + fieldName + " field does not change to Error");
				}
			}
			Thread.sleep(2000);
		} else {
			//Validate error messages match messages in expected_msg
			String[] message = expected_msg.split(",");
			Set<String> expected_message = new HashSet<String>(Arrays.asList(message));
			Assert.assertTrue(signUpPage.verifyAllAlertMessage(expected_message), "Alerts does not match! ");
			Thread.sleep(2000);
		}

		Thread.sleep(2000);
	}

	
	@Description("Test to verify error message appears with invalid data in iterator")
	@Story("Invalid data")
	@Test(dataProvider = "signUpData_invalid_i", dataProviderClass = DataProviderFactory.class)
	public void f_invalid_excel_i(String firstName, String lastName, String companyName, String email,
			String password, String confirmPassword, String expectedMsg) throws InterruptedException {

		helper.waitForPageLoaded();

		dashboardPage = signUpPage.signUp(firstName, lastName, companyName, email, password, confirmPassword);

		if (expectedMsg.isEmpty()) {
			//Using another method for validate blank field
			validateBlankField("First Name",firstName,signUpPage::verifyErrorFirstname);
			validateBlankField("Last Name",lastName,signUpPage::verifyErrorLastname);
			validateBlankField("Company Name",companyName,signUpPage::verifyErrorCompanyname);
			validateBlankField("Email",email,signUpPage::verifyErrorEmail);
			validateBlankField("Password",password,signUpPage::verifyErrorPassword);
			validateBlankField("Confirm Password",confirmPassword,signUpPage::verifyErrorConfirmPwd);
		} else {
			//Validate error messages match messages in expected_msg
			String[] message = expectedMsg.split(",");
			Set<String> expected_message = new HashSet<String>(Arrays.asList(message));
			Assert.assertTrue(signUpPage.verifyAllAlertMessage(expected_message), "Alerts does not match! ");
		}

		Thread.sleep(2000);
	}
	
	private void validateBlankField(String fieldName, String fieldValue, Supplier<Boolean> checkField) {
		if(fieldValue.isEmpty()) {
			Assert.assertTrue(checkField.get(),
					"Blank Field - " + fieldName + " field does not change to Error");
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterFinish1Case(ITestResult result) {
		System.out.println("Finish 1 case and refresh page. Total: " + ++testCaseCounter);
	}

}
