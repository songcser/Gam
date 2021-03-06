package com.stark.web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.stark.web.dao.ActivityDAO;
import com.stark.web.dao.IActivityDAO;
import com.stark.web.dao.IArticleDAO;
import com.stark.web.define.RedisInfo;
import com.stark.web.define.EnumBase.ActivityStatus;
import com.stark.web.define.EnumBase.ActivityType;
import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.ArticleInfo;

public class ActivityManager implements IActivityManager{

	private IActivityDAO activityDao;
	
	public void setActivityDao(IActivityDAO activityDao){
		this.activityDao = activityDao;
	}
	
	@Resource
	private IArticleDAO articleDao;
	
	public void setArticleDao(IArticleDAO articleDao){
		this.articleDao = articleDao;
	}
	
	@Override
	public int addActivity(ActivityInfo activity) {
		int activityId = activityDao.addActivity(activity);
		if(activityId>0){
			activityDao.addRedisActivity(activity);
			activityDao.addRedisAllActivity(activityId);
			if(activity.getType()==ActivityType.Banner.getIndex()){
				activityDao.addRedisBannerList(activity.getActivityId());
			}
			else if(activity.getType()==ActivityType.TopRecommend.getIndex()){
				activityDao.addRedisTopList(activity.getActivityId());
			}
			activityDao.addRedisActivityZSet(RedisInfo.ACTIVITYORDERZSET,activity.getOrder(),activity.getActivityId());
		}
		return activityId;
	}

	@Override
	public ActivityInfo getActivity(int activityId) {
		ActivityInfo activityInfo = activityDao.getRedisActivity(activityId);
		if(activityInfo==null){
			activityInfo = activityDao.getActivity(activityId);
			if(activityInfo!=null){
				activityDao.addRedisActivity(activityInfo);
			}
		}
		return activityInfo;
	}

	@Override
	public List<ActivityInfo> getAllStartUpActivity() {
		return activityDao.getOnLineActivity();
	}

	@Override
	public boolean addBannerPic(int activityId, String fileName) {
		boolean result = activityDao.addBannerPic(activityId,fileName);
		if(result){
			activityDao.addRedisBannerPic(activityId,fileName);
		}
		return result;
	}

	@Override
	public List<ActivityInfo> getBannerActivity() {
		
		List<ActivityInfo> list = activityDao.getRedisActivityList(RedisInfo.ACTIVITYBANNERLIST,5);
		if(list==null||list.size()==0){
			list = activityDao.getActivityByType(ActivityType.Banner.getIndex(),5);
			if(list!=null){
				for (Iterator<ActivityInfo> iterator = list.iterator(); iterator.hasNext();) {
					ActivityInfo activityInfo = iterator.next();
					activityDao.addRedisActivity(activityInfo);
					activityDao.addRedisBannerList(activityInfo.getActivityId());
				}
			}
		}
	    return list ;
	}

	@Override
	public List<ActivityInfo> getTopRecommendActivity() {
		List<ActivityInfo> list = activityDao.getRedisActivityList(RedisInfo.ACTIVITYTOPLIST,2);
		//System.out.println(list.size());
		if(list==null||list.isEmpty()){
			list = activityDao.getActivityByType(ActivityType.TopRecommend.getIndex(),2);
			//System.out.println(list.size());
			if(list!=null){
				for (Iterator<ActivityInfo> iterator = list.iterator(); iterator.hasNext();) {
					ActivityInfo activityInfo = iterator.next();
					activityDao.addRedisActivity(activityInfo);
					activityDao.addRedisTopList(activityInfo.getActivityId());
				}
			}
		}
		
	    return list ;
	    //return activityDao.getActivityByType(EnumBase.ActivityType.TopRecommend.getIndex(),2);
	}

	@Override
	public boolean addContentPic(int activityId, String fileName) {
		boolean result = activityDao.addContentPic(activityId,fileName);
		if(result){
			activityDao.addRedisContentPic(activityId,fileName);
		}
		return result;
	}

	@Override
	public boolean delete(int activityId) {
		return activityDao.delete(activityId);
	}

	@Override
	public boolean setActivityStatus(int activityId, int status) {
		boolean result = activityDao.setActivityStatus(activityId,status);
		if(result){
			ActivityInfo ac = getActivity(activityId);
			activityDao.setRedisActivity(ActivityInfo.getKey(activityId), ActivityInfo.STATUS, status+"");
			if(status==ActivityStatus.Delete.getIndex()){
				activityDao.removeRedisAllActivity(activityId);
				activityDao.removeRedisBannerList(activityId);
				activityDao.removeRedisTopList(activityId);
				activityDao.removeRedisActivityZSet(RedisInfo.ACTIVITYORDERZSET,activityId);
			}
			else if(status==ActivityStatus.OnLine.getIndex()){
				activityDao.addRedisActivityZSet(RedisInfo.ACTIVITYONLINEZSET,ac.getOrder(),ac.getActivityId());
			}
			else if(status==ActivityStatus.OffLine.getIndex()){
				activityDao.removeRedisActivityZSet(RedisInfo.ACTIVITYONLINEZSET, activityId);
			}
		}
		return result;
	}

	@Override
	public List<ActivityInfo> getAllActivity() {
		List<ActivityInfo> list = activityDao.getRedisActivityList(RedisInfo.ACTIVITYALLLIST,-1);
		if(list==null||list.isEmpty()){
			list = activityDao.getAllActivity();
			if(list!=null&&!list.isEmpty()){
				for (Iterator<ActivityInfo> iterator = list.iterator(); iterator.hasNext();) {
					ActivityInfo activityInfo = iterator.next();
					activityDao.addRedisActivity(activityInfo);
					activityDao.addRedisAllActivity(activityInfo.getActivityId());
				}
			}
		}
		return list;
	}

	@Override
	public List<ActivityInfo> getAllShowList() {
		String key = RedisInfo.ACTIVITYORDERZSET;
		Set<String> acIds = activityDao.getRedisActivityZSet(key);
		List<ActivityInfo> acList = new ArrayList<ActivityInfo>();
		if(acIds!=null&&!acIds.isEmpty()){
			for(String id:acIds){
				ActivityInfo ac = getActivity(Integer.parseInt(id));
				acList.add(ac);
			}
			return acList;
		}
		
		List<Integer> types = new ArrayList<Integer>();
		types.add(ActivityType.Join.getIndex());
		types.add(ActivityType.NoJoin.getIndex());
		List<ActivityInfo> list = activityDao.getActivityByType(types);
		if(list!=null){
			for(ActivityInfo ac:list){
				activityDao.addRedisActivityZSet(key,ac.getOrder(),ac.getActivityId());
			}
		}
		return list;
	}

	@Override
	public void setActivityOrder(int activityId, int order) {
		boolean result = activityDao.setActivityOrder(activityId,order);
		if(result){
			String key = ActivityInfo.getKey(activityId);
			activityDao.setRedisActivity(key,ActivityInfo.ORDER,order+"");
			activityDao.addRedisActivityZSet(RedisInfo.ACTIVITYORDERZSET,order,activityId);
		}
	}

	@Override
	public boolean moveToShow(int showId, int articleId) {
		boolean result = activityDao.addArticleToShow(showId,articleId);
		if(result){
			articleDao.addRedisActivityAuditingList(showId,articleId);
			
		}
		return result;
	}

	@Override
	public boolean moveToShow(int showId, int articleId, int type) {
		boolean result = activityDao.addArticleToShow(showId,articleId);
		result = articleDao.changeArticleType(articleId,type);
		result = articleDao.changeArticleShowId(articleId,showId);
		if(result){
			articleDao.addRedisActivityAuditingList(showId,articleId);
			articleDao.changeRedisArticleType(articleId,type);
			articleDao.setRedisArticleInfo(ArticleInfo.getKey(articleId), ArticleInfo.ACTIVITYID, showId+"");
		}
		return result;
	}

	@Override
	public List<ActivityInfo> getOnlineShowList() {
		String key = RedisInfo.ACTIVITYONLINEZSET;
		Set<String> acIds = activityDao.getRedisActivityZSet(key);
		List<ActivityInfo> acList = new ArrayList<ActivityInfo>();
		if(acIds!=null&&!acIds.isEmpty()){
			for(String id:acIds){
				ActivityInfo ac = getActivity(Integer.parseInt(id));
				acList.add(ac);
			}
			return acList;
		}
		
		List<Integer> types = new ArrayList<Integer>();
		types.add(ActivityType.Join.getIndex());
		types.add(ActivityType.NoJoin.getIndex());
		List<ActivityInfo> list = activityDao.getOnlineActivityByType(types);
		if(list!=null){
			for(ActivityInfo ac:list){
				activityDao.addRedisActivityZSet(key,ac.getOrder(),ac.getActivityId());
			}
		}
		return list;
	}
	
}
