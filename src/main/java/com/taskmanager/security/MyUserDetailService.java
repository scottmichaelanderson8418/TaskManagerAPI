package com.taskmanager.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;

@Component
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	MyUserRepository myUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("loadUserByUsername() Entered.....");

		Optional<MyUser> myUser = myUserRepository.findByUsername(username);

		if (myUser.isPresent()) {
			// we will provide a userDetails for the user
			// We are creating a "User" instance with the MyUser "myUser" instance
			var userObj = myUser.get();

			System.out.println("loadUserByUsername() Exit.....");
			return User.builder().username(userObj.getUsername()).password(userObj.getPassword())
					.roles(getRoles(userObj)).build();

		} else {
			throw new UsernameNotFoundException(username);
		}

	}

	//
	private String[] getRoles(MyUser myUser) {

		// if user role is empty just return "USER"
		if (myUser.getRole() == null) {

			return new String[] { "USER" };

		}
		// this will return a String[]
		return myUser.getRole().split(",");

	}

}
