package com.example.demo.service.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.AccountActivationConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.model.AccountActivation;
import com.example.demo.model.User;
import com.example.demo.repository.AccountActivationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccountActivationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountActivationServiceTest {
	
	@Autowired
	private AccountActivationService accountActivationService;
	
	@Autowired
	private AccountActivationRepository accountActivationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testActivateExisting() {
		this.accountActivationService.activate(AccountActivationConstants.CODE_ONE);
		assertTrue(this.accountActivationService.activate(AccountActivationConstants.CODE_ONE).isEnabled());
	}
	
	@Test(expected = NullPointerException.class)
	@Transactional
	@Rollback(true)
	public void testActivateNonExisting() {
		this.accountActivationService.activate(AccountActivationConstants.NON_EXISTING_CODE);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testSaveValid() {
		long size = this.accountActivationRepository.count();
		User user = this.userRepository.findById(UserConstants.ID_ONE).orElse(null);
		AccountActivation accountActivation = this.accountActivationService.save(user);
		assertEquals(size + 1,this.accountActivationRepository.count());
		assertEquals(UserConstants.EMAIL_ONE, accountActivation.getUser().getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, accountActivation.getUser().getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, accountActivation.getUser().getLastName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testSaveNullUser() {
		this.accountActivationService.save(null);
	}

}
