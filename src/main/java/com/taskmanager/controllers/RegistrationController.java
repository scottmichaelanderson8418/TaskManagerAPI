package com.taskmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;


@RestController
public class RegistrationController {

	
	@Autowired
	private MyUserRepository myUserRepository; // Repository for managing user data

	
	@Autowired
	private PasswordEncoder passwordEncoder; // Encoder for password hashing

	
	@PostMapping("/register/user")
	public ResponseEntity<String> createUser(@RequestBody MyUser user) {

		// Check if the username already exists
		if (myUserRepository.findByUsername(user.getUsername()).isPresent()) {
			// Return conflict response if username already exists
			return new ResponseEntity<>("User already exists. Try another username.",
					HttpStatus.CONFLICT);
		}

		// Encode user password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// Save user details to the repository
		myUserRepository.save(user);

		// Return success response upon successful registration
		return new ResponseEntity<>("Registration is successful :)", HttpStatus.OK);
	}
}
