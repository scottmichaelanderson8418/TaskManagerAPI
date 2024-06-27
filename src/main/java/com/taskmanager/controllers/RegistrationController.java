package com.taskmanager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;

@RestController
public class RegistrationController {

	@Autowired
	private MyUserRepository myUserRepository;

	/*
	 * When the class has only one constructor argument, and the argument matches the field name -- then
	 * spring can automatically identify the dependency to be injected public
	 * RegistrationController(MyUserRepository myUserRepository) { this.myUserRepository =
	 * myUserRepository; }
	 */

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/register/user")
	public ResponseEntity<?> registerNewUser(@RequestBody MyUser myUser) {

		if (myUserRepository.findByUsername(myUser.getUsername()).isPresent()) {

			return new ResponseEntity<String>("User already exist in database", HttpStatus.OK);

		}

		MyUser newUser = new MyUser();

		newUser.setPassword(passwordEncoder.encode(myUser.getPassword()));

		newUser.setUsername(myUser.getUsername());

		newUser.setRole(myUser.getRole());

		myUserRepository.save(newUser);

		return new ResponseEntity<MyUser>(newUser, HttpStatus.OK);

	}

	@GetMapping("/getAllUsers")
	public List<MyUser> getUsers(MyUserRepository myUserRepository) {

		List<MyUser> myUserList;

		myUserList = myUserRepository.findAll();

		// for (int i = 0; i < myUserList.size(); i++) {
		// System.out.println(myUserList.get(i).toString());
		// }

		return myUserList;

	}

}
