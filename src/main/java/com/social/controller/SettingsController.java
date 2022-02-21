package com.social.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.social.entities.User;
import com.social.entities.UserData;
import com.social.services.ProfileService;
import com.social.services.SettingService;

@Controller
public class SettingsController {

	@Autowired
	private ProfileService profileService;

	@Autowired
	private SettingService settingService;
	
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting() {
		return "redirect:/setting/account";
	}
	
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
	
	@ResponseBody
	@RequestMapping(value = "/setting/update-account", method = RequestMethod.POST)
	public String updateAccountInfo(
			@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "bio", required = false) String bio,
			@RequestParam(value = "gender", required = true) String gender,
			@RequestParam(value = "dob", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,
			Principal principal) {
		String name = principal.getName();
		User user = this.profileService.getUserByEmail(name);
		if(firstName.trim().equals("") || lastName.trim().equals("") || userName.trim().equals("")) {
			return "fillDetail";
		}
		if(this.profileService.existsUserName(userName) && !user.getUserName().equals(userName)) {
			return "notUsername";
		}
		String done = this.settingService.updateProfile(user, firstName, lastName, userName, phone, bio, gender, dob);
		return done;
	}
	
	@ResponseBody
	@RequestMapping(value = "/setting/check-username", method = RequestMethod.POST)
	public String checkUser(@RequestParam("username") String username, Principal principal) {
		String name = principal.getName();
		User user = this.profileService.getUserByEmail(name);
		boolean f = this.profileService.checkUserNameAvailableOrNot(username, user);
		if(f) {
			return "no";
		} else {
			return "done";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/setting/update-visibility", method = RequestMethod.POST)
	public String updateVisibility(
			@RequestParam("accountMode") boolean accountMode,
			@RequestParam("showEmail") boolean showEmail,
			@RequestParam("showPhone") boolean showPhone,
			@RequestParam("showDOB") boolean showDOB,
			Principal principal) {
		String name = principal.getName();
		User user = this.profileService.getUserByEmail(name);
		String doneString = this.settingService.updateVisibility(user, accountMode, showEmail, showPhone, showDOB);
		return doneString;
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
	
	@RequestMapping(value = "/setting/update-password", method = RequestMethod.POST)
	public String updatePassword(@RequestParam("oPassword") String oldPassword,@RequestParam("nPassword") String newPassword, Principal principal) {
		String name = principal.getName();
		User user = this.profileService.getUserByEmail(name);
		String done = this.settingService.updatePassword(user, oldPassword, newPassword);
		return done;
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
