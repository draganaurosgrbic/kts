package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.Constants;
import com.example.demo.dto.CulturalOfferDTO;
import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.mapper.CulturalOfferMapper;
import com.example.demo.model.CulturalOffer;
import com.example.demo.service.UserFollowingService;

@RestController
@RequestMapping(value = "/api/cultural_offers", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('guest')")
public class UserFollowingController {
	
	@Autowired
	private UserFollowingService userFollowingService;
	
	@Autowired
	private CulturalOfferMapper culturalOfferMapper;

	@PostMapping(value = "/filter_followings")
	public ResponseEntity<List<CulturalOfferDTO>> filter(@RequestBody FilterParamsDTO filters, @RequestParam int page, @RequestParam int size, HttpServletResponse response){
		Pageable pageable = PageRequest.of(page, size);
		Page<CulturalOffer> culturalOffers = this.userFollowingService.filter(filters, pageable);
		response.setHeader(Constants.ENABLE_HEADER, Constants.FIRST_PAGE_HEADER + ", " + Constants.LAST_PAGE_HEADER);
		response.setHeader(Constants.FIRST_PAGE_HEADER, culturalOffers.isFirst() + "");
		response.setHeader(Constants.LAST_PAGE_HEADER, culturalOffers.isLast() + "");
		return new ResponseEntity<>(this.culturalOfferMapper.map(culturalOffers.toList()), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{culturalOfferId}/toggle_subscription")
	public ResponseEntity<Void> toggleSubscription(@PathVariable long culturalOfferId){
		this.userFollowingService.toggleSubscription(culturalOfferId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
