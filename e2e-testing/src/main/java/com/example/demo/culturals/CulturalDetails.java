package com.example.demo.culturals;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CulturalDetails {

	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-cultural-list/app-cultural-details[1]/div/mat-card/mat-card-content/div/div[2]/div[1]/app-bold-text/span")
	private WebElement nameField;
	
	@FindBy(xpath = "//*/app-cultural-list/app-cultural-details[1]/div/mat-card/mat-card-content/div/div[2]/div[2]/span")
	private WebElement typeField;
	
	@FindBy(xpath = "//*/app-cultural-list/app-cultural-details[1]/div/mat-card/mat-card-content/div/div[2]/div[3]/span")
	private WebElement locationField;
		
	@FindBy(xpath = "//*/app-cultural-list/app-cultural-details[1]/div/mat-card/mat-card-content/div/div[3]/button")
	private WebElement moreButton;
		
	public CulturalDetails(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void ensureDetailsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameField));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.typeField));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.locationField));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.moreButton));		
	}

	public void click() {
		this.nameField.click();
	}
	
	public void moreButtonClick() {
		this.moreButton.click();
	}
	
	public String nameText() {
		return this.nameField.getText();
	}
	
	public String typeText() {
		return this.typeField.getText().split(",")[0];
	}
	
	public String locationText() {
		return this.locationField.getText();
	}
	
}
