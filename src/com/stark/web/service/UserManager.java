package com.stark.web.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.RelUserFollow;
import com.stark.web.entity.RelUserFriend;
import com.stark.web.entity.UserInfo;
import com.stark.web.hunter.FileManager;
import com.stark.web.dao.IUserDAO;
import com.stark.web.define.EnumBase;
import com.stark.web.define.RedisInfo;
import com.stark.web.define.EnumBase.UserRole;

public class UserManager implements IUserManager{

	
	private IUserDAO userDao;
	
	//@Resource
	//private IArticleManager articleManager;
	
	
	public void setUserDao(IUserDAO userDao) {
		this.userDao = userDao;
	}
	
	//public void setArticleManager(IArticleManager articleManager){
	//	this.articleManager = articleManager;
	//}

	@Override
	public int addUser(UserInfo uInfo) {
		uInfo.setPhoneNumber("");
		uInfo.setSignature("");
		
		int userId = userDao.addUser(uInfo);
		if(userId>0){
			uInfo.setUserId(userId);
			userDao.addRedisUser(uInfo);
			int role = uInfo.getRole();
			userDao.addRedisUsers(RedisInfo.USERNAMELIST+uInfo.getName(), userId);
			if(role==UserRole.Normal.getIndex()){
				userDao.addRedisUserCount(RedisInfo.NORMALCOUNT);
				String key = RedisInfo.USERNORMALLIST;
				userDao.addRedisUsersL(key,userId);
				
			}
			else if(role==UserRole.Operatior.getIndex()){
				userDao.addRedisUserCount(RedisInfo.OPERATIORCOUNT);
				userDao.addRedisUsers(RedisInfo.USEROSLIST, userId);
			}
			else if(role==UserRole.Simulation.getIndex()||uInfo.getRole()==UserRole.Important.getIndex()){
				userDao.addRedisUserCount(RedisInfo.SIMULATIONCOUNT);
				userDao.addRedisUsers(RedisInfo.USEROSLIST, userId);
			}
			else if(role==UserRole.Organization.getIndex()){
				userDao.addRedisUserCount(RedisInfo.USERORGANIZATIONCOUNT);
				userDao.addRedisUsers(RedisInfo.USERORGANIZATIONLIST, userId);
				userDao.addRedisUsers(RedisInfo.USEROSLIST, userId);
			}
			String email = uInfo.getEmail();
			if(email!=null&&uInfo.getPassword()!=null){
				userDao.addRedisUserEmailPassword(email,uInfo.getPassword(),userId);
				userDao.addRedisEmails(email);
			}
			
		}
		return userId;
	}

	@Override
	public boolean updateUser(UserInfo uInfo) {
		userDao.updateRedisUser(uInfo);
		return userDao.updateUser(uInfo);
	}

	@Override
	public UserInfo getUser(int id) {
		UserInfo user = userDao.getRedisUser(id);
		//System.out.println(user);
		if(user==null){
			user = userDao.getUserInfo(id);
			if(user!=null){
				userDao.addRedisUser(user);
				user = userDao.getRedisUser(id);
				//setUserCount(user);
			}
			
		}
		
		return user;
	}

	@Override
	public List<UserInfo> getUserById(String id) {
		return null;
	}

	@Override
	public List<UserInfo> getMustFollowUserList() {
		return userDao.getUsersByRole(EnumBase.UserRole.MustFollow.getIndex());
	}

	@Override
	public List<UserInfo> getRecommendUserList() {
		return userDao.getUsersByRole(EnumBase.UserRole.Recommend.getIndex());
	}

	@Override
	public UserInfo verificationUser(String number, String password) {
		
		return userDao.verificationUser(number,password);
	}

	@Override
	public boolean addFriend(RelUserFriend rel) {
		
		return userDao.addFriend(rel);
	}

	@Override
	public RelUserFriend getRelUserFriend(String relId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteFriend(RelUserFriend rel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addFollow(RelUserFollow rel) {
		if(isFollow(rel.getUser().getUserId(),rel.getFollow().getUserId())){
			return false;
		}
		boolean result = userDao.addFollow(rel);
		if(result){
			//addFollowingCount(rel.getUser().getUserId());
			//addFansCount(rel.getFollow().getUserId());
			userDao.addRedisFollowingZSet(rel);
			userDao.addRedisFansZSet(rel);
			String key = RedisInfo.USERFOLLOWFLAG+rel.getUser().getUserId()+":"+rel.getFollow().getUserId();
			userDao.setRedisString(key, "1");
		}
		return result;
	}

	@Override
	public UserInfo getUserByDoorplate(int doorplate) {
		return userDao.getUserByDoorplate(doorplate);
	}

	@Override
	public List<UserInfo> getFollowList(int userId,int page,int maxCount) {
		UserInfo u = getUser(userId);
		int count = u.getFollowingCount();
		Set<String> ids = userDao.getRedisUserIds(RedisInfo.USERFOLLOWZSET+userId,page,maxCount);
		List<UserInfo> list = new ArrayList<UserInfo>();
		int size = 0;
		if(ids!=null){
			size = ids.size();
		}
		
		if(ids==null||ids.isEmpty()||page*maxCount+size<count){
			List<RelUserFollow> ruf = userDao.getUserFollowList(userId,page*maxCount+size,maxCount-size);
			if(ruf!=null&&!ruf.isEmpty()){
				for(RelUserFollow rel : ruf){
					UserInfo user = getUser(rel.getFollow().getUserId());
					if(user!=null){
						list.add(user);
						userDao.addRedisOnlyFollowingZSet(rel);
						String key = RedisInfo.USERFOLLOWFLAG+rel.getUser().getUserId()+":"+rel.getFollow().getUserId();
						userDao.setRedisString(key, "1");
//						setUserCount(user);
						//addFollowingCount(rel.getUser().getUserId());
					}
				}
			}
			return list;
		}
		
//		if(size==maxCount){
			for(String id:ids){
				UserInfo user = getUser(Integer.parseInt(id));
				if(user!=null){
					list.add(user);
				}
			}
			return list;
//		}
		
//		if(ids!=null&&!ids.isEmpty()){
//			size=ids.size();
//			for(String id:ids){
//				UserInfo user = getUser(Integer.parseInt(id));
//				if(user!=null){
//					list.add(user);
//				}
//			}
//			if(size==maxCount)
//				return list;
//		}
//		List<RelUserFollow> ruf = userDao.getUserFollowList(userId,page*maxCount+size,maxCount-size);
//		System.out.println(ruf.size());
//		//list = userDao.getFollowList(userId,page,maxCount);
//		if(ruf!=null&&!ruf.isEmpty()){
//			for(RelUserFollow rel : ruf){
//				UserInfo user = getUser(rel.getFollow().getUserId());
//				if(user!=null){
//					list.add(user);
//					userDao.addRedisOnlyFollowingZSet(rel);
////					setUserCount(user);
//					//addFollowingCount(rel.getUser().getUserId());
//				}
//			}
//		}
//		return list;
	}

	@Override
	public List<UserInfo> getFansList(int userId,int page, int maxCount) {
		UserInfo u = getUser(userId);
		int count = u.getFansCount();
		Set<String> ids = userDao.getRedisUserIds(RedisInfo.USERFANSZSET+userId,page,maxCount);
		int size = 0;
		if(ids!=null){
			size = ids.size();
		}
		List<UserInfo> list = new ArrayList<UserInfo>();
		if(ids==null||ids.isEmpty()||page*maxCount+size<count){
			List<RelUserFollow> ruf = userDao.getUserFansList(userId,page*maxCount,maxCount);
			if(ruf!=null&&!ruf.isEmpty()){
				for(int i=page*maxCount;i<(page+1)*maxCount&&i<ruf.size();i++){
					RelUserFollow rel = ruf.get(i);
					UserInfo user = getUser(rel.getUser().getUserId());
					if(user!=null){
						list.add(user);
						userDao.addRedisOnlyFansZSet(rel);
					}
				}
			}
			return list;
		}
		
//		if(size==maxCount){
			for(String id:ids){
				UserInfo user = getUser(Integer.parseInt(id));
				if(user!=null){
					list.add(user);
				}
			}
			return list;
//		}
		
//		return list;
//		if(ids!=null&&!ids.isEmpty()){
//			size=ids.size();
//			for(String id:ids){
//				UserInfo user = getUser(Integer.parseInt(id));
//				if(user!=null){
//					list.add(user);
//				}
//			}
//			if(size==maxCount)
//				return list;
//		}
//		List<RelUserFollow> ruf = userDao.getUserFansList(userId,page*maxCount+size,maxCount-size);
//		System.out.println(ruf.size());
//		//list = userDao.getFollowList(userId,page,maxCount);
//		if(ruf!=null&&!ruf.isEmpty()){
//			for(RelUserFollow rel : ruf){
//				UserInfo user = getUser(rel.getUser().getUserId());
//				if(user!=null){
//					list.add(user);
//					userDao.addRedisOnlyFansZSet(rel);
////					setUserCount(user);
//					//addFansCount(rel.getFollow().getUserId());
//				}
//			}
//		}
//		return list;
	}

	@Override
	public boolean isFollow(int userId, int followId) {
		String key = RedisInfo.USERFOLLOWFLAG+userId+":"+followId;
		String flag = userDao.getRedisString(key);
		if(flag==null){
			boolean result = userDao.isFollow(userId, followId);
			if(result){
				userDao.setRedisString(key, "1");
			}
			else {
				userDao.setRedisString(key, "0");
			}
			return result;
		}
		if(flag.equals("1")){
			return true;
		}
		else if(flag.equals("0")){
			return false;
		}
		return false;
//		boolean result = userDao.isRedisFollow(userId,followId);
//		if(!result){
//			result = userDao.isFollow(userId,followId);
//			if(result){
//				RelUserFollow rel = new RelUserFollow();
//				rel.setUser(new UserInfo(userId));
//				rel.setFollow(new UserInfo(followId));
//				rel.setDate(new Date());
//				userDao.addRedisOnlyFollowingZSet(rel);
//				
//			}
//		}
//		return result;
	}

	@Override
	public boolean addFollowingCount(int userId) {
		return userDao.addRedisFollowingCount(userId);
	}

	@Override
	public boolean addFansCount(int followId) {
		return userDao.addRedisFansCount(followId);
	}

	@Override
	public UserInfo isAdmin(String username, String password) {
		String key = RedisInfo.USERADMINPASSWORD+username+":"+password;
		String id = userDao.getRedisUserPassword(key);
		if(id!=null&&!id.equals("")){
			return getUser(Integer.parseInt(id));
		}
		UserInfo user = userDao.isExist(username, password, EnumBase.UserRole.Admin.getIndex());
		if(user!=null){
			//userDao.addRedisUser(user);
			getUser(user.getUserId());
			userDao.addRedisUserPassword(key,user.getUserId());
		}
			
		return user;
	}

	@Override
	public List<UserInfo> getOperatiors() {
		List<String> ids = userDao.getRedisUsers(RedisInfo.USEROSLIST);
		List<UserInfo> list = new ArrayList<UserInfo>();
		if(ids!=null&&!ids.isEmpty()){
			
			for(String id : ids){
				UserInfo user =getUser(Integer.parseInt(id));
				if(user!=null){
					list.add(user);
				}
				
			}
			return list;
		}
		
		List<Integer> roles = new ArrayList<Integer>();
		roles.add(UserRole.Operatior.getIndex());
		roles.add(UserRole.Simulation.getIndex());
		roles.add(UserRole.Important.getIndex());
		list = userDao.getUsersByRoles(roles);
		if(list!=null){
			for(UserInfo user : list){
				userDao.addRedisUsers(RedisInfo.USEROSLIST,user.getUserId());
				setUserCount(user);
			}
		}
		return list;
	}

	@Override
	public int isAdminByDoorplate(String doorplate, String password) {
		return userDao.isAdminByDoorplate(doorplate,password,EnumBase.UserRole.Admin.getIndex());
	}

	@Override
	public UserInfo getUserByPhoneNumber(String phoneNumber) {
		return userDao.getUserByPhoneNumber(phoneNumber);
	}

	@Override
	public UserInfo getUserByQQOpenId(String qqOpendId) {
		return userDao.getUserByQQOpenId(qqOpendId);
	}

	@Override
	public UserInfo getUserBySinaOpenId(String sinaOpenId) {
		return userDao.getUserBySinaOpenId(sinaOpenId);
	}

	@Override
	public List<UserInfo> getAllUser() {
		String key = RedisInfo.USERALLSET;
		Set<String> ids = userDao.getRedisUserSet(key);
		if(ids!=null&&!ids.isEmpty()){
			List<UserInfo> users = idSetToUserList(ids);
			return users;
		}
		List<UserInfo> list = userDao.getAllUser();
		addRedisUserList(key,list);
		return list;
	}

	private void addRedisUserList(String key, List<UserInfo> list) {
		if(list!=null){
			for(UserInfo user:list){
				if(user!=null){
					userDao.addRedisUser(user);
					userDao.addRedisUserSet(key,user.getUserId());
				}
				
			}
		}
		
	}

	private List<UserInfo> idSetToUserList(Set<String> ids) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		if(ids==null||ids.isEmpty()){
			return null;
		}
		for(String id:ids){
			UserInfo user = getUser(Integer.parseInt(id));
			list.add(user);
		}
		return list;
	}

	@Override
	public boolean deleteUser(String userId) {
		return userDao.deleteUser(userId);
	}

	@Override
	public List<UserInfo> getFateFriend(String userId) {
		return userDao.getFateFriend(userId);
	}

	@Override
	public long getMaxDoorplate() {
		return userDao.getMaxDoorplate();
	}

	@Override
	public boolean addUserHeadPic(int userId, String fileName) {
		boolean result = userDao.addUserHeadPic(userId,fileName);
		if(result){
			userDao.addRedisUserHeadPic(userId, fileName);
		}
		return result;
	}

	@Override
	public boolean removeFollow(int userId,int followId) {
		boolean result = userDao.removeFollow(userId,followId);
		if(result){
			userDao.removeRedisFollow(userId,followId);
			userDao.removeRedisFans(userId,followId);
			
			subFollowingCount(userId);
			subFansCount(followId);
			String key = RedisInfo.USERFOLLOWFLAG+userId+":"+followId;
			userDao.setRedisString(key, "0");
		}
		return result;
	}

	@Override
	public List<UserInfo> getPraiseUsers(int articleId) {
		return userDao.getPraiseUser(articleId);
	}

	@Override
	public int getFansCount(int userId) {
		int count = userDao.getRedisFansCount(userId);
		if(count==0){
			count = getUser(userId).getFansCount();
			//userDao.setRedisFansCount(userId,count);
		}
		return count;
	}

	@Override
	public int getFollowCount(int userId) {
		int count = userDao.getRedisFollowsCount(userId);
		if(count==0){
			count = getUser(userId).getFollowingCount();
			//userDao.setRedisFollowCount(userId,count);
		}
		return count;
	}

	@Override
	public int getDoorplateById(int userId) {
		return userDao.getDoorplateById(userId);
	}

	@Override
	public boolean updateUserName(int userId, String name) {
		return userDao.updateUserName(userId,name);
	}
	
	@Override
	public Map<String, Object> userToMap(UserInfo user){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("userId", user.getUserId());
		//map.put("doorplate", user.getDoorplate());
		map.put("name", user.getName());
		map.put("sex",user.getSex());
		//map.put("phoneNumber", user.getPhoneNumber());
		//map.put("birthday", format.format(user.getBirthday()));
		//map.put("signature", user.getSignature());
		String homeTown = user.getHomeTown();
		if(homeTown==null){
			map.put("homeTown","");
		}else {
			map.put("homeTown",user.getHomeTown());
		}
		
		String email = user.getEmail();
		if(email!=null){
			map.put("email", email);
		}
		
		map.put("headPic",user.getHeadUrl());
		map.put("fansCount", user.getFansCount());
		map.put("followCount", user.getFollowingCount());
		map.put("articleCount", user.getArticleCount());
		map.put("praiseCount", user.getPraiseCount());
		map.put("noticeStatus", user.getNoticeStatus());
		map.put("email", user.getEmail());
		//map.put("type", new ArrayList<String>(user.getType()));
		
		return map;
	}

	public int getArticleCount(int userId) {
	    
	    return userDao.getArticleCount(userId);
	}

	@Override
	public UserInfo isOperatior(String name, String password) {
		String key = RedisInfo.USEROPERATIORPASSWORD+name+":"+password;
		String id = userDao.getRedisUserPassword(key);
		if(id!=null&&!id.equals("")){
			return getUser(Integer.parseInt(id));
		}
		UserInfo user = userDao.isExist(name, password, EnumBase.UserRole.Operatior.getIndex());
		if(user!=null){
			getUser(user.getUserId());
			userDao.addRedisUserPassword(key, user.getUserId());
		}
		return user;
	}

	@Override
	public int getOperatiorCount() {
		String count = userDao.getRedisUserCount(RedisInfo.OPERATIORCOUNT);
		if(count==null||count.equals("")){
			int c = userDao.getUserCountByRole(UserRole.Operatior.getIndex());
			userDao.setRedisUserCount(RedisInfo.OPERATIORCOUNT,c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public int getSimulationCount() {
		String count = userDao.getRedisUserCount(RedisInfo.SIMULATIONCOUNT);
		if(count==null||count.equals("")){
			int c = userDao.getUserCountByRole(UserRole.Simulation.getIndex());
			int c1 = userDao.getUserCountByRole(UserRole.Important.getIndex());
			userDao.setRedisUserCount(RedisInfo.SIMULATIONCOUNT,c+c1);
			return c+c1;
		}
		return Integer.parseInt(count);
	}

	@Override
	public int getNormalCount() {
		String count = userDao.getRedisUserCount(RedisInfo.NORMALCOUNT);
		if(count==null||count.equals("")){
			int c = userDao.getUserCountByRole(UserRole.Normal.getIndex());
			userDao.setRedisUserCount(RedisInfo.NORMALCOUNT,c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public UserInfo isExistQQOpenId(String qqOpenId) {
		String key = RedisInfo.USERQQOPENID+qqOpenId;
		String id = userDao.getRedisOpenId(key);
		UserInfo user = null;
		if(id==null){
			user = userDao.isExistQQOpenId(qqOpenId);
			if(user!=null){
				userDao.setRedisOpenId(key,user.getUserId());
			}
		}
		else {
			user = getUser(Integer.parseInt(id));
		}
		return user;
	}

	@Override
	public UserInfo isExistSinaOpenId(String sinaOpenId) {
		String key = RedisInfo.USERSINAOPENID+sinaOpenId;
		String id = userDao.getRedisOpenId(key);
		UserInfo user = null;
		if(id==null){
			user = userDao.isExistSinaOpenId(sinaOpenId);
			if(user!=null){
				userDao.setRedisOpenId(key,user.getUserId());
			}
		}
		else {
			user = getUser(Integer.parseInt(id));
		}
		return user;
		//return userDao.isExistSinaOpenId(sinaOpenId);
	}

	@Override
	public UserInfo isExistWeChatOpenId(String weChatOpenId) {
		String key = RedisInfo.USERWECHATOPENID+weChatOpenId;
		String id = userDao.getRedisOpenId(key);
		UserInfo user = null;
		if(id==null){
			user = userDao.isExistWeChatOpenId(weChatOpenId);
			if(user!=null){
				userDao.setRedisOpenId(key,user.getUserId());
			}
		}
		else {
			user = getUser(Integer.parseInt(id));
		}
		return user;
		//return userDao.isExistWeChatOpenId(weChatOpenId);
	}

	@Override
	public String getUserHeadPic(int userId, String headPicUrl) {
		//String imageName = headPicUrl.substring(headPicUrl.lastIndexOf("/") + 1, headPicUrl.length());
		//if(!imageName.toLowerCase().endsWith(".jpg")&&!imageName.toLowerCase().endsWith(".png")&&!imageName.toLowerCase().endsWith(".jpeg")&&!imageName.toLowerCase().endsWith(".bmp")){
		//	imageName = imageName+".jpeg";
		//}
		
		SimpleDateFormat sdf = WebManager.getDateFormat();
		String imageName = sdf.format(new Date());
		String path = FileManager.getUserPicturePath(userId, imageName);
		String suffix = FileManager.uploadURL(path, headPicUrl);
		userDao.addUserHeadPic(userId,imageName+suffix);
		userDao.addRedisUserHeadPic(userId,imageName+suffix);
		return imageName;
	}

	@Override
	public void subFollowingCount(int userId) {
		userDao.subRedisFollowingCount(userId);
	}

	@Override
	public void subFansCount(int followId) {
		userDao.subRedisFansCount(followId);
	}
	
	private void setUserCount(UserInfo user){
		int id = user.getUserId();
		int count = userDao.getFansCount(user.getUserId());
		user.setFansCount(count);
		count = userDao.getFollowCount(user.getUserId());
		user.setFollowingCount(count);
		count = userDao.getArticleCount(id);
		user.setArticleCount(count);
		count = userDao.getPraiseCount(id);
		user.setPraiseCount(count);
	}

	@Override
	public void addChildUser(int parentId, int userId) {
		int result = userDao.addChildUser(parentId,userId);
		if(result>0){
			userDao.addRedisChildUser(RedisInfo.USERCHILDLIST+parentId,userId);
		}
	}

	@Override
	public List<UserInfo> getChildUser(int userId) {
		List<String> ids = userDao.getRedisUsers(RedisInfo.USERCHILDLIST+userId);
		if(ids!=null&&!ids.isEmpty()){
			List<UserInfo> users = new ArrayList<UserInfo>();
			for(String id:ids){
				UserInfo user = getUser(Integer.parseInt(id));
				if(user!=null){
					users.add(user);
				}
			}
			return users;
		}
		List<UserInfo> users = new ArrayList<UserInfo>();
		List<Object[]> lists = userDao.getChildUser(userId);
		for(int i=0;i<lists.size();i++){
			Object[] objs = lists.get(i);
			UserInfo user = new UserInfo();
			int uId = (int)objs[0];
			user.setUserId(uId);
			user.setName(""+objs[1]);
			user.setPassword(""+objs[2]);
			user.setSex((int)objs[3]);
			user.setRole((int)objs[4]);
			user.setHomeTown(""+objs[5]);
			user.setHeadPic(""+objs[6]);
			users.add(user);
			
		}
		for(int i=users.size()-1;i>=0;i--){
			UserInfo user = users.get(i);
			userDao.addRedisUser(user);
		}
		return users;
	}

	@Override
	public boolean isChildUser(int userId, int childId) {
		boolean isChild = userDao.isChildUser(userId,childId);
		return isChild;
	}

	@Override
	public UserInfo verificationEmail(String email, String password) {
		String userId = userDao.getRedisByEmailPassword(email,password);
		if(userId!=null&&!userId.equals("")){
			UserInfo user = getUser(Integer.parseInt(userId));
			return user;
		}
		UserInfo user = userDao.verificationEmail(email,password);
		if(user!=null){
			userDao.addRedisUser(user);
		}
		return user;
	}

	@Override
	public boolean verificationEmail(String email) {
		boolean result = userDao.isRedisEmail(email);
		if(!result){
			result = userDao.verificationEmail(email);
			if(result){
				userDao.addRedisEmails(email);
			}
		}
		
		return result;
	}

	@Override
	public boolean logout(int userId) {
		
		return userDao.addRedisLogoutUser(userId);
	}

	@Override
	public Set<String> getLogoutUser() {
		return userDao.getRedisLogoutUser();
	}

	@Override
	public List<UserInfo> getMeetList(int userId,int sex,int maxCount) {
		List<UserInfo> uList = null;
		if(sex==2){
			uList = userDao.getMeetList(maxCount);
		}
		else {
			uList = userDao.getMeetList(sex,maxCount);
		}
		
		return uList;
	}

	@Override
	public void addFollowArticle(int userId, List<ArticleInfo> alist) {
		
	}

	@Override
	public List<UserInfo> getAllFansList(int userId) {
		List<UserInfo> users = new ArrayList<UserInfo>();
		String key = RedisInfo.USERFANSZSET+userId;
		Set<String> ids = userDao.getRedisZSetUserIds(key);
		if(ids!=null&&!ids.isEmpty()){
			int size = ids.size();
			UserInfo user = getUser(userId);
			if(user.getFansCount()==size){
				for(String id :ids){
					users.add(getUser(Integer.parseInt(id)));
				}
				return users;
			}
		}
		
		users.clear();
		List<RelUserFollow> ruf = userDao.getUserFansList(userId,0,-1);
		if(users!=null){
			userDao.deleteRedisKey(key);
			for(RelUserFollow rel : ruf){
				
				UserInfo u = getUser(rel.getUser().getUserId());
				if(u!=null){
					users.add(u);
					userDao.addRedisOnlyFansZSet(rel);
				}
			}
		}
		
		
		return users;
	}

	@Override
	public List<UserInfo> getUserByName(String name) {
		String key = RedisInfo.USERNAMELIST+name;
		List<String> ids = userDao.getRedisUsers(key);
		List<UserInfo> list = null;
		list = idsToUserList(ids);
		if(list!=null&&!list.isEmpty())
			return list;
		
		List<UserInfo> users = userDao.getUserByName(name);
		if(users!=null&&!users.isEmpty()){
			for(UserInfo user:users)
			userDao.addRedisUsers(key, user.getUserId());
		}
		return users;
	}

	private List<UserInfo> idsToUserList(List<String> ids) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		if(ids==null||ids.isEmpty()){
			return null;
		}
		for(String id:ids){
			UserInfo user = getUser(Integer.parseInt(id));
			list.add(user);
		}
		return list;
	}

	@Override
	public List<UserInfo> getMarkUsers() {
		String key = RedisInfo.USERMARKLIST;
		List<String> ids = userDao.getRedisUsers(key);
		//System.out.println(ids.size());
		if(ids!=null&&!ids.isEmpty()){
			List<UserInfo> list = idsToUserList(ids);
			return list;
		}
		
		List<UserInfo> users = userDao.getUsersByRole(UserRole.Mark.getIndex());
		//System.out.println(ids.size());
		if(users!=null&&!users.isEmpty()){
			for(UserInfo user:users)
			userDao.addRedisUsers(key, user.getUserId());
		}
		return users;
	}

	@Override
	public boolean markUser(int userId) {
		String key = RedisInfo.USERMARKLIST;
		boolean result = userDao.updateUserInfo(userId,"role",UserRole.Mark.getIndex());
		if(result){
			userDao.updateRedisUserInfo(UserInfo.getKey(userId),UserInfo.ROLE,UserRole.Mark.getIndex()+"");
			userDao.addRedisUsers(key, userId);
		}
		
		return result;
	}

	@Override
	public List<UserInfo> getLastUsers(int maxResult) {
		String key = RedisInfo.USERNORMALLIST;
		List<String> ids = userDao.getRedisUsers(key,maxResult);
		List<UserInfo> list = idsToUserList(ids);
		if(list!=null&&!list.isEmpty())
			return list;
		List<Integer> roles = new ArrayList<Integer>();
		roles.add(UserRole.Normal.getIndex());
		roles.add(UserRole.Mark.getIndex());
		List<UserInfo> users = userDao.getUsersByRoles(roles,maxResult);
		if(users!=null&&!users.isEmpty()){
			for(UserInfo user:users)
			userDao.addRedisUsers(key, user.getUserId());
		}
		return users;
	}

	@Override
	public List<UserInfo> getPraiseUsers(int articleId, int page, int maxUserCount) {
		String key = RedisInfo.ARTICLEPRAISELIST+articleId;
		List<String> ids = userDao.getRedisUsers(key,page,maxUserCount);
		List<UserInfo> list = idsToUserList(ids);
		if(list!=null&&!list.isEmpty())
			return list;
		List<UserInfo> users = userDao.getPraiseUser(articleId,page,maxUserCount);
		if(users!=null&&!users.isEmpty()){
			for(UserInfo user:users)
			userDao.addRedisUsers(key, user.getUserId());
		}
		return users;
	}

	@Override
	public int getOrganizationCount() {
		String count = userDao.getRedisUserCount(RedisInfo.USERORGANIZATIONCOUNT);
		if(count==null||count.equals("")){
			int c = userDao.getUserCountByRole(UserRole.Organization.getIndex());
			userDao.setRedisUserCount(RedisInfo.USERORGANIZATIONCOUNT,c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public void uploadQQHeadPic() {
		//SimpleDateFormat sdf = WebManager.getDateFormat();
		//String imageName = sdf.format(new Date());
		//String path = FileManager.getUserPicturePath(userId, imageName);
		String suffix = FileManager.uploadURL("User/Temp", "http://wx.qlogo.cn/mmopen/PiajxSqBRaEIfMCtGDicyQJq4sqtJhWOPYPLCJLImwLmqr3kM59GzxmCdTQKubfo0Ve6mkQ0pdpYg4Wib5FwYia4GQ/0");
	}

	
}
