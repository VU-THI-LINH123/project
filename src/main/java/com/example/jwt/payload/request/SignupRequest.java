package com.example.jwt.payload.request;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.*;

import com.example.jwt.payload.response.JwtResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignupRequest {

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	private Set<String> role;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

}
