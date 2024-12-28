package com.example.demo.comments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class CommentDetails {
	
	private static final String STAR_XPATH = "//*/app-comment-list/app-comment-details[1]/div/mat-card/mat-card-content/div[1]/span[1]/app-star-rating/div/div/button";
	private static final String CAROUSEL_XPATH = "//*/app-comment-list/app-comment-details[1]/div/mat-card/mat-card-content/mat-accordion/mat-expansion-panel/mat-expansion-panel-header";	

	private WebDriver browser;
		
	@FindBy(xpath = "//*/app-comment-list/app-comment-details[1]/div/mat-card/mat-card-content/div[1]/span[2]/button[1]")
	private WebElement editButton;
	
	@FindBy(xpath = "//*/app-comment-list/app-comment-details[1]/div/mat-card/mat-card-content/div[1]/span[2]/button[2]")
	private WebElement deleteButton;
	
	@FindBy(xpath = "//*/app-comment-list/app-comment-details[1]/div/mat-card/mat-card-content/div[2]")
	private WebElement text;
	
	@FindBy(xpath = CAROUSEL_XPATH)
	private WebElement carouselToggle;
	
	@FindBy(xpath = "//*/app-comment-list/app-comment-details[1]/div/mat-card/mat-card-content/mat-accordion/mat-expansion-panel/div/div/app-carousel/mdb-carousel/div/div/mdb-carousel-item/img")
	private WebElement image;
	
	@FindBy(xpath = STAR_XPATH + "[1]")
	private WebElement firstStar;
	
	@FindBy(xpath = STAR_XPATH + "[2]")
	private WebElement secondStar;

	@FindBy(xpath = STAR_XPATH + "[3]")
	private WebElement thirdStar;

	@FindBy(xpath = STAR_XPATH + "[4]")
	private WebElement fourthStar;

	@FindBy(xpath = STAR_XPATH + "[5]")
	private WebElement fifthStar;
		
	public CommentDetails(WebDriver browser) {
		super();
		this.browser = browser;
	}
		
	public void ensureTextDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.text));		
	}

	public void ensureButtonsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.editButton));		
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.deleteButton));		
	}
		
	public void ensureNoStarsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[1]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[2]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[3]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[4]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[5]")));
	}
	
	public void ensureOneStarDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.firstStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[2]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[3]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[4]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[5]")));
	}
	
	public void ensureTwoStarsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.firstStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.secondStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[3]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[4]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[5]")));
	}
	
	public void ensureThreeStarsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.firstStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.secondStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.thirdStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[4]")));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[5]")));
	}
	
	public void ensureFourStarsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.firstStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.secondStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.thirdStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.fourthStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(STAR_XPATH + "[5]")));
	}
	
	public void ensureFiveStarsDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.firstStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.secondStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.thirdStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.fourthStar));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.fifthStar));
	}
	
	public void ensureHasImages() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.visibilityOf(this.carouselToggle));
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
	
	public void carouselToggleClick() {
		this.carouselToggle.click();
	}
	
	public String getText() {
		return this.text.getText();
	}
	
}
