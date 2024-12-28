package com.example.demo.api;

import org.springframework.data.domain.Pageable;

public class CulturalOfferAPI {
	
	public static final String API_BASE = "/api/cultural_offers";
	public static final String API_HAS_NAME = API_BASE + "/has_name";
	public static final String API_FILTER_NAMES = API_BASE + "/filter_names";
	public static final String API_FILTER_LOCATIONS = API_BASE + "/filter_locations";
	public static final String API_FILTER_TYPES = API_BASE + "/filter_types";
	
	public static String API_FILTER(Pageable pageable) {		
		return API_BASE + "/filter?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
	}
	
	public static String API_DELETE(long id) {		
		return API_BASE + "/" + id;
	}
	
	public static String API_FILTER_FOLLOWINGS(Pageable pageable) {		
		return API_BASE + "/filter_followings?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
	}
	
	public static String API_TOGGLE_SUBSCRIPTION(long id) {
		return API_BASE + "/" + id + "/toggle_subscription";
	}

}
