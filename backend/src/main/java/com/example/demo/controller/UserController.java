package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.ProfileUploadDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.security.TokenUtils;
import com.example.demo.service.UserService;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('guest')")
public class UserController {
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TokenUtils tokenUtils;
		
	@PostMapping
	public ResponseEntity<ProfileDTO> update(@Valid @ModelAttribute ProfileUploadDTO profileDTO) {
		User user = this.userService.save(this.userMapper.map(profileDTO), profileDTO.getImage());
		String accessToken = this.tokenUtils.generateToken(user.getUsername());
		return new ResponseEntity<>(new ProfileDTO(user, accessToken), HttpStatus.OK);
	}

}
