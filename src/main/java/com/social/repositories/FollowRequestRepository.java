package com.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.entities.FollowRequest;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Integer>{

}
