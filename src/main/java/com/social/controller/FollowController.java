package com.social.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.social.entities.User;
import com.social.helper.FollowUser;
import com.social.services.FollowService;
import com.social.services.ProfileService;

@Controller
public class FollowController {
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private FollowService followService;

	@ResponseBody
	@RequestMapping(value = "/get-user/followers", method = RequestMethod.POST)
	public ResponseEntity<List<FollowUser>> getUserFollowers(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			if(this.followService.ifFollowed(user, profileUser) || profileUser.getUserData().isAccountMode()) {
				List<FollowUser> list = this.followService.getUserFollowers(profileUser);
				return ResponseEntity.ok(list);
			}
		}
		return ResponseEntity.ok(List.of());
	}

	@ResponseBody
	@RequestMapping(value = "/get-user/following", method = RequestMethod.POST)
	public ResponseEntity<List<FollowUser>> getUserFollowing(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			if(this.followService.ifFollowed(user, profileUser) || profileUser.getUserData().isAccountMode()) {
				List<FollowUser> list = this.followService.getUserFollowing(profileUser);
				return ResponseEntity.ok(list);
			}
		}
		return ResponseEntity.ok(List.of());
	}
	
	@ResponseBody
	@RequestMapping(value = "/follow/do", method = RequestMethod.POST)
	public String doFollow(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			if(user.getId()==profileUser.getId()) 
				return "no";
			String done = this.followService.doFollow(user,profileUser);
			return done;
		} else {
			return "notLogin";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/follow/cancel", method = RequestMethod.POST)
	public String concelFollowRequest(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			String done = this.followService.cancelFollowRequest(user,profileUser);
			return done;
		} else {
			return "notLogin";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/follow/accept", method = RequestMethod.POST)
	public String acceptFollowRequest(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			String done = this.followService.acceptFollowRequest(user,profileUser);
			return done;
		} else {
			return "notLogin";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/follow/decline", method = RequestMethod.POST)
	public String declineFollowRequest(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			String done = this.followService.declineFollowRequest(user,profileUser);
			return done;
		} else {
			return "notLogin";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/follow/remove", method = RequestMethod.POST)
	public String doUnfollow(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			String done = this.followService.doUnfollow(user,profileUser);
			return done;
		} else {
			return "notLogin";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/follow/remove-follower", method = RequestMethod.POST)
	public String removeFollowers(Principal principal, @RequestParam("username") String username) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			User profileUser = this.profileService.getUserByUserName(username);
			String done = this.followService.removeFollower(user,profileUser);
			return done;
		} else {
			return "notLogin";
		}
	}
	
}
