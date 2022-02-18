package com.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.social.entities.User;
import com.social.entities.UserData;
import com.social.services.FollowService;
import com.social.services.ProfileService;
import com.social.services.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private FollowService followService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Principal principal, Model model) {
		if(principal == null) {
			return "public/signin";
		}
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("title", "Home | Post Sharing");
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		return "user/home";
	}
	
	@RequestMapping(value = "/people", method = RequestMethod.GET)
	public String people(@RequestParam(value = "tab", required = false) String tab, Principal principal, Model model) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("title", "People | Post Sharing");
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		if(tab==null) {
			tab= "sent";
		}
		if(!(tab.equals("received") || tab.equals("followers") || tab.equals("following"))) {
			tab = "sent";
		}
		if(tab.equals("sent")) {
			model.addAttribute("list", this.followService.getSentFollowRequests(userByEmail));
		} else if(tab.equals("received")) {
			model.addAttribute("list", this.followService.getReceivedFollowRequests(userByEmail));
		}  else if(tab.equals("followers")) {
			model.addAttribute("list", this.followService.getFollowers(userByEmail));
		}  else if(tab.equals("following")) {
			model.addAttribute("list", this.followService.getFollowing(userByEmail));
		} 
		model.addAttribute("tab", tab);
		return "user/people";
	}
	
	@RequestMapping(value = "/notification", method = RequestMethod.GET)
	public String notification(Principal principal, Model model) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		
		
		return "user/notification";
	}
	
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("title", "Login | Post Sharing");
		return "public/signin";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("title", "Registration | Post Sharing");
		return "public/signup";
	}
	
	@ResponseBody
	@RequestMapping(value = "/registrationOTP", method = RequestMethod.POST)
	public String registrationOTP(@RequestParam("oEmail") String email) {
		if(email.trim().equals("")) {
			return "fillDetail";
		}
		return this.userService.userSendRegistrationOtp(email.trim());
	}

	@ResponseBody
	@RequestMapping(value = "/registrationuser", method = RequestMethod.POST)
	public String registrationuser(@ModelAttribute User user, @RequestParam("otp") String otp) {
		if(otp.trim().equals("") || user.getFirstName().trim().equals("") || user.getLastName().trim().equals("") ||
					user.getEmail().trim().equals("") || user.getPassword().trim().equals("") ) {
			return "fillDetail";
		}
		return this.userService.userRegistration(user,otp);
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.GET)
	public String forgot(Model model) {
		model.addAttribute("title", "Forgot | Post Sharing");
		return "public/forgot";
	}

	@ResponseBody
	@RequestMapping(value = "/sendOtpForgot", method = RequestMethod.POST)
	public String sendOtpForgot(@RequestParam("oEmail") String email) {
		if(email.trim().equals("")) {
			return "fillDetail";
		}
		return this.userService.sendOtpForForgot(email.trim());
	}
	
	@ResponseBody
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public String forgotPassword(@RequestParam("email") String email, 
				@RequestParam("otp") String otp, 
				@RequestParam("password") String password) {
		if(email.trim().equals("") || otp.trim().equals("") || password.trim().equals("")) {
			return "fillDetail";
		}
		return this.userService.forgotPassword(email.trim(), otp.trim(), password.trim());
	}


}
