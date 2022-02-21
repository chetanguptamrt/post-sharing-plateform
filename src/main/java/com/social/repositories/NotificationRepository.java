package com.social.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.social.entities.Notification;
import com.social.entities.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{

	public List<Notification> getByUser(User user);

	@Modifying
	@Transactional
	@Query(value =  "update notification set seen = :s where user_id = :u",nativeQuery = true)
	int setAllSeenTrue(@Param("s") Boolean seen, @Param("u") int user);
	
}
