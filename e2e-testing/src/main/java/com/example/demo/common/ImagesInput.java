package com.example.demo.common;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class ImagesInput {
	
	private static final String FIRST_IMAGE_XPATH = "//*/app-images-input/div/app-image[1]/div/app-spacer-container/div/button";
	private static final String SECOND_IMAGE_XPATH = "//*/app-images-input/div/app-image[2]/div/app-spacer-container/div/button";
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-images-input/input")
	private WebElement fileInput;
	
	@FindBy(xpath = "//*/app-images-input/div/app-image[1]")
	private WebElement firstImage;

	@FindBy(xpath = "//*/app-images-input/div/app-image[2]")
	private WebElement secondImage;
	
	@FindBy(xpath = FIRST_IMAGE_XPATH)
	private WebElement firstImageDelete;
	
	@FindBy(xpath = SECOND_IMAGE_XPATH)
	private WebElement secondImageDelete;

	public ImagesInput(WebDriver browser) {
		super();
		this.browser = browser;
	}
	
	public void ensureNoImagesDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(FIRST_IMAGE_XPATH)));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(SECOND_IMAGE_XPATH)));
	}
	
	public void ensureOneImageDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstImage));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(SECOND_IMAGE_XPATH)));
	}
	
	public void ensureTwoImagesDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstImage));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.secondImage));
	}
	
	public void uploadFile(String filePath) {
		this.fileInput.sendKeys(new File(filePath).getAbsolutePath());
	}
	
	public void deleteFirstImage() {
		this.firstImageDelete.click();
	}
	
	public void deleteSecondImage() {
		this.secondImageDelete.click();
	}

	public int imagesCount() {
		return this.browser.findElements(By.tagName("app-image")).size();
	}
	
}
