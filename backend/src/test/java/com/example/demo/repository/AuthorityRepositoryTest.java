package com.example.demo.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.AuthorityConstants;
import com.example.demo.model.Authority;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorityRepositoryTest {
		
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Test
	public void testFindByNameExisting() {
		Authority authority = 
				this.authorityRepository
				.findByName(AuthorityConstants.NAME_ONE);
		assertNotNull(authority);
		assertEquals(AuthorityConstants.ID_ONE, authority.getId());
		assertEquals(AuthorityConstants.NAME_ONE, authority.getName());
	}

	@Test
	public void testFindByNameNonExisting() {
		Authority authority = 
				this.authorityRepository
				.findByName(AuthorityConstants.NON_EXISTING_NAME);
		assertNull(authority);
	}
	
}
