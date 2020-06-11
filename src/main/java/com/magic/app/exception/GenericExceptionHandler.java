package com.magic.app.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<CustomErrorResponse> handleGenericServerError(Exception e, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		CustomErrorResponse error = new CustomErrorResponse(status.name(), e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		error.setStatus(status.value());
		return new ResponseEntity<>(error, status);
	}
}
