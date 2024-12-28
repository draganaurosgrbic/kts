package com.example.demo.news;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class NewsDetails {

	private static final String CAROUSEL_XPATH = "//*/app-news-list/app-news-details[1]/div/mat-card/mat-card-content/app-carousel";	

	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-news-list/app-news-details[1]/div/mat-card/mat-card-content/app-spacer-container/div/button[1]")
	private WebElement editButton;
	
	@FindBy(xpath = "//*/app-news-list/app-news-details[1]/div/mat-card/mat-card-content/app-spacer-container/div/button[2]")
	private WebElement deleteButton;

	@FindBy(xpath = "//*/app-news-list/app-news-details[1]/div/mat-card/mat-card-content/div[2]")
	private WebElement text;

	@FindBy(xpath = "//*/app-news-list/app-news-details[1]/div/mat-card/mat-card-content/app-carousel/mdb-carousel/div/div/mdb-carousel-item/img")
	private WebElement image;

	public NewsDetails(WebDriver browser) {
		super();
		this.browser = browser;
	}

	public void ensureTextDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT))
				.until(ExpectedConditions.elementToBeClickable(this.text));
	}

	public void ensureButtonsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT))
				.until(ExpectedConditions.elementToBeClickable(this.editButton));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT))
				.until(ExpectedConditions.elementToBeClickable(this.deleteButton));
	}
	
	public void ensureHasImages() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CAROUSEL_XPATH)));
	}
	
	public void ensureHasNoImages() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CAROUSEL_XPATH)));
	}
	
	public void ensureImageDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.image));
	}
	
	public void editButtonClick() {
		this.editButton.click();
	}

	public void deleteButtonClick() {
		this.deleteButton.click();
	}
	
	public String getText() {
		return this.text.getText();
	}

}
