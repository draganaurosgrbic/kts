package com.example.demo.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.UserConstants;
import com.example.demo.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testFindByEmailExisting() {
		User user = 
				this.userRepository
				.findByEmail(UserConstants.EMAIL_ONE);
		assertNotNull(user);
		assertEquals(UserConstants.ID_ONE, user.getId());
		assertEquals(UserConstants.EMAIL_ONE, user.getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, user.getLastName());
	}
	
	@Test
	public void testFindByEmailNonExisting() {
		User user = 
				this.userRepository
				.findByEmail(UserConstants.NON_EXISTING_EMAIL);
		assertNull(user);
	}
	
	@Test
	public void testHasEmailNewUserNonExisting() {
		User user = 
				this.userRepository
				.hasEmail(null, UserConstants.NON_EXISTING_EMAIL);
		assertNull(user);
	}
	
	@Test
	public void testHasEmailNewUserExisting() {
		User user = 
				this.userRepository
				.hasEmail(null, UserConstants.EMAIL_ONE);
		assertNotNull(user);
	}
	
	@Test
	public void testHasEmailOldUserOwnEmail() {
		User user = 
				this.userRepository
				.hasEmail(UserConstants.ID_ONE, UserConstants.EMAIL_ONE);
		assertNull(user);
	}
	
	@Test
	public void testHasEmailOldUserNonExisting() {
		User user = 
				this.userRepository
				.hasEmail(UserConstants.ID_ONE, UserConstants.NON_EXISTING_EMAIL);
		assertNull(user);
	}
	
	@Test
	public void testHasEmailOldUserExisting() {
		User user = 
				this.userRepository
				.hasEmail(UserConstants.ID_ONE, UserConstants.EMAIL_TWO);
		assertNotNull(user);
	}

}
