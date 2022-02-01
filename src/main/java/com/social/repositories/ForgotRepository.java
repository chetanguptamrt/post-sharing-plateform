package com.social.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.entities.Forgot;

public interface ForgotRepository extends JpaRepository<Forgot, Integer>{

	public List<Forgot> findAllByEmail(String email);

	public boolean existsByEmailAndOtp(String email, String otp);

}
