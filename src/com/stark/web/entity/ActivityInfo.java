package com.stark.web.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stark.web.hunter.FileManager;
import com.stark.web.service.WebManager;

public class ActivityInfo {
	
	private static String key = "Activity:Info:Hash:";
	
	public static String SUBJECT = "Subject";
	public static String BANNERPIC="BannerPic";
	public static String CONTENTPIC = "ContentPic";
	public static String OFFDATE = "OffDate";
	public static String TYPE = "Type";
	
	private int activityId;
	private String subject;
	private UserInfo user;
	private ArticleInfo article;
	private String bannerPic;
	private String contentPic;
	private Date offDate;
	private int order;
	private int status;
	private int type;
	private Set<ArticleInfo> articles = new HashSet<ArticleInfo>();
	
	public ActivityInfo(){
		
	}
	
	public ActivityInfo(int activityId) {
		this.activityId = activityId;
	}
	
	public String getKey(){
		return key+activityId;
	}
	
	public static String getKey(int activityId) {
		return key+activityId;
	}
	
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBannerPic() {
		return bannerPic;
	}
	
	public String getBannerPicUrl(){
	    return FileManager.getActivityPictureUrl(activityId, bannerPic);
	}
	
	public void setBannerPic(String bannerPic) {
		this.bannerPic = bannerPic;
	}
	
	public Date getOffDate() {
		return offDate;
	}
	public void setOffDate(Date offDate) {
		this.offDate = offDate;
	}
	public String getOffDateString(){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(this.offDate);
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	
	
	@Override
	public String toString() {
		return "ActivityInfo [activityId=" + activityId + ", subject="
				+ subject + ", user=" + user
				+ ", bannerPic=" + bannerPic + ", offDate=" + offDate
				+ ", status=" + status + "]";
	}
	public int getType() {
	    return type;
	}
	public void setType(int type) {
	    this.type = type;
	}
	public Set<ArticleInfo> getArticles() {
	    return articles;
	}
	public void setArticles(Set<ArticleInfo> articles) {
	    this.articles = articles;
	}
	public ArticleInfo getArticle() {
	    return article;
	}
	public void setArticle(ArticleInfo article) {
	    this.article = article;
	}
	public String getContentPic() {
	    return contentPic;
	}
	public void setContentPic(String contentPic) {
	    this.contentPic = contentPic;
	}
	
	public String getContentPicUrl(){
	    return FileManager.getActivityPictureUrl(activityId, contentPic);
	}

	public static String getKey(String activityId) {
		
		return key+activityId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
}
