package com.stark.web.entity;

import java.util.HashSet;
import java.util.Set;

import com.stark.web.service.FileManager;

public class TagInfo {
	
	private int tagId;

	private String content;
	private int useCount;
	private int status ;
	private String picture;
	private String pictureUrl;
	private Set<ArticleInfo> articles = new HashSet<ArticleInfo>();
	//getter and setter
	
	public String getContent() {
		return content;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	public Set<ArticleInfo> getArticles() {
		return articles;
	}
	public void setArticles(Set<ArticleInfo> articles) {
		this.articles = articles;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPictureUrl() {
		pictureUrl = FileManager.getTagPictureUrl(tagId, picture);
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
