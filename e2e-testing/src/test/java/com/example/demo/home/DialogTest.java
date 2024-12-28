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
import com.example.demo.culturals.CulturalDialog;

public class DialogTest {
	
	private WebDriver browser;
	
	private HomePage homePage;
	private CulturalDetails culturalDetails;
	private CulturalDialog culturalDialog;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
	  	System.setProperty("webdriver.chrome.driver", TestConstants.CHROME_DRIVER_PATH);
		this.browser = new ChromeDriver(Utilities.SSLIgnore());
		this.browser.manage().window().maximize();
		this.homePage = PageFactory.initElements(this.browser, HomePage.class);
		this.culturalDetails = PageFactory.initElements(this.browser, CulturalDetails.class);
		this.culturalDialog = PageFactory.initElements(this.browser, CulturalDialog.class);
		this.browser.navigate().to(TestConstants.HOME_PATH);
		this.homePage.ensureMapDisplayed();
		this.homePage.toggleButtonClick();
		this.culturalDetails.ensureDetailsDisplayed();
		this.culturalDetails.moreButtonClick();
		this.culturalDialog.ensureDetailsDisplayed();
	}
	
	@Test
	public void testDialog() {
		assertEquals(this.culturalDetails.nameText(), this.culturalDialog.nameText());
		assertEquals(this.culturalDetails.typeText(), this.culturalDialog.typeText());
		assertEquals(this.culturalDetails.locationText(), this.culturalDialog.locationText());
		this.culturalDialog.ensureCloseButtonDisplayed();
		this.culturalDialog.closeButtonClick();
		this.culturalDialog.ensureDialogClosed();
		assertEquals(TestConstants.HOME_PATH, this.browser.getCurrentUrl());
	}
	
	@After
	public void cleanUp() {
		this.browser.quit();
	}

}
