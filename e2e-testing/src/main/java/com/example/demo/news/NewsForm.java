package com.example.demo.news;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class NewsForm {

	private WebDriver browser;

	@FindBy(xpath = "//*/app-news-form/form/div/mat-form-field/div/div[1]/div[3]/textarea")
	private WebElement textInput;

	@FindBy(xpath = "//*/app-news-form/form/div/mat-form-field/div/div[2]/div/mat-error")
	private WebElement textError;

	@FindBy(xpath = "//*/app-news-form/form/app-save-cancel/app-spacer-container/div/span[1]/button")
	private WebElement cancelButton;
	
	@FindBy(xpath = "//*/app-news-form/form/app-save-cancel/app-spacer-container/div/span[2]/button")
	private WebElement saveButton;
			
	public NewsForm(WebDriver browser) {
		super();
		this.browser = browser;
	}
			
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.textInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.cancelButton));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.saveButton));
	}

	public void ensureTextErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.textError));		
	}
	
	public void ensureDialogClosed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("app-comment-form")));
	}
	
	public void textInputFill(String value) {
		this.textInput.clear();
		this.textInput.sendKeys(value);
		this.textInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void cancelButtonClick() {
		this.cancelButton.click();
	}
	
	public void saveButtonClick() {
		this.saveButton.click();
	}

	public boolean emptyTextError() {
		return this.textError.isDisplayed() && this.textError.getText().equals("Some text is required!");
	}
	
}
