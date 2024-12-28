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

public class ValidCategoryTest {

	private WebDriver browser;

	private HomePage homePage;
	private LoginPage loginPage;
	private CatTypeDialog catTypeDialog;
	private CategoryForm categoryForm;
	private CatTypeDetails categoryDetails;
	private DeleteConfirmation deleteConfirmation;

	private static final String SUCCESS = "Category successfully added!";

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
		this.categoryDetails = PageFactory.initElements(this.browser, CatTypeDetails.class);
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
		this.homePage.catsButtonClick();
		this.catTypeDialog.ensureListTabDisplayed();
		this.catTypeDialog.listTabClick();
		this.catTypeDialog.ensureCreateTabDisplayed();
		this.catTypeDialog.createTabClick();
		this.categoryForm.ensureFormDisplayed();
	}
	
	@Test
	public void test() {
		String name = "aaaaaaaaaa";
		this.categoryForm.nameInputFill(name);
		this.categoryForm.saveButtonClick();
		this.homePage.ensureSnackBarDisplayed();
		assertEquals(SUCCESS, this.homePage.snackBarText());
		this.homePage.closeSnackBar();
		this.catTypeDialog.ensureListTabDisplayed();
		this.catTypeDialog.listTabClick();
		assertEquals(name, this.categoryDetails.nameText());
		
		this.categoryDetails.deleteButtonClick();
		this.deleteConfirmation.ensureDialogDisplayed();
		this.deleteConfirmation.confirmButtonClick();
		this.deleteConfirmation.ensureDialogClosed();
		this.homePage.ensureSnackBarDisplayed();
		assertEquals(TestConstants.ITEM_REMOVED_SUCCESS, this.homePage.snackBarText());
		this.homePage.closeSnackBar();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}
	
}
