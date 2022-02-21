package com.social.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.social.entities.Notification;
import com.social.entities.User;
import com.social.repositories.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	public List<Notification> getNotifications(User user){
		return this.notificationRepository.getByUser(user);
	}
	
	public void seenNotification(User user) {
		this.notificationRepository.setAllSeenTrue(true, user.getId());
	}

}
