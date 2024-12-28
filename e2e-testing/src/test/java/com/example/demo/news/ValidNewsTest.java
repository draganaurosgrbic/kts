package com.example.demo.news;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.example.demo.Constants;
import com.example.demo.TestConstants;
import com.example.demo.Utilities;
import com.example.demo.common.DeleteConfirmation;
import com.example.demo.common.HomePage;
import com.example.demo.common.ImagesInput;
import com.example.demo.culturals.CulturalDetails;
import com.example.demo.culturals.CulturalDialog;
import com.example.demo.user.LoginPage;

public class ValidNewsTest {

	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CulturalDetails culturalDetails;
	private CulturalDialog culturalDialog;
	private NewsForm newsForm;
	private NewsDetails newsDetails;
	private DeleteConfirmation deleteConfirmation;
	private ImagesInput imagesInput;

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
		this.deleteConfirmation = PageFactory.initElements(this.browser, DeleteConfirmation.class);
		this.imagesInput = PageFactory.initElements(this.browser, ImagesInput.class);
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
		this.culturalDialog.ensureAddNewsButtonDisplayed();
		this.culturalDialog.addNewsButtonClick();
		this.newsForm.ensureFormDisplayed();
	}

	@Test
	public void testOnlyText() {
		String text = "dummy";
		this.newsForm.textInputFill(text);
		this.newsForm.saveButtonClick();
		this.newsForm.ensureDialogClosed();
		this.newsDetails.ensureButtonsDisplayed();
		this.newsDetails.ensureTextDisplayed();
		this.newsDetails.ensureHasNoImages();
		assertEquals(text, this.newsDetails.getText());

		text = "jummy";
		this.newsDetails.editButtonClick();
		this.newsForm.ensureFormDisplayed();
		this.newsForm.textInputFill(text);
		this.newsForm.saveButtonClick();
		this.browser.manage().timeouts().implicitlyWait(Constants.TIMEOUT_WAIT, TimeUnit.MILLISECONDS);
		this.newsForm.ensureDialogClosed();		
		this.newsDetails.ensureButtonsDisplayed();
		this.newsDetails.ensureTextDisplayed();
		this.newsDetails.ensureHasNoImages();
		assertEquals(text, this.newsDetails.getText());
		
		this.newsDetails.deleteButtonClick();
		this.deleteConfirmation.ensureDialogDisplayed();
		this.deleteConfirmation.confirmButtonClick();
		this.deleteConfirmation.ensureDialogClosed();
		this.homePage.ensureSnackBarDisplayed();
		assertEquals(TestConstants.ITEM_REMOVED_SUCCESS, this.homePage.snackBarText());
		this.homePage.closeSnackBar();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testImages() {
		String text = "dummy";
		this.newsForm.textInputFill(text);
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureOneImageDisplayed();
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureTwoImagesDisplayed();
		assertEquals(2, this.imagesInput.imagesCount());
		
		this.imagesInput.deleteSecondImage();
		this.imagesInput.ensureOneImageDisplayed();
		this.imagesInput.deleteFirstImage();
		this.imagesInput.ensureNoImagesDisplayed();
		assertEquals(0, this.imagesInput.imagesCount());

		this.newsForm.saveButtonClick();
		this.newsForm.ensureDialogClosed();
		this.newsDetails.ensureButtonsDisplayed();
		this.newsDetails.ensureTextDisplayed();
		this.newsDetails.ensureHasNoImages();
		assertEquals(text, this.newsDetails.getText());

		text = "jummy";
		this.newsDetails.editButtonClick();
		this.newsForm.ensureFormDisplayed();
		this.newsForm.textInputFill(text);
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureOneImageDisplayed();
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureTwoImagesDisplayed();
		assertEquals(2, this.imagesInput.imagesCount());
		
		this.imagesInput.deleteSecondImage();
		this.imagesInput.ensureOneImageDisplayed();
		this.imagesInput.deleteFirstImage();
		this.imagesInput.ensureNoImagesDisplayed();
		assertEquals(0, this.imagesInput.imagesCount());
		
		this.newsForm.saveButtonClick();
		this.browser.manage().timeouts().implicitlyWait(Constants.TIMEOUT_WAIT, TimeUnit.MILLISECONDS);
		this.newsForm.ensureDialogClosed();
		this.newsDetails.ensureButtonsDisplayed();
		this.newsDetails.ensureTextDisplayed();
		this.newsDetails.ensureHasNoImages();
		assertEquals(text, this.newsDetails.getText());
		
		this.newsDetails.deleteButtonClick();
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
