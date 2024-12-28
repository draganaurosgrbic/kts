package com.example.demo.news;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.example.demo.TestConstants;
import com.example.demo.Utilities;
import com.example.demo.common.HomePage;
import com.example.demo.culturals.CulturalDetails;
import com.example.demo.culturals.CulturalDialog;
import com.example.demo.user.LoginPage;

public class EditNewsTest {

	private WebDriver browser;

	private HomePage homePage;
	private LoginPage loginPage;
	private CulturalDetails culturalDetails;
	private CulturalDialog culturalDialog;
	private NewsForm newsForm;
	private NewsDetails newsDetails;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.culturalDetails = PageFactory.initElements(this.browser, CulturalDetails.class);
		this.culturalDialog = PageFactory.initElements(this.browser, CulturalDialog.class);
		this.newsForm = PageFactory.initElements(this.browser, NewsForm.class);
		this.newsDetails = PageFactory.initElements(this.browser, NewsDetails.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.ADMIN_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		this.culturalDetails.moreButtonClick();
		this.culturalDialog.ensureToggleDrawerDisplayed();
		this.culturalDialog.toggleDrawerClick();
		this.culturalDialog.ensureCommentsTabDisplayed();
		this.culturalDialog.commentsTabClick();
		this.culturalDialog.ensureNewsTabDisplayed();
		this.culturalDialog.newsTabClick();
		this.newsDetails.ensureButtonsDisplayed();
		this.newsDetails.editButtonClick();
		this.newsForm.ensureFormDisplayed();
	}

	@Test
	public void testCancel() {
		this.newsForm.cancelButtonClick();
		this.newsForm.ensureDialogClosed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}

	@Test
	public void testEmptyText() {
		this.newsForm.textInputFill("");
		this.newsForm.saveButtonClick();
		this.newsForm.ensureTextErrorDisplayed();
		assertTrue(this.newsForm.emptyTextError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testBlankText() {
		this.newsForm.textInputFill("  ");
		this.newsForm.saveButtonClick();
		this.newsForm.ensureTextErrorDisplayed();
		assertTrue(this.newsForm.emptyTextError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}

	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
