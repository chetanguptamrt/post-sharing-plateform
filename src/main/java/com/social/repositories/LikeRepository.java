package com.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Integer>{

}
