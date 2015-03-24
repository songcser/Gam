package com.stark.web.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.stark.web.define.EnumBase.ArticleType;
import com.stark.web.hunter.FileManager;

public class ArticleInfo {

	//private static final long serialVersionUID = -6394020536933371467L;
	private static String key = "Article:Info:Hash:";
	public static String ARTICLEID = "ArticleId";
	public static String TYPE = "Type";
	public static String USERID = "UserId";
	public static String CONTENT = "Content";
	public static String DATE = "Date";
	public static String PRAISECOUNT = "PraiseCount";
	public static String COMMENTCOUNT = "CommentCount";
	public static String ACTIVITYID = "ActivityId";
	public static String REFERENCE = "Reference";
	public static String BROWSECOUNT = "BrowseCount";
	public static String TITLE = "Title";
	public static String RICHTEXT = "RichText";
	public static String COLLECTIONCOUNT = "CollectionCount";

	private int articleId;

	private UserInfo user;
	private String content;
	private ActivityInfo activity;
	private Date date;
	private int praiseCount;
	private int commentCount;
	private int collectionCount;
	private int status;
	private String reference;
	private String url;
	private String title;
	private String richText;
	private int type;
	private ArticleInfo originalArticle;
	private Set<String> picSet = new HashSet<String>();
	private Set<TagInfo> tags = new HashSet<TagInfo>();
	private Set<UserInfo> praiseUser = new HashSet<UserInfo>();
	private Set<String> atUser = new HashSet<String>();
	private Set<ActivityInfo> activities = new HashSet<ActivityInfo>();
	private Set<CommentInfo> comments = new HashSet<CommentInfo>();
	private Set<UserInfo> collectors = new HashSet<UserInfo>();
	private int browseCount;

	public ArticleInfo(int articleId) {
		this.articleId = articleId;
	}

	public ArticleInfo() {
	}

	// getter and setter

	public UserInfo getUser() {
		return user;
	}

	public String getKey() {
		return key + articleId;
	}

	public static String getKey(int articleId) {
		return key + articleId;
	}

	public static String getKey(String id) {
		return key + id;
	}
	
	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
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

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getPicSet() {
		return picSet;
	}

	public void setPicSet(Set<String> picSet) {
		this.picSet = picSet;
	}

	public Set<TagInfo> getTags() {
		return tags;
	}

	public void setTags(Set<TagInfo> tags) {
		this.tags = tags;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<UserInfo> getPraiseUser() {
		return praiseUser;
	}

	public void setPraiseUser(Set<UserInfo> praiseUser) {
		this.praiseUser = praiseUser;
	}

	public Set<String> getAtUser() {
		return atUser;
	}

	public void setAtUser(Set<String> atUser) {
		this.atUser = atUser;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArticleInfo getOriginalArticle() {
		return originalArticle;
	}

	public void setOriginalArticle(ArticleInfo originalArticle) {
		this.originalArticle = originalArticle;
	}

	public List<String> getTagsList() {
		List<String> tagList = new ArrayList<String>();
		for (Iterator<TagInfo> iterator = tags.iterator(); iterator.hasNext();) {
			String tag = iterator.next().getContent();
			tagList.add(tag);

		}

		return tagList;
	}

	public List<String> getPicList() {
		List<String> picList = new ArrayList<String>();
		 Set<String > set = picSet;
		 for (Iterator<String> iterator = set.iterator(); iterator.hasNext();)
		 {
    		 String pic = iterator.next();
    		 String path = FileManager.getArticlePictureUrl(articleId, pic);
    		 //System.out.println("Path:"+path);
    		 picList.add(path);
		 }

		return picList;
	}

	public ActivityInfo getActivity() {
		return activity;
	}

	public void setActivity(ActivityInfo activity) {
		this.activity = activity;
	}

	public Set<CommentInfo> getComments() {
		return comments;
	}

	public void setComments(Set<CommentInfo> comments) {
		this.comments = comments;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public Set<ActivityInfo> getActivities() {
		return activities;
	}

	public void setActivities(Set<ActivityInfo> activities) {
		this.activities = activities;
	}

	public Set<UserInfo> getCollectors() {
		return collectors;
	}

	public void setCollectors(Set<UserInfo> collectors) {
		this.collectors = collectors;
	}

	public String getRichText() {
		return richText;
	}

	public void setRichText(String richText) {
		this.richText = richText;
	}

	public int getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(int collectionCount) {
		this.collectionCount = collectionCount;
	}

	
	
}
