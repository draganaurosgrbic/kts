package com.example.demo.api;

import org.springframework.data.domain.Pageable;

public class NewsAPI {
	
	public static final String API_BASE = "/api/news";
	
	public static String API_FILTER(long culturalOfferId, Pageable pageable) {		
		return CulturalOfferAPI.API_BASE + "/" + culturalOfferId + "/filter_news?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
	}
	
	public static String API_DELETE(long id) {
		return API_BASE + "/" + id;
	}

}
