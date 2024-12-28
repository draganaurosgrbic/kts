package com.example.demo.common;

import java.io.File;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ImageInput {
		
	@FindBy(xpath = "//*/app-image-input/div/app-spacer-container/div/input")
	private WebElement fileInput;
	
	public void uploadFile(String filePath) {
		this.fileInput.sendKeys(new File(filePath).getAbsolutePath());
	}

}
