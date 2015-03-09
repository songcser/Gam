package com.stark.web.entity;

import java.util.Date;

public class RelUserFriend {
	private int id;
	private UserInfo user;
	private UserInfo friend;
	private Date requestDate;
	private Date agreeDate;
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
	public UserInfo getFriend() {
		return friend;
	}
	public void setFriend(UserInfo friend) {
		this.friend = friend;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getAgreeDate() {
		return agreeDate;
	}
	public void setAgreeDate(Date agreeDate) {
		this.agreeDate = agreeDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
