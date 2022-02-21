package com.social.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.social.entities.Follow;
import com.social.entities.FollowRequest;
import com.social.entities.User;
import com.social.entities.UserData;
import com.social.repositories.FollowRepository;
import com.social.repositories.FollowRequestRepository;
import com.social.repositories.UserDataRepository;
import com.social.repositories.UserRepository;

@Service
public class SettingService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDataRepository userDataRepository;
	
	@Autowired
	private FollowRequestRepository followRequestRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	public String updatePassword(User user, String oldPassword, String newPassword) {
		if(this.passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(newPassword);
			this.userRepository.save(user);
			return "done";
		} else {
			return "invalid";
		}
	}

	public String updateProfile(User user, String firstName, String lastName, String userName, String phone, String bio,
			String gender, Date dob) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(userName);
		UserData userData = user.getUserData();
		userData.setBio(bio);
		userData.setDateOfBirth(dob);
		userData.setGender(gender);
		userData.setPhone(phone);
		this.userRepository.save(user);
		this.userDataRepository.save(userData);
		return "done";
	}

	public String updateVisibility(User user, boolean accountMode, boolean showEmail, boolean showPhone,
			boolean showDOB) {
		if(accountMode) {
			List<FollowRequest> byUser = this.followRequestRepository.getByUser(user);
			byUser.forEach(fr->{
				Follow follow = new Follow();
				follow.setFollowByUser(fr.getUserWhoSend());
				follow.setFollowedToUser(fr.getUser());
				this.followRepository.save(follow);
				this.followRequestRepository.deleteById(fr.getId());
			});
		}
		UserData userData = user.getUserData();
		userData.setAccountMode(accountMode);
		userData.setShowDateOfBirth(showDOB);
		userData.setShowEmail(showEmail);
		userData.setShowPhone(showPhone);
		this.userDataRepository.save(userData);
		return "done";
	}
	
	
}
