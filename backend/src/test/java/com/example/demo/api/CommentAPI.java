package com.example.demo.api;

import org.springframework.data.domain.Pageable;

public class CommentAPI {
	
	public static final String API_BASE = "/api/comments";
	
	public static String API_LIST(long culturalOfferId, Pageable pageable) {		
		return CulturalOfferAPI.API_BASE + "/" + culturalOfferId + "/comments?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
	}
		
	public static String API_DELETE(long id) {
		return API_BASE + "/" + id;
	}

}
