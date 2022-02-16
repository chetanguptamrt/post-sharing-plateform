package com.social.helper;

public class FollowUser {
	
	private String name;
	
	private String profileImagePath;
	
	private String userName;

	public FollowUser() {
		super();
	}

	public FollowUser(String name, String profileImagePath, String userName) {
		super();
		this.name = name;
		this.profileImagePath = profileImagePath;
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "FollowUser [name=" + name + ", profileImagePath=" + profileImagePath + ", userName=" + userName + "]";
	}
	
}
