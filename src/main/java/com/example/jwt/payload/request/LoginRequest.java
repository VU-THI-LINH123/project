package com.example.jwt.payload.request;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.example.jwt.payload.response.JwtResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginRequest {
	@NotBlank(message = "email is mandatory")
	private String email;

	@NotBlank(message = "password is mandatory")
	private String password;

	
}
