package com.example.demo.cats;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class TypeForm {
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-type-form/div/form/div/mat-form-field[1]/div/div[1]/div/input")
	private WebElement nameInput;
		
	@FindBy(xpath = "//*/app-type-form/div/form/div/mat-form-field[2]/div/div[1]/div/input")
	private WebElement categoryInput;
	
	@FindBy(xpath = "//*/app-type-form/div/form/div/mat-form-field[1]/div/div[3]/div/mat-error")
	private WebElement nameError;

	@FindBy(xpath = "//*/app-type-form/div/form/div/mat-form-field[2]/div/div[3]/div/mat-error")
	private WebElement categoryError;
		
	@FindBy(xpath = "//*/app-type-form/div/form/app-center-container/div/button")
	private WebElement saveButton;
		
	public TypeForm(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameInput));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.categoryInput));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.saveButton));		
	}
			
	public void nameInputFill(String value) {
		this.nameInput.clear();
		this.nameInput.sendKeys(value);
		this.nameInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void categoryInputFill(String value) {
		this.categoryInput.clear();
		this.categoryInput.sendKeys(value);
		this.categoryInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void saveButtonClick() {
		this.saveButton.click();
	}
		
	public void ensureCategoryErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.categoryError));
	}
	
	public void ensureNameErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameError));
	}
	
	public boolean emptyCategoryError() {
		return this.categoryError.isDisplayed() && this.categoryError.getText().equals("Category is required!");
	}
	
	public boolean nonExistingCategoryError() {
		return this.categoryError.isDisplayed() && this.categoryError.getText().equals("Category not found!");
	}
	
	public boolean emptyNameError() {
		return this.nameError.isDisplayed() && this.nameError.getText().equals("Name is required!");
	}
	
	public boolean takenNameError() {
		return this.nameError.isDisplayed() && this.nameError.getText().equals("Name already exists!");
	}
		
}
