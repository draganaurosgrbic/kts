package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.User;
import com.example.demo.model.UserFollowing;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.UserFollowingRepository;

@Service
@Transactional(readOnly = true)
public class UserFollowingService {
	
	@Autowired
	private UserFollowingRepository userFollowingRepository;
			
	@Autowired
	private CulturalOfferRepository culturalOfferRepository;
	
	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	public Page<CulturalOffer> filter(FilterParamsDTO filters, Pageable pageable){
		return this.userFollowingRepository.filter(this.userService.currentUser().getId(), filters.getName(), filters.getLocation(), filters.getType(), pageable);
	}
	
	@Transactional(readOnly = false)
	public void toggleSubscription(long culturalOfferId) {
		User user = this.userService.currentUser();
		UserFollowing userFollowing = this.userFollowingRepository.findByUserIdAndCulturalOfferId(user.getId(), culturalOfferId);
		if (userFollowing == null) {
			this.userFollowingRepository.save(new UserFollowing(user, this.culturalOfferRepository.findById(culturalOfferId).orElse(null)));		
		}
		else {
			this.userFollowingRepository.deleteById(userFollowing.getId());
		}
	}
	
}
