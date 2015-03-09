package com.stark.web.entity;

import java.util.Date;

public class RelUserFollow {
	private int id;
	
	private UserInfo user;
	private UserInfo follow;
	private Date date;
	private int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public UserInfo getFollow() {
		return follow;
	}
	public void setFollow(UserInfo follow) {
		this.follow = follow;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
