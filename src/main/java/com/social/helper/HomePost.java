package com.social.helper;

public class HomePost {

	private int id;
	
	private String caption;
	
	private String format;
	
	private String pathOfPost;
	
	private boolean edit;

	private long totalLikes;
	
	private String name;
	
	private String username;
	
	private String profile;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPathOfPost() {
		return pathOfPost;
	}

	public void setPathOfPost(String pathOfPost) {
		this.pathOfPost = pathOfPost;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public long getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(long l) {
		this.totalLikes = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
}
