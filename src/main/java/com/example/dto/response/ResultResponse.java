package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResultResponse {
	private int statusCode;
	private String messageCode;
	private String message;
	private Object result;
	public static final int STATUS_CODE_SUCCESS = 200;
	public static final int STATUS_CODE_BAD_REQUEST = 409;
	public static final int STATUS_CODE_NOT_FOUND = 404;
	public static final int STATUS_CODE_UNKNOWN_FAILED = 500;
	public static final int STATUS_CODE_UNAUTHORIZED = 401;
	public static final int STATUS_CODE_FORBIDDEN = 403;
	public static final int STATUS_CODE_NOT_SUPPORT = 300;
	public static final int STATUS_CODE_TOO_MANY_REQUEST = 309;

	public static final String MESSEGE_CODE_SUCCESS = "api.success";
	public static final String MESSEGE_SUCCESS = "Success!";
	public static final String MESSEGE_CODE_NULL_TIENGHI = null;
}
