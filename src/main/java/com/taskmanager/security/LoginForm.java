package com.taskmanager.security;


public record LoginForm(String username, String password) {

	
	public LoginForm {
		// No additional logic needed in this case
	}
}
