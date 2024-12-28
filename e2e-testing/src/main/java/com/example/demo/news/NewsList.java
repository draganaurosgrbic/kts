package com.example.demo.news;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class NewsList {
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-news-list/app-paginator/div/app-spacer-container/div/span[3]/button")
	private WebElement nextButton;
	
	@FindBy(xpath = "//*/app-news-list/app-paginator/div/app-spacer-container/div/span[1]/button")
	private WebElement previousButton;
	
	@FindBy(xpath = "//*/app-news-list/div[1]/mat-expansion-panel/mat-expansion-panel-header")
	private WebElement toggleFilter;
	
	public NewsList(WebDriver browser) {
		super();
		this.browser = browser;
	}

	public void nextButtonClick() {
		this.nextButton.click();
	}

	public void previousButtonClick() {
		this.previousButton.click();
	}
	
	public void toggleFilterClick() {
		this.toggleFilter.click();
	}
	
	public int newsCount() {
		return this.browser.findElements(By.tagName("app-news-details")).size();
	}
		
	public void ensurePreviousButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.previousButton));
	}
	
	public void ensureNextButtonDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nextButton));
	}
		
	public void ensureFirstPage() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*/app-news-list/app-paginator/div/app-spacer-container/div/span[1]/button")));
	}
	
	public void ensureLastPage() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*/app-news-list/app-paginator/div/app-spacer-container/div/span[3]/button")));
	}
	
	public void ensureToggleFilterDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.toggleFilter));
	}

}
