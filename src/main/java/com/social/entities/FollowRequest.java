package com.social.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "follow_request")
public class FollowRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@JoinColumn(name = "user_who_send_request_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private User userWhoSend;

	public FollowRequest() {
		super();
	}
	
	public FollowRequest(int id, User user, User userWhoSend) {
		super();
		this.id = id;
		this.user = user;
		this.userWhoSend = userWhoSend;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUserWhoSend() {
		return userWhoSend;
	}

	public void setUserWhoSend(User userWhoSend) {
		this.userWhoSend = userWhoSend;
	}

	@Override
	public String toString() {
		return "FollowRequest [id=" + id + ", user=" + user + ", userWhoSend=" + userWhoSend + "]";
	}

}
