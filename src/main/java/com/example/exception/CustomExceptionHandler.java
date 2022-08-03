package com.example.exception;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.dto.response.ResultResponse;

@RestControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResultResponse handleException(Exception ex, WebRequest request) {
		ex.printStackTrace();
		return new ResultResponse(HttpStatus.BAD_REQUEST.value(), "api.error", ex.getMessage(), null);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomException(CustomException ex, WebRequest request) {
		return ResponseEntity.status(ex.getStatusCode())
				.body(new ResultResponse(ex.getStatusCode(), ex.getMessageCode(), ex.getMessage(), null));
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
	   Map<String, String> errors = new HashMap<>();
	 
	   ex.getBindingResult().getFieldErrors().forEach(error ->
	           errors.put(error.getField(), error.getDefaultMessage()));
	 
	   return errors;
	} 
	
}
