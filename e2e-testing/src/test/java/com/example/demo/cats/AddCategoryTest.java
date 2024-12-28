package com.example.demo.cats;

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

public class AddCategoryTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CatTypeDialog catTypeDialog;
	private CategoryForm categoryForm;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.catTypeDialog = PageFactory.initElements(this.browser, CatTypeDialog.class);
		this.categoryForm = PageFactory.initElements(this.browser, CategoryForm.class);
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
		this.homePage.catsButtonClick();
		this.catTypeDialog.ensureListTabDisplayed();
		this.catTypeDialog.listTabClick();
		this.catTypeDialog.ensureCreateTabDisplayed();
		this.catTypeDialog.createTabClick();
		this.categoryForm.ensureFormDisplayed();
	}
	
	@Test
	public void testCancel() {
		this.catTypeDialog.ensureCancelButtonDisplayed();
		this.catTypeDialog.cancelButtonClick();
		this.catTypeDialog.ensureDialogClosed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}

	@Test
	public void testEmptyName() {
		this.categoryForm.nameInputFill("");
		this.categoryForm.saveButtonClick();
		this.categoryForm.ensureNameErrorDisplayed();
		assertTrue(this.categoryForm.emptyNameError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testBlankName() {
		this.categoryForm.nameInputFill("  ");
		this.categoryForm.saveButtonClick();
		this.categoryForm.ensureNameErrorDisplayed();
		assertTrue(this.categoryForm.emptyNameError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testTakenName() {
		this.categoryForm.nameInputFill(TestConstants.TAKEN_CATEGORY_NAME);
		this.categoryForm.saveButtonClick();
		this.categoryForm.ensureNameErrorDisplayed();
		assertTrue(this.categoryForm.takenNameError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
