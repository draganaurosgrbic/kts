package com.example.demo.news;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class FilterForm {

	private WebDriver browser;

	@FindBy(xpath = "//*/app-news-list/div/mat-expansion-panel/div/div/form/mat-form-field[1]/div/div[1]/div[3]/input")
	private WebElement startFilter;
	
	@FindBy(xpath = "//*/app-news-list/div/mat-expansion-panel/div/div/form/mat-form-field[2]/div/div[1]/div[3]/input")
	private WebElement endFilter;
	
	@FindBy(xpath = "//*/app-news-list/div/mat-expansion-panel/div/div/app-spacer-container/div/span[2]/button")
	private WebElement filterButton;

	public FilterForm(WebDriver browser) {
		super();
		this.browser = browser;
	}

	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT))
				.until(ExpectedConditions.elementToBeClickable(this.startFilter));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT))
				.until(ExpectedConditions.elementToBeClickable(this.endFilter));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT))
				.until(ExpectedConditions.elementToBeClickable(this.filterButton));
	}

	public void startFilterFill(String value) {
		this.startFilter.clear();
		this.startFilter.sendKeys(value);
	}

	public void endFilterFill(String value) {
		this.endFilter.clear();
		this.endFilter.sendKeys(value);
	}

	public void filterButtonClick() {
		this.filterButton.click();
	}

}
