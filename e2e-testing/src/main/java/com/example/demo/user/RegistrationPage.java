package com.example.demo.user;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class RegistrationPage {
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[1]/div/div[1]/div/input")
	private WebElement firstNameInput;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[2]/div/div[1]/div/input")
	private WebElement lastNameInput;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[3]/div/div[1]/div/input")
	private WebElement emailInput;
		
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[4]/div/div[1]/div/input")
	private WebElement passwordInput;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[5]/div/div[1]/div/input")
	private WebElement passwordConfirmationInput;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[1]/div/div[3]/div/mat-error")
	private WebElement firstNameError;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[2]/div/div[3]/div/mat-error")
	private WebElement lastNameError;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[3]/div/div[3]/div/mat-error")
	private WebElement emailError;

	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[4]/div/div[3]/div/mat-error")
	private WebElement passwordError;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/div/mat-form-field[5]/div/div[3]/div/mat-error")
	private WebElement passwordConfirmationError;
	
	@FindBy(xpath = "//*/app-register-form/app-form-container/div/mat-card/mat-card-content/form/app-center-container/div/button")
	private WebElement registerButton;

	@FindBy(tagName = Constants.SNACKBAR)
	private WebElement snackBar;

	public RegistrationPage(WebDriver browser) {
		super();
		this.browser = browser;
	}
				
	public void firstNameInputFill(String value) {
		this.firstNameInput.clear();
		this.firstNameInput.sendKeys(value);
		this.firstNameInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void lastNameInputFill(String value) {
		this.lastNameInput.clear();
		this.lastNameInput.sendKeys(value);
		this.lastNameInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
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
	
	public void passwordConfirmationInputFill(String value) {
		this.passwordConfirmationInput.clear();
		this.passwordConfirmationInput.sendKeys(value);
		this.passwordConfirmationInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void registerButtonClick() {
		this.registerButton.click();
	}
	
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstNameInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.lastNameInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.emailInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.passwordInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.passwordConfirmationInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.registerButton));
	}
			
	public void ensureFirstNameErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstNameError));
	}
	
	public void ensureLastNameErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.lastNameError));
	}
	
	public void ensureEmailErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.emailError));
	}
	
	public void ensurePasswordErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.passwordError));
	}
	
	public void ensurePasswordConfirmationErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.passwordConfirmationError));
	}
	
	public void ensureSnackBarDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.snackBar));
	}
	
	public boolean invalidEmailError() {
		return this.emailError.isDisplayed() && this.emailError.getText().equals("Valid email is required!");
	}
	
	public boolean emailTakenError() {
		return this.emailError.isDisplayed() && this.emailError.getText().equals("Email already exists!");
	}
	
	public boolean emptyFirstNameError() {
		return this.firstNameError.isDisplayed() && this.firstNameError.getText().equals("First name is required!");
	}
	
	public boolean emptyLastNameError() {
		return this.lastNameError.isDisplayed() && this.lastNameError.getText().equals("Last name is required!");
	}
	
	public boolean emptyPasswordError() {
		return this.passwordError.isDisplayed() && this.passwordError.getText().equals("Password is required!");
	}
	
	public boolean confirmationPasswordError() {
		return this.passwordConfirmationError.isDisplayed() && this.passwordConfirmationError.getText().equals("Passwords do not match!");
	}
	
	public String snackBarText() {
		return this.snackBar.findElement(By.tagName("span")).getText();
	}
	
	public void closeSnackBar() {
		this.snackBar.findElement(By.tagName("button")).click();
	}
	
}
