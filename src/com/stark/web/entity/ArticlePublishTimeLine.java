package com.stark.web.entity;

import java.util.Date;

public class ArticlePublishTimeLine {
	private int id;
	private UserInfo user;
	private ArticleInfo article;
	private Date date;
	private int type;
	
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
	public ArticleInfo getArticle() {
		return article;
	}
	public void setArticle(ArticleInfo article) {
		this.article = article;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
