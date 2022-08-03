package com.example.jwt.payload.response;

import java.util.List;
import java.util.Set;

import com.example.domain.Role;
import com.example.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtResponse {
	private String token;
}
