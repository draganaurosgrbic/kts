package com.example.demo.news;

import static org.junit.Assert.assertTrue;
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

public class PaginateFilterNewsTest {

	private WebDriver browser;

	private HomePage homePage;
	private CulturalDetails culturalDetails;
	private CulturalDialog culturalDialog;
	private NewsDetails newsDetails;
	private NewsList newsList;
	private FilterForm filterForm;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.culturalDetails = PageFactory.initElements(this.browser, CulturalDetails.class);
		this.culturalDialog = PageFactory.initElements(this.browser, CulturalDialog.class);
		this.newsDetails = PageFactory.initElements(this.browser, NewsDetails.class);
		this.newsList = PageFactory.initElements(this.browser, NewsList.class);
		this.filterForm = PageFactory.initElements(this.browser, FilterForm.class);
		this.browser.navigate().to(TestConstants.HOME_PATH);
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
		this.newsDetails.ensureTextDisplayed();
	}

	@Test
	public void testPagination() {
		this.newsList.ensureFirstPage();
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.newsList.newsCount());

		String text = this.newsDetails.getText();
		this.newsList.ensureNextButtonDisplayed();
		this.newsList.nextButtonClick();
		this.newsDetails.ensureTextDisplayed();
		assertNotEquals(this.newsDetails.getText(), text);

		text = this.newsDetails.getText();
		this.newsList.ensurePreviousButtonDisplayed();
		this.newsList.previousButtonClick();
		this.newsDetails.ensureTextDisplayed();
		assertNotEquals(this.newsDetails.getText(), text);
		
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.newsList.newsCount());
		this.newsList.ensureFirstPage();
	}

	@Test
	public void testFilterAll() {
		this.newsList.ensureToggleFilterDisplayed();
		this.newsList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.startFilterFill("");
		this.filterForm.endFilterFill("");
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(TestConstants.SMALL_PAGE_SIZE, this.newsList.newsCount());
	}
	
	@Test
	public void testFilterMore() {
		this.newsList.ensureToggleFilterDisplayed();
		this.newsList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.startFilterFill(TestConstants.FILTER_START_DATE);
		this.filterForm.endFilterFill("");
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertTrue(this.newsList.newsCount() > 1);
	}

	@Test
	public void testFilterOne() {
		this.newsList.ensureToggleFilterDisplayed();
		this.newsList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.startFilterFill(TestConstants.FILTER_START_DATE);
		this.filterForm.endFilterFill(TestConstants.FILTER_END_DATE);
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(1, this.newsList.newsCount());
	}

	@Test
	public void testFilterNone() {
		this.newsList.ensureToggleFilterDisplayed();
		this.newsList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.startFilterFill(TestConstants.FILTER_NONE_DATE);
		this.filterForm.endFilterFill(TestConstants.FILTER_NONE_DATE);
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(0, this.newsList.newsCount());
	}

	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
