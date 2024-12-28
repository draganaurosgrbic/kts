package com.example.demo.culturals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CulturalDialog {

	private WebDriver browser;
		
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/app-spacer-container/div/span[1]/button[1]")
	private WebElement editButton;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/app-spacer-container/div/span[1]/button[2]")
	private WebElement deleteButton;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer/div/button")
	private WebElement addCommentButton;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer/div/button")
	private WebElement addNewsButton;

	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/app-spacer-container/div/span[1]/button")
	private WebElement subUnsubButton;

	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/app-close-button/app-spacer-container/div/button")
	private WebElement closeButton;

	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/app-spacer-container/div/span[2]/button")
	private WebElement toggleDrawer;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer/div/mat-tab-group/mat-tab-header/div[2]/div/div/div[1]")
	private WebElement commentsTab;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer/div/mat-tab-group/mat-tab-header/div[2]/div/div/div[2]")
	private WebElement newsTab;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/div[2]/div[1]/app-bold-text/span")
	private WebElement nameField;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/div[2]/div[2]/span")
	private WebElement typeField;
	
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/div[2]/div[3]/span")
	private WebElement locationField;
				
	@FindBy(xpath = "//*/app-cultural-dialog/mat-drawer-container/mat-drawer-content/div/app-spacer-container/div/span[1]/button/span[1]")
	private WebElement subUnsubButtonText;

	public CulturalDialog(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void editButtonClick() {
		this.editButton.click();
	}
	
	public void deleteButtonClick() {
		this.deleteButton.click();
	}
		
	public void addCommentButtonClick() {
		this.addCommentButton.click();
	}
	
	public void addNewsButtonClick() {
		this.addNewsButton.click();
	}
	
	public void subUnsubButtonClick() {
		this.subUnsubButton.click();
	}
	
	public void closeButtonClick() {
		this.closeButton.click();
	}

	public void toggleDrawerClick() {
		this.toggleDrawer.click();
	}
	
	public void commentsTabClick() {
		this.commentsTab.click();
	}

	public void newsTabClick() {
		this.newsTab.click();
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
		
	public String getSubUnsubButtonText() {
		return this.subUnsubButtonText.getText();	
	}
	
	public void ensureEditButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.editButton));		
	}
	
	public void ensureDeleteButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.deleteButton));		
	}
		
	public void ensureAddCommentButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.addCommentButton));		
	}
	
	public void ensureAddNewsButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.addNewsButton));		
	}

	public void ensureSubUnsubButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.subUnsubButton));		
	}
	
	public void ensureCloseButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.closeButton));		
	}

	public void ensureToggleDrawerDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.toggleDrawer));		
	}
	
	public void ensureCommentsTabDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.commentsTab));		
	}
	
	public void ensureNewsTabDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.newsTab));		
	}
	
	public void ensureDialogClosed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("app-cultural-dialog")));
	}
	
	public void ensureDetailsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameField));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.typeField));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.locationField));		
	}

}
