package testcases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;

import base.helpers.ActionKeys;
import base.listeners.ReportListener;
import base.setup.BaseSetup;
import base.setup.DataProviderFactory;
import base.setup.DriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import pages.DashboardPage;
import pages.SignInPage;
import pages.SignUpPage;

@Listeners(ReportListener.class)
public class SignUpTest extends BaseSetup {
	
	private static int testCaseCounter = 0;

	private SignUpPage signUpPage;
	
	private SignInPage signInPage;
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
	
	@Parameters({ "isParallel" })
	@BeforeClass(alwaysRun = true)
	public void setUpForTest(@Optional("true") boolean isParallel) {
		if (!isParallel) {
			System.out.println("Sign Up driver (no parallel): " + DriverManager.getDriver());
			signUpPage = new SignUpPage();
		}
	}
	
	@Parameters({ "isParallel" })
	@BeforeMethod(alwaysRun = true)
	public void beforeStart1Case(@Optional("true") boolean isParallel) {
		if (isParallel) {
			System.out.println("Sign Up driver (parallel): " + DriverManager.getDriver());
			signUpPage = new SignUpPage();
		}
		
		ActionKeys.openURL("https://demo.growcrm.io/signup");
	}

	@Test
	public void verifySignUpPage() {

		ActionKeys.verifyTrue(signUpPage.verifyPageTitle(), "Verify the Sign Up page's title", "Sign Up Page's title does not match");
		ActionKeys.verifyTrue(signUpPage.verifyPageHeader(), "Verify the Sign Up page's header", "Sign Up Page's header does not macth");
		ActionKeys.verifyTrue(signUpPage.verifySignInLink(), "Verify the sign in link does exist on the sign up page", "Sign In Link does not match");

		ActionKeys.sleep(2);
	}

	@Test
	public void redirectToSignInPage() {

		signInPage = signUpPage.goToSignInPage();

		ActionKeys.verifyTrue(signInPage.verifyPageTitle(), "Verify that open the Sign In page", "Sign In page's title is not as expected - Wrong redirect");
		ActionKeys.verifyTrue(signInPage.verifyPageHeader(), "Verify the Sign In page's header", "Sign In page's header does not match - Wrong redirect");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_blankAllField() {
		
		signUpPage.signUp("", "", "", "", "", "");

		ActionKeys.verifyTrue(signUpPage.verifyErrorFirstname(), "Blank Field - First Name field does not change to Error");
		ActionKeys.verifyTrue(signUpPage.verifyErrorLastname(), "Blank Field - Last Name field does not change to Error");
		ActionKeys.verifyTrue(signUpPage.verifyErrorCompanyname(), "Blank Field - Company Name field does not change to Error");
		ActionKeys.verifyTrue(signUpPage.verifyErrorEmail(), "Blank Field - Email field does not change to Error");
		ActionKeys.verifyTrue(signUpPage.verifyErrorPassword(), "Blank Field - Password field does not change to Error");
		ActionKeys.verifyTrue(signUpPage.verifyErrorConfirmPwd(), "Blank Field - Confirm Password field does not change to Error");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_blankSpaceFirstLastCompanyName() {
		
		signUpPage.signUp(" ", " ", " ", "client_test@demo.com", "123456", "123456");

		// String[] firstLastCompanyRequired = {firstRequired,lastRequired,companyNameRequired};
		List<String> firstLastCompanyRequired = new ArrayList<String>();
		firstLastCompanyRequired.add(firstRequired);
		firstLastCompanyRequired.add(lastRequired);
		firstLastCompanyRequired.add(companyNameRequired);

		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(firstLastCompanyRequired), "All error messages are not found!");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_blankFirstLastCompanyName() {

		signUpPage.signUp("", "", "", "client_test@demo.com", "123456", "123456");

		ActionKeys.verifyTrue(signUpPage.verifyErrorFirstname(), "Blank Field - First Name field does not change to Error");
		ActionKeys.verifyTrue(signUpPage.verifyErrorLastname(), "Blank Field - Last Name field does not change to Error");
		ActionKeys.verifyTrue(signUpPage.verifyErrorCompanyname(), "Blank Field - Company Name field does not change to Error");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_lackofDomainEmail() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@", "123456", "123456");
		
		List<String> invalidEmail = new ArrayList<String>();
		invalidEmail.add(emailInvalid);
		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_lackofNameEmail() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "@example.com", "123456", "123456");

		List<String> invalidEmail = new ArrayList<String>();
		invalidEmail.add(emailInvalid);
		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_blankspaceEmail() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", " ", "123456", "123456");

		List<String> invalidEmail = new ArrayList<String>();
		invalidEmail.add(emailInvalid);
		invalidEmail.add(emailRequired);
		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_invalidEmail() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client.com", "123456", "123456");

		List<String> invalidEmail = new ArrayList<String>();
		invalidEmail.add(emailInvalid);
		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Inavlid Email");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_blankEmail() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "", "123456", "123456");

		ActionKeys.verifyTrue(signUpPage.verifyErrorEmail(), "Blank Email");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_existedEmail() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "mike@example.com", "123456", "123456");

		List<String> invalidEmail = new ArrayList<String>();
		invalidEmail.add(emailExisted);
		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(invalidEmail), "Existed Email");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_blankPasswordConfirmPwd() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "", "");

		ActionKeys.verifyTrue(signUpPage.verifyErrorPassword(), "Invalid Password");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_passwordLessThan6Chars() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "12345", "12345");

		ActionKeys.verifyTrue(signUpPage.verifyErrorPassword(), "Invalid Password");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_password6BlankSpace() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "      ", "      ");

		List<String> invalidPwd = new ArrayList<String>();
		invalidPwd.add(passwordRequired);
		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(invalidPwd), "Existed Email");

		ActionKeys.sleep(2);
	}

	@Description("Test to verify error message appears with invalid data")
	@Story("Invalid data")
	@Test
	public void f_notMatchPasswordConfirmPwd() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "123456", "123457");

		List<String> invalidConfirmPwd = new ArrayList<String>();
		invalidConfirmPwd.add(confirmPasswordInvalid);
		ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(invalidConfirmPwd), "Invalid Confirm Password!");

		ActionKeys.sleep(2);
	}

	@Test
	public void f_blankConfirmPwd() {

		signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test@example.com", "123456", "");

		ActionKeys.verifyTrue(signUpPage.verifyErrorConfirmPwd(), "Invalid Confirm Password");

		ActionKeys.sleep(2);
	}

	@Test
	public void p_minimumValidData() {

		dashboardPage = signUpPage.signUp("A", "B", "D", "t@e.c", "123456", "123456");

		ActionKeys.verifyTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		ActionKeys.verifyTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		ActionKeys.sleep(2);
	}

	@Test
	public void p_blankspaceBeforeAfterEmail() {

		dashboardPage = signUpPage.signUp("Tester", "Demo", "Learning AutoTest", " client_test@example.com ", "123456",
				"123456");

		ActionKeys.verifyTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		ActionKeys.verifyTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		ActionKeys.sleep(2);
	}

	@Test
	public void p_specialChars() {

		dashboardPage = signUpPage.signUp("Tester!", "Demo@", "Learning# AutoTest$", "client_test_1@example.com",
				"%123456", "%123456");

		ActionKeys.verifyTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		ActionKeys.verifyTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		ActionKeys.sleep(2);
	}

	@Test
	public void p_validAllFields() {

		dashboardPage = signUpPage.signUp("Tester", "Demo", "Learning AutoTest", "client_test_2@example.com", "123456",
				"123456");

		ActionKeys.verifyTrue(dashboardPage.verifySignUpSuccess(successSignUp), "Alert does not match. Sign Up Failed!");
		ActionKeys.verifyTrue(dashboardPage.verifyPageTitle(), "Dashboard Page title does not match");

		ActionKeys.sleep(2);
	}

	@Test(dataProvider = "signUpData_invalid", dataProviderClass = DataProviderFactory.class)
	@Description("Test to verify error message appears with invalid data in array")
	@Story("Invalid data")
	public void f_invalid_excel(String first_name, String last_name, String company_name, String email, String password,
			String confirm_password, String expected_msg) {

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
					ActionKeys.verifyTrue(signUpPage.verifyErrorField(fieldName),
							"Blank Field - " + fieldName + " field does not change to Error");
				}
			}
			ActionKeys.sleep(2);
		} else {
			//Validate error messages match messages in expected_msg
			String[] message = expected_msg.split(",");
			List<String> expected_message = new ArrayList<String>(Arrays.asList(message));
			ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(expected_message), "Alerts does not match! ");
			ActionKeys.sleep(2);
		}

		ActionKeys.sleep(2);
	}

	
	@Description("Test to verify error message appears with invalid data in iterator")
	@Story("Invalid data")
	@Test(dataProvider = "signUpData_invalid_i", dataProviderClass = DataProviderFactory.class)
	public void f_invalid_excel_i(String firstName, String lastName, String companyName, String email,
			String password, String confirmPassword, String expectedMsg) {

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
			List<String> expected_message = new ArrayList<String>(Arrays.asList(message));
			ActionKeys.verifyTrue(signUpPage.verifyAllAlertMessage(expected_message), "Alerts does not match! ");
		}

		ActionKeys.sleep(2);
	}
	
	private void validateBlankField(String fieldName, String fieldValue, Supplier<Boolean> checkField) {
		if(fieldValue.isEmpty()) {
			ActionKeys.verifyTrue(checkField.get(),
					"Blank Field - " + fieldName + " field does not change to Error");
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterFinish1Case(ITestResult result) {
		System.out.println("Finish 1 case and refresh page. Total: " + ++testCaseCounter);
	}

}
