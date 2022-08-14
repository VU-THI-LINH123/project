package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest

class ProjectApplicationTests {

//	@Test
//	void contextLoads() {
//	}
	@Test
	public String tEncodePassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode("vuthilinh.itk12.1998@gmail.com");
		System.out.println("con yeu e");
		System.out.println(encodedPassword);
		return encodedPassword;
	
	}
}
