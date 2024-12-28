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
import com.example.demo.common.ImageInput;
import com.example.demo.user.LoginPage;

public class ValidTypeTest {

	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CatTypeDialog catTypeDialog;
	private TypeForm typeForm;
	private CatTypeDetails typeDetails;
	private DeleteConfirmation deleteConfirmation;
	private ImageInput imageInput;

	private static final String SUCCESS = "Type successfully added!";

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
		this.typeDetails = PageFactory.initElements(this.browser, CatTypeDetails.class);
		this.deleteConfirmation = PageFactory.initElements(this.browser, DeleteConfirmation.class);
		this.imageInput = PageFactory.initElements(this.browser, ImageInput.class);
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
	public void test() {
		String category = "institution";
		String name = "aaaaaaaaaa";
		this.typeForm.categoryInputFill(category);
		this.typeForm.nameInputFill(name);
		this.imageInput.uploadFile(TestConstants.TEST_IMAGE);
		this.catTypeDialog.createTabClick();
		this.typeForm.saveButtonClick();
		this.homePage.ensureSnackBarDisplayed();
		assertEquals(SUCCESS, this.homePage.snackBarText());
		this.homePage.closeSnackBar();
		this.catTypeDialog.ensureListTabDisplayed();
		this.catTypeDialog.listTabClick();
		assertEquals(name, this.typeDetails.nameText());

		this.typeDetails.deleteButtonClick();
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
