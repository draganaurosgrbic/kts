package com.example.demo.service.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.AccountActivationConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.model.AccountActivation;
import com.example.demo.model.User;
import com.example.demo.repository.AccountActivationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccountActivationService;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountActivationServiceTest {
	
	@Autowired
	private AccountActivationService accountActivationService;
	
	@MockBean
	private AccountActivationRepository accountActivationRepository;
	
	@MockBean
	private UserRepository userRepository;
	
	@Test
	public void testActivateExisting() {
		AccountActivation accountActivation = new AccountActivation();
		User user = new User();
		accountActivation.setUser(user);
		Mockito.when(this.accountActivationRepository.findByCode(AccountActivationConstants.CODE_ONE))
		.thenReturn(accountActivation);
		Mockito.when(this.userRepository.save(user))
		.thenReturn(user);
		assertTrue(this.accountActivationService.activate(AccountActivationConstants.CODE_ONE).isEnabled());
	}
	
	@Test(expected = NullPointerException.class)
	public void testActivateNonExisting() {
		Mockito.when(this.accountActivationRepository.findByCode(AccountActivationConstants.NON_EXISTING_CODE))
		.thenReturn(null);
		this.accountActivationService.activate(AccountActivationConstants.NON_EXISTING_CODE);
	}
	
	@Test
	public void testSaveValid() {
		Mockito.when(this.accountActivationRepository.count())
		.thenReturn((long) MainConstants.ONE_SIZE);
		long size = this.accountActivationRepository.count();
		User user = new User();
		user.setEmail(UserConstants.EMAIL_ONE);
		user.setFirstName(UserConstants.FIRST_NAME_ONE);
		user.setLastName(UserConstants.LAST_NAME_ONE);
		AccountActivation accountActivation = new AccountActivation(user);
		Mockito.when(this.accountActivationRepository.save(any(AccountActivation.class)))
		.thenReturn(accountActivation);
		accountActivation = this.accountActivationService.save(user);
		Mockito.when(this.accountActivationRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1,this.accountActivationRepository.count());
		assertEquals(UserConstants.EMAIL_ONE, accountActivation.getUser().getEmail());
		assertEquals(UserConstants.FIRST_NAME_ONE, accountActivation.getUser().getFirstName());
		assertEquals(UserConstants.LAST_NAME_ONE, accountActivation.getUser().getLastName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveNullUser() {
		Mockito.when(this.accountActivationRepository.save(any(AccountActivation.class)))
		.thenThrow(ConstraintViolationException.class);
		this.accountActivationService.save(null);
	}

}
