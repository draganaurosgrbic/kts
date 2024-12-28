package com.example.demo.comments;

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

public class AddCommentTest {

	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CulturalDetails culturalDetails;
	private CulturalDialog culturalDialog;
	private CommentForm commentForm;
	
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
		this.commentForm = PageFactory.initElements(this.browser, CommentForm.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.GUEST_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		this.culturalDetails.moreButtonClick();
		this.culturalDialog.ensureToggleDrawerDisplayed();
		this.culturalDialog.toggleDrawerClick();
		this.culturalDialog.ensureAddCommentButtonDisplayed();
		this.culturalDialog.addCommentButtonClick();
		this.commentForm.ensureFormDisplayed();
	}
	
	@Test
	public void testCancel() {
		this.commentForm.cancelButtonClick();
		this.commentForm.ensureDialogClosed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testEmptyText() {
		this.commentForm.textInputFill("");
		this.commentForm.saveButtonClick();
		this.commentForm.ensureTextErrorDisplayed();
		assertTrue(this.commentForm.emptyTextError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testBlankText() {
		this.commentForm.textInputFill("  ");
		this.commentForm.saveButtonClick();
		this.commentForm.ensureTextErrorDisplayed();
		assertTrue(this.commentForm.emptyTextError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
		
	@After
	public void cleanUp() {
		this.browser.quit();
	}
	
}
