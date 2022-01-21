package com.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.entities.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer>{

}
