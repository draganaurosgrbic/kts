package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.api.AuthAPI;
import com.example.demo.api.UserAPI;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.ProfileUploadDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	private String accessToken;
	
	@Autowired
	private UserRepository userRepository;
			
	@Before
	public void guestLogin() {
		LoginDTO login = new LoginDTO();
		login.setEmail(UserConstants.LOGIN_EMAIL);
		login.setPassword(UserConstants.LOGIN_PASSWORD);
		ResponseEntity<ProfileDTO> response = this.restTemplate.postForEntity(AuthAPI.API_LOGIN, login, ProfileDTO.class);
		this.accessToken = response.getBody().getAccessToken();
	}
		
	@Test
	public void testUpdateValid() {
		long size = this.userRepository.count();
		ProfileUploadDTO userDTO = this.testingUserDTO();
		ResponseEntity<ProfileDTO> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ProfileDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ProfileDTO user = response.getBody();
		assertEquals(size, this.userRepository.count());
		assertEquals(UserConstants.LOGIN_ID, user.getId());
		assertEquals(UserConstants.LOGIN_EMAIL, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test
	public void testUpdateNullEmail() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setEmail(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateEmptyEmail() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setEmail("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateBlankEmail() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setEmail("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateInvalidEmail() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setEmail(UserConstants.NEW_PASSWORD);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.INVALID_EMAIL, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateNonUniqueEmail() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setEmail(UserConstants.EMAIL_ONE);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.UNIQUE_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateNullFirstName() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setFirstName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateEmptyFirstName() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setFirstName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateBlankFirstName() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setFirstName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateNullLastName() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setLastName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateEmptyLastName() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setLastName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateBlanklLastName() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setLastName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdatePasswordWrongOldPassword() {
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setOldPassword(UserConstants.NEW_PASSWORD);
		userDTO.setNewPassword(UserConstants.NEW_PASSWORD);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(ExceptionConstants.BAD_CREDENTIALS, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdatePasswordCorrectOldPassword() {
		long size = this.userRepository.count();
		ProfileUploadDTO userDTO = this.testingUserDTO();
		userDTO.setOldPassword(UserConstants.LOGIN_PASSWORD);
		userDTO.setNewPassword(UserConstants.NEW_PASSWORD);
		ResponseEntity<ProfileDTO> response = 
				this.restTemplate.exchange(
						UserAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(userDTO), 
						ProfileDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ProfileDTO user = response.getBody();
		assertEquals(size, this.userRepository.count());
		assertEquals(UserConstants.LOGIN_ID, user.getId());
		assertEquals(UserConstants.LOGIN_EMAIL, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());

		LoginDTO login = new LoginDTO();
		login.setEmail(UserConstants.LOGIN_EMAIL);
		login.setPassword(UserConstants.NEW_PASSWORD);
		response = 
				this.restTemplate.postForEntity(
						AuthAPI.API_LOGIN, 
						login, 
						ProfileDTO.class);
		this.accessToken = response.getBody().getAccessToken();
		userDTO.setOldPassword(UserConstants.NEW_PASSWORD);
		userDTO.setNewPassword(UserConstants.LOGIN_PASSWORD);
		this.restTemplate.exchange(
				UserAPI.API_BASE, 
				HttpMethod.POST, 
				this.httpEntity(userDTO), 
				ProfileDTO.class);
	}
	
	private ProfileUploadDTO testingUserDTO() {
		ProfileUploadDTO user = new ProfileUploadDTO();
		user.setEmail(UserConstants.LOGIN_EMAIL);
		user.setFirstName(UserConstants.FIRST_NAME_ONE);
		user.setLastName(UserConstants.LAST_NAME_ONE);
		return user;
	}
	
	private HttpEntity<Object> httpEntity(ProfileUploadDTO upload){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);			
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("email", upload.getEmail());
		body.add("firstName", upload.getFirstName());
		body.add("lastName", upload.getLastName());
		body.add("oldPassword", upload.getOldPassword());
		body.add("newPassword", upload.getNewPassword());
		return new HttpEntity<Object>(body, headers);
	}
	
}
