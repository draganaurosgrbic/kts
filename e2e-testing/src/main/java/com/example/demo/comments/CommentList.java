package com.example.demo.comments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CommentList {
	
	private static final String NEXT_BUTTON_XPATH = "//*/app-comment-list/app-paginator/div/app-spacer-container/div/span[3]/button";
	private static final String PREVIOUS_BUTTON_XPATH = "//*/app-comment-list/app-paginator/div/app-spacer-container/div/span[1]/button";
	
	private WebDriver browser;
	
	@FindBy(xpath = NEXT_BUTTON_XPATH)
	private WebElement nextButton;
	
	@FindBy(xpath = PREVIOUS_BUTTON_XPATH)
	private WebElement previousButton;

	public CommentList(WebDriver browser) {
		super();
		this.browser = browser;
	}

	public void ensurePreviousButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.previousButton));
	}
	
	public void ensureNextButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nextButton));
	}
	
	public void ensureFirstPage() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(PREVIOUS_BUTTON_XPATH)));
	}
	
	public void ensureLastPage() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(NEXT_BUTTON_XPATH)));
	}
	
	public void nextButtonClick() {
		this.nextButton.click();
	}

	public void previousButtonClick() {
		this.previousButton.click();
	}
	
	public int commentsCount() {
		return this.browser.findElements(By.tagName("app-comment-details")).size();
	}

}
