package com.stark.web.entity;

import java.util.Date;

public class RelArticleForward {
	private int id;
	private ArticleInfo article;
	private UserInfo user;
	private Date date;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArticleInfo getArticle() {
		return article;
	}
	public void setArticle(ArticleInfo article) {
		this.article = article;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
