package com.example.demo.followings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.example.demo.TestConstants;
import com.example.demo.Utilities;
import com.example.demo.common.HomePage;
import com.example.demo.culturals.CulturalDetails;
import com.example.demo.culturals.CulturalDialog;
import com.example.demo.culturals.CulturalList;
import com.example.demo.user.LoginPage;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SubscribeUnsubscribeTest {

	private static final String SUB_BUTTON_TEXT = "email Subscribe";
	private static final String UNSUB_BUTTON_TEXT = "unsubscribe Unsubscribe";
	
	private WebDriver browser;

	private HomePage homePage;
	private LoginPage loginPage;
	private CulturalDetails culturalOfferDetails;
	private CulturalDialog culturalOfferDialog;
	private CulturalList culturalOfferList;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.loginPage = PageFactory.initElements(this.browser, LoginPage.class);
		this.culturalOfferDetails = PageFactory.initElements(this.browser, CulturalDetails.class);
		this.culturalOfferList = PageFactory.initElements(this.browser, CulturalList.class);
		this.culturalOfferDialog = PageFactory.initElements(this.browser, CulturalDialog.class);
		this.browser.navigate().to(TestConstants.LOGIN_PATH);
		this.loginPage.ensureFormDisplayed();
		this.loginPage.emailInputFill(TestConstants.GUEST_EMAIL);
		this.loginPage.passwordInputFill(TestConstants.LOGIN_PASSWORD);
		this.loginPage.loginButtonClick();
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalOfferDetails.ensureDetailsDisplayed();
		this.culturalOfferDetails.moreButtonClick();
		this.culturalOfferDialog.ensureSubUnsubButtonDisplayed();
	}

	@Test
	public void testSubscribe() {
		this.culturalOfferDialog.subUnsubButtonClick();
		this.culturalOfferDialog.ensureSubUnsubButtonDisplayed();
		assertEquals(UNSUB_BUTTON_TEXT, this.culturalOfferDialog.getSubUnsubButtonText());

		this.browser.navigate().to(TestConstants.HOME_PATH);
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalOfferList.ensureCulturalsTabDisplayed();
		this.culturalOfferList.culturalsTabClick();
		this.culturalOfferList.ensureFollowingsTabDisplayed();
		this.culturalOfferList.followingsTabClick();

		this.culturalOfferList.ensureFirstPage();
		this.culturalOfferDetails.ensureDetailsDisplayed();
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalOfferList.offersCount());

		this.culturalOfferList.ensureNextButtonDisplayed();
		this.culturalOfferList.nextButtonClick();
		this.culturalOfferList.ensureLastPage();
		this.culturalOfferDetails.ensureDetailsDisplayed();
		assertEquals(3, this.culturalOfferList.offersCount());
	}

	@Test
	public void testUnsubscribe() {
		this.culturalOfferDialog.subUnsubButtonClick();
		this.culturalOfferDialog.ensureSubUnsubButtonDisplayed();
		assertEquals(SUB_BUTTON_TEXT, this.culturalOfferDialog.getSubUnsubButtonText());

		this.browser.navigate().to(TestConstants.HOME_PATH);
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalOfferList.ensureCulturalsTabDisplayed();
		this.culturalOfferList.culturalsTabClick();
		this.culturalOfferList.ensureFollowingsTabDisplayed();
		this.culturalOfferList.followingsTabClick();

		this.culturalOfferList.ensureFirstPage();
		this.culturalOfferDetails.ensureDetailsDisplayed();
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalOfferList.offersCount());

		this.culturalOfferList.ensureNextButtonDisplayed();
		this.culturalOfferList.nextButtonClick();
		this.culturalOfferList.ensureLastPage();
		this.culturalOfferDetails.ensureDetailsDisplayed();
		assertEquals(2, this.culturalOfferList.offersCount());
	}

	@After
	public void cleanUp() {
		this.browser.quit();
	}

}