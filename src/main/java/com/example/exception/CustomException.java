package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomException extends Exception {
	private static final long serialVersionUID = 1L;

	private int statusCodej;
	private String messageCode;
	private String message;
}
