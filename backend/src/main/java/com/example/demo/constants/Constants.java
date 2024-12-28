package com.example.demo.constants;

import java.io.File;

public class Constants {
	
	public static final String BACKEND_URL = "https://localhost:8080";
	public static final String FRONTEND_URL = "https://localhost:4200";	
	public static final String ADMIN_AUTHORITY = "admin";
	public static final String GUEST_AUTHORITY = "guest";

	public static final String STATIC_FOLDER = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "static";
	public static final String ENABLE_HEADER = "Access-Control-Expose-Headers";
	public static final String FIRST_PAGE_HEADER = "first-page";
	public static final String LAST_PAGE_HEADER = "last-page";
		
}
