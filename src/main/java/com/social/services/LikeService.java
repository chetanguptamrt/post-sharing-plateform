package com.social.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.social.entities.Like;
import com.social.entities.Post;
import com.social.entities.User;
import com.social.helper.FetchPost;
import com.social.helper.FollowUser;
import com.social.helper.TempPost;
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

	public List<FollowUser> getPostLikes(Post post) {
		List<Like> list = this.likeRepository.getByPost(post);
		List<FollowUser> reverseList = new LinkedList<FollowUser>();
		for(int i=list.size()-1; i>=0; i--) {
			User user = list.get(i).getUser();
			reverseList.add(new FollowUser(user.getFirstName()+" "+user.getLastName(),
							user.getUserData().getProfileImagePath(),
							user.getUserName()));
		}
		return reverseList;
	}

	public Page<Like> getLikesByUser(User user, int i) {
		Pageable pageable = (Pageable) PageRequest.of(i, 10);
		return this.likeRepository.getByUserOrderByIdDesc(user, pageable);
	}

	public FetchPost getLikePostsByUser(User user, int pageNo) {
		FetchPost fetchPost = new FetchPost();
		Pageable pageable = (Pageable) PageRequest.of(pageNo, 10);
		Page<Like> page = this.likeRepository.getByUserOrderByIdDesc(user, pageable);
		List<Like> content = page.getContent();
		List<TempPost> list = new ArrayList<TempPost>();
		content.forEach(c->{
			Post post = c.getPost();
			TempPost tPost = new TempPost();
			tPost.setFormat(post.getFormat());
			tPost.setId(post.getId());
			tPost.setPathOfPost(post.getPathOfPost());
			list.add(tPost);
		});
		fetchPost.setPageNo(page.getNumber());
		fetchPost.setSize(page.getNumberOfElements());
		fetchPost.setTotalElement(page.getTotalElements());
		fetchPost.setPosts(list);
		return fetchPost;
	}
	
}
