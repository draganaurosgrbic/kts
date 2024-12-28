package com.example.demo.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class DeleteConfirmation {

	private WebDriver browser;

	@FindBy(xpath = "//*/app-delete-confirmation/div/div[2]/app-spacer-container/div/span[1]/button")
	private WebElement cancelButton;
	
	@FindBy(xpath = "//*/app-delete-confirmation/div/div[2]/app-spacer-container/div/span[2]/button")
	private WebElement confirmButton;

	public DeleteConfirmation(WebDriver browser) {
		super();
		this.browser = browser;
	}
		
	public void ensureDialogDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.cancelButton));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.confirmButton));		
	}
	
	public void ensureDialogClosed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("app-delete-confirmation")));		
	}
	
	public void cancelButtonClick() {
		this.cancelButton.click();
	}
	
	public void confirmButtonClick() {
		this.confirmButton.click();
	}
	
}
