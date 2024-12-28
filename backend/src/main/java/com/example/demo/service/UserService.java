package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constants.Constants;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repository.UserFollowingRepository;
import com.example.demo.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserFollowingRepository userFollowingRepository;

	@Autowired
	private ImageService imageService;
		
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		return this.userRepository.findByEmail(username);	
	}
	
	@Transactional(readOnly = true)
	public boolean hasEmail(UniqueCheckDTO param) {
		return this.userRepository.hasEmail(param.getId(), param.getName()) != null;
	}
	
	@Transactional(readOnly = false)
	public User save(User user, MultipartFile upload) {
		if (upload != null) {
			Image image = new Image(this.imageService.store(upload));
			user.setImage(image.getPath());
			this.imageService.save(image);
		}
		return this.userRepository.save(user);
	}
							
	@Transactional(readOnly = true)
	public User currentUser() {
		try {
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public boolean userIsFollowing(long culturalOfferId) {
		try {
			User user = this.currentUser();
			if (!user.getAuthority().getName().equals(Constants.GUEST_AUTHORITY)) {
				return false;
			}
			return this.userFollowingRepository.findByUserIdAndCulturalOfferId(user.getId(), culturalOfferId) != null;
		}
		catch(Exception e) {
			return false;
		}
	}

}
