package com.example.demo.api;

public class AuthAPI {
	
	public static final String API_BASE = "/auth";
	public static final String API_HAS_EMAIL = API_BASE + "/has_email";
	public static final String API_REGISTER = API_BASE + "/register";
	public static final String API_LOGIN = API_BASE + "/login";

	public static final String API_ACTIVATE(String code) {
		return API_BASE + "/activate/" + code;
	}

}
