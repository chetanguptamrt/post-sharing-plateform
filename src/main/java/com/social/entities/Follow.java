package com.social.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Follow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JsonIgnore
	@JoinColumn(name = "follow_by_user", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.EAGER)
	private User followByUser;

	@JsonIgnore
	@JoinColumn(name = "followed_to_user", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.EAGER)
	private User followedToUser;

	public Follow() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Follow(int id, User followByUser, User followedToUser) {
		super();
		this.id = id;
		this.followByUser = followByUser;
		this.followedToUser = followedToUser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getFollowByUser() {
		return followByUser;
	}

	public void setFollowByUser(User followByUser) {
		this.followByUser = followByUser;
	}

	public User getFollowedToUser() {
		return followedToUser;
	}

	public void setFollowedToUser(User followedToUser) {
		this.followedToUser = followedToUser;
	}

	@Override
	public String toString() {
		return "Follow [id=" + id + ", followByUser=" + followByUser + ", followedToUser=" + followedToUser + "]";
	}
	
}
