package com.stark.web.dao;

import java.util.List;
import java.util.Set;

import com.stark.web.define.EnumBase.ActivityStatus;
import com.stark.web.entity.ActivityInfo;

public interface IActivityDAO {

	public int addActivity(ActivityInfo activity);

	public ActivityInfo getActivity(int activityId);

	public ActivityInfo getAllStartUpActivity(ActivityStatus status);

	public List<ActivityInfo> getActivityByStatus(ActivityStatus status);

	public boolean addBannerPic(int activityId, String fileName);

	List<ActivityInfo> getOnLineActivity();

	public List<ActivityInfo> getActivityByType(int type, int size);

	public boolean addContentPic(int activityId, String fileName);

	public boolean delete(int activityId);

	public boolean setActivityStatus(int activityId, int status);

	public List<ActivityInfo> getAllActivity();

	public void addRedisActivity(ActivityInfo activity);

	public void addRedisBannerList(int activityId);

	public void addRedisTopList(int activityId);

	public List<ActivityInfo> getRedisActivityList(String key, int size);

	public void addRedisContentPic(int activityId, String fileName);

	public void addRedisBannerPic(int activityId, String fileName);

	public ActivityInfo getRedisActivity(int activityId);

	public void addRedisAllActivity(int activityId);

	public void removeRedisAllActivity(int activityId);

	public void removeRedisBannerList(int activityId);

	public void removeRedisTopList(int activityId);

	public List<ActivityInfo> getActivityByType(List<Integer> types);

	public Set<String> getRedisActivityZSet(String key);

	public void addRedisActivityZSet(String key, int activityId);

	public void addRedisActivityZSet(String key, int order, int activityId);

	public boolean setActivityOrder(int activityId, int order);

	public void setRedisActivity(String key, String field, String value);

	public void removeRedisActivityZSet(String key, int activityId);

	public boolean addArticleToShow(int showId, int articleId);
}
