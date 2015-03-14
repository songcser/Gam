package com.stark.web.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.stark.web.define.RedisInfo;
import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.CommentInfo;
import com.stark.web.entity.UserInfo;
import com.sun.org.apache.xerces.internal.dom.CommentImpl;

public class CommentDAO implements ICommentDAO{

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public int addComment(CommentInfo cInfo) {
		
		return (int)sessionFactory.getCurrentSession().save(cInfo);
	}
	
	private IRedisDAO redisDao;

	
	public IRedisDAO getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(IRedisDAO redisDao) {
		this.redisDao = redisDao;
	}

	@Override
	public boolean updateComment(CommentInfo uInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteComment(int cid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CommentInfo getComment(int id) {
		
		return (CommentInfo)sessionFactory.getCurrentSession().get(CommentInfo.class, id);
	}

	@Override
	public List<CommentInfo> getCommentByArticle(int aid,int start,int maxResults) {
		String hql = "from CommentInfo as c where c.article.articleId = ? order by c.date desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, aid);
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.list();
	}
	@Override
	public int getCommentCount(int articleId) {
		String hql = "select count(c.commentId) from CommentInfo as c where c.article.articleId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, articleId);
		String result = query.uniqueResult()+"";
		return Integer.parseInt(result);
	}
	@Override
	public boolean addRedisComment(CommentInfo cInfo) {
		if(redisDao==null)
			return false;
		Map<String, String> map = new HashMap<String, String>();
		map.put(CommentInfo.ARTICLEID, cInfo.getArticle().getArticleId()+"");
		map.put(CommentInfo.USERID, cInfo.getUser().getUserId()+"");
		map.put(CommentInfo.CONTENT, cInfo.getContent());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		map.put(CommentInfo.DATE,sdf.format(cInfo.getDate()) );
		boolean result = redisDao.hmset(cInfo.getKey(), map)!=null;
		if(result){
			redisDao.expire(cInfo.getKey(), 60*60*24*30);
		}
		
		return result;
	}
	@Override
	public void addRedisArticleList(int articleId, int commentId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(RedisInfo.ARTICLECOMMENTLIST+articleId, commentId+"");
	}
	@Override
	public List<String> getRedisCommentIds(String key,int start,int end) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, start, end);
	}
	@Override
	public CommentInfo getRedisComment(int cid) {
		if(redisDao==null)
			return null;
		Map<String, String> cMap = redisDao.hgetAll(CommentInfo.getKey(cid));
		if(cMap==null||cMap.isEmpty()){
			return null;
		}
		CommentInfo comment = new CommentInfo();
		comment.setCommentId(cid);
		comment.setArticle(new ArticleInfo(Integer.parseInt(cMap.get(CommentInfo.ARTICLEID))));
		comment.setUser(new UserInfo(Integer.parseInt(cMap.get(CommentInfo.USERID))));
		comment.setContent(cMap.get(CommentInfo.CONTENT));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			comment.setDate(sdf.parse(cMap.get(CommentInfo.DATE)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return comment;
	}
	@Override
	public List<CommentInfo> getCommentByArticle(int articleId) {
		String hql = "from CommentInfo as c where c.article.articleId = ? order by c.date desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, articleId);
		return query.list();
	}
	@Override
	public void addRedisArticleRList(int articleId, int commentId) {
		if(redisDao==null)
			return ;
		redisDao.rpush(RedisInfo.ARTICLECOMMENTLIST+articleId, commentId+"");
	}
}
