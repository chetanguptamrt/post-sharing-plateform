package com.social.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "post_timing")
	private Date date;
	
	private String format;
	
	private String caption;
	
	@Column(name = "path_of_post")
	private String pathOfPost;
	
	private boolean edit;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
	private List<Like> likes;

	public Post() {
		super();
	}

	public Post(int id, Date date, String format, String caption, String pathOfPost, User user, Boolean edit) {
		super();
		this.id = id;
		this.date = date;
		this.format = format;
		this.caption = caption;
		this.pathOfPost = pathOfPost;
		this.user = user;
		this.edit = edit;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getPathOfPost() {
		return pathOfPost;
	}

	public void setPathOfPost(String pathOfPost) {
		this.pathOfPost = pathOfPost;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Like> getLikes() {
		return likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", date=" + date + ", format=" + format + ", caption=" + caption + ", pathOfPost="
				+ pathOfPost + ", edit=" + edit + ", user=" + user + ", likes=" + likes + "]";
	}

}
