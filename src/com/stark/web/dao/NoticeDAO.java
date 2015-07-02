package com.stark.web.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.stark.web.controller.NoticeController;
import com.stark.web.define.RedisInfo;
import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.CommentInfo;
import com.stark.web.entity.NoticeInfo;
import com.stark.web.entity.UserInfo;

public class NoticeDAO implements INoticeDAO{

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private IRedisDAO redisDao;

	
	public IRedisDAO getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(IRedisDAO redisDao) {
		this.redisDao = redisDao;
	}

	
	@Override
	public int addNotice(NoticeInfo nInfo) {
		int id = (int)sessionFactory.getCurrentSession().save(nInfo);
		return id;
	}

	@Override
	public boolean updateNotice(NoticeInfo nInfo) {
		
		return false;
	}

	@Override
	public boolean deleteNotice(int nid) {
		String hql = "delete from NoticeInfo as n where n.noticeId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, nid);
		return query.executeUpdate()>0;
	}

	@Override
	public NoticeInfo getNotice(int id) {
		return (NoticeInfo)sessionFactory.getCurrentSession().get(NoticeInfo.class, id);
	}

	@Override
	public List<NoticeInfo> getNoticeByUser(int uid) {
		String hql = "from NoticeInfo as n where n.user.userId = ? and n.status = 0";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, uid);
		return query.list();
	}
	@Override
	public boolean updateStatus(int noticeId, int status) {
		String sql = "update NoticeInfo as n set n.Status = ? where n.Id = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, status);
		query.setInteger(1, noticeId);
		int result = query.executeUpdate();
		return result>0;
	}
	@Override
	public List<NoticeInfo> getLastNotice(int userId, int type) {
		String hql = "from NoticeInfo as n where n.user.userId = ? and n.type = ? and n.status = 0";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		query.setInteger(1, type);
		return query.list();
	}
	@Override
	public boolean addRedisNotice(NoticeInfo nInfo) {
		if(redisDao==null)
			return false;
		Map<String, String> map = new HashMap<String, String>();
		map.put(NoticeInfo.SENDERID, nInfo.getSender().getUserId()+"");
		ArticleInfo article = nInfo.getArticle();
		if(article!=null){
			map.put(NoticeInfo.ARTICLEID, nInfo.getArticle().getArticleId()+"");
		}
		
		map.put(NoticeInfo.USERID, nInfo.getUser().getUserId()+"");
		if(nInfo.getContent()!=null){
			map.put(NoticeInfo.CONTENT,nInfo.getContent() );
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		map.put(NoticeInfo.DATE,sdf.format(nInfo.getDate()) );
		map.put(NoticeInfo.TYPE, nInfo.getType()+"");
		map.put(NoticeInfo.STATUS, nInfo.getStatus()+"");
		
		boolean result = redisDao.hmset(nInfo.getKey(), map)!=null;
		if(result){
			redisDao.expire(nInfo.getKey(), 60*60*24*30);
		}
		
		return result;
	}

	@Override
	public void addRedisUserList(int userId, int noticeId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(RedisInfo.USERNOTICELIST+userId, noticeId+"");
	}

	@Override
	public List<String> getRedisNoticeIds(String key) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, 0, -1);
	}

	@Override
	public NoticeInfo getRedisNotice(int id) {
		if(redisDao==null)
			return null;
		NoticeInfo notice = new NoticeInfo();
		Map<String, String> map = redisDao.hgetAll(NoticeInfo.getKey(id));
		if(map==null||map.isEmpty()){
			return null;
		}
		notice.setNoticeId(id);
		notice.setSender(new UserInfo(Integer.parseInt(map.get(NoticeInfo.SENDERID))));
		String articleId = map.get(NoticeInfo.ARTICLEID);
		if(articleId!=null){
			notice.setArticle(new ArticleInfo(Integer.parseInt(articleId)));
		}
		
		notice.setUser(new UserInfo(Integer.parseInt(map.get(NoticeInfo.USERID))));
		String content = map.get(NoticeInfo.CONTENT);
		if(content!=null){
			notice.setContent(content);
		}
		else {
			notice.setContent("");
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			notice.setDate(sdf.parse(map.get(NoticeInfo.DATE)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		notice.setType(Integer.parseInt(map.get(NoticeInfo.TYPE)));
		notice.setStatus(Integer.parseInt(map.get(NoticeInfo.STATUS)));
		
		return notice;
	}

	@Override
	public void setRedisUserNoticeStatus(int userId, int status) {
		if(redisDao==null)
			return;
		String key = UserInfo.getKey(userId);
		redisDao.hset(key,UserInfo.NOTICESTATUS, status+"");
	}

	@Override
	public int getRedisUserNoticeStatus(int userId) {
		if(redisDao==null)
			return -1;
		String key = RedisInfo.USERNOTICESTATUS+userId;
		String result = redisDao.get(key);
		if(result==null){
			return 0;
		}
		
		return Integer.parseInt(result);
	}

	@Override
	public void removeRedisUserList(int userId, int noticeId) {
		redisDao.lrem(RedisInfo.USERNOTICELIST+userId, 0, noticeId+"");
	}

	@Override
	public void updateRedisNotice(String key, String field, String value) {
		redisDao.hset(key, field, value);
	}

	@Override
	public List<NoticeInfo> getNoticeByUser(int userId, int type) {
		String hql = "from NoticeInfo as n where n.user.userId = ? and n.status = 0 and n.type=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		query.setInteger(1, type);
		return query.list();
	}

	@Override
	public void addRedisUserList(String key, int noticeId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(key, noticeId+"");
	}

	@Override
	public List<NoticeInfo> getNoticeByUser(int userId, int type1, int type2) {
		String hql = "from NoticeInfo as n where n.user.userId = ? and n.status = 0 and (n.type=? or n.type=?)";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		query.setInteger(1, type1);
		query.setInteger(2, type2);
		return query.list();
	}

}
