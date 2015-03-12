package com.stark.web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.stark.web.dao.ActivityDAO;
import com.stark.web.dao.IActivityDAO;
import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.RedisInfo;
import com.stark.web.entity.EnumBase.ActivityStatus;
import com.stark.web.entity.EnumBase.ActivityType;

public class ActivityManager implements IActivityManager{

	private IActivityDAO activityDao;
	
	public void setActivityDao(IActivityDAO activityDao){
		this.activityDao = activityDao;
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
			if(status==ActivityStatus.Delete.getIndex()){
				activityDao.removeRedisAllActivity(activityId);
				activityDao.removeRedisBannerList(activityId);
				activityDao.removeRedisTopList(activityId);
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
		List<Integer> types = new ArrayList<Integer>();
		types.add(ActivityType.Join.getIndex());
		types.add(ActivityType.NoJoin.getIndex());
		List<ActivityInfo> list = activityDao.getActivityByType(types);
		return list;
	}
	
}
