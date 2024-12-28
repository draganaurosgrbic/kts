package com.example.demo.culturals;

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

public class PaginateFilterCulturalTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private CulturalList culturalList;
	private CulturalDetails culturalDetails;
	private FilterForm filterForm;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.culturalList = PageFactory.initElements(this.browser, CulturalList.class);
		this.culturalDetails = PageFactory.initElements(this.browser, CulturalDetails.class);
		this.filterForm = PageFactory.initElements(this.browser, FilterForm.class);
		this.browser.navigate().to(TestConstants.HOME_PATH);
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
	}
	
	@Test
	public void testPagination() {
		this.culturalList.ensureFirstPage();
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());

		String name = this.culturalDetails.nameText();
		this.culturalList.ensureNextButtonDisplayed();
		this.culturalList.nextButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensureNextButtonDisplayed();
		this.culturalList.nextButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensureNextButtonDisplayed();
		this.culturalList.nextButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensureNextButtonDisplayed();
		this.culturalList.nextButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensureNextButtonDisplayed();
		this.culturalList.nextButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensurePreviousButtonDisplayed();
		this.culturalList.previousButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensurePreviousButtonDisplayed();
		this.culturalList.previousButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensurePreviousButtonDisplayed();
		this.culturalList.previousButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensurePreviousButtonDisplayed();
		this.culturalList.previousButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		name = this.culturalDetails.nameText();
		this.culturalList.ensurePreviousButtonDisplayed();
		this.culturalList.previousButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		assertNotEquals(this.culturalDetails.nameText(), name);
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
		
		this.culturalList.ensureFirstPage();
	}
	
	@Test
	public void testNameFilterMore() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill(TestConstants.NAME_FILTER_MORE);
		this.filterForm.locationFilterFill("");
		this.filterForm.typeFilterFill("");
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertTrue(this.culturalList.offersCount() > 1);
		assertTrue(this.culturalList.offerNamesContainParam(TestConstants.NAME_FILTER_MORE));
	}
	
	@Test
	public void testNameFilterOne() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill(TestConstants.NAME_FILTER_ONE);
		this.filterForm.locationFilterFill("");
		this.filterForm.typeFilterFill("");
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(1, this.culturalList.offersCount());
		assertTrue(this.culturalList.offerNamesContainParam(TestConstants.NAME_FILTER_ONE));
	}
	
	@Test
	public void testNameFilterNone() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill(TestConstants.FILTER_NONE);
		this.filterForm.locationFilterFill("");
		this.filterForm.typeFilterFill("");
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(0, this.culturalList.offersCount());
	}
	
	@Test
	public void testLocationFilterMore() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill("");
		this.filterForm.locationFilterFill(TestConstants.LOCATION_FILTER_MORE);
		this.filterForm.typeFilterFill("");
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertTrue(this.culturalList.offersCount() > 1);
		assertTrue(this.culturalList.offerLocationsContainParam(TestConstants.LOCATION_FILTER_MORE));
	}
	
	@Test
	public void testLoocationFilterOne() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill("");
		this.filterForm.locationFilterFill(TestConstants.LOCATION_FILTER_ONE);
		this.filterForm.typeFilterFill("");
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(1, this.culturalList.offersCount());
		assertTrue(this.culturalList.offerLocationsContainParam(TestConstants.LOCATION_FILTER_ONE));
	}
	
	@Test
	public void testLocationFilterNone() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill("");
		this.filterForm.locationFilterFill(TestConstants.FILTER_NONE);
		this.filterForm.typeFilterFill("");
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(0, this.culturalList.offersCount());
	}
	
	@Test
	public void testTypeFilterMore() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill("");
		this.filterForm.locationFilterFill("");
		this.filterForm.typeFilterFill(TestConstants.TYPE_FILTER_MORE);
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertTrue(this.culturalList.offersCount() > 1);
		assertTrue(this.culturalList.offerTypesContainParam(TestConstants.TYPE_FILTER_MORE));
	}
	
	@Test
	public void testTypeFilterNone() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill("");
		this.filterForm.locationFilterFill("");
		this.filterForm.typeFilterFill(TestConstants.FILTER_NONE);
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(0, this.culturalList.offersCount());
	}
	
	@Test
	public void testFilterAll() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill("");
		this.filterForm.locationFilterFill("");
		this.filterForm.typeFilterFill("");
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		this.culturalList.ensureNextButtonDisplayed();
		assertEquals(TestConstants.LARGE_PAGE_SIZE, this.culturalList.offersCount());
	}
	
	@Test
	public void testFilterNone() {
		this.culturalList.ensureToggleFilterDisplayed();
		this.culturalList.toggleFilterClick();
		this.filterForm.ensureFormDisplayed();
		this.filterForm.nameFilterFill(TestConstants.FILTER_NONE);
		this.filterForm.locationFilterFill(TestConstants.FILTER_NONE);
		this.filterForm.typeFilterFill(TestConstants.FILTER_NONE);
		this.culturalList.ensureTitleDisplayed();
		this.culturalList.titleClick();
		this.filterForm.filterButtonClick();
		this.filterForm.ensureFormDisplayed();
		assertEquals(0, this.culturalList.offersCount());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}
	
}
