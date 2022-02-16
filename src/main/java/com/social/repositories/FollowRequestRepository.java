package com.social.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.entities.FollowRequest;
import com.social.entities.User;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Integer>{

	public boolean existsByUserAndUserWhoSend(User user, User profileUser);

	public FollowRequest getByUserAndUserWhoSend(User user, User profileUser);

	public List<FollowRequest> getByUserWhoSend(User user);

	public List<FollowRequest> getByUser(User user);
	
}
