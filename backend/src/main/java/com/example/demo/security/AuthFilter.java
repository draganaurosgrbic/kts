package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthFilter extends OncePerRequestFilter {
	
	private UserDetailsService userService;
	private TokenUtils tokenUtils;
	
	public AuthFilter(UserDetailsService userService, TokenUtils tokenUtils) {
		super();
		this.userService = userService;
		this.tokenUtils = tokenUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = this.tokenUtils.getToken(request);
		if (token != null) {
			String username = this.tokenUtils.getUsername(token);
			if (username != null) {
				UserDetails user = this.userService.loadUserByUsername(username);
				if (user != null && this.tokenUtils.validateToken(token, user)) {
					AuthToken authToken = new AuthToken(user, token);
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		filterChain.doFilter(request, response);		
	}
	
}
