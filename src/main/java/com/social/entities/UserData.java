package com.social.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "data_of_birth")
	private Date dateOfBirth;
	
	private String phone;
	
	private String gender;
	
	private String bio;
	
	@Column(name = "account_mode")
	private boolean accountMode;
	
	@Column(name = "profile_image_path")
	private String profileImagePath;

	@Column(name = "show_phone")
	private boolean showPhone;
	
	@Column(name = "show_email")
	private boolean showEmail;
	
	@Column(name = "show_date_of_birth")
	private boolean showDateOfBirth;

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user; 
	
	public UserData() {
		super();
	}

	public UserData(int id, Date dateOfBirth, String phone, String gender, String bio, boolean accountMode,
			String profileImagePath, boolean showPhone, boolean showEmail, boolean showDateOfBirth, User user) {
		super();
		this.id = id;
		this.dateOfBirth = dateOfBirth;
		this.phone = phone;
		this.gender = gender;
		this.bio = bio;
		this.accountMode = accountMode;
		this.profileImagePath = profileImagePath;
		this.showPhone = showPhone;
		this.showEmail = showEmail;
		this.showDateOfBirth = showDateOfBirth;
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public boolean isAccountMode() {
		return accountMode;
	}

	public void setAccountMode(boolean accountMode) {
		this.accountMode = accountMode;
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}

	public boolean isShowPhone() {
		return showPhone;
	}

	public void setShowPhone(boolean showPhone) {
		this.showPhone = showPhone;
	}

	public boolean isShowEmail() {
		return showEmail;
	}

	public void setShowEmail(boolean showEmail) {
		this.showEmail = showEmail;
	}

	public boolean isShowDateOfBirth() {
		return showDateOfBirth;
	}

	public void setShowDateOfBirth(boolean showDateOfBirth) {
		this.showDateOfBirth = showDateOfBirth;
	}

	@Override
	public String toString() {
		return "UserData [id=" + id + ", dateOfBirth=" + dateOfBirth + ", phone=" + phone + ", gender=" + gender
				+ ", bio=" + bio + ", accountMode=" + accountMode + ", profileImagePath=" + profileImagePath
				+ ", showPhone=" + showPhone + ", showEmail=" + showEmail + ", showDateOfBirth=" + showDateOfBirth
				+ ", user=" + user + "]";
	}

}
