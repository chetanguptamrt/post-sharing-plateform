package com.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.social.entities.User;
import com.social.entities.UserData;
import com.social.services.ProfileService;

@Controller
public class SettingsController {

	@Autowired
	private ProfileService profileService;

	@RequestMapping(value = "/setting/account", method = RequestMethod.GET)
	public String accountSetting(Principal principal, Model model) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		
		
		return "user/setting/account";
	}
	
	@RequestMapping(value = "/setting/change-password", method = RequestMethod.GET)
	public String changePasswordSetting(Principal principal, Model model) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		
		
		return "user/setting/changePassword";
	}
	
	@RequestMapping(value = "/setting/like-post", method = RequestMethod.GET)
	public String likePostSetting(Principal principal, Model model) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		
		
		return "user/setting/likePost";
	}
	
}
