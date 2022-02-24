package com.social.services;

import java.util.LinkedList;
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
		List<Notification> list = this.notificationRepository.getByUser(user);
		List<Notification> reverse = new LinkedList<Notification>();
		for(int i=list.size()-1; i>=0; i--) {
			reverse.add(list.get(i));
		}
		return reverse;
	}
	
	public void seenNotification(User user) {
		this.notificationRepository.setAllSeenTrue(true, user.getId());
	}

}
