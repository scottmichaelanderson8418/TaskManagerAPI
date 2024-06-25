
package com.taskmanager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;


@Service
public class MyUserDetailService implements UserDetailsService {

	
	@Autowired
	private MyUserRepository myUserRepository;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<MyUser> user = myUserRepository.findByUsername(username);
		// If the username is found then an object of the "UserDetails" Interface is
		// created using the username and password
		if (user.isPresent()) {
			var userObj = user.get();
			// uses the "User" class "builder()" method to create a "UserBuiler"
			// the "UserBuilder" has the "username()" and "password()" and "roles()" methods
			// for building and instance of "UserBuilder" class
			// the "UserBuilder" class contains a method called "build()" that returns a
			// "UserDetails" object
			return User.builder().username(userObj.getUsername()).password(userObj.getPassword())
					.roles(getRoles(userObj)).build();

		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	
	// the getRoles method passes in an instance of "MyUser" class
	private String[] getRoles(MyUser user) {
		// if the user does not have a role set the role = "USER"
		if (user.getRole() == null) {
			// create a new String array containing the role= "USER"
			return new String[] { "USER" };
		}
		// the split() method will separate a string by "," and create a String[] array
		return user.getRole().split(",");
	}
}