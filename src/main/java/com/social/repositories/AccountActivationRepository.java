package com.social.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.entities.AccountActivation;

public interface AccountActivationRepository extends JpaRepository<AccountActivation, Integer>{

	public List<AccountActivation> findAllByEmail(String email);

	boolean existsByEmailAndOtp(String trim, String otp);

}
