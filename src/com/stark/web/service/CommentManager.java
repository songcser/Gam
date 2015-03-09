package com.stark.web.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.stark.web.dao.ICommentDAO;
import com.stark.web.entity.CommentInfo;
import com.stark.web.entity.RedisInfo;
import com.sun.org.apache.xerces.internal.dom.CommentImpl;

public class CommentManager implements ICommentManager {

	private ICommentDAO commentDao;

	public void setCommentDao(ICommentDAO commentDao) {
		this.commentDao = commentDao;
	}

	@Override
	public int addComment(CommentInfo cInfo) {
		int id = commentDao.addComment(cInfo);
		if(id>0){
			commentDao.addRedisComment(cInfo);
			commentDao.addRedisArticleList(cInfo.getArticle().getArticleId(),cInfo.getCommentId());
			
		}
		return id;
	}

	@Override
	public boolean updateComment(CommentInfo cInfo) {
		return false;
	}

	@Override
	public CommentInfo getComment(int cid) {
		CommentInfo comment = commentDao.getRedisComment(cid);
		if(comment==null){
			comment = commentDao.getComment(cid);
			if(comment!=null){
				commentDao.addRedisComment(comment);
			}
		}
		return comment;
	}

	@Override
	public List<CommentInfo> getCommentByArticleId(int aid,int page,int maxResults) {
		List<String> ids = commentDao.getRedisCommentIds(RedisInfo.ARTICLECOMMENTLIST+aid,page*maxResults,(page+1)*maxResults-1);
		List<CommentInfo> list = new ArrayList<CommentInfo>();
		int size = 0;
		if(ids!=null&&!ids.isEmpty()){
			size = ids.size();
			for(String id : ids){
				CommentInfo comment = getComment(Integer.parseInt(id));
				if(comment!=null){
					list.add(comment);
				}
			}
			if(size==maxResults)
				return list;
		}
		List<CommentInfo> tlist = commentDao.getCommentByArticle(aid,page*maxResults+size,maxResults-size);
		if(tlist!=null){
			for(int i=0;i<tlist.size();i++){
				CommentInfo comment = tlist.get(i);
				if(comment!=null){
					list.add(comment);
					commentDao.addRedisComment(comment);
					commentDao.addRedisArticleRList(comment.getArticle().getArticleId(), comment.getCommentId());
					
				}
			}
		}
		return list;
	}

	@Override
	public boolean deleteComment(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public static String ListToJson(List<CommentInfo> objs) {
		StringBuilder json = new StringBuilder("[");

		for (Iterator<CommentInfo> iterator = objs.iterator(); iterator.hasNext();) {
			CommentInfo object = iterator.next();
			json.append(object.toJson()).append(",");
		}
		json.append("]");

		return json.toString();
	}

	@Override
	public int getCommentCount(int articleId) {
		return commentDao.getCommentCount(articleId);
	}

	@Override
	public List<CommentInfo> getCommentByArticleId(int articleId) {
		List<String> ids = commentDao.getRedisCommentIds(RedisInfo.ARTICLECOMMENTLIST+articleId,0,-1);
		List<CommentInfo> list = new ArrayList<CommentInfo>();
		if(ids!=null&&!ids.isEmpty()){
			for(String id : ids){
				CommentInfo comment = getComment(Integer.parseInt(id));
				if(comment!=null){
					list.add(comment);
				}
			}
			
			return list;
		}
		list = commentDao.getCommentByArticle(articleId);
		if(list!=null){
			int size = list.size();
			for(int i=size-1;i>=0;i--){
				CommentInfo comment = list.get(i);
				if(comment!=null){
					commentDao.addRedisComment(comment);
					commentDao.addRedisArticleList(comment.getArticle().getArticleId(), comment.getCommentId());
				}
			}
		}
		return list;
	}

}
