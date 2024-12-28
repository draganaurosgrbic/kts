package com.example.demo.user;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class LoginPage {
	
	private WebDriver browser;
		
	@FindBy(xpath = "//*/app-login-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[1]/div/div[1]/div/input")
	private WebElement emailInput;
		
	@FindBy(xpath = "//*/app-login-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[2]/div/div[1]/div/input")
	private WebElement passwordInput;
	
	@FindBy(xpath = "//*/app-login-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[1]/div/div[3]/div/mat-error")
	private WebElement emailError;

	@FindBy(xpath = "//*/app-login-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[2]/div/div[3]/div/mat-error")
	private WebElement passwordError;
	
	@FindBy(xpath = "//*/app-login-form/app-form-container/div/mat-card/mat-card-content/form/app-center-container/div/button")
	private WebElement loginButton;
	
	@FindBy(tagName = Constants.SNACKBAR)
	private WebElement snackBar;
	
	public LoginPage(WebDriver browser) {
		super();
		this.browser = browser;
	}
		
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.emailInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.passwordInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.loginButton));
	}
		
	public void ensureEmailErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.emailError));
	}
	
	public void ensurePasswordErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.passwordError));
	}
	
	public void ensureSnackBarDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.snackBar));
	}

	public void emailInputFill(String value) {
		this.emailInput.clear();
		this.emailInput.sendKeys(value);			
		this.emailInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void passwordInputFill(String value) {
		this.passwordInput.clear();
		this.passwordInput.sendKeys(value);			
		this.passwordInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void loginButtonClick() {
		this.loginButton.click();
	}
		
	public boolean emptyEmailError() {
		return this.emailError.isDisplayed() && this.emailError.getText().equals("Email is required!");
	}
	
	public boolean emptyPasswordError() {
		return this.passwordError.isDisplayed() && this.passwordError.getText().equals("Password is required!");
	}
	
	public String snackBarText() {
		return this.snackBar.findElement(By.tagName("span")).getText();
	}
	
	public void closeSnackBar() {
		this.snackBar.findElement(By.tagName("button")).click();
	}

}
