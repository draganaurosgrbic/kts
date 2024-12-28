package com.example.demo.service.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.Constants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Authority;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthToken;
import com.example.demo.security.TokenUtils;
import com.example.demo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Test
	public void testloadUserByUsernameExisting() {
		User user = (User) this.userService
				.loadUserByUsername(UserConstants.EMAIL_ONE);
		assertNotNull(user);
		assertEquals(UserConstants.ID_ONE, user.getId());
		assertEquals(UserConstants.EMAIL_ONE, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test
	public void testloadUserByUsernameNonExisting() {
		User user = (User) this.userService
				.loadUserByUsername(UserConstants.NON_EXISTING_EMAIL);
		assertNull(user);
	}
	
	@Test
	public void testHasEmailNewUserNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(UserConstants.NON_EXISTING_EMAIL);
		assertFalse(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailNewUserExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(UserConstants.EMAIL_ONE);
		assertTrue(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailOldUserOwnEmail() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.EMAIL_ONE);
		assertFalse(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailOldUserNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.NON_EXISTING_EMAIL);
		assertFalse(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailOldUserExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.EMAIL_TWO);
		assertTrue(this.userService.hasEmail(param));
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddValid() {
		long size = this.userRepository.count();
		User user = this.testingUser();
		user = this.userService.save(user, null);
		assertEquals(size + 1, this.userRepository.count());
		assertEquals(UserConstants.NON_EXISTING_EMAIL, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullEmail() {
		User user = this.testingUser();
		user.setEmail(null);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyEmail() {
		User user = this.testingUser();
		user.setEmail("");
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankEmail() {
		User user = this.testingUser();
		user.setEmail("  ");
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddInvalidEmail() {
		User user = this.testingUser();
		user.setEmail(UserConstants.NEW_PASSWORD);
		this.userService.save(user, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNonUniqueEmail() {
		User user = this.testingUser();
		user.setEmail(UserConstants.EMAIL_ONE);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullPassword() {
		User user = this.testingUser();
		user.setPassword(null);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyPassword() {
		User user = this.testingUser();
		user.setPassword("");
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankPassword() {
		User user = this.testingUser();
		user.setPassword(" ");
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullFirstName() {
		User user = this.testingUser();
		user.setFirstName(null);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyFirstName() {
		User user = this.testingUser();
		user.setFirstName("");
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankFirstName() {
		User user = this.testingUser();
		user.setFirstName("  ");
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullLastName() {
		User user = this.testingUser();
		user.setLastName(null);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyLastName() {
		User user = this.testingUser();
		user.setLastName("");
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlanklLastName() {
		User user = this.testingUser();
		user.setLastName("  ");
		this.userService.save(user, null);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateValid() {
		long size = this.userRepository.count();
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user = this.userService.save(user, null);
		assertEquals(size, this.userRepository.count());
		assertEquals(UserConstants.ID_ONE, user.getId());
		assertEquals(UserConstants.NON_EXISTING_EMAIL, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail(null);
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail("");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail("  ");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateInvalidEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail(UserConstants.NEW_PASSWORD);
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNonUniqueEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail(UserConstants.EMAIL_TWO);
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullPassword() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setPassword(null);
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyPassword() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setPassword("");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankPassword() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setPassword(" ");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullFirstName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setFirstName(null);
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyFirstName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setFirstName("");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankFirstName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setFirstName("  ");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullLastName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setLastName(null);
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyLastName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setLastName("");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlanklLastName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setLastName("  ");
		this.userService.save(user, null);
		this.userRepository.count();
	}
	
	@Test
	public void testCurrentUserNull() {
		assertNull(this.userService.currentUser());
	}
	
	@Test
	public void testCurrentUserNotNull() {
		this.setAuthentication(false);
		User user = this.userService.currentUser();
		assertNotNull(user);
		assertEquals(UserConstants.ID_TWO, user.getId());
		assertEquals(UserConstants.EMAIL_TWO, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_TWO, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_TWO, user.getLastName());
	}
	
	@Test
	public void testUserIsFollowingNullUser() {
		assertFalse(this.userService.userIsFollowing(CulturalOfferConstants.ID_ONE));
	}
	
	@Test
	public void testUserIsFollowingAdminUser() {
		this.setAuthentication(true);
		assertFalse(this.userService.userIsFollowing(CulturalOfferConstants.ID_ONE));
	}
	
	@Test
	public void testUserIsFollowingNonExisting() {
		this.setAuthentication(false);
		assertFalse(this.userService.userIsFollowing(CulturalOfferConstants.ID_TWO));
	}
	
	@Test
	public void testUserIsFollowingExisting() {
		this.setAuthentication(false);
		assertTrue(this.userService.userIsFollowing(CulturalOfferConstants.ID_ONE));
	}
	
	private User testingUser() {
		User user = new User();
		user.setEmail(UserConstants.NON_EXISTING_EMAIL);
		user.setPassword(UserConstants.LOGIN_PASSWORD);
		user.setFirstName(UserConstants.FIRST_NAME_ONE);
		user.setLastName(UserConstants.LAST_NAME_ONE);
		return user;
	}
	
	public void setAuthentication(boolean admin) {
		User user = this.userRepository.findById(UserConstants.ID_TWO).orElse(null);
		user.setAuthorities(Set.of(new Authority()));
		if (admin) {
			user.getAuthority().setName(Constants.ADMIN_AUTHORITY);
		}
		else {
			user.getAuthority().setName(Constants.GUEST_AUTHORITY);			
		}
		String token = this.tokenUtils.generateToken(user.getUsername());
		AuthToken authToken = new AuthToken(user, token);
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
}
