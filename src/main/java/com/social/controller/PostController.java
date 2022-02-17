package com.social.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.social.helper.FollowUser;
import com.social.services.FollowService;
import com.social.services.LikeService;
import com.social.services.PostService;
import com.social.services.ProfileService;

@Controller
public class PostController {
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private LikeService likeService;
	
	@RequestMapping(value = "/p/{id}", method = RequestMethod.GET)
	public String viewPost(@PathVariable("id") int id, Principal principal, Model model) {
		model.addAttribute("title", "Post Sharing");
		if(!this.postService.checkPostExistById(id)) {
			model.addAttribute("message", "Post not found");
			return "error/msg";
		}
		Post postById = this.postService.getPostById(id);
		User profileUser = postById.getUser();
		boolean showPost = false;
		if(principal!=null) {
			String name = principal.getName();
			User userByEmail = this.profileService.getUserByEmail(name);
			UserData userDataByUser = this.profileService.getUserDataByUser(userByEmail);
			String profileImagePath = userDataByUser.getProfileImagePath();
			model.addAttribute("user", userByEmail);
			model.addAttribute("userProfile", profileImagePath);
			model.addAttribute("like", this.likeService.checkUserLikesPost(userByEmail, postById));
		}
		if(profileUser.getUserData().isAccountMode()) {
			showPost = true;
		}
		else if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			if(this.followService.ifFollowed(user, profileUser)) {
				showPost = true;
			} else if(user.getId()==postById.getUser().getId()) {
				showPost = true;
			}
		}
		if(showPost) {
			model.addAttribute("count", this.likeService.countByPost(postById));
			model.addAttribute("post", postById);
			model.addAttribute("profileUser", profileUser);
			String profileImagePath = profileUser.getUserData().getProfileImagePath();
			model.addAttribute("profileUserProfile", profileImagePath);
			return "user/post/view";
		} else {
			model.addAttribute("message", "Post not found");
			return "error/msg";
		}
	}

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
				model.addAttribute("title", "Post Sharing");
				model.addAttribute("message", "Post Not Found!!");
				return "error/msg";
			}
			model.addAttribute("title", "Edit Post | Post Sharing");
			model.addAttribute("post", post);
			return "user/post/editPost";	
		} catch (Exception e) {
			model.addAttribute("title", "Post Sharing");
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
	
	@ResponseBody
	@RequestMapping(value = "/post/remove", method = RequestMethod.POST)
	public String deletePost(@RequestParam("id") String pId, Principal principal) {
		try {
			int id = Integer.parseInt(pId);
			String name = principal.getName();
			User user = this.profileService.getUserByEmail(name);
			if(user==null) {
				return "no";
			}
			String doneString = this.postService.deletePost(user, id);
			return doneString;	
		} catch (Exception e) {
			return "no";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/post/like", method = RequestMethod.POST)
	public String likePost(@RequestParam("id") String pId, Principal principal) {
		try {
			int id = Integer.parseInt(pId);
			if(principal!=null) {
				String name = principal.getName();
				User user = this.profileService.getUserByEmail(name);
				Post post = this.postService.getPostById(id);
				User profileUser = post.getUser();
				if(this.followService.ifFollowed(user, profileUser) || profileUser.getUserData().isAccountMode() || user.getId()==profileUser.getId()) {
					String done = this.likeService.likePost(user,post);
					return done;
				}
				return "no";
			} else {
				return "notLogin";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "no";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/post/likes", method = RequestMethod.POST)
	public ResponseEntity<List<FollowUser>> getUserFollowers(Principal principal, @RequestParam("id") int pId) {
		if(principal!=null) {
			User user = this.profileService.getUserByEmail(principal.getName());
			Post post = this.postService.getPostById(pId);
			User profileUser = post.getUser();
			if(this.followService.ifFollowed(user, profileUser) || profileUser.getUserData().isAccountMode() || user.getId()==profileUser.getId()) {
				List<FollowUser> list = this.likeService.getPostLikes(post);
				return ResponseEntity.ok(list);
			}
		}
		return ResponseEntity.ok(List.of());
	}
}
