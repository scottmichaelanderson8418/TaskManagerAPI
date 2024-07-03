package com.taskmanager.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.service.MyUserService;

@RequestMapping(value = "/api")
@RestController
public class RegistrationController {
	Logger logger = LoggerFactory.getLogger(RegistrationController.class.getName());

	@Autowired
	MyUserService myUserService;

	@Autowired
	private MyUserRepository myUserRepository;

	/*
	 * When the class has only one constructor argument, and the argument matches the field name -- then spring can
	 * automatically identify the dependency to be injected public RegistrationController(MyUserRepository
	 * myUserRepository) { this.myUserRepository = myUserRepository; }
	 */

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/register/user")
	public ResponseEntity<?> registerNewUser(@RequestBody MyUserDto myUserDto) {
		logger.trace("ENTERED……………………………………registerNewUser()");

		if (myUserRepository.findByUsername(myUserDto.getUsername()).isPresent()) {

			logger.trace("EXITED……………………………………registerNewUser()");
			return new ResponseEntity<String>("User already exist in database", HttpStatus.OK);

		}

		MyUserDto newMyUserDto = myUserService.createUser(myUserDto);

		return new ResponseEntity<MyUserDto>(newMyUserDto, HttpStatus.OK);

	}

	@GetMapping("/getAllUsers")
	public List<MyUser> getUsers(MyUserRepository myUserRepository) {
		logger.trace("ENTERED……………………………………getUsers()");

		List<MyUser> myUserList;

		myUserList = myUserRepository.findAll();

		// for (Long i = 0; i < myUserList.size(); i++) {
		// System.out.prLongln(myUserList.get(i).toString());
		// }

		logger.trace("EXITED……………………………………getUsers()");
		return myUserList;

	}

}
