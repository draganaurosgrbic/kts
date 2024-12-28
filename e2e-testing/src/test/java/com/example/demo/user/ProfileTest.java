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
import com.example.demo.common.ImageInput;

public class ProfileTest {

	private WebDriver browser;

	private HomePage homePage;
	private LoginPage loginPage;
	private ProfilePage profilePage;
	private ImageInput imageInput;
	
	private static final String SUCCESS = "Your profile has been updated!";
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.profilePage = PageFactory.initElements(this.browser, ProfilePage.class);
		this.imageInput = PageFactory.initElements(this.browser, ImageInput.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.GUEST_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.browser.navigate().to(TestConstants.PROFILE_PATH);
		this.profilePage.ensureFormDisplayed();
	}
	
	@Test
	public void testEmptyForm() {
		this.profilePage.emailInputFill("");
		this.profilePage.firstNameInputFill("");
		this.profilePage.lastNameInputFill("");
		this.profilePage.saveButtonClick();
		this.profilePage.ensureEmailErrorDisplayed();
		this.profilePage.ensureFirstNameErrorDisplayed();
		this.profilePage.ensureLastNameErrorDisplayed();
		assertTrue(this.profilePage.invalidEmailError());
		assertTrue(this.profilePage.emptyFirstNameError());
		assertTrue(this.profilePage.emptyLastNameError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testBlankForm() {
		this.profilePage.emailInputFill("  ");
		this.profilePage.firstNameInputFill("  ");
		this.profilePage.lastNameInputFill("  ");
		this.profilePage.saveButtonClick();
		this.profilePage.ensureEmailErrorDisplayed();
		this.profilePage.ensureFirstNameErrorDisplayed();
		this.profilePage.ensureLastNameErrorDisplayed();
		assertTrue(this.profilePage.invalidEmailError());
		assertTrue(this.profilePage.emptyFirstNameError());
		assertTrue(this.profilePage.emptyLastNameError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testInvalidEmail() {
		this.profilePage.emailInputFill("dummy");
		this.profilePage.saveButtonClick();
		this.profilePage.ensureEmailErrorDisplayed();
		assertTrue(this.profilePage.invalidEmailError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testTakenEmail() {
		this.profilePage.emailInputFill(TestConstants.ADMIN_EMAIL);
		this.profilePage.saveButtonClick();
		this.profilePage.ensureEmailErrorDisplayed();
		assertTrue(this.profilePage.emailTakenError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testNewPasswordNoOldPassword() {
		this.profilePage.newPasswordInputFill("dummy");
		this.profilePage.saveButtonClick();
		this.profilePage.ensurePasswordErrorDisplayed();
		assertTrue(this.profilePage.oldPasswordError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testConfirmationPasswordNoOldPassword() {
		this.profilePage.newPasswordConfirmationInputFill("dummy");
		this.profilePage.saveButtonClick();
		this.profilePage.ensurePasswordErrorDisplayed();
		assertTrue(this.profilePage.oldPasswordError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testNewPasswordNoConfirmationPassword() {
		this.profilePage.oldPasswordInputFill("dummy");
		this.profilePage.newPasswordInputFill("dummy");
		this.profilePage.saveButtonClick();
		this.profilePage.ensurePasswordErrorDisplayed();
		assertTrue(this.profilePage.confirmationPasswordError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testConfirmationPasswordNoNewPassword() {
		this.profilePage.oldPasswordInputFill("dummy");
		this.profilePage.newPasswordConfirmationInputFill("dummy");
		this.profilePage.saveButtonClick();
		this.profilePage.ensurePasswordErrorDisplayed();
		assertTrue(this.profilePage.confirmationPasswordError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testWrongPasswordConfirmation() {
		this.profilePage.oldPasswordInputFill("dummy");
		this.profilePage.newPasswordInputFill("dummy");
		this.profilePage.newPasswordConfirmationInputFill("dummy2");
		this.profilePage.saveButtonClick();
		this.profilePage.ensurePasswordErrorDisplayed();
		assertTrue(this.profilePage.confirmationPasswordError());
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testSaveProfile() {
		this.profilePage.emailInputFill("asd@gmail.com");
		this.profilePage.firstNameInputFill("dummy");
		this.profilePage.lastNameInputFill("dummy");
		this.imageInput.uploadFile(TestConstants.TEST_IMAGE);
		this.profilePage.saveButtonClick();
		this.profilePage.ensureSnackBarDisplayed();
		assertEquals(SUCCESS, this.profilePage.snackBarText());
		this.profilePage.closeSnackBar();
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
		this.profilePage.emailInputFill(TestConstants.GUEST_EMAIL);
		this.profilePage.firstNameInputFill(TestConstants.GUEST_FIRST_NAME);
		this.profilePage.lastNameInputFill(TestConstants.GUEST_LAST_NAME);
		this.profilePage.saveButtonClick();
		this.profilePage.ensureSnackBarDisplayed();
		assertEquals(SUCCESS, this.profilePage.snackBarText());
		this.profilePage.closeSnackBar();
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testSavePassword() {
		this.profilePage.oldPasswordInputFill(TestConstants.LOGIN_PASSWORD);
		this.profilePage.newPasswordInputFill(TestConstants.NEW_PASSWORD);
		this.profilePage.newPasswordConfirmationInputFill(TestConstants.NEW_PASSWORD);
		this.profilePage.saveButtonClick();
		this.profilePage.ensureSnackBarDisplayed();
		assertEquals(SUCCESS, this.profilePage.snackBarText());
		this.profilePage.closeSnackBar();
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
		this.profilePage.oldPasswordInputFill(TestConstants.NEW_PASSWORD);
		this.profilePage.newPasswordInputFill(TestConstants.LOGIN_PASSWORD);
		this.profilePage.newPasswordConfirmationInputFill(TestConstants.LOGIN_PASSWORD);
		this.profilePage.saveButtonClick();
		this.profilePage.ensureSnackBarDisplayed();
		assertEquals(SUCCESS, this.profilePage.snackBarText());
		this.profilePage.closeSnackBar();
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());		
	}
	
	@Test
	public void testSavePasswordWrongOldPassword() {
		this.profilePage.oldPasswordInputFill("dummy");
		this.profilePage.newPasswordInputFill(TestConstants.NEW_PASSWORD);
		this.profilePage.newPasswordConfirmationInputFill(TestConstants.NEW_PASSWORD);
		this.profilePage.saveButtonClick();
		this.profilePage.ensureSnackBarDisplayed();
		assertEquals(TestConstants.ERROR_MESSAGE, this.profilePage.snackBarText());
		this.profilePage.closeSnackBar();
		assertEquals(TestConstants.PROFILE_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}
	
}
