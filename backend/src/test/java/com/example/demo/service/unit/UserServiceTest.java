package com.example.demo.service.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.Constants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Authority;
import com.example.demo.model.User;
import com.example.demo.model.UserFollowing;
import com.example.demo.repository.UserFollowingRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthToken;
import com.example.demo.security.TokenUtils;
import com.example.demo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private UserFollowingRepository userFollowingRepository;

	@Autowired
	private TokenUtils tokenUtils;
		
	@Test
	public void testloadUserByUsernameExisting() {
		User user = new User();
		user.setId(UserConstants.ID_ONE);
		user.setEmail(UserConstants.EMAIL_ONE);
		user.setFirstName(UserConstants.FIRST_NAME_ONE);
		user.setLastName(UserConstants.LAST_NAME_ONE);
		Mockito.when(this.userRepository.findByEmail(UserConstants.EMAIL_ONE))
		.thenReturn(user);
		user = (User) this.userService.loadUserByUsername(UserConstants.EMAIL_ONE);
		assertNotNull(user);
		assertEquals(UserConstants.ID_ONE, user.getId());
		assertEquals(UserConstants.EMAIL_ONE, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test
	public void testloadUserByUsernameNonExisting() {
		Mockito.when(this.userRepository.findByEmail(UserConstants.NON_EXISTING_EMAIL))
		.thenReturn(null);
		User user = (User) this.userService.loadUserByUsername(UserConstants.NON_EXISTING_EMAIL);
		assertNull(user);
	}
	
	@Test
	public void testHasEmailNewUserNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(UserConstants.NON_EXISTING_EMAIL);
		Mockito.when(this.userRepository.hasEmail(param.getId(), param.getName()))
		.thenReturn(null);
		assertFalse(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailNewUserExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(UserConstants.EMAIL_ONE);
		Mockito.when(this.userRepository.hasEmail(param.getId(), param.getName()))
		.thenReturn(new User());
		assertTrue(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailOldUserOwnEmail() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.EMAIL_ONE);
		Mockito.when(this.userRepository.hasEmail(param.getId(), param.getName()))
		.thenReturn(null);
		assertFalse(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailOldUserNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.NON_EXISTING_EMAIL);
		Mockito.when(this.userRepository.hasEmail(param.getId(), param.getName()))
		.thenReturn(null);
		assertFalse(this.userService.hasEmail(param));
	}
	
	@Test
	public void testHasEmailOldUserExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(UserConstants.ID_ONE);
		param.setName(UserConstants.EMAIL_TWO);
		Mockito.when(this.userRepository.hasEmail(param.getId(), param.getName()))
		.thenReturn(new User());
		assertTrue(this.userService.hasEmail(param));
	}
	
	@Test
	public void testAddValid() {
		Mockito.when(this.userRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.userRepository.count();
		User user = this.testingUser();
		Mockito.when(this.userRepository.save(user))
		.thenReturn(user);
		user = this.userService.save(user, null);
		Mockito.when(this.userRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.userRepository.count());
		assertEquals(UserConstants.NON_EXISTING_EMAIL, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullEmail() {
		User user = this.testingUser();
		user.setEmail(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyEmail() {
		User user = this.testingUser();
		user.setEmail("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankEmail() {
		User user = this.testingUser();
		user.setEmail("  ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddInvalidEmail() {
		User user = this.testingUser();
		user.setEmail(UserConstants.NEW_PASSWORD);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testAddNonUniqueEmail() {
		User user = this.testingUser();
		user.setEmail(UserConstants.EMAIL_ONE);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(DataIntegrityViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullPassword() {
		User user = this.testingUser();
		user.setPassword(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyPassword() {
		User user = this.testingUser();
		user.setPassword("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankPassword() {
		User user = this.testingUser();
		user.setPassword(" ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullFirstName() {
		User user = this.testingUser();
		user.setFirstName(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyFirstName() {
		User user = this.testingUser();
		user.setFirstName("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankFirstName() {
		User user = this.testingUser();
		user.setFirstName("  ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullLastName() {
		User user = this.testingUser();
		user.setLastName(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyLastName() {
		User user = this.testingUser();
		user.setLastName("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlanklLastName() {
		User user = this.testingUser();
		user.setLastName("  ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test
	public void testUpdateValid() {
		Mockito.when(this.userRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.userRepository.count();
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		Mockito.when(this.userRepository.save(user))
		.thenReturn(user);
		user = this.userService.save(user, null);
		assertEquals(size, this.userRepository.count());
		assertEquals(UserConstants.ID_ONE, user.getId());
		assertEquals(UserConstants.NON_EXISTING_EMAIL, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail("  ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateInvalidEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail(UserConstants.NEW_PASSWORD);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testUpdateNonUniqueEmail() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setEmail(UserConstants.EMAIL_TWO);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(DataIntegrityViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullPassword() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setPassword(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyPassword() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setPassword("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankPassword() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setPassword(" ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullFirstName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setFirstName(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyFirstName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setFirstName("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankFirstName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setFirstName("  ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullLastName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setLastName(null);
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyLastName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setLastName("");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlanklLastName() {
		User user = this.testingUser();
		user.setId(UserConstants.ID_ONE);
		user.setLastName("  ");
		Mockito.when(this.userRepository.save(user))
		.thenThrow(ConstraintViolationException.class);
		this.userService.save(user, null);
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
		Mockito.when(this.userFollowingRepository.findByUserIdAndCulturalOfferId(UserConstants.ID_TWO, CulturalOfferConstants.ID_TWO))
		.thenReturn(null);
		assertFalse(this.userService.userIsFollowing(CulturalOfferConstants.ID_TWO));
	}
	
	@Test
	public void testUserIsFollowingExisting() {
		this.setAuthentication(false);
		Mockito.when(this.userFollowingRepository.findByUserIdAndCulturalOfferId(UserConstants.ID_TWO, CulturalOfferConstants.ID_ONE))
		.thenReturn(new UserFollowing());
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
		User user = new User();
		user.setId(UserConstants.ID_TWO);
		user.setEmail(UserConstants.EMAIL_TWO);
		user.setFirstName(UserConstants.FIRST_NAME_TWO);
		user.setLastName(UserConstants.LAST_NAME_TWO);
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
