package com.social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.social.entities.Like;
import com.social.entities.Post;
import com.social.entities.User;
import com.social.repositories.LikeRepository;

@Service
public class LikeService {

	@Autowired
	private LikeRepository likeRepository;
	
	public boolean checkUserLikesPost(User user, Post post) {
		return this.likeRepository.existsByUserAndPost(user, post);
	}

	public String likePost(User user, Post post) {
		if(checkUserLikesPost(user, post)) {
			Like byUserAndPost = this.likeRepository.getByUserAndPost(user, post);
			this.likeRepository.deleteById(byUserAndPost.getId());
			return "dislike";
		} else {
			Like like = new Like();
			like.setPost(post);
			like.setUser(user);
			this.likeRepository.save(like);
			return "done";
		}
	}

	public long countByPost(Post post) {
		return this.likeRepository.countByPost(post);
	}
	
}