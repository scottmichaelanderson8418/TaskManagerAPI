package com.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.taskmanager.service.MyUserDetailService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private MyUserDetailService userDetailService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	

	// the security filter chain tells the computer which urls to pass through and
	// which to authenticate
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		// we disable csrf to enable post request
		return httpSecurity.csrf(AbstractHttpConfigurer::disable)

				// lambda expression uses the httpSecurity.authorizeHttpRequests method with
				// "registry" as an argument to defined the authorization rules yeah

				.authorizeHttpRequests(registry -> {

					registry.requestMatchers("/home", "/register
	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailService;
	}

	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		// set the userDetailsService for authentication
		provider.setUserDetailsService(userDetailService);
		// Set password encoder (which is BCryptPasswordEncoder)
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	
	@Bean
	public AuthenticationManager authenticationManager() {
		// Create AuthenticationManager using the configured authentication provider
		return new ProviderManager(authenticationProvider());
	}

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Use BCryptPasswordEncoder for password encoding
		return new BCryptPasswordEncoder();
	}
}