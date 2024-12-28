package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler
	public ResponseEntity<ExceptionMessage> handleException(Exception exception){
		String message = exception.getMessage();
		if (message == null || message.contains("is null")) {
			return new ResponseEntity<>(new ExceptionMessage(ExceptionConstants.NOT_FOUND, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
		}
		else if (message.contains("entity with id")) {
			return new ResponseEntity<>(new ExceptionMessage(ExceptionConstants.INVALID_ID, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
		}
		else if (message.contains("must not be null") || message.contains("must not be blank")) {
			return new ResponseEntity<>(new ExceptionMessage(ExceptionConstants.NOT_EMPTY_VIOLATION, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
		else if (message.contains("ConstraintViolationException")) {
			return new ResponseEntity<>(new ExceptionMessage(ExceptionConstants.UNIQUE_VIOLATION, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
		else if (message.contains("must be a well-formed email address")) {
			return new ResponseEntity<>(new ExceptionMessage(ExceptionConstants.INVALID_EMAIL, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ExceptionMessage(message, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
