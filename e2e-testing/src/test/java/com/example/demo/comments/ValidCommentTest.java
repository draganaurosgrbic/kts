package com.example.demo.comments;

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
import com.example.demo.common.ImagesInput;
import com.example.demo.culturals.CulturalDetails;
import com.example.demo.culturals.CulturalDialog;
import com.example.demo.user.LoginPage;

public class ValidCommentTest {

	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CulturalDetails culturalDetails;
	private CulturalDialog culturalDialog;
	private CommentForm commentForm;
	private CommentDetails commentDetails;
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
		this.commentForm = PageFactory.initElements(this.browser, CommentForm.class);
		this.commentDetails = PageFactory.initElements(this.browser, CommentDetails.class);
		this.deleteConfirmation = PageFactory.initElements(this.browser, DeleteConfirmation.class);
		this.imagesInput = PageFactory.initElements(this.browser, ImagesInput.class);
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
	public void testOnlyText() {
		String text = "dummy";
		this.commentForm.textInputFill(text);
		this.commentForm.saveButtonClick();
		this.commentForm.ensureDialogClosed();
		this.commentDetails.ensureNoStarsDisplayed();
		this.commentDetails.ensureButtonsDisplayed();
		this.commentDetails.ensureTextDisplayed();
		this.commentDetails.ensureHasNoImages();
		assertEquals(text, this.commentDetails.getText());

		text = "jummy";
		this.commentDetails.editButtonClick();
		this.commentForm.ensureFormDisplayed();
		this.commentForm.textInputFill(text);
		this.commentForm.saveButtonClick();
		this.commentForm.ensureDialogClosed();
		this.commentDetails.ensureNoStarsDisplayed();
		this.commentDetails.ensureButtonsDisplayed();
		this.commentDetails.ensureTextDisplayed();
		this.commentDetails.ensureHasNoImages();
		assertEquals(text, this.commentDetails.getText());
		
		this.commentDetails.deleteButtonClick();
		this.deleteConfirmation.ensureDialogDisplayed();
		this.deleteConfirmation.confirmButtonClick();
		this.deleteConfirmation.ensureDialogClosed();
		this.homePage.ensureSnackBarDisplayed();
		assertEquals(TestConstants.ITEM_REMOVED_SUCCESS, this.homePage.snackBarText());
		this.homePage.closeSnackBar();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@Test
	public void testStars() {
		String text = "dummy";
		this.commentForm.textInputFill(text);
		this.commentForm.thirdStarClick();
		this.commentForm.saveButtonClick();
		this.commentForm.ensureDialogClosed();
		this.commentDetails.ensureThreeStarsDisplayed();
		this.commentDetails.ensureButtonsDisplayed();
		this.commentDetails.ensureTextDisplayed();
		this.commentDetails.ensureHasNoImages();
		assertEquals(text, this.commentDetails.getText());

		text = "jummy";
		this.commentDetails.editButtonClick();
		this.commentForm.ensureFormDisplayed();
		this.commentForm.textInputFill(text);
		this.commentForm.secondStarClick();
		this.commentForm.saveButtonClick();
		this.commentForm.ensureDialogClosed();
		this.commentDetails.ensureTwoStarsDisplayed();
		this.commentDetails.ensureButtonsDisplayed();
		this.commentDetails.ensureTextDisplayed();
		this.commentDetails.ensureHasNoImages();
		assertEquals(text, this.commentDetails.getText());
		
		this.commentDetails.deleteButtonClick();
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
		this.commentForm.textInputFill(text);
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureOneImageDisplayed();
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureTwoImagesDisplayed();
		assertEquals(2, this.imagesInput.imagesCount());
		
		this.commentForm.saveButtonClick();
		this.commentForm.ensureDialogClosed();
		this.commentDetails.ensureNoStarsDisplayed();
		this.commentDetails.ensureButtonsDisplayed();
		this.commentDetails.ensureTextDisplayed();
		this.commentDetails.ensureHasImages();
		this.commentDetails.carouselToggleClick();
		this.commentDetails.ensureImageDisplayed();
		assertEquals(text, this.commentDetails.getText());

		text = "jummy";
		this.commentDetails.editButtonClick();
		this.commentForm.ensureFormDisplayed();
		this.commentForm.textInputFill(text);
		this.imagesInput.deleteSecondImage();
		this.imagesInput.ensureOneImageDisplayed();
		this.imagesInput.deleteFirstImage();
		this.imagesInput.ensureNoImagesDisplayed();
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureOneImageDisplayed();
		this.imagesInput.uploadFile(TestConstants.TEST_IMAGE);
		this.imagesInput.ensureTwoImagesDisplayed();
		assertEquals(2, this.imagesInput.imagesCount());
		
		this.commentForm.saveButtonClick();
		this.commentForm.ensureDialogClosed();
		this.commentDetails.ensureNoStarsDisplayed();
		this.commentDetails.ensureButtonsDisplayed();
		this.commentDetails.ensureTextDisplayed();
		this.commentDetails.ensureHasImages();
		this.commentDetails.carouselToggleClick();
		this.commentDetails.ensureImageDisplayed();
		assertEquals(text, this.commentDetails.getText());
		
		this.commentDetails.deleteButtonClick();
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
