package com.salesavvy.app.Controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesavvy.app.Dao.UserResponse;

import com.salesavvy.app.entites.User;

import com.salesavvy.app.userServices.UserServiceContract;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5167", allowCredentials = "true")
public class UserController {

	private final UserServiceContract Contract;

	UserController(UserServiceContract Contract) {
		this.Contract = Contract;
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User nuserData) {

		try {
			
			User nuser = Contract.registerUser(nuserData);

			return ResponseEntity.ok(Map.of("message", "User registered successfully", "user",
					new UserResponse(nuser.getUserId(), nuser.getUsername(), nuser.getEmail(), nuser.getRole().name())));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

}