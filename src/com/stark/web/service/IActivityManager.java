package com.stark.web.service;

import java.util.List;
import java.util.Map;

import com.stark.web.entity.ActivityInfo;

public interface IActivityManager {

	public int addActivity(ActivityInfo activity);

	public ActivityInfo getActivity(int activityId);

	public List<ActivityInfo> getAllStartUpActivity();

	public boolean addBannerPic(int activityId, String fileName);

	public List<ActivityInfo> getBannerActivity();

	public List<ActivityInfo> getTopRecommendActivity();

	public boolean addContentPic(int activityId, String fileName);

	public boolean delete(int activityId);

	public boolean setActivityStatus(int activityId, int index);

	public List<ActivityInfo> getAllActivity();

	public List<ActivityInfo> getAllShowList();

	public void setActivityOrder(int activityId, int order);

	public boolean moveToShow(int showId, int articleId);

	public boolean moveToShow(int showId, int articleId, int type);

	public List<ActivityInfo> getOnlineShowList();


}
