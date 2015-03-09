package com.stark.web.service;

import java.util.List;

import com.stark.web.entity.CommentInfo;

public interface ICommentManager {
	public int addComment(CommentInfo cInfo);
	
	public boolean updateComment(CommentInfo cInfo);
	
	public boolean deleteComment(int id);
	
	public CommentInfo getComment(int cid);
	
	public List<CommentInfo> getCommentByArticleId(int articleId, int page, int maxresults);

	public int getCommentCount(int articleId);

	public List<CommentInfo> getCommentByArticleId(int articleId);
}
