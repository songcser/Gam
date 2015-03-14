package com.stark.web.dao;

import java.util.List;
import java.util.Set;

import com.stark.web.entity.RelUserFollow;
import com.stark.web.entity.RelUserFriend;
import com.stark.web.entity.UserInfo;

public interface IUserDAO {
	public int addUser(UserInfo uInfo);
	
	public boolean updateUser(UserInfo uInfo);
	
	public UserInfo getUserInfo(int id);

	public List<UserInfo> getAllUser();
	 
	public boolean addFriend(RelUserFriend rel);
	
	public boolean addFollow(RelUserFollow rel);

	public UserInfo getUserByDoorplate(int doorplate);

	public List<UserInfo> getFollowList(String userId, int page, int maxCount);

	public List<UserInfo> getFansList(int userId, int page, int maxCount);

	public boolean isFollow(int userId, int followId);

	public boolean addRedisFollowingCount(int userId);

	public boolean addRedisFansCount(int followId);

	public UserInfo isExist(String username, String password, int type);

	public List<UserInfo> getUsersByRole(int role);

	public UserInfo verificationUser(String number, String password);

	public int isAdminByDoorplate(String doorplate, String password,int role);

	public UserInfo getUserByPhoneNumber(String phoneNumber);

	public UserInfo getUserByQQOpenId(String qqOpendId);

	public UserInfo getUserBySinaOpenId(String sinaOpenId);

	public boolean deleteUser(String userId);

	public List<UserInfo> getUsersByPage(String userId);

	public long getMaxDoorplate();

	public boolean addUserHeadPic(int userId, String fileName);

	boolean removeFollow(int userId, int followId);

	public List<UserInfo> getPraiseUser(int articleId);

	public int getFansCount(int userId);

	public int getFollowCount(int userId);

	public int getDoorplateById(int userId);

	public boolean updateUserName(int userId, String name);

	public List<UserInfo> getFateFriend(String userId);

	public List<UserInfo> getUsersByRole(int role, int role2, int role3);

	public int getArticleCount(int userId);

	public int getUserCountByRole(int role);

	public UserInfo isExistQQOpenId(String qqOpenId);

	public UserInfo isExistSinaOpenId(String sinaOpenId);

	public UserInfo isExistWeChatOpenId(String weChatOpenId);

	public boolean addRedisUser(UserInfo uInfo);

	public boolean addRedisUserHeadPic(int userId, String imageName);

	public UserInfo getRedisUser(int id);

	public String getRedisUserCount(String key);

	public void setRedisUserCount(String key, int value);

	public void addRedisUserCount(String key);

	public List<UserInfo> getUsersByRoles(List<Integer> roles);

	public List<String> getRedisUsers(String key);

	public long addRedisUsers(String key, int userId);

	int getPraiseCount(int userId);

	public void subRedisFollowingCount(int userId);

	public void subRedisFansCount(int followId);

	public void addRedisFansSet(int userId, int followId);

	public String getRedisOpenId(String key);

	public void setRedisOpenId(String key, int userId);

	public Set<String> getRedisUserIds(String key, int page, int maxResults);

	public void addRedisFollowingSet(int userId, int userId2);

	public long addRedisFollowingZSet(RelUserFollow rel);

	public long addRedisFansZSet(RelUserFollow rel);

	public List<RelUserFollow> getUserFollowList(int userId, int page, int maxCount);

	public List<RelUserFollow> getUserFansList(int userId, int page, int maxCount);

	public int getRedisFansCount(int userId);

	public void setRedisFansCount(int userId, int count);

	public int getRedisFollowsCount(int userId);

	public void setRedisFollowCount(int userId, int count);

	public boolean isRedisFollow(int userId, int followId);

	public boolean updateRedisUser(UserInfo uInfo);

	public void removeRedisFollow(int userId, int followId);

	public void removeRedisFans(int userId, int followId);

	public int addChildUser(int parentId, int userId);

	public List<Object[]> getChildUser(int userId);

	public void addRedisChildUser(String key, int userId);

	public boolean isChildUser(int userId, int childId);

	public long addRedisOnlyFansZSet(RelUserFollow rel);

	public long addRedisOnlyFollowingZSet(RelUserFollow rel);

	public UserInfo verificationEmail(String email, String password);

	public void addRedisUserEmailPassword(String email, String password, int userId);

	public String getRedisByEmailPassword(String email, String password);

	public boolean verificationEmail(String email);

	public void addRedisEmails(String email);

	public boolean isRedisEmail(String email);

	public boolean addRedisLogoutUser(int userId);

	public Set<String> getRedisLogoutUser();

	public List<UserInfo> getMeetList(int maxCount);

	public List<UserInfo> getMeetList(int sex, int maxCount);

	public Set<String> getRedisSetUserIds(String string);

	public List<UserInfo> getFansList(int userId);

	public void deleteRedisKey(String key);


}
