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

public class AddTypeTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CatTypeDialog catTypeDialog;
	private TypeForm typeForm;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.catTypeDialog = PageFactory.initElements(this.browser, CatTypeDialog.class);
		this.typeForm = PageFactory.initElements(this.browser, TypeForm.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.ADMIN_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.homePage.moreButtonClick();
		this.homePage.ensureCatsTypesButtonDisplayed();
		this.homePage.catsTypesButtonClick();
		this.homePage.ensureTypesButtonDisplayed();
		this.homePage.typesButtonClick();
		this.catTypeDialog.ensureListTabDisplayed();
		this.catTypeDialog.listTabClick();
		this.catTypeDialog.ensureCreateTabDisplayed();
		this.catTypeDialog.createTabClick();
		this.typeForm.ensureFormDisplayed();
	}
	
	@Test
	public void testCancel() {
		this.catTypeDialog.ensureCancelButtonDisplayed();
		this.catTypeDialog.cancelButtonClick();
		this.catTypeDialog.ensureDialogClosed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testEmptyForm() {
		this.typeForm.categoryInputFill("");
		this.typeForm.nameInputFill("");
		this.typeForm.saveButtonClick();
		this.typeForm.ensureCategoryErrorDisplayed();
		this.typeForm.ensureNameErrorDisplayed();
		assertTrue(this.typeForm.emptyCategoryError());
		assertTrue(this.typeForm.emptyNameError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testBlankForm() {
		this.typeForm.categoryInputFill("  ");
		this.typeForm.nameInputFill("  ");
		this.typeForm.saveButtonClick();
		this.typeForm.ensureCategoryErrorDisplayed();
		this.typeForm.ensureNameErrorDisplayed();
		assertTrue(this.typeForm.emptyCategoryError());
		assertTrue(this.typeForm.emptyNameError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testNonExistingCategory() {
		this.typeForm.categoryInputFill("dummy");
		this.typeForm.nameInputFill("");
		this.typeForm.saveButtonClick();
		this.typeForm.ensureCategoryErrorDisplayed();
		this.typeForm.ensureNameErrorDisplayed();
		assertTrue(this.typeForm.nonExistingCategoryError());
		assertTrue(this.typeForm.emptyNameError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testTakenName() {
		this.typeForm.categoryInputFill("");
		this.typeForm.nameInputFill(TestConstants.TAKEN_TYPE_NAME);
		this.typeForm.saveButtonClick();
		this.typeForm.ensureCategoryErrorDisplayed();
		this.typeForm.ensureNameErrorDisplayed();
		assertTrue(this.typeForm.emptyCategoryError());
		assertTrue(this.typeForm.takenNameError());
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
