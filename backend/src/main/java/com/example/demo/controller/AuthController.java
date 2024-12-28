package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BooleanDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.security.TokenUtils;
import com.example.demo.service.AccountActivationService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("permitAll()")
public class AuthController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountActivationService accountActivationService;
		
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private TokenUtils tokenUtils;
		
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping(value = "/has_email")
	public ResponseEntity<BooleanDTO> hasEmail(@RequestBody UniqueCheckDTO param) {
		return new ResponseEntity<>(new BooleanDTO(this.userService.hasEmail(param)), HttpStatus.OK);
	}
		
	@GetMapping(value = "/activate/{code}")
	public ResponseEntity<Void> activate(@PathVariable String code){
		this.accountActivationService.activate(code);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterDTO registerDTO){
		this.accountActivationService.save(this.userService.save(this.userMapper.map(registerDTO), null));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<ProfileDTO> login(@Valid @RequestBody LoginDTO loginDTO){
		this.authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
		String accessToken = this.tokenUtils.generateToken(loginDTO.getEmail());
		User user = (User) this.userService.loadUserByUsername(loginDTO.getEmail());
		return new ResponseEntity<>(new ProfileDTO(user, accessToken), HttpStatus.OK);
	}
		
}
