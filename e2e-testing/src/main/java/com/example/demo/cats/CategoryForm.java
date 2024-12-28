package com.example.demo.cats;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CategoryForm {
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-cat-form/div/form/div/mat-form-field/div/div[1]/div/input")
	private WebElement nameInput;
	
	@FindBy(xpath = "//*/app-cat-form/div/form/div/mat-form-field/div/div[3]/div/mat-error")
	private WebElement nameError;
		
	@FindBy(xpath = "//*/app-cat-type-dialog/mat-tab-group/div/mat-tab-body[2]/div/app-cat-form/div/form/app-center-container/div/button")
	private WebElement saveButton;
	
	public CategoryForm(WebDriver browser) {
		super();
		this.browser = browser;
	}
			
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameInput));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.saveButton));		
	}
	
	public void nameInputFill(String value) {
		this.nameInput.clear();
		this.nameInput.sendKeys(value);
		this.nameInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
		
	public void saveButtonClick() {
		this.saveButton.click();
	}
		
	public void ensureNameErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameError));
	}
	
	public boolean emptyNameError() {
		return this.nameError.isDisplayed() && this.nameError.getText().equals("Name is required!");
	}
	
	public boolean takenNameError() {
		return this.nameError.isDisplayed() && this.nameError.getText().equals("Name already exists!");
	}

}
