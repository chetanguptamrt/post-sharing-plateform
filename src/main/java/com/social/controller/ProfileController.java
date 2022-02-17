package com.social.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.social.entities.Post;
import com.social.entities.User;
import com.social.entities.UserData;
import com.social.services.FollowService;
import com.social.services.PostService;
import com.social.services.ProfileService;

@Controller
public class ProfileController {

	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private PostService postService;
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profileWithoutUsername(Principal principal) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		return "redirect:/u/"+userByEmail.getUserName();
	}
	
	/*
	 * Cases to see user profile
	 * if login
	 * 		if follow
	 * 			show post, followers, about (unfollow, about)
	 * 		else if followRequest
	 * 			nothing (cancelRequest)
	 * 		else if not follow
	 * 			if public
	 * 				show post, followers, about (follow, about)
	 * 			else if private
	 * 				nothing (follow)
	 * 		else if login==profileuser
	 * 			show post, followers, about (about, settings)
	 * else not login
	 * 		if public
	 * 			show post (follow)
	 * 		else private
	 * 			nothing (follow)
	*/
	@RequestMapping(value = "/u/{username}", method = RequestMethod.GET)
	public String profile(@PathVariable("username") String username,Principal principal, Model model) {
		int followField = 4;
		boolean showPost = false; 
		User userByUserName = this.profileService.getUserByUserName(username);
		UserData userDataByUserName = this.profileService.getUserDataByUser(userByUserName);
		if(userByUserName==null) {
			model.addAttribute("title", "Post Sharing");
			model.addAttribute("message", "User not found!!");
			return "error/msg";
		}
		if(principal!=null) {
			String name = principal.getName();
			User userByEmail = this.profileService.getUserByEmail(name);
			UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
			String profileImagePath = userDataByUser.getProfileImagePath();
			model.addAttribute("user", userByEmail);
			model.addAttribute("userProfile", profileImagePath);
			
			if(this.followService.ifFollowed(userByEmail, userByUserName)) {
				showPost = true;
				followField = 1;
			} else if(this.followService.isSendRequest(userByEmail, userByUserName)) {
				followField = 2;
			} else if(userByEmail.getUserName().equals(username.toLowerCase().trim())) {
				followField = 5;
				showPost = true;
			} else if(!this.followService.ifFollowed(userByEmail, userByUserName)) {
				if(userDataByUserName.isAccountMode()) {
					showPost = true;
					followField = 3;
				} else {
					followField = 4;
				}
			}
		} else {
			if(userDataByUserName.isAccountMode()) {
				followField = 4;
				showPost = true;
			} else {
				followField = 4;
				showPost = false;
			}
		}
		
		model.addAttribute("title", "Post Sharing");
		model.addAttribute("profileUser", userByUserName);
		model.addAttribute("profileUserData", userDataByUserName);
		model.addAttribute("followOption", followField);
		model.addAttribute("showPost", showPost);
		if(showPost) {
			List<Post> posts = this.postService.getPostByUser(userByUserName);
			model.addAttribute("posts", posts);
		}
		model.addAttribute("totalPost", this.postService.countUserPost(userByUserName));
		model.addAttribute("totalFollowers", this.followService.countUserFollowers(userByUserName));
		model.addAttribute("totalFollowing", this.followService.countUserFollowing(userByUserName));
		return "user/profile";
	}
	
}
