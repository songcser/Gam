package com.stark.web.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.EnumBase;
import com.stark.web.entity.EnumBase.ActivityStatus;
import com.stark.web.entity.EnumBase.ActivityType;
import com.stark.web.entity.RedisInfo;

public class ActivityDAO implements IActivityDAO{
	private SessionFactory sessionFactory;
	
	private IRedisDAO redisDao;
	
	public IRedisDAO getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(IRedisDAO redisDao) {
		this.redisDao = redisDao;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public  SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public int addActivity(ActivityInfo activity) {
		int id = (int)sessionFactory.getCurrentSession().save(activity);
		return id;
	}

	@Override
	public ActivityInfo getActivity(int activityId) {
		return (ActivityInfo)sessionFactory.getCurrentSession().get(ActivityInfo.class, activityId);
	}

	@Override
	public ActivityInfo getAllStartUpActivity(ActivityStatus status) {
		String hql = "from ActivityInfo as a where a.status = ? and a.offDate > ? order by a.activityId desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, status.getIndex());
		query.setDate(1, new Date());
		return null;
	}
	
	@Override
	public List<ActivityInfo> getActivityByStatus(ActivityStatus status) {
		String hql = "from ActivityInfo as a where a.status = ? order by a.activityId desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, status.getIndex());
		
		return query.list();
	}
	
	@Override
	public List<ActivityInfo> getOnLineActivity() {
		String hql = "from ActivityInfo as a where a.status = ? and a.offDate > ? order by a.activityId desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, EnumBase.ActivityStatus.OnLine.getIndex());
		query.setDate(1, new Date());
		return query.list();
	}

	@Override
	public boolean addBannerPic(int activityId, String fileName) {
		String hql = "update ActivityInfo as a set a.bannerPic = ? where a.activityId = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, fileName);
		query.setInteger(1, activityId);
		return query.executeUpdate()>0;
	}

	@Override
	public List<ActivityInfo> getActivityByType(int type,int size) {
	    String hql = "from ActivityInfo as a where a.type = ? and a.offDate > ? and a.status != ? order by a.activityId desc";
	    Query query = sessionFactory.getCurrentSession().createQuery(hql);
	    query.setInteger(0, type);
	    query.setDate(1, new Date());
	    query.setInteger(2, ActivityStatus.Delete.getIndex());
	    if(size!=0){
		query.setFirstResult(0);
		query.setMaxResults(size);
	    }
	    return query.list();
	}

	@Override
	public boolean addContentPic(int activityId, String fileName) {
		String hql = "update ActivityInfo as a set a.contentPic = ? where a.activityId = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, fileName);
		query.setInteger(1, activityId);
		return query.executeUpdate()>0;
	}

	@Override
	public boolean delete(int activityId) {
		String hql = "delete from ActivityInfo as a where a.activityId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, activityId);
		
		return query.executeUpdate()>0;
	}

	@Override
	public boolean setActivityStatus(int activityId, int status) {
		String hql = "update ActivityInfo as a set a.status = ? where a.activityId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, status);
		query.setInteger(1, activityId);
		
		return query.executeUpdate()>0;
	}

	@Override
	public List<ActivityInfo> getAllActivity() {
		String hql = "from ActivityInfo as a where a.status = ? or a.status = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, ActivityStatus.OffLine.getIndex());
		query.setInteger(1, ActivityStatus.OnLine.getIndex());
		
		return query.list();
	}

	@Override
	public void addRedisActivity(ActivityInfo activity) {
		if(redisDao==null)
			return ;
		Map<String, String> map = new HashMap<String, String>();
		if(activity.getSubject()!=null){
			map.put(ActivityInfo.SUBJECT, activity.getSubject());
		}
		if(activity.getBannerPic()!=null){
			map.put(ActivityInfo.BANNERPIC, activity.getBannerPic());
		}
		
		map.put(ActivityInfo.CONTENTPIC, activity.getContentPic());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		map.put(ActivityInfo.OFFDATE, sdf.format(activity.getOffDate()));
		map.put(ActivityInfo.TYPE,activity.getType()+"");
		
		redisDao.hmset(activity.getKey(), map);
	}

	@Override
	public void addRedisBannerList(int activityId) {
		if(redisDao==null)
			return ;
		redisDao.rpush(RedisInfo.ACTIVITYBANNERLIST,activityId+"");
	}

	@Override
	public void addRedisTopList(int activityId) {
		if(redisDao==null)
			return;
		redisDao.rpush(RedisInfo.ACTIVITYTOPLIST, activityId+"");
	}

	public ActivityInfo getRedisActivity(int activityId) {
		if(redisDao==null)
			return null;
		Map<String, String> acMap = redisDao.hgetAll(ActivityInfo.getKey(activityId));
		if(acMap==null||acMap.isEmpty())
			return null;
		//System.out.println(acMap);
		ActivityInfo ac = new ActivityInfo();
		ac.setActivityId(activityId);
		ac.setSubject(acMap.get(ActivityInfo.SUBJECT));
		ac.setBannerPic(acMap.get(ActivityInfo.BANNERPIC));
		ac.setContentPic(acMap.get(ActivityInfo.CONTENTPIC));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			ac.setOffDate(sdf.parse(acMap.get(ActivityInfo.OFFDATE)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ac.setType(Integer.parseInt(acMap.get(ActivityInfo.TYPE)));
		
		return ac;
	}
	
	@Override
	public List<ActivityInfo> getRedisActivityList(String key){
		if(redisDao==null)
			return null;
		List<String> acIds = redisDao.lrange(key, 0, -1);
		List<ActivityInfo> list = new ArrayList<ActivityInfo>();
		Date now = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		if(acIds!=null){
			for(String id:acIds){
				Map<String, String> acMap = redisDao.hgetAll(ActivityInfo.getKey(id));
				if(acMap==null||acMap.isEmpty()){
					ActivityInfo activityInfo = getActivity(Integer.parseInt(id));
					
					if(activityInfo!=null&&activityInfo.getOffDate().compareTo(now)>0){
						addRedisActivity(activityInfo);
						list.add(activityInfo);
					}
					continue;
				}
					
				//System.out.println(acMap);
				ActivityInfo ac = new ActivityInfo();
				ac.setActivityId(Integer.parseInt(id));
				ac.setSubject(acMap.get(ActivityInfo.SUBJECT));
				ac.setBannerPic(acMap.get(ActivityInfo.BANNERPIC));
				ac.setContentPic(acMap.get(ActivityInfo.CONTENTPIC));
				
				try {
					Date date =sdf.parse(acMap.get(ActivityInfo.OFFDATE));
					
					if(date.compareTo(now)<0){
						redisDao.lrem(key,0,id);
						continue;
					}
					ac.setOffDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				ac.setType(Integer.parseInt(acMap.get(ActivityInfo.TYPE)));
				
				list.add(ac);
			}
		}
		else {
			return null;
		}
		
		return list;
	}

	
	@Override
	public void addRedisContentPic(int activityId, String fileName) {
		if(redisDao==null)
			return ;
		redisDao.hset(ActivityInfo.getKey(activityId),ActivityInfo.CONTENTPIC, fileName);
	}

	@Override
	public void addRedisBannerPic(int activityId, String fileName) {
		if(redisDao==null)
			return ;
		redisDao.hset(ActivityInfo.getKey(activityId),ActivityInfo.BANNERPIC, fileName);
	}

	@Override
	public void addRedisAllActivity(int activityId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(RedisInfo.ACTIVITYALLLIST, activityId+"");
	}

	@Override
	public void removeRedisAllActivity(int activityId) {
		if(redisDao==null)
			return ;
		redisDao.lrem(RedisInfo.ACTIVITYALLLIST, 0, activityId+"");
	}

	@Override
	public void removeRedisBannerList(int activityId) {
		if(redisDao==null)
			return;
		redisDao.lrem(RedisInfo.ACTIVITYBANNERLIST, 0, activityId+"");
	}

	@Override
	public void removeRedisTopList(int activityId) {
		if(redisDao==null)
			return;
		redisDao.lrem(RedisInfo.ACTIVITYTOPLIST, 0, activityId+"");
	}

}
