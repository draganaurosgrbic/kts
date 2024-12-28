package com.example.demo.user;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.demo.Constants;

public class ProfilePage {
	
	private WebDriver browser;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[1]/div/div[1]/div/input")
	private WebElement emailInput;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[2]/div/div[1]/div/input")
	private WebElement firstNameInput;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[3]/div/div[1]/div/input")
	private WebElement lastNameInput;
		
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[4]/div/div[1]/div/input")
	private WebElement oldPasswordInput;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[5]/div/div[1]/div/input")
	private WebElement newPasswordInput;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[6]/div/div[1]/div/input")
	private WebElement newPasswordConfirmationInput;
		
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[1]/div/div[3]/div/mat-error")
	private WebElement emailError;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[2]/div/div[3]/div/mat-error")
	private WebElement firstNameError;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-form-field[3]/div/div[3]/div/mat-error")
	private WebElement lastNameError;

	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[1]/div/mat-error")
	private WebElement passwordError;
	
	@FindBy(xpath = "//*/app-profile-form/app-form-container/div/mat-card/mat-card-content/form/div[2]/app-spacer-container/div/span[2]/button")
	private WebElement saveButton;

	@FindBy(tagName = Constants.SNACKBAR)
	private WebElement snackBar;

	public ProfilePage(WebDriver browser) {
		super();
		this.browser = browser;
	}
			
	public void emailInputFill(String value) {
		this.emailInput.clear();
		this.emailInput.sendKeys(value);
		this.emailInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
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
	
	public void oldPasswordInputFill(String value) {
		this.oldPasswordInput.clear();
		this.oldPasswordInput.sendKeys(value);
		this.oldPasswordInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void newPasswordInputFill(String value) {
		this.newPasswordInput.clear();
		this.newPasswordInput.sendKeys(value);
		this.newPasswordInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void newPasswordConfirmationInputFill(String value) {
		this.newPasswordConfirmationInput.clear();
		this.newPasswordConfirmationInput.sendKeys(value);
		this.newPasswordConfirmationInput.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
	}
	
	public void saveButtonClick() {
		this.saveButton.click();
	}
	
	public void ensureFormDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.emailInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstNameInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.lastNameInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.oldPasswordInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.newPasswordInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.newPasswordConfirmationInput));
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.saveButton));
	}
		
	public void ensureEmailErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.emailError));
	}
	
	public void ensureFirstNameErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.firstNameError));
	}
	
	public void ensureLastNameErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.lastNameError));
	}
	
	public void ensurePasswordErrorDisplayed() {
		(new WebDriverWait(this.browser, Constants.TIMEOUT_WAIT)).until(ExpectedConditions.elementToBeClickable(this.passwordError));
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
	
	public boolean oldPasswordError() {
		return this.passwordError.isDisplayed() && this.passwordError.getText().equals("Old password is required!");
	}
	
	public boolean confirmationPasswordError() {
		return this.passwordError.isDisplayed() && this.passwordError.getText().equals("Passwords do not match!");
	}
	
	public String snackBarText() {
		return this.snackBar.findElement(By.tagName("span")).getText();
	}
	
	public void closeSnackBar() {
		this.snackBar.findElement(By.tagName("button")).click();
	}
	
}
