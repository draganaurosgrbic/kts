package com.example.demo.api;

import org.springframework.data.domain.Pageable;

public class TypeAPI {
	
	public static final String API_BASE = "/api/types";
	public static final String API_HAS_NAME = API_BASE + "/has_name";
	public static final String API_FILTER_NAMES = API_BASE + "/filter_names";
	
	public static String API_LIST(Pageable pageable) {		
		return API_BASE + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
	}
	
	public static String API_DELETE(long id) {		
		return API_BASE + "/" + id;
	}

}
