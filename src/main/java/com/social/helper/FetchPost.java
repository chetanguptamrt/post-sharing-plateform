package com.social.helper;

import java.util.List;

public class FetchPost {
	
	private long totalElement;
	
	private int size;
	
	private int pageNo;
	
	private List<TempPost> posts;

	public long getTotalElement() {
		return totalElement;
	}

	public void setTotalElement(long totalElement) {
		this.totalElement = totalElement;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public List<TempPost> getPosts() {
		return posts;
	}

	public void setPosts(List<TempPost> posts) {
		this.posts = posts;
	}
	
}
