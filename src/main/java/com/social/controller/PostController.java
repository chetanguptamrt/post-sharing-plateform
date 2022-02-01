package com.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.social.entities.Post;
import com.social.entities.User;
import com.social.entities.UserData;
import com.social.services.PostService;
import com.social.services.ProfileService;

@Controller
public class PostController {
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PostService postService;

	@RequestMapping(value = "/post/add", method = RequestMethod.GET)
	public String addPost(Model model, Principal principal) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		model.addAttribute("title", "Add Post | Post Sharing");
		return "user/post/addPost";
	}

	@RequestMapping(value = "/post/edit/{id}", method = RequestMethod.GET)
	public String editPost(@PathVariable("id") String sId, Model model, Principal principal) {
		String name = principal.getName();
		User userByEmail = this.profileService.getUserByEmail(name);
		UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
		String profileImagePath = userDataByUser.getProfileImagePath();
		model.addAttribute("user", userByEmail);
		model.addAttribute("userProfile", profileImagePath);
		try {
			int id = Integer.parseInt(sId);
			Post post = this.postService.getPostByIdAndUser(id, userByEmail);
			if(post==null) {
				model.addAttribute("message", "Post Not Found!!");
				return "error/msg";
			}
			model.addAttribute("title", "Edit Post | Post Sharing");
			model.addAttribute("post", post);
			return "user/post/editPost";	
		} catch (Exception e) {
			model.addAttribute("message", "Post Not Found!!");
			return "error/msg";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/post/upload", method = RequestMethod.POST)
	public String uploadPost(Principal principal, @RequestParam("caption") String caption, @RequestParam("media") MultipartFile file) {
		String name = principal.getName();
		User user = this.profileService.getUserByEmail(name);
		if(user==null) {
			return "no";
		}
		String doneString = this.postService.uploadPost(user, caption, file);
		return doneString;
	}
	
	@ResponseBody
	@RequestMapping(value = "/post/update", method = RequestMethod.POST)
	public String updatePost(@RequestParam(name = "caption") String caption, @RequestParam("id") String sId, Principal principal) {
		try {
			int id = Integer.parseInt(sId);
			String name = principal.getName();
			User user = this.profileService.getUserByEmail(name);
			if(user==null) {
				return "no";
			}
			String doneString = this.postService.editPost(user, caption, id);
			return doneString;	
		} catch (Exception e) {
			return "no";
		}
	}
	
}
