package com.social.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.entities.Post;
import com.social.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer>{

	public Post getByIdAndUser(int postId, User user);

	public long countByUser(User user);

	public Post getByPathOfPost(String pathOfPost);

	public List<Post> getByUser(User user);

}
