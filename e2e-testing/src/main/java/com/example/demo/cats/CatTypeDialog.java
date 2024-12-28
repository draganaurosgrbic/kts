package com.example.demo.cats;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CatTypeDialog {
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-cat-type-dialog/mat-tab-group/mat-tab-header/div[2]/div/div/div[1]")
	private WebElement listTab;

	@FindBy(xpath = "//*/app-cat-type-dialog/mat-tab-group/mat-tab-header/div[2]/div/div/div[2]")
	private WebElement createTab;
		
	@FindBy(xpath = "//*/app-cat-type-dialog/app-close-button/app-spacer-container/div/button")
	private WebElement cancelButton;
	
	public CatTypeDialog(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void ensureListTabDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.listTab));
	}

	public void ensureCreateTabDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.createTab));
	}
	
	public void ensureCancelButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.cancelButton));
	}
		
	public void ensureDialogClosed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("app-cat-type-dialog")));
	}
	
	public void listTabClick() {
		this.listTab.click();
	}
			
	public void createTabClick() {
		this.createTab.click();
	}
	
	public void cancelButtonClick() {
		this.cancelButton.click();
	}
	
}
