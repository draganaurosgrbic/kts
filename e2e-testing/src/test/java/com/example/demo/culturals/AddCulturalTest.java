package com.example.demo.culturals;

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
import com.example.demo.user.LoginPage;

public class AddCulturalTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CulturalForm culturalForm;
		
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.culturalForm = PageFactory.initElements(this.browser, CulturalForm.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.ADMIN_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.homePage.addOfferButtonClick();
		this.culturalForm.ensureFormDisplayed();
	}
	
	@Test
	public void testCancel() {
		this.culturalForm.cancelButtonClick();
		this.culturalForm.ensureDialogClosed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testEmptyForm() {
		this.culturalForm.typeInputFill("");
		this.culturalForm.nameInputFill("");
		this.culturalForm.locationInputFill("");
		this.culturalForm.saveButtonClick();
		this.culturalForm.ensureTypeErrorDisplayed();
		this.culturalForm.ensureNameErrorDisplayed();
		this.culturalForm.ensureLocationErrorDisplayed();
		assertTrue(this.culturalForm.emptyTypeError());
		assertTrue(this.culturalForm.emptyNameError());
		assertTrue(this.culturalForm.invalidLocationError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testBlankForm() {
		this.culturalForm.typeInputFill("  ");
		this.culturalForm.nameInputFill("  ");
		this.culturalForm.locationInputFill("  ");
		this.culturalForm.saveButtonClick();
		this.culturalForm.ensureTypeErrorDisplayed();
		this.culturalForm.ensureNameErrorDisplayed();
		this.culturalForm.ensureLocationErrorDisplayed();
		assertTrue(this.culturalForm.emptyTypeError());
		assertTrue(this.culturalForm.emptyNameError());
		assertTrue(this.culturalForm.invalidLocationError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testNonExistingType() {
		this.culturalForm.typeInputFill("dummy");
		this.culturalForm.nameInputFill("");
		this.culturalForm.locationInputFill("");
		this.culturalForm.saveButtonClick();
		this.culturalForm.ensureTypeErrorDisplayed();
		this.culturalForm.ensureNameErrorDisplayed();
		this.culturalForm.ensureLocationErrorDisplayed();
		assertTrue(this.culturalForm.nonExistingTypeError());
		assertTrue(this.culturalForm.emptyNameError());
		assertTrue(this.culturalForm.invalidLocationError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testTakenName() {
		this.culturalForm.typeInputFill("");
		this.culturalForm.nameInputFill(TestConstants.TAKEN_OFFER_NAME);
		this.culturalForm.locationInputFill("");
		this.culturalForm.saveButtonClick();
		this.culturalForm.ensureTypeErrorDisplayed();
		this.culturalForm.ensureNameErrorDisplayed();
		this.culturalForm.ensureLocationErrorDisplayed();
		assertTrue(this.culturalForm.emptyTypeError());
		assertTrue(this.culturalForm.takenNameError());
		assertTrue(this.culturalForm.invalidLocationError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}
	
}
