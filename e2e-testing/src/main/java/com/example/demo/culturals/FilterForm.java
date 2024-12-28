package com.example.demo.culturals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class FilterForm {
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-cultural-list/div[1]/mat-expansion-panel/div/div/app-filter-form/form/mat-form-field[1]/div/div[1]/div[3]/input")
	private WebElement nameFilter;
	
	@FindBy(xpath = "//*/app-cultural-list/div[1]/mat-expansion-panel/div/div/app-filter-form/form/mat-form-field[2]/div/div[1]/div[3]/input")
	private WebElement locationFilter;
	
	@FindBy(xpath = "//*/app-cultural-list/div[1]/mat-expansion-panel/div/div/app-filter-form/form/mat-form-field[3]/div/div[1]/div[3]/input")
	private WebElement typeFilter;
	
	@FindBy(xpath = "//*/app-cultural-list/div[1]/mat-expansion-panel/div/div/app-filter-form/form/app-spacer-container/div/span[2]/button")
	private WebElement filterButton;
	
	public FilterForm(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.nameFilter));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.locationFilter));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.typeFilter));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.filterButton));
		this.browser.manage().timeouts().implicitlyWait(Constants.TIMEOUT_WAIT, TimeUnit.MILLISECONDS);
	}
	
	public void nameFilterFill(String value) {
		this.nameFilter.clear();
		this.nameFilter.sendKeys(value);
	}
	
	public void locationFilterFill(String value) {
		this.locationFilter.clear();
		this.locationFilter.sendKeys(value);
	}
	
	public void typeFilterFill(String value) {
		this.typeFilter.clear();
		this.typeFilter.sendKeys(value);
	}
	
	public void filterButtonClick() {
		this.filterButton.click();
	}
	
}
