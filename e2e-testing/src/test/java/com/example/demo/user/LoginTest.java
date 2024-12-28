package com.example.demo.user;

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

public class LoginTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private ProfilePage profilePage;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.profilePage = PageFactory.initElements(this.browser, ProfilePage.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
	}
	
	@Test
	public void testEmptyForm() {
		this.loginPage.emailInputFill("");
		this.loginPage.passwordInputFill("");
		this.loginPage.loginButtonClick();
		this.loginPage.ensureEmailErrorDisplayed();
		this.loginPage.ensurePasswordErrorDisplayed();
		assertTrue(this.loginPage.emptyEmailError());
		assertTrue(this.loginPage.emptyPasswordError());
		assertEquals(TestConstants.LOGIN_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testBlankForm() {
		this.loginPage.emailInputFill("  ");
		this.loginPage.passwordInputFill("  ");
		this.loginPage.loginButtonClick();
		this.loginPage.ensureEmailErrorDisplayed();
		this.loginPage.ensurePasswordErrorDisplayed();
		assertTrue(this.loginPage.emptyEmailError());
		assertTrue(this.loginPage.emptyPasswordError());
		assertEquals(TestConstants.LOGIN_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testNonExistingUser() {
		this.loginPage.emailInputFill("dummy@gmail.com");
		this.loginPage.passwordInputFill("dummy");
		this.loginPage.loginButtonClick();
		this.loginPage.ensureSnackBarDisplayed();
		assertEquals(TestConstants.ERROR_MESSAGE, this.loginPage.snackBarText());
		this.loginPage.closeSnackBar();
		assertEquals(TestConstants.LOGIN_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testAdmin() {
		this.loginPage.emailInputFill(TestConstants.ADMIN_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
		this.homePage.moreButtonClick();
		this.homePage.ensureLogoutButtonDisplayed();
		this.homePage.logoutButtonClick();
		this.loginPage.ensureFormDisplayed();
		assertEquals(TestConstants.LOGIN_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testGuest() {
		this.loginPage.emailInputFill(TestConstants.GUEST_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
		this.homePage.moreButtonClick();
		this.homePage.ensureProfileButtonDisplayed();
		this.homePage.profileButtonClick();
		this.homePage.ensureEditProfileButtonDiplayed();
		this.homePage.editProfileButtonClick();
		this.profilePage.ensureFormDisplayed();
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
