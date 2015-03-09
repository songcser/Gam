package com.stark.web.dao;

import java.util.List;

import com.stark.web.entity.CommentInfo;

public interface ICommentDAO {
	public int addComment(CommentInfo cInfo);
	
	public boolean updateComment(CommentInfo uInfo);
	
	public boolean deleteComment(int cid);
	
	public CommentInfo getComment(int cid);
	
	public List<CommentInfo> getCommentByArticle(int aid, int page, int maxResults);

	public int getCommentCount(int articleId);

	public boolean addRedisComment(CommentInfo cInfo);

	public void addRedisArticleList(int articleId, int commentId);

	public List<String> getRedisCommentIds(String key,int start,int end);

	public CommentInfo getRedisComment(int cid);

	public List<CommentInfo> getCommentByArticle(int articleId);

	public void addRedisArticleRList(int articleId, int commentId);
}
