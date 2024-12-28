package com.example.demo.cats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.example.demo.TestConstants;
import com.example.demo.Utilities;
import com.example.demo.common.DeleteConfirmation;
import com.example.demo.common.HomePage;
import com.example.demo.user.LoginPage;

public class DeleteTypeTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CatTypeDetails typeDetails;
	private DeleteConfirmation deleteConfirmation;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.typeDetails = PageFactory.initElements(this.browser, CatTypeDetails.class);
		this.deleteConfirmation = PageFactory.initElements(this.browser, DeleteConfirmation.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.ADMIN_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.homePage.moreButtonClick();
		this.homePage.ensureCatsTypesButtonDisplayed();
		this.homePage.catsTypesButtonClick();
		this.homePage.ensureCatsButtonDisplayed();
		this.homePage.typesButtonClick();
		this.typeDetails.ensureDetailsDisplayed();
	}
	
	@Test
	public void testCancel() {
		this.typeDetails.deleteButtonClick();
		this.deleteConfirmation.ensureDialogDisplayed();
		this.deleteConfirmation.cancelButtonClick();
		this.deleteConfirmation.ensureDialogClosed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testConfirmWithCulturalOffer() {
		this.typeDetails.deleteButtonClick();
		this.deleteConfirmation.ensureDialogDisplayed();
		this.deleteConfirmation.confirmButtonClick();
		this.homePage.ensureSnackBarDisplayed();
		assertEquals(TestConstants.ITEM_REMOVED_ERROR, this.homePage.snackBarText());
		this.homePage.closeSnackBar();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}
	
}
