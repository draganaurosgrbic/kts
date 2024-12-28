package com.example.demo.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.AccountActivationConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.model.AccountActivation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountActivationRepositoryTest {

	@Autowired
	private AccountActivationRepository accountActivationRepository;
	
	@Test
	public void testFindByCodeExisting() {
		AccountActivation accountActivation = 
				this.accountActivationRepository
				.findByCode(AccountActivationConstants.CODE_ONE);
		assertNotNull(accountActivation);
		assertEquals(AccountActivationConstants.ID_ONE, accountActivation.getId());
		assertEquals(AccountActivationConstants.CODE_ONE, accountActivation.getCode());
		assertEquals(UserConstants.ID_ONE, accountActivation.getUser().getId());
	}
	
	@Test
	public void testFindByCodeNonExisting() {
		AccountActivation accountActivation = 
				this.accountActivationRepository
				.findByCode(AccountActivationConstants.NON_EXISTING_CODE);
		assertNull(accountActivation);
	}
	
}
