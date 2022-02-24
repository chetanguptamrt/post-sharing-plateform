package com.social.services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.social.entities.AccountActivation;
import com.social.entities.Forgot;
import com.social.entities.User;
import com.social.entities.UserData;
import com.social.helper.SearchUser;
import com.social.repositories.AccountActivationRepository;
import com.social.repositories.ForgotRepository;
import com.social.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountActivationRepository accountActivationRepository;
	
	@Autowired
	private ForgotRepository forgotRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User getUserByEmailOrUsername(String username) {
		return this.userRepository.getUserByEmailOrUserName(username, username);
	}
	
	public String userSendRegistrationOtp(String email) {
		if(!this.userRepository.existsByEmail(email)){
			this.accountActivationRepository.findAllByEmail(email).forEach(active -> {
				this.accountActivationRepository.delete(active);
			});
			String otp = this.getRandom();
			String body = "Hello,\n\n"
                    + "Your verification code is : "+otp+"\n"
                    + "If you are having any issue with your account, please don't hesitate to contact us.\n\n"
                    + "Thanks!\n"
                    + "Post Sharing";
			this.mailService.send(email, "One time password | Registration", body);
			AccountActivation AccountActivation = new AccountActivation();
			AccountActivation.setEmail(email);
			AccountActivation.setOtp(otp);
			this.accountActivationRepository.save(AccountActivation);
			return "done";
		} else {
			return "userAlready";
		}
	}

	public String userRegistration(User user, String otp) {
		if(!this.userRepository.existsByEmail(user.getEmail().trim())){
			if(this.accountActivationRepository.existsByEmailAndOtp(user.getEmail().trim(), otp)) {
				this.accountActivationRepository.findAllByEmail(user.getEmail().trim()).forEach(active -> {
					this.accountActivationRepository.delete(active);
				});
				user.setActive(true);
				user.setJoiningDate(new Date());
				user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
				user.setRole("ROLE_USER");
				this.userRepository.save(user);
				user.setUserName(user.getFirstName().toLowerCase().trim()+user.getId());
				UserData userData = new UserData();
				userData.setAccountMode(true);
				userData.setGender("no");
				userData.setProfileImagePath("default.jpg");
				userData.setPhone("");
				userData.setShowDateOfBirth(false);
				userData.setShowEmail(false);
				userData.setShowPhone(false);
				userData.setBio("");
				userData.setUser(user);
				user.setUserData(userData);
				this.userRepository.save(user);
				return "done";
			} else {
				return "notMatched";
			}
		} else {
			return "userAlready";
		}
	}

	public String sendOtpForForgot(String email) {
		if(this.userRepository.existsByEmail(email)){
			this.forgotRepository.findAllByEmail(email).forEach(active -> {
				this.forgotRepository.delete(active);
			});
			String otp = this.getRandom();
			String body = "Hello,\n\n"
                    + "Your verification code is : "+otp+"\n"
                    + "If you are having any issue with your account, please don't hesitate to contact us.\n\n"
                    + "Thanks!\n"
                    + "Post Sharing";
			this.mailService.send(email, "One time password | Forgot Password", body);
			Forgot forgot = new Forgot();
			forgot.setEmail(email);
			forgot.setOtp(otp);
			this.forgotRepository.save(forgot);
			return "done";
		} else {
			return "noUser";
		}
	}

	public String forgotPassword(String email, String otp, String password) {
		if(this.userRepository.existsByEmail(email)){
			if(this.forgotRepository.existsByEmailAndOtp(email, otp)) {
				this.forgotRepository.findAllByEmail(email).forEach(active -> {
					this.forgotRepository.delete(active);
				});
				User user = this.userRepository.getUserByEmail(email);
				user.setPassword(this.bCryptPasswordEncoder.encode(password));
				this.userRepository.save(user);
				return "done";	
			} else {
				return "notMatched";
			}
		} else {
			return "noUser";
		}
	}

	private String getRandom() {
		return String.valueOf((int) (Math.random()*(999999-111111+1)+111111));
	}

	public List<SearchUser> searchUser(String search) {
		List<User> list = this.userRepository.findTop10ByFirstNameOrLastNameOrUserNameContaining(search, search, search);
		List<SearchUser> sUsers = new LinkedList<SearchUser>();
		list.forEach(u->{
			SearchUser sUser = new SearchUser();
			sUser.setName(u.getFirstName()+" "+u.getLastName());
			sUser.setProfile(u.getUserData().getProfileImagePath());
			sUser.setUsername(u.getUserName());
			sUsers.add(sUser);
		});
		return sUsers;
	}
	
}
