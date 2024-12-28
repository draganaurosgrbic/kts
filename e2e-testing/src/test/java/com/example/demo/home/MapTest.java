package com.example.demo.home;

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
import com.example.demo.culturals.CulturalDetails;

public class MapTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private CulturalDetails culturalDetails;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.culturalDetails = PageFactory.initElements(this.browser, CulturalDetails.class);
		this.browser.navigate().to(TestConstants.HOME_PATH);
		this.homePage.ensureMapDisplayed();
	}

	@Test
	public void testMap()  {
		this.homePage.toggleButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		this.culturalDetails.click();
		this.homePage.ensureBalloonDisplayed();
		this.homePage.toggleButtonClick();
		this.homePage.ensureBalloonCloseDisplayed();
		this.homePage.closeBalloon();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}
	
}
