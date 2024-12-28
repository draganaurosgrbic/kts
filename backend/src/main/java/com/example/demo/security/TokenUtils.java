package com.example.demo.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils {

	@Value("KTS-NVT-APP")
	private String appName;
	
	@Value("KTS-NVT-SECRET")
	private String appSecret;
	
	@Value("Authorization")
	private String authHeader;
		
	@Value("1000000000")
	private long expiresIn;
	
	private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
	
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuer(this.appName)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + this.expiresIn))
				.signWith(this.signatureAlgorithm, this.appSecret).compact();
	}
	
	public boolean validateToken(String token, UserDetails user) {
		String username = this.getUsername(token);
		return username != null && username.equals(user.getUsername());
	}
	
	public String getToken(HttpServletRequest request) {
		return request.getHeader(this.authHeader);
	}
	
	public String getUsername(String token) {
		return this.getClaims(token).getSubject();
	}
	
	private Claims getClaims(String token) {
		return Jwts.parser()
				.setSigningKey(this.appSecret)
				.parseClaimsJws(token)
				.getBody();
	}
	
}
