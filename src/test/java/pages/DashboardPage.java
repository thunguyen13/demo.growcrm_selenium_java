package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


import base.Helpers;

public class DashboardPage {
	
	WebDriver driver;
	Helpers helper;
	
	private By selectedItem_SiderBar = By.xpath("//span[normalize-space()='Dashboard']");
	private By usernameDropdown = By.xpath("//a[@id='user-dropdown']");
	
	private By myProfileChoice = By.xpath("//a[normalize-space()='My Profile']");
	
	private By notyMessage = By.xpath("//div[@class='noty_message']/span");
	
	
	public DashboardPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		helper = new Helpers(this.driver);
	}
	
	public Boolean verifyPageTitle() {
		String expectedTitle = "ABC Inc";
		return helper.verifyPageTitle(expectedTitle);
	}
	
	public Boolean verifyPageSelected() {
		String expectedItem = "Dashboard";
		return helper.verifyText(selectedItem_SiderBar, expectedItem);
	}
	
	public Boolean verifyDisplayCorrectNameUser(String username) {
		return helper.verifyText(usernameDropdown, username);	
	}
	
	public Boolean verifySignUpSuccess(String msg) {
		return helper.verifyMsgInAlert(notyMessage, msg);
	}
	
	public void redirectToMyProfile() {
		helper.chooseOneOptionFromDropdown(usernameDropdown, myProfileChoice);
	}

}
