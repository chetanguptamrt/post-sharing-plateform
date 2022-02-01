package com.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.social.entities.User;
import com.social.entities.UserData;
import com.social.services.ProfileService;

@Controller
public class ProfileController {

	@Autowired
	private ProfileService profileService;
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profileWithoutUsername(Principal principal) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		return "redirect:/u/"+userByEmail.getUserName();
	}
	
	@RequestMapping(value = "/u/{username}", method = RequestMethod.GET)
	public String profile(@PathVariable("username") String username,Principal principal, Model model) {
		int followField = 2;
		if(principal!=null) {
			String name = principal.getName();
			User userByEmail = this.profileService.getUserByEmail(name);
			UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
			String profileImagePath = userDataByUser.getProfileImagePath();
			model.addAttribute("user", userByEmail);
			model.addAttribute("userProfile", profileImagePath);
			if(userByEmail.getUserName().equals(username.toLowerCase().trim())) {
				followField = 1;
			}
		}
		User userByUserName = this.profileService.getUserByUserName(username);
		if(userByUserName==null) {
			model.addAttribute("message", "User not found!!");
			return "error/msg";
		}
		model.addAttribute("title", "Post Sharing");
		model.addAttribute("profileUser", userByUserName);
		UserData userDataByUser2 = this.profileService.getUserDataByUser(userByUserName);
		String profileImagePath2 = userDataByUser2.getProfileImagePath();
		model.addAttribute("profileUserProfile", profileImagePath2);
		model.addAttribute("followOption", followField);
		return "user/profile";
	}
	
}
