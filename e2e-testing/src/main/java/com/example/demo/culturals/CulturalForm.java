package com.example.demo.culturals;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CulturalForm {

	private WebDriver browser;

	@FindBy(xpath = "//*/app-cultural-form/form/div[1]/div/mat-form-field[1]/div/div[1]/div/input")
	private WebElement typeInput;
	
	@FindBy(xpath = "//*/app-cultural-form/form/div[1]/div/mat-form-field[2]/div/div[1]/div/input")
	private WebElement nameInput;
	
	@FindBy(xpath = "//*/app-cultural-form/form/div[1]/div/mat-form-field[3]/div/div[1]/div/span[1]/input")
	private WebElement locationInput;
	
	@FindBy(xpath = "//*/app-cultural-form/form/div[1]/div/mat-form-field[1]/div/div[3]/div/mat-error")
	private WebElement typeError;
	
	@FindBy(xpath = "//*/app-cultural-form/form/div[1]/div/mat-form-field[2]/div/div[3]/div/mat-error")
	private WebElement nameError;
	
	@FindBy(xpath = "//*/app-cultural-form/form/div[1]/div/mat-form-field[3]/div/div[3]/div/mat-error")
	private WebElement locationError;
		
	@FindBy(xpath = "//*/app-cultural-form/form/div[2]/app-save-cancel/app-spacer-container/div/span[1]/button")
	private WebElement cancelButton;
	
	@FindBy(xpath = "//*/app-cultural-form/form/div[2]/app-save-cancel/app-spacer-container/div/span[2]/button")
	private WebElement saveButton;
	
	@FindBy(xpath = "//*/app-cultural-form/form/div[1]/div/mat-form-field[3]/div/div[1]/div/span[1]/span/div/span/div[1]")
	private WebElement autocompleteSuggestion;
			
	public CulturalForm(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.typeInput));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameInput));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.locationInput));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.cancelButton));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.saveButton));		
	}
		
	public void typeInputFill(String value) {
		this.typeInput.clear();
		this.typeInput.sendKeys(value);
		this.typeInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void nameInputFill(String value) {
		this.nameInput.clear();
		this.nameInput.sendKeys(value);
		this.nameInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void locationInputFill(String value) {
		int length = this.locationInput.getAttribute("value").length();
		this.locationInput.clear();
		for (int i = 0; i < length; ++i) {
			this.locationInput.sendKeys(Keys.BACK_SPACE);
		}
		this.locationInput.sendKeys(value);
		this.locationInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void cancelButtonClick() {
		this.cancelButton.click();
	}
	
	public void saveButtonClick() {
		this.saveButton.click();
	}

	public void autocompleteSuggestionClick() {
		this.autocompleteSuggestion.click();
	}
		
	public void ensureTypeErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.typeError));
	}
	
	public void ensureNameErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameError));
	}

	public void ensureLocationErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.locationError));
	}
	
	public void ensureAutocompleteSuggestionDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.autocompleteSuggestion));
	}

	public void ensureDialogClosed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("app-cultural-form")));
	}

	public boolean emptyTypeError() {
		return this.typeError.isDisplayed() && this.typeError.getText().equals("Type is required!");
	}
	
	public boolean nonExistingTypeError() {
		return this.typeError.isDisplayed() && this.typeError.getText().equals("Type not found!");
	}
	
	public boolean emptyNameError() {
		return this.nameError.isDisplayed() && this.nameError.getText().equals("Name is required!");
	}
	
	public boolean takenNameError() {
		return this.nameError.isDisplayed() && this.nameError.getText().equals("Name already exists!");
	}
	
	public boolean invalidLocationError() {
		return this.locationError.isDisplayed() && this.locationError.getText().equals("Valid location is required!");
	}
	
}
