package com.stark.web.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.stark.web.define.RedisInfo;
import com.stark.web.define.EnumBase.ArticleType;
import com.stark.web.define.EnumBase.UserRole;
import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.RelUserFollow;
import com.stark.web.entity.RelUserFriend;
import com.stark.web.entity.UserInfo;
import com.stark.web.service.WebManager;

public class UserDAO implements IUserDAO{

	private SessionFactory sessionFactory;
	
	private RedisTemplate<String, String> redisTemplate;
	private IRedisDAO redisDao;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	public IRedisDAO getRedisDao() {
		return redisDao;
	}
	public void setRedisDao(IRedisDAO redisDao) {
		this.redisDao = redisDao;
	}
	
	
	@Override
	public int addUser(UserInfo uInfo) {
		int id = (int)sessionFactory.getCurrentSession().save(uInfo);
		return id;
	}
	@Override
	public boolean updateUser(UserInfo uInfo) {
		//String hql = "update UserInfo u set u=?  where u.userId=?";
		//Query query = sessionFactory.getCurrentSession().createQuery(hql);
		//query.set(0, uInfo);
		//query.set
		//query.setString(1,uInfo.getId());
		sessionFactory.getCurrentSession().update(uInfo);
		return true;
		//return false;
	}
	
	@Override
	public UserInfo getUserInfo(int id) {
		//String hql = "from UserInfo u where u.id=?";
		//Query query = sessionFactory.getCurrentSession().createQuery(hql);
		//query.setString(0, id);
		//int userId = Integer.parseInt(id);
		UserInfo user = (UserInfo)sessionFactory.getCurrentSession().get(UserInfo.class, id);
		//return (UserInfo) query.uniqueResult();
		return user;
	}
	
	@Override
	public List<UserInfo> getAllUser() {
		String hql = "from UserInfo";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
		//return null;
	}
	
	@Override
	public boolean addFriend(RelUserFriend rel) {
		sessionFactory.getCurrentSession().save(rel);
		return false;
	}
	
	@Override
	public boolean addFollow(RelUserFollow rel) {
		int id = (int)sessionFactory.getCurrentSession().save(rel);
		return id>0;
	}
	
	@Override
	public UserInfo getUserByDoorplate(int doorplate) {

		String hql = "from UserInfo as u where u.doorplate = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, doorplate);
		query.setMaxResults(1);
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public List<UserInfo> getFollowList(String userId,int page,int maxCount) {
		String hqlString = "select u from UserInfo as u,RelUserFollow as r where r.user.userId = ? and r.follow.userId=u.userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		query.setString(0, userId);
		query.setFirstResult(page*maxCount);
		query.setMaxResults(maxCount);
		return query.list();
	}
	
	@Override
	public List<UserInfo> getFansList(int userId,int page,int maxCount) {
		String hqlString = "select u from UserInfo as u,RelUserFollow as r where r.follow.userId = ? and r.user.userId=u.userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		query.setInteger(0, userId);
		query.setFirstResult(page*maxCount);
		query.setMaxResults(maxCount);
		return query.list();
	}
	
	@Override
	public boolean isFollow(int userId, int followId) {
		String hql = "from RelUserFollow as r where r.user.userId = ? and r.follow.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		query.setInteger(1, followId);
		return query.uniqueResult() != null;
	}
	
	@Override
	public boolean addRedisFollowingCount(int userId) {
		//String sql = "update USERINFO as u set u.FOLLOWINGCOUNT = u.FOLLOWINGCOUNT+1 where u.ID = ?";
		//Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		//query.setInteger(0, userId);
		long result = redisDao.hincrby(UserInfo.getKey(userId), UserInfo.FOLLOWINGCOUNT, 1);
		return result>0;
	}
	
	@Override
	public boolean addRedisFansCount(int followId) {
		
		//String sql = "update USERINFO as u set u.FANSCOUNT = u.FANSCOUNT+1 where u.ID = ?";
		//Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		//query.setInteger(0, followId);
		long result = redisDao.hincrby(UserInfo.getKey(followId), UserInfo.FANSCOUNT, 1);
		return result>0;
	}
	
	@Override
	public UserInfo isExist(String username, String password,int role) {
		//System.out.println(username+password+role);
		String hql = "from UserInfo as u where u.name =:name and u.password=:password and u.role =:role";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString("name", username);
		query.setString("password", password);
		query.setInteger("role", role);
		
		
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public List<UserInfo> getUsersByRole(int role) {
		String hql = "from UserInfo as u where u.role = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, role);
		
		return query.list();
	}
	
	@Override
	public UserInfo verificationUser(String number, String password) {
		String hql = "from UserInfo as u where u.phoneNumber = ? and u.password=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, number);
		query.setString(1, password);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UserInfo) query.uniqueResult();
	}
	
	@Override
	public int isAdminByDoorplate(String doorplate, String password,int role) {
		String hql = "select u.userId from UserInfo as u where u.doorplate = ? and u.password = ? and u.role = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, doorplate);
		query.setString(1, password);
		query.setInteger(2, role);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (int)query.uniqueResult();
	}
	
	@Override
	public UserInfo getUserByPhoneNumber(String phoneNumber) {
		String hql = "from UserInfo as u where u.phoneNumber = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, phoneNumber);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public UserInfo getUserByQQOpenId(String qqOpenId) {
		String hql = "from UserInfo as u where u.qqOpenId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, qqOpenId);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public UserInfo getUserBySinaOpenId(String sinaOpenId) {
		String hql = "from UserInfo as u where u.sinaOpenId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sinaOpenId);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public boolean deleteUser(String userId) {
		String hql = "delete from UserInfo as u where u.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		
		return query.executeUpdate()>0;
	}
	
	@Override
	public List<UserInfo> getUsersByPage(String userId) {
		String hql = "select u from UserInfo as u, RelUserFollow as rel where u.userId = rel.follow.userId and rel.user.userId != ?  order by rand()";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		query.setFirstResult(0);
		query.setMaxResults(10);
		return null;
	}
	
	@Override
	public long getMaxDoorplate() {
		String hql = "select max(u.doorplate) from UserInfo as u";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		Object result = query.uniqueResult();
		if(result==null)
			return 0;
		int max = (int)result;
		return max;
	}
	
	@Override
	public boolean addUserHeadPic(int userId, String fileName) {
		String sql = "update UserInfo as u set u.HeadPic = ? where u.Id = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, fileName);
		query.setInteger(1, userId);
		return query.executeUpdate()>0;
	}
	
	@Override
	public boolean removeFollow(int userId,int followId) {
		String hql = "delete from RelUserFollow as r where r.user.userId = ? and r.follow.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		query.setInteger(1, followId);
		return query.executeUpdate()>0;
	}
	
	@Override
	public List<UserInfo> getPraiseUser(int articleId) {
		String hql = "select u from UserInfo as u join u.praiseArticle as a where a.articleId = ?";
		//String sql = "select u.* from USERINFO as u,ARTICLEINFO as a,RELARTICLEINFO as rel where u.Id = rel.USERID and rel.ARTICLEID = a.ID and a.ID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, articleId);
		query.setFirstResult(0);
		query.setMaxResults(9);
		return query.list();
	}
	
	@Override
	public int getFansCount(int userId) {
		String hql = "select count(*) from RelUserFollow as r where r.follow.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		List list = query.list();
		return Integer.parseInt(list.get(0)+"");
	}
	
	@Override
	public int getFollowCount(int userId) {
		String hql = "select count(*) from RelUserFollow as r where r.user.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		List list = query.list();
		return Integer.parseInt(list.get(0)+"");
	}
	
	@Override
	public int getDoorplateById(int userId) {
		String hql = "select u.doorplate from UserInfo as u where u.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		return 0;
	}
	
	@Override
	public boolean updateUserName(int userId, String name) {
		String hql = "update UserInfo as u set u.name = ? where u.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, name);
		query.setInteger(1, userId);
		
		return query.executeUpdate()>0;
	}
	
	@Override
	public List<UserInfo> getFateFriend(String userId) {
		String hql = "select u from UserInfo as u where u.userId not in (select rel.follow.userId from RelUserFollow as rel where rel.user.userId = ?)  order by rand()";
		//String hql = "select u from UserInfo as u, RelUserFollow as rel where rel.user.userId = ? and rel.follow.userId  != u.userId  order by rand()";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		query.setFirstResult(0);
		query.setMaxResults(10);
		return query.list();
	}
	
	@Override
	public List<UserInfo> getUsersByRole(int role, int role2, int role3) {
		String hql = "select u from UserInfo as u where u.role=? or u.role=? or u.role=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, role);
		query.setInteger(1, role2);
		query.setInteger(2, role3);
		
		return query.list();
	}
	@Override
	public int getArticleCount(int userId) {
	    String hql = "select count(*) from ArticleInfo as a where a.user.userId=? and a.type!=?";
	    Query query = sessionFactory.getCurrentSession().createQuery(hql);
	    query.setInteger(0, userId);
	    query.setInteger(1, ArticleType.Delete.getIndex());
	    List list = query.list();
	    return Integer.parseInt(list.get(0)+"");
	}
	@Override
	public int getUserCountByRole(int role) {
		String hql = "select count(u.userId) from UserInfo as u where u.role = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, role);
		String count = query.uniqueResult()+"";
		return Integer.parseInt(count);
	}
	@Override
	public UserInfo isExistQQOpenId(String qqOpenId) {
		String hql = "from UserInfo as u where u.qqOpenId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, qqOpenId);
		query.setMaxResults(1);
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public UserInfo isExistSinaOpenId(String sinaOpenId) {
		String hql = "from UserInfo as u where u.sinaOpenId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sinaOpenId);
		query.setMaxResults(1);
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public UserInfo isExistWeChatOpenId(String weChatOpenId) {
		String hql = "from UserInfo as u where u.weChatOpenId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, weChatOpenId);
		query.setMaxResults(1);
		return (UserInfo)query.uniqueResult();
	}
	
	@Override
	public boolean addRedisUser(UserInfo uInfo) {
		if(redisDao==null)
			return false;
		Map<String, String> map = new HashMap<String, String>();
		map.put(UserInfo.NAME, uInfo.getName());
		map.put(UserInfo.ROLE, uInfo.getRole()+"");
		map.put(UserInfo.SEX, uInfo.getSex()+"");
		if(uInfo.getPassword()!=null){
			map.put(UserInfo.PASSWORD, uInfo.getPassword());
		}
		
		if(uInfo.getHomeTown()!=null){
			map.put(UserInfo.HOMETOWN,uInfo.getHomeTown());
		}
		
		if(uInfo.getHeadPic()==null){
			map.put(UserInfo.HEADPIC, "");
		}
		else{
			map.put(UserInfo.HEADPIC, uInfo.getHeadPic());
		}
		
		if(uInfo.getEmail()!=null){
			map.put(UserInfo.EMAIL, uInfo.getEmail());
		}
		int count = getFollowCount(uInfo.getUserId());
		map.put(UserInfo.FOLLOWINGCOUNT, count+"");
		count = getFansCount(uInfo.getUserId());
		map.put(UserInfo.FANSCOUNT, count+"");
		count = getArticleCount(uInfo.getUserId());
		map.put(UserInfo.ARTICLECOUNT, count+"");
		count = getPraiseCount(uInfo.getUserId());
		map.put(UserInfo.PRAISECOUNT, count+"");
		
		String qqOpenId = uInfo.getQqOpenId();
		
		if(qqOpenId!=null){
			map.put(UserInfo.QQOPENID, qqOpenId);
		}
		String sinaOpenId = uInfo.getSinaOpenId();
		if(sinaOpenId!=null){
			map.put(UserInfo.SINAOPENID, sinaOpenId);
		}
		String weChatOpenId = uInfo.getWeChatOpenId();
		if(weChatOpenId!=null){
			map.put(UserInfo.WECHATOPENID,weChatOpenId);
		}
		Date date = uInfo.getLastLogonDate();
		if(date!=null){
			SimpleDateFormat sdf=WebManager.getDateFormat();
			map.put(UserInfo.LASTLOGONDATE, sdf.format(date));
		}
		
		//System.out.println(map);
		return redisDao.hmset( uInfo.getKey(),map)!=null;
	}

	@Override
	public int getPraiseCount(int userId) {
		String sql = "select count(rel.UserId) from RelArticlePraise as rel ,ArticleInfo as a where a.UserId = ? and a.Id = rel.ArticleId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, userId);
		String count = query.uniqueResult()+"";
		return Integer.parseInt(count);
	}

	@Override
	public boolean addRedisUserHeadPic(int userId, String imageName) {
		//RedisSerializer<String> serializer = redisDao.getRedisSerializer();
		//BoundHashOperations<String, byte[], byte[]> boundHashOperations = redisDao.getRedisTemplate().boundHashOps(UserInfo.getKey(userId));
		//boundHashOperations.put(serializer.serialize(UserInfo.HEADPIC), serializer.serialize(imageName));
		if(redisDao==null)
			return false;
		return redisDao.hset(UserInfo.getKey(userId),UserInfo.HEADPIC,imageName)!=null;
	}

	@Override
	public UserInfo getRedisUser(int id) {
		if(redisDao==null)
			return null;
		Map<String, String> map = redisDao.hgetAll(UserInfo.getKey(id));
		if(map!=null&&!map.isEmpty()){
			UserInfo user = new UserInfo();
			user.setUserId(id);
			user.setName(map.get(UserInfo.NAME));
			user.setPassword(map.get(UserInfo.PASSWORD));
			String role = map.get(UserInfo.ROLE);
			if(role==null){
				user.setRole(0);
			}
			else {
				user.setRole(Integer.parseInt(role));
			}
			String sex = map.get(UserInfo.SEX);
			if(role==null){
				user.setSex(0);
			}
			else{
				user.setSex(Integer.parseInt(sex));
			}
			user.setHomeTown(map.get(UserInfo.HOMETOWN));
			user.setHeadPic(map.get(UserInfo.HEADPIC));
			String qqOpendId = map.get(UserInfo.QQOPENID);
			if(qqOpendId!=null){
				user.setQqOpenId(qqOpendId);
			}else {
				user.setQqOpenId("");
			}
			String sinaOpenId = map.get(UserInfo.SINAOPENID);
			if(sinaOpenId!=null){
				user.setSinaOpenId(sinaOpenId);
			}
			else {
				user.setSinaOpenId("");
			}
			String weChatOpenId = map.get(UserInfo.WECHATOPENID);
			if(weChatOpenId!=null){
				user.setWeChatOpenId(weChatOpenId);
			}else {
				user.setWeChatOpenId("");
			}
			
			String email = map.get(UserInfo.EMAIL);
			if(email!=null){
				user.setEmail(email);
			}
			else {
				user.setEmail("");
			}
			//user.setSinaOpenId(map.get(UserInfo.SINAOPENID));
			//user.setWeChatOpenId(map.get(UserInfo.WECHATOPENID));
			String followCount = map.get(UserInfo.FOLLOWINGCOUNT);
			if(followCount!=null){
				user.setFollowingCount(Integer.parseInt(followCount));
			}
			
			String fansCount = map.get(UserInfo.FANSCOUNT);
			if(fansCount!=null){
				user.setFansCount(Integer.parseInt(fansCount));
			}
			
			String articleCount = map.get(UserInfo.ARTICLECOUNT);
			if(articleCount!=null){
				user.setArticleCount(Integer.parseInt(articleCount));
			}
			
			String count = map.get(UserInfo.PRAISECOUNT);
			if(count!=null){
				user.setPraiseCount(Integer.parseInt(count));
			}
			
			String noticeStatus = map.get(UserInfo.NOTICESTATUS);
			if(noticeStatus==null){
				user.setNoticeStatus(0);
			}
			else {
				user.setNoticeStatus(Integer.parseInt(noticeStatus));
			}
			
			String date = map.get(UserInfo.LASTLOGONDATE);
			if(date!=null){
				SimpleDateFormat sdf = WebManager.getDateFormat();
				try {
					user.setLastLogonDate(sdf.parse(date));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			return user;
		}
		return null;
	}

	@Override
	public String getRedisUserCount(String key) {
		if(redisDao==null)
			return null;
		return redisDao.get(key);
	}

	@Override
	public void setRedisUserCount(String key, int value) {
		if(redisDao==null)
			return ;
		redisDao.set(key, value+"");
	}

	@Override
	public void addRedisUserCount(String key) {
		if(redisDao==null)
			return;
		redisDao.incr(key);
	}

	@Override
	public List<UserInfo> getUsersByRoles(List<Integer> roles) {
		
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<roles.size();i++){
			
			if(i<roles.size()-1){
				builder.append(" u.role=? or");
			}
			else {
				builder.append(" u.role=?");
			}
		}
		String hql = "from UserInfo as u where "+builder+ "";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i=0;i<roles.size();i++){
			query.setInteger(i, roles.get(i));
		}
		return query.list();
	}
	
	@Override
	public List<UserInfo> getUsersByRoles(List<Integer> roles,int maxResult) {
		
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<roles.size();i++){
			
			if(i<roles.size()-1){
				builder.append(" u.role=? or");
			}
			else {
				builder.append(" u.role=?");
			}
		}
		String hql = "from UserInfo as u where "+builder+ " order by u.userId desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i=0;i<roles.size();i++){
			query.setInteger(i, roles.get(i));
		}
		query.setFirstResult(0);
		query.setMaxResults(maxResult);
		
		return query.list();
	}

	@Override
	public List<String> getRedisUsers(String key) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, 0, -1);
	}
	
	@Override
	public List<String> getRedisUsers(String key,int maxResult) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, 0, maxResult);
	}

	@Override
	public long addRedisUsers(String key, int userId) {
		if(redisDao==null)
			return 0;
		return redisDao.rpush(key, userId+"");
	}

	@Override
	public void subRedisFollowingCount(int userId) {
		if(redisDao==null)
			return ;
		redisDao.hincrby(UserInfo.getKey(userId), UserInfo.FOLLOWINGCOUNT, -1);
	}

	@Override
	public void subRedisFansCount(int followId) {
		if(redisDao==null)
			return ;
		redisDao.hincrby(UserInfo.getKey(followId), UserInfo.FANSCOUNT, -1);
	}

	@Override
	public void addRedisFollowingSet(int userId, int followId) {
		if(redisDao==null)
			return ;
		redisDao.sadd(RedisInfo.USERFOLLOWZSET+userId, followId+"");
	}

	@Override
	public void addRedisFansSet(int userId, int followId) {
		if(redisDao==null)
			return ;
		redisDao.sadd(RedisInfo.USERFANSZSET+followId, userId+"");
	}

	@Override
	public String getRedisOpenId(String key) {
		if(redisDao==null)
			return null;
		return redisDao.get(key);
	}

	@Override
	public void setRedisOpenId(String key, int userId) {
		if(redisDao==null)
			return ;
		redisDao.set(key, userId+"");
	}

	@Override
	public Set<String> getRedisUserIds(String key, int page, int maxResults) {
		if(redisDao==null)
			return null;
		return redisDao.zrevrange(key, page*maxResults, (page+1)*maxResults-1);
	}

	@Override
	public long addRedisFollowingZSet(RelUserFollow rel) {
		if(redisDao==null)
			return -1;
		String key = RedisInfo.USERFOLLOWZSET+rel.getUser().getUserId();
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		//double score = Double.parseDouble(sdf.format(rel.getDate()));
		double score = rel.getId();
		String member = rel.getFollow().getUserId()+"";
		Long result = redisDao.zadd(key,score,member);
		if(result>0){
			addRedisFollowingCount(rel.getUser().getUserId());
		}
		return result;
	}

	@Override
	public long addRedisFansZSet(RelUserFollow rel) {
		if(redisDao==null)
			return -1;
		String key = RedisInfo.USERFANSZSET+rel.getFollow().getUserId();
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		//double score = Double.parseDouble(sdf.format(rel.getDate()));
		double score = rel.getId();
		String member = rel.getUser().getUserId()+"";
		Long result = redisDao.zadd(key,score,member);
		if(result>0){
			addRedisFansCount(rel.getFollow().getUserId());
		}
		
		return result;
	}

	@Override
	public List<RelUserFollow> getUserFollowList(int userId, int start, int maxCount) {
		System.out.println(start+"----"+maxCount);
		String hqlString = "select r from RelUserFollow as r where r.user.userId = ? order by r.id desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		query.setInteger(0, userId);
		//query.setFirstResult(start);
		//query.setMaxResults(maxCount);
		return query.list();
	}

	@Override
	public List<RelUserFollow> getUserFansList(int userId, int start, int maxCount) {
		String hqlString = "select r from RelUserFollow as r where r.follow.userId = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		query.setInteger(0, userId);
		//query.setFirstResult(start);
		//query.setMaxResults(maxCount);
		return query.list();
	}

	@Override
	public int getRedisFansCount(int userId) {
		if(redisDao==null)
			return 0;
		Object count = redisDao.hget(UserInfo.getKey(userId),UserInfo.FANSCOUNT);
		if(count==null){
			return 0;
		}
		else {
			return Integer.parseInt(count+"");
		}
	}

	@Override
	public void setRedisFansCount(int userId, int count) {
		if(redisDao==null)
			return ;
		redisDao.hset(UserInfo.getKey(userId), UserInfo.FANSCOUNT, count+"");
	}

	@Override
	public int getRedisFollowsCount(int userId) {
		if(redisDao==null)
			return 0;
		Object count = redisDao.hget(UserInfo.getKey(userId),UserInfo.FOLLOWINGCOUNT);
		if(count==null){
			return 0;
		}
		else {
			return Integer.parseInt(count+"");
		}
	}

	@Override
	public void setRedisFollowCount(int userId, int count) {
		if(redisDao==null)
			return ;
		redisDao.hset(UserInfo.getKey(userId), UserInfo.FOLLOWINGCOUNT, count+"");
	}

	@Override
	public boolean isRedisFollow(int userId, int followId) {
		if(redisDao==null)
			return false;
		Double result = redisDao.zscore(RedisInfo.USERFOLLOWZSET+userId,followId+"");
		if(result==null){
			return false;
		}
		//System.out.println(result);
		return true;
	}

	@Override
	public boolean updateRedisUser(UserInfo uInfo) {
		if(redisDao==null)
			return false;
		Map<String, String> map = new HashMap<String, String>();
		map.put(UserInfo.NAME, uInfo.getName());
		map.put(UserInfo.ROLE, uInfo.getRole()+"");
		map.put(UserInfo.SEX, uInfo.getSex()+"");
		map.put(UserInfo.PASSWORD, uInfo.getPassword());
		if(uInfo.getHomeTown()!=null){
			map.put(UserInfo.HOMETOWN,uInfo.getHomeTown());
		}
		
		if(uInfo.getHeadPic()==null){
			map.put(UserInfo.HEADPIC, "");
		}
		else{
			map.put(UserInfo.HEADPIC, uInfo.getHeadPic());
		}
		//int count = getFollowCount(uInfo.getUserId());
		map.put(UserInfo.FOLLOWINGCOUNT, uInfo.getFollowingCount()+"");
		//count = getFansCount(uInfo.getUserId());
		map.put(UserInfo.FANSCOUNT, uInfo.getFansCount()+"");
		//count = getArticleCount(uInfo.getUserId());
		map.put(UserInfo.ARTICLECOUNT, uInfo.getArticleCount()+"");
		//count = getPraiseCount(uInfo.getUserId());
		map.put(UserInfo.PRAISECOUNT, uInfo.getPraiseCount()+"");
		
		String qqOpenId = uInfo.getQqOpenId();
		if(qqOpenId!=null){
			map.put(UserInfo.QQOPENID, qqOpenId);
		}
		String sinaOpenId = uInfo.getSinaOpenId();
		if(sinaOpenId!=null){
			map.put(UserInfo.SINAOPENID, sinaOpenId);
		}
		String weChatOpenId = uInfo.getWeChatOpenId();
		if(weChatOpenId!=null){
			map.put(UserInfo.WECHATOPENID,weChatOpenId);
		}
		
		SimpleDateFormat sdf = WebManager.getDateFormat();
		Date date = uInfo.getLastLogonDate();
		if(date!=null){
			map.put(UserInfo.LASTLOGONDATE, sdf.format(date));
		}
		
		return redisDao.hmset( uInfo.getKey(),map)!=null;
	}

	@Override
	public void removeRedisFollow(int userId, int followId) {
		if(redisDao==null)
			return ;
		String key = RedisInfo.USERFOLLOWZSET+userId;
		String member = followId+"";
		
		redisDao.zrem(key,member);
	}

	@Override
	public void removeRedisFans(int userId, int followId) {
		if(redisDao==null)
			return;
		String key = RedisInfo.USERFANSZSET+followId;
		String member = userId+"";
		
		redisDao.zrem(key,member);
	}

	@Override
	public int addChildUser(int parentId, int userId) {
		String sql = "insert into RelChildUser(UserId,ChildId) values (?,?)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0,parentId);
		query.setInteger(1, userId);
		
		return query.executeUpdate();
	}

	@Override
	public List<Object[]> getChildUser(int userId) {
		//String hql = "select u from UserInfo as u join u.childUser as a where a.userId = ?";
		String sql = "select u.Id,u.Name,u.Password,u.Sex,u.Role,u.HomeTown,u.HeadPic from UserInfo as u,RelChildUser as rel where u.Id = rel.ChildId and rel.UserId = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, userId);
		//query.setFirstResult(0);
		//query.setMaxResults(9);
		return query.list();
	}

	@Override
	public void addRedisChildUser(String key, int userId) {
		redisDao.lpush(key, userId+"");
	}

	@Override
	public boolean isChildUser(int userId, int childId) {
		String sql = "select * from RelChildUser rel where rel.UserId=? and rel.ChildId=?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, userId);
		query.setInteger(1, childId);
		
		return query.list().size()>0;
	}

	@Override
	public long addRedisOnlyFansZSet(RelUserFollow rel) {
		if(redisDao==null)
			return -1;
		String key = RedisInfo.USERFANSZSET+rel.getFollow().getUserId();
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		//double score = Double.parseDouble(sdf.format(rel.getDate()));
		double score = rel.getId();
		String member = rel.getUser().getUserId()+"";
		return redisDao.zadd(key,score,member);
		
	}

	@Override
	public long addRedisOnlyFollowingZSet(RelUserFollow rel) {
		if(redisDao==null)
			return -1;
		String key = RedisInfo.USERFOLLOWZSET+rel.getUser().getUserId();
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		//double score = Double.parseDouble(sdf.format(rel.getDate()));
		double score = rel.getId();
		String member = rel.getFollow().getUserId()+"";
		Long result = redisDao.zadd(key,score,member);
		return result;
	}

	@Override
	public UserInfo verificationEmail(String email, String password) {
		String hql = "from UserInfo as u where u.email = ? and u.password=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, email);
		query.setString(1, password);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UserInfo) query.uniqueResult();
	}

	@Override
	public void addRedisUserEmailPassword(String email, String password, int userId) {
		String key = RedisInfo.USEREMAILPASSWORD+email+":"+password;
		redisDao.set(key, userId+"");
	}

	@Override
	public String getRedisByEmailPassword(String email, String password) {
		String key = RedisInfo.USEREMAILPASSWORD+email+":"+password;
		
		return redisDao.get(key);
	}

	@Override
	public boolean verificationEmail(String email) {
		String hql = "from UserInfo as u where u.email = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, email);
		//query.setMaxResults(1);
		//query.setFirstResult(0);
		return query.list().size()>0;
	}

	@Override
	public void addRedisEmails(String email) {
		String key = RedisInfo.USEREMAILSET;
		redisDao.sadd(key, email);
	}

	@Override
	public boolean isRedisEmail(String email) {
		String key = RedisInfo.USEREMAILSET;
		return redisDao.sismember(key, email);
	}

	@Override
	public boolean addRedisLogoutUser(int userId) {
		String key = RedisInfo.USERLOGINSET;
		long result = redisDao.sadd(key, ""+userId);
		//System.out.println(result);
		return result>0;
	}

	@Override
	public Set<String> getRedisLogoutUser() {
		String key = RedisInfo.USERLOGINSET;
		return redisDao.smembers(key);
	}

	@Override
	public List<UserInfo> getMeetList(int count) {
		String hql = "select u from UserInfo as u where (select count(a.articleId) from ArticleInfo as a where a.user.userId=u.userId) > 3 order by rand()";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(count);
		return query.list();
	}

	@Override
	public List<UserInfo> getMeetList(int sex, int maxCount) {
		String hql = "select u from UserInfo as u where u.sex =:sex and (select count(a.articleId) from ArticleInfo as a where a.user.userId=u.userId) > 3 order by rand()";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("sex", sex);
		query.setFirstResult(0);
		query.setMaxResults(maxCount);
		return query.list();
	}

	@Override
	public Set<String> getRedisSetUserIds(String key) {
		if(redisDao==null)
			return null;
		return redisDao.zrevrange(key, 0, -1);
	}

	@Override
	public List<UserInfo> getFansList(int userId) {
		String hqlString = "select u from UserInfo as u,RelUserFollow as r where r.follow.userId = ? and r.user.userId=u.userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		query.setInteger(0, userId);
		return query.list();
	}

	@Override
	public void deleteRedisKey(String key) {
		if(redisDao==null)
			return ;
		redisDao.del(key);
	}

	@Override
	public String getRedisUserPassword(String key) {
		
		return redisDao.get(key);
	}

	@Override
	public void addRedisUserPassword(String key, int userId) {
		redisDao.set(key, userId+"");
	}

	@Override
	public List<UserInfo> getUserByName(String name) {
		String hql = "select u from UserInfo as u where u.name =:name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString("name", name);
		return query.list();
	}

	@Override
	public boolean updateUserInfo(int userId, String field, int value) {
		String hql = "update UserInfo as u set u."+field+" =:value where u.userId=:userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("value", userId);
		query.setInteger("userId", userId);
		return query.executeUpdate()>0;
	}

	@Override
	public void updateRedisUserInfo(String key, String field, String value) {
		redisDao.hset(key, field, value);
	}

	@Override
	public List<String> getRedisUsers(String key, int page, int maxResults) {
		return redisDao.lrange(key, page*maxResults, (page+1)*maxResults-1);
	}

	@Override
	public Set<String> getRedisUserSet(String key) {
		return redisDao.smembers(key);
	}

	@Override
	public void addRedisUserSet(String key, int userId) {
		redisDao.sadd(key,userId+"");
	}


	
}
