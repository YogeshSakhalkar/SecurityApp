package com.example.SecurityApp;

import com.example.SecurityApp.entites.User;
import com.example.SecurityApp.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityAppApplicationTests {

	@Autowired
	private JwtService jwtService;



	@Test
	void contextLoads() {
		User user = new User(4L, "yogesh@gmail.com", "1234");
		String token = this.jwtService.generateKey(user);
		System.out.println(token);
		Long id = this.jwtService.getUserIdFromToken("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiI0IiwiZW1haWwiOiJhbnVqQGdtYWlsLmNvbSIsInJvbGVzIjpbIlVTRVIiLCJBRE1JTiJdLCJpYXQiOjE3MjE2NTMyNTgsImV4cCI6MTcyMTY1MzMxOH0.fhjAtKv56iN9XsyPxNh6V3rr6Eds9LuWVkqklcRQNzgTq7GY-46TG2LFvyDshiZe");
		System.out.println(id);
	}
}
