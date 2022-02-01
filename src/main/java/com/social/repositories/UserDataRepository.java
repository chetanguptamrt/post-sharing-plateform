package com.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.entities.User;
import com.social.entities.UserData;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Integer>{

	public UserData getByUser(User user);

}
