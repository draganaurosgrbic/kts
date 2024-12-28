package com.example.demo.cats;

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
import com.example.demo.user.LoginPage;

public class PaginateCatsTypesTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private LoginPage loginPage;
	private CatTypeList catTypeList;
	private CatTypeDetails catTypeDetails;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.catTypeList = PageFactory.initElements(this.browser, CatTypeList.class);
		this.catTypeDetails = PageFactory.initElements(this.browser, CatTypeDetails.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.ADMIN_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.homePage.moreButtonClick();
		this.homePage.ensureCatsTypesButtonDisplayed();
		this.homePage.catsTypesButtonClick();
	}
	
	@Test
	public void testPaginateCats() {
		this.homePage.ensureCatsButtonDisplayed();
		this.homePage.catsButtonClick();
		
		this.catTypeList.ensureFirstPage();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());

		String name = this.catTypeDetails.nameText();
		this.catTypeList.ensureNextButtonDisplayed();
		this.catTypeList.nextButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensureNextButtonDisplayed();
		this.catTypeList.nextButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensureNextButtonDisplayed();
		this.catTypeList.nextButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensurePreviousButtonDisplayed();
		this.catTypeList.previousButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensurePreviousButtonDisplayed();
		this.catTypeList.previousButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());

		name = this.catTypeDetails.nameText();
		this.catTypeList.ensurePreviousButtonDisplayed();
		this.catTypeList.previousButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());

		this.catTypeList.ensureFirstPage();
	}
	
	@Test
	public void testPaginateTypes() {
		this.homePage.ensureTypesButtonDisplayed();
		this.homePage.typesButtonClick();
		
		this.catTypeList.ensureFirstPage();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());

		String name = this.catTypeDetails.nameText();
		this.catTypeList.ensureNextButtonDisplayed();
		this.catTypeList.nextButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensureNextButtonDisplayed();
		this.catTypeList.nextButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensureNextButtonDisplayed();
		this.catTypeList.nextButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensurePreviousButtonDisplayed();
		this.catTypeList.previousButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());
		
		name = this.catTypeDetails.nameText();
		this.catTypeList.ensurePreviousButtonDisplayed();
		this.catTypeList.previousButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());

		name = this.catTypeDetails.nameText();
		this.catTypeList.ensurePreviousButtonDisplayed();
		this.catTypeList.previousButtonClick();
		this.catTypeDetails.ensureDetailsDisplayed();
		assertNotEquals(this.catTypeDetails.nameText(), name);
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.catTypeList.catsTypesCount());

		this.catTypeList.ensureFirstPage();
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
