package com.example.demo.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.api.AuthAPI;
import com.example.demo.constants.AccountActivationConstants;
import com.example.demo.constants.Constants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.BooleanDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.AccountActivation;
import com.example.demo.model.User;
import com.example.demo.repository.AccountActivationRepository;
import com.example.demo.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountActivationRepository accountActivationRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void testHasEmailNewUserNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(UserConstants.NON_EXISTING_EMAIL);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						AuthAPI.API_HAS_EMAIL, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isValue());
	}
	
	@Test
	public void testHasEmailNewUserExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(UserConstants.EMAIL_ONE);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						AuthAPI.API_HAS_EMAIL, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isValue());
	}
	
	@Test
	public void testHasEmailOldUserOwnEmail() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.EMAIL_ONE);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						AuthAPI.API_HAS_EMAIL, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isValue());
	}
	
	@Test
	public void testHasEmailOldUserNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.NON_EXISTING_EMAIL);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						AuthAPI.API_HAS_EMAIL, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isValue());
	}
	
	@Test
	public void testHasEmailOldUserExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.EMAIL_TWO);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						AuthAPI.API_HAS_EMAIL, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isValue());
	}
	
	@Test
	public void testActivateExisting() {
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						AuthAPI.API_ACTIVATE(AccountActivationConstants.CODE_ONE), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(this.userRepository.findById(UserConstants.ID_ONE).orElse(null).isEnabled());
	}
	
	@Test
	public void testActivateNonExisting() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_ACTIVATE(AccountActivationConstants.NON_EXISTING_CODE), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_FOUND, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterValid() {
		long size = this.userRepository.count();
		RegisterDTO registration = this.testingRegistration();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		User user = this.userRepository.findAll().get((int) (this.userRepository.count() - 1));
		AccountActivation accountActivation = 
				this.accountActivationRepository
				.findAll().get((int) (this.accountActivationRepository.count() - 1));
		assertEquals(size + 1, this.userRepository.count());
		assertEquals(UserConstants.NON_EXISTING_EMAIL, user.getEmail());
		assertTrue(this.passwordEncoder.matches(UserConstants.LOGIN_PASSWORD, user.getPassword()));
		assertEquals(UserConstants.LOGIN_FIRST_NAME, user.getFirstName());
		assertEquals(UserConstants.LOGIN_LAST_NAME, user.getLastName());
		assertEquals(user.getId(), accountActivation.getUser().getId());
		this.accountActivationRepository.deleteById(accountActivation.getId());
		this.userRepository.deleteById(user.getId());
	}
	
	@Test
	public void testRegisterNullEmail() {
		RegisterDTO registration = this.testingRegistration();
		registration.setEmail(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterEmptyEmail() {
		RegisterDTO registration = this.testingRegistration();
		registration.setEmail("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterBlankEmail() {
		RegisterDTO registration = this.testingRegistration();
		registration.setEmail("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterInvalidEmail() {
		RegisterDTO registration = this.testingRegistration();
		registration.setEmail(UserConstants.NEW_PASSWORD);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.INVALID_EMAIL, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterNonUniqueEmail() {
		RegisterDTO registration = this.testingRegistration();
		registration.setEmail(UserConstants.LOGIN_EMAIL);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.UNIQUE_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterNullPassword() {
		RegisterDTO registration = this.testingRegistration();
		registration.setPassword(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterEmptyPassword() {
		RegisterDTO registration = this.testingRegistration();
		registration.setPassword("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterBlankPassword() {
		RegisterDTO registration = this.testingRegistration();
		registration.setPassword("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterNullFirstName() {
		RegisterDTO registration = this.testingRegistration();
		registration.setFirstName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterEmptyFirstName() {
		RegisterDTO registration = this.testingRegistration();
		registration.setFirstName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterBlankFirstName() {
		RegisterDTO registration = this.testingRegistration();
		registration.setFirstName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterNullLastName() {
		RegisterDTO registration = this.testingRegistration();
		registration.setLastName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterEmptyLastName() {
		RegisterDTO registration = this.testingRegistration();
		registration.setLastName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testRegisterBlanklLastName() {
		RegisterDTO registration = this.testingRegistration();
		registration.setLastName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_REGISTER, 
						HttpMethod.POST, 
						this.httpEntity(registration), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testLoginExisting() {
		LoginDTO login = this.testingLogin();
		ResponseEntity<ProfileDTO> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ProfileDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ProfileDTO profile = response.getBody();
		assertEquals(UserConstants.LOGIN_ID, profile.getId());
		assertEquals(Constants.GUEST_AUTHORITY, profile.getRole());
		assertEquals(UserConstants.LOGIN_EMAIL, profile.getEmail());
		assertEquals(UserConstants.LOGIN_FIRST_NAME, profile.getFirstName());
		assertEquals(UserConstants.LOGIN_LAST_NAME, profile.getLastName());
	}
	
	@Test
	public void testLoginNonExisting() {
		LoginDTO login = this.testingLogin();
		login.setPassword(UserConstants.NEW_PASSWORD);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(ExceptionConstants.BAD_CREDENTIALS, response.getBody().getMessage());
	}
	
	@Test
	public void testLoginNullEmail() {
		LoginDTO login = this.testingLogin();
		login.setEmail(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testLoginEmptyEmail() {
		LoginDTO login = this.testingLogin();
		login.setEmail("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testLoginBlankEmail() {
		LoginDTO login = this.testingLogin();
		login.setEmail("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testLoginNullPassword() {
		LoginDTO login = this.testingLogin();
		login.setPassword(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testLoginEmptyPassword() {
		LoginDTO login = this.testingLogin();
		login.setPassword("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testLoginBlankPassword() {
		LoginDTO login = this.testingLogin();
		login.setPassword("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						AuthAPI.API_LOGIN, 
						HttpMethod.POST, 
						this.httpEntity(login), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	private RegisterDTO testingRegistration() {
		RegisterDTO registration = new RegisterDTO();
		registration.setEmail(UserConstants.NON_EXISTING_EMAIL);
		registration.setPassword(UserConstants.LOGIN_PASSWORD);
		registration.setFirstName(UserConstants.LOGIN_FIRST_NAME);
		registration.setLastName(UserConstants.LOGIN_LAST_NAME);
		return registration;
	}
	
	private LoginDTO testingLogin() {
		LoginDTO login = new LoginDTO();
		login.setEmail(UserConstants.LOGIN_EMAIL);
		login.setPassword(UserConstants.LOGIN_PASSWORD);
		return login;
	}
	
	private HttpEntity<Object> httpEntity(Object obj){
		HttpHeaders headers = new HttpHeaders();
		return new HttpEntity<Object>(obj, headers);
	}
	
}
