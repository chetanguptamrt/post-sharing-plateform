package com.social.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.entities.Follow;
import com.social.entities.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer>{

	public boolean existsByFollowByUserAndFollowedToUser(User user, User profileUser);

	public long countByFollowByUser(User user);

	public long countByFollowedToUser(User user);
	
	public Follow getByFollowByUserAndFollowedToUser(User user, User profileUser);

	public List<Follow> getByFollowByUser(User user);

	public List<Follow> getByFollowedToUser(User user);
	
}
