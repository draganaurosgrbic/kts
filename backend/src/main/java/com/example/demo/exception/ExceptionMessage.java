package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ExceptionMessage {
	
	private String message;
	private HttpStatus status;
	
	public ExceptionMessage() {
		super();
	}

	public ExceptionMessage(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
