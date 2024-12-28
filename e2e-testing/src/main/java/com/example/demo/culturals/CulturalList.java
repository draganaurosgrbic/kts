package com.example.demo.culturals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CulturalList {
	
	private static final String NEXT_BUTTON_XPATH = "//*/app-cultural-list/app-paginator/div/app-spacer-container/div/span[3]/button";
	private static final String PREVIOUS_BUTTON_XPATH = "//*/app-cultural-list/app-paginator/div/app-spacer-container/div/span[1]/button";
	
	private WebDriver browser;
	
	@FindBy(xpath = NEXT_BUTTON_XPATH)
	private WebElement nextButton;
	
	@FindBy(xpath = PREVIOUS_BUTTON_XPATH)
	private WebElement previousButton;
	
	@FindBy(xpath = "//*/app-cultural-list/app-paginator/div/app-spacer-container/div/span[2]")
	private WebElement title;

	@FindBy(xpath = "//*/app-cultural-list/div[1]/mat-expansion-panel/mat-expansion-panel-header")
	private WebElement toggleFilter;
		
	@FindBy(xpath = "//*/app-home/mat-drawer-container/mat-drawer/div/div/mat-tab-group/mat-tab-header/div[2]/div/div/div[1]")
	private WebElement culturalsTab;
	
	@FindBy(xpath = "//*/app-home/mat-drawer-container/mat-drawer/div/div/mat-tab-group/mat-tab-header/div[2]/div/div/div[2]")
	private WebElement followingsTab;
	
	public CulturalList(WebDriver browser) {
		super();
		this.browser = browser;
	}

	public void nextButtonClick() {
		this.nextButton.click();
	}

	public void previousButtonClick() {
		this.previousButton.click();
	}
		
	public void titleClick() {
		this.title.click();
	}
	
	public void toggleFilterClick() {
		this.toggleFilter.click();
	}

	public void culturalsTabClick() {
		this.culturalsTab.click();
	}

	public void followingsTabClick() {
		this.followingsTab.click();
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
	
	public void ensureTitleDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.title));
	}
	
	public void ensureToggleFilterDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.toggleFilter));
	}
	
	public void ensureCulturalsTabDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.culturalsTab));
	}

	public void ensureFollowingsTabDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.followingsTab));
	}
	
	public int offersCount() {
		return this.browser.findElements(By.tagName("app-cultural-details")).size();
	}
	
	public boolean offerNamesContainParam(String param) {
		return this.browser.findElements(By.xpath("//*/app-cultural-list/app-cultural-details/div/mat-card/mat-card-content/div/div[2]/div[1]/app-bold-text/span"))
				.stream().allMatch(offer -> offer.getText().toLowerCase().contains(param.toLowerCase()));
	}
	
	public boolean offerLocationsContainParam(String param) {
		return this.browser.findElements(By.xpath("//*/app-cultural-list/app-cultural-details[1]/div/mat-card/mat-card-content/div/div[2]/div[3]/span"))
				.stream().allMatch(offer -> offer.getText().toLowerCase().contains(param.toLowerCase()));
	}

	public boolean offerTypesContainParam(String param) {
		return this.browser.findElements(By.xpath("//*/app-cultural-list/app-cultural-details[1]/div/mat-card/mat-card-content/div/div[2]/div[2]/span"))
				.stream().allMatch(offer -> offer.getText().toLowerCase().contains(param.toLowerCase()));
	}
	
}
