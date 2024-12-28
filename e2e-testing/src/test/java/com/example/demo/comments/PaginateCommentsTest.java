package com.example.demo.comments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

public class PaginateCommentsTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private CulturalDetails culturalDetails;
	private CulturalDialog culturalDialog;
	private CommentDetails commentDetails;
	private CommentList commentList;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.culturalDetails = PageFactory.initElements(this.browser, CulturalDetails.class);
		this.culturalDialog = PageFactory.initElements(this.browser, CulturalDialog.class);
		this.commentDetails = PageFactory.initElements(this.browser, CommentDetails.class);
		this.commentList = PageFactory.initElements(this.browser, CommentList.class);
		this.browser.navigate().to(TestConstants.HOME_PATH);
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		this.culturalDetails.moreButtonClick();
		this.culturalDialog.ensureToggleDrawerDisplayed();
		this.culturalDialog.toggleDrawerClick();
		this.commentDetails.ensureTextDisplayed();
	}
	
	@Test
	public void testPagination() {
		this.commentList.ensureFirstPage();
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.commentList.commentsCount());

		String text = this.commentDetails.getText();
		this.commentList.ensureNextButtonDisplayed();
		this.commentList.nextButtonClick();
		this.commentDetails.ensureTextDisplayed();
		assertNotEquals(this.commentDetails.getText(), text);

		text = this.commentDetails.getText();
		this.commentList.ensurePreviousButtonDisplayed();
		this.commentList.previousButtonClick();
		this.commentDetails.ensureTextDisplayed();
		assertNotEquals(this.commentDetails.getText(), text);
		
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.commentList.commentsCount());
		this.commentList.ensureFirstPage();
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
