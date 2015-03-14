package com.stark.web.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.RelUserFollow;
import com.stark.web.entity.RelUserFriend;
import com.stark.web.entity.UserInfo;

public interface IUserManager {
	public int addUser(UserInfo uInfo);
	
	public boolean updateUser(UserInfo uInfo);
	
	public UserInfo getUser(int uId);
	
	public List<UserInfo> getUserById(String id);
	
	public List<UserInfo> getMustFollowUserList();
	
	public List<UserInfo> getRecommendUserList();
	
	public UserInfo verificationUser(String number,String password);
	
	public boolean addFriend(RelUserFriend rel);
	
	public RelUserFriend getRelUserFriend(String relId);
	
	public boolean deleteFriend(RelUserFriend rel);
	
	public boolean addFollow(RelUserFollow rel);

	public UserInfo getUserByDoorplate(int doorplate);

	public List<UserInfo> getFollowList(int userId, int page,int maxCount);

	public List<UserInfo> getFansList(int userId, int page, int maxRequestFollowCount);

	public boolean isFollow(int userId, int followId);

	public boolean addFollowingCount(int userId);

	public boolean addFansCount(int followId);

	public UserInfo isAdmin(String username, String password);

	public List<UserInfo> getOperatiors();

	public int isAdminByDoorplate(String doorplate, String password);

	public UserInfo getUserByPhoneNumber(String phoneNumber);

	public UserInfo getUserByQQOpenId(String qqOpendId);

	public UserInfo getUserBySinaOpenId(String sinaOpenId);

	public List<UserInfo> getAllUser();

	public boolean deleteUser(String userId);

	public List<UserInfo> getFateFriend(String userId);

	public long getMaxDoorplate();

	public boolean addUserHeadPic(int userId, String fileName);

	boolean removeFollow(int userId, int followId);

	public List<UserInfo> getPraiseUsers(int articleId);

	public int getFansCount(int userId);

	public int getFollowCount(int userId);

	public int getDoorplateById(int userId);

	public boolean updateUserName(int parseInt, String name);
	
	public Map<String, Object> userToMap(UserInfo user);
	
	public int getArticleCount(int userId);

	public UserInfo isOperatior(String name, String password);

	public int getOperatiorCount();

	public int getSimulationCount();

	public int getNormalCount();

	public UserInfo isExistQQOpenId(String qqOpenId);

	public UserInfo isExistSinaOpenId(String sinaOpenId);

	public UserInfo isExistWeChatOpenId(String weChatOpenId);

	public String getUserHeadPic(int userId, String headPicUrl);

	public void subFollowingCount(int userId);

	public void subFansCount(int followId);

	public void addChildUser(int parseInt, int userId);

	public List<UserInfo> getChildUser(int userId);

	public boolean isChildUser(int userId, int childId);

	public UserInfo verificationEmail(String email, String password);

	public boolean verificationEmail(String email);

	public boolean logout(int userId);

	public Set<String> getLogoutUser();

	public List<UserInfo> getMeetList(int userId, int sex,int maxCount);

	public void addFollowArticle(int userId, List<ArticleInfo> alist);

	public List<UserInfo> getAllFansList(int userId);

}
