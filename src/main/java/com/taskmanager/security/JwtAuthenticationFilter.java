package com.taskmanager.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taskmanager.service.MyUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	@Autowired
	private JwtService jwtService;

	
	@Autowired
	private MyUserDetailService myUserDetailService;

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Get the Authorization header from the request
		String authHeader = request.getHeader("Authorization");

		// If the Authorization header is missing or does not start with "Bearer ",
		// continue the filter chain
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Extract the JWT token from the Authorization header
		String jwt = authHeader.substring(7);

		// Extract the username from the JWT token
		String username = jwtService.extractUsername(jwt);

		// If username is not null and the authentication context is not already set
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// Load user details using the username extracted from the token
			UserDetails userDetails = myUserDetailService.loadUserByUsername(username);

			// Validate the token and user details
			if (userDetails != null && jwtService.isTokenValid(jwt)) {

				// Create an authentication token and set the authentication context
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
						userDetails.getPassword(), userDetails.getAuthorities());

				// Set additional details for the authentication token
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set the authentication context
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		// Continue the filter chain
		filterChain.doFilter(request, response);
	}
}
