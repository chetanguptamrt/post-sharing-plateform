package com.social.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private boolean seen;
	
	@Column(name = "redirect_url")
	private String redirectUrl;
	
	private String text;
	
	@Column(name = "icon_path")
	private String iconPath;
	
	@Column(name = "date_time")
	private Date date;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	public Notification() {
		super();
	}

	public Notification(int id, boolean seen, String redirectUrl, String text, String iconPath, Date date, User user) {
		super();
		this.id = id;
		this.seen = seen;
		this.redirectUrl = redirectUrl;
		this.text = text;
		this.iconPath = iconPath;
		this.date = date;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", seen=" + seen + ", redirectUrl=" + redirectUrl + ", text=" + text
				+ ", iconPath=" + iconPath + ", date=" + date + ", user=" + user + "]";
	}
	
}
