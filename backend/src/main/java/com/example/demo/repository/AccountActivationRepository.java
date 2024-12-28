package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AccountActivation;

@Repository
public interface AccountActivationRepository extends JpaRepository<AccountActivation, Long> {
	
	public AccountActivation findByCode(String code);
	
}
