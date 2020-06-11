package com.magic.app.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public CustomErrorResponse handleGenericServerError(Exception e) {
		CustomErrorResponse error = new CustomErrorResponse("SERVER_ERROR", e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.OK.value());
		return error;
	}
}
