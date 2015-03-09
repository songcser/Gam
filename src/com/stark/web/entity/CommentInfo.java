package com.stark.web.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CommentInfo {
	
	private static final String key = "Comment:Info:Hash:";
	public static final String COMMENTID = "CommentId";
	public static final String ARTICLEID = "ArticleId";
	public static final String USERID = "UserId";
	public static final String CONTENT = "Content";
	public static final String DATE = "Date";
	
	private int commentId;
	private ArticleInfo article;
	private UserInfo user;
	private String content;
	private Date date;
	private Set<String> atUsers = new HashSet<String>();
	
	//getter and setter
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	
	public String getKey() {
		return key+commentId;
	}
	
	public static String getKey(int commentId){
		return key+commentId;
	}
	
	public ArticleInfo getArticle() {
		return article;
	}
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public void setArticle(ArticleInfo article) {
		this.article = article;
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
	
	public String toJson() {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String json = "{\"commentId\":\""+commentId+"\","
				+"\"userId\":\""+user.getUserId()+"\","
				+"\"userName\":\""+user.getName()+"\","
				+"\"headPic\":\""+user.getHeadPic()+"\","
				+"\"content\":\""+content+"\","
				+ "\"date\":\""+format.format(date)+"\"}";
		return json;
	}
	public Set<String> getAtUsers() {
		return atUsers;
	}
	public void setAtUsers(Set<String> atUsers) {
		this.atUsers = atUsers;
	}
	@Override
	public String toString() {
		return "CommentInfo [commentId=" + commentId + ", articleId=" + article.getArticleId()
				+ ", userId=" + user.getUserId() + ", content=" + content + ", date=" + date
				+ ", atUsers=" + atUsers + "]";
	}
	
}
