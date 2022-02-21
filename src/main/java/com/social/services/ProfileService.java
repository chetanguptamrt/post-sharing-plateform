package com.social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.social.entities.User;
import com.social.entities.UserData;
import com.social.repositories.UserDataRepository;
import com.social.repositories.UserRepository;

@Service
public class ProfileService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDataRepository userDataRepository;

	public User getUserByEmail(String email) {
		return this.userRepository.getUserByEmail(email);
	}
	
	public User getUserByUserName(String userName) {
		return this.userRepository.getUserByUserName(userName);
	}
	
	public UserData getUserDataByUser(User user) {
		return this.userDataRepository.getByUser(user);
	}

	public boolean checkUserNameAvailableOrNot(String username, User user) {
		if(username.trim().equals(user.getUserName())) {
			return false;
		}
		return this.userRepository.existsByUserName(username);
	}
	
	public boolean existsUserName(String username) {
		return this.userRepository.existsByUserName(username);
	}
	
}
