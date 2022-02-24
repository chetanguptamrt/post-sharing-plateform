package com.social.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.social.entities.Like;
import com.social.entities.Post;
import com.social.entities.User;

public interface LikeRepository extends JpaRepository<Like, Integer>{

	public boolean existsByUserAndPost(User user, Post post);

	public Like getByUserAndPost(User user, Post post);

	public long countByPost(Post post);

	public List<Like> getByPost(Post post);

	public Page<Like> getByUserOrderByIdDesc(User user, Pageable pageable);

}
