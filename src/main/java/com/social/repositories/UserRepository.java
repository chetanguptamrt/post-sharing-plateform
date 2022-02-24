package com.social.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	public User getUserByEmailOrUserName(String email, String userName);

	public User getUserByEmail(String email);

	public boolean existsByEmail(String email);

	public User getUserByUserName(String userName);

	public boolean existsByUserName(String username);

	public List<User> findTop10ByFirstNameOrLastNameOrUserNameContaining(String search, String search2, String search3);
	
}
