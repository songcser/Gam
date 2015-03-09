package com.stark.web.entity;

import java.util.Date;

public class NoticeInfo {
	
	private static String key = "Notice:Info:Hash:";
	
	public static final String USERID = "UserId";
	public static final String SENDERID = "SenderId";
	public static final String ARTICLEID = "ArticleId";
	public static final String CONTENT = "Content";
	public static final String DATE = "Date";
	public static final String TYPE = "Type";
	public static final String STATUS = "Status";
	
	private int noticeId;
	
	private UserInfo user;
	private UserInfo sender;
	private ArticleInfo article;
	private String content;
	private Date date;
	private int type;
	private int status;
	
	//getter and setter
	
	public String getKey() {
		return key+noticeId;
	}
	public static String getKey(int id) {
		return key+id;
	}
	
	
	public UserInfo getUser() {
		return user;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UserInfo getSender() {
		return sender;
	}
	public void setSender(UserInfo sender) {
		this.sender = sender;
	}
	public ArticleInfo getArticle() {
		return article;
	}
	public void setArticle(ArticleInfo article) {
		this.article = article;
	}

	

}
