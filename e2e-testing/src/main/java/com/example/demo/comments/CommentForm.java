package com.example.demo.comments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CommentForm {

	private WebDriver browser;

	@FindBy(xpath = "//*/app-comment-form/form/div/app-star-rating/div/div/button[1]")
	private WebElement firstStar;
	
	@FindBy(xpath = "//*/app-comment-form/form/div/app-star-rating/div/div/button[2]")
	private WebElement secondStar;
	
	@FindBy(xpath = "//*/app-comment-form/form/div/app-star-rating/div/div/button[3]")
	private WebElement thirdStar;

	@FindBy(xpath = "//*/app-comment-form/form/div/app-star-rating/div/div/button[4]")
	private WebElement fourthStar;
	
	@FindBy(xpath = "//*/app-comment-form/form/div/app-star-rating/div/div/button[5]")
	private WebElement fifthStar;

	@FindBy(xpath = "//*/app-comment-form/form/div/mat-form-field/div/div[1]/div[3]/textarea")
	private WebElement textInput;

	@FindBy(xpath = "//*/app-comment-form/form/div/mat-form-field/div/div[2]/div/mat-error")
	private WebElement textError;

	@FindBy(xpath = "//*/app-comment-form/form/app-save-cancel/app-spacer-container/div/span[1]/button")
	private WebElement cancelButton;
	
	@FindBy(xpath = "//*/app-comment-form/form/app-save-cancel/app-spacer-container/div/span[2]/button")
	private WebElement saveButton;
			
	public CommentForm(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void firstStarClick() {
		this.firstStar.click();
	}
	
	public void secondStarClick() {
		this.secondStar.click();
	}
	
	public void thirdStarClick() {
		this.thirdStar.click();
	}
	
	public void fourthStarClick() {
		this.fourthStar.click();
	}
	
	public void fifthStarClick() {
		this.fifthStar.click();
	}
		
	public void cancelButtonClick() {
		this.cancelButton.click();
	}
	
	public void saveButtonClick() {
		this.saveButton.click();
	}
	
	public void textInputFill(String value) {
		this.textInput.clear();
		this.textInput.sendKeys(value);
		this.textInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}

	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstStar));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.secondStar));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.thirdStar));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.fourthStar));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstStar));
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
	
	public boolean emptyTextError() {
		return this.textError.isDisplayed() && this.textError.getText().equals("Some text is required!");
	}
	
}
