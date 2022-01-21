package com.social.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Forgot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String email;
	
	private String otp;

	public Forgot() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Forgot(int id, String email, String otp) {
		super();
		this.id = id;
		this.email = email;
		this.otp = otp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "AccountActivation [id=" + id + ", email=" + email + ", otp=" + otp + "]";
	}
	
}
