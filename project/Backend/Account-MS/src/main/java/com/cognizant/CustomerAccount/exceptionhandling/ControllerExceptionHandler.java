package com.cognizant.CustomerAccount.exceptionhandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.cognizant.CustomerAccount.model.CustomerAccountErrorResponse;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(MinimumBalanceException.class)
	public ResponseEntity<CustomerAccountErrorResponse> minimumBalanceException(MinimumBalanceException exception,
			WebRequest request) {
		CustomerAccountErrorResponse response = new CustomerAccountErrorResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE,
				exception.getMessage(), "Minimum Balance Problem");
		return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<CustomerAccountErrorResponse> accountNotFoundException(AccountNotFoundException exception,
			WebRequest request) {
		CustomerAccountErrorResponse response = new CustomerAccountErrorResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE,
				exception.getMessage(), "CustomerAccount not found");
		return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<CustomerAccountErrorResponse> accessDeniedException(AccessDeniedException exception,
			WebRequest request) {
		CustomerAccountErrorResponse response = new CustomerAccountErrorResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE,
				exception.getMessage(), "Access Denied");
		return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomerAccountErrorResponse> globalException(Exception exception, WebRequest request) {
		CustomerAccountErrorResponse response = new CustomerAccountErrorResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE,
				exception.getMessage(), "Error occurred");
		return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	}

}
