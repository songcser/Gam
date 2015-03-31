package com.stark.web.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.stark.web.define.RedisInfo;
import com.stark.web.define.EnumBase.ArticleType;
import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.ArticlePublishTimeLine;
import com.stark.web.entity.ChartletInfo;
import com.stark.web.entity.FileInfo;
import com.stark.web.entity.RelArticleForward;
import com.stark.web.entity.RelChartletPicture;
import com.stark.web.entity.UserInfo;

public class ArticleDAO implements IArticleDAO {

	private SessionFactory sessionFactory;

	//private RedisTemplate<String, String> redisTemplate;
	private IRedisDAO redisDao;

	
	public IRedisDAO getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(IRedisDAO redisDao) {
		this.redisDao = redisDao;
	}

//	protected RedisSerializer<String> getRedisSerializer() {
//		return redisTemplate.getStringSerializer();
//	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public int addArticle(ArticleInfo aInfo) {
		return (int) sessionFactory.getCurrentSession().save(aInfo);
	}

	@Override
	public boolean updateArticle(ArticleInfo aInfo) {
		return false;
	}

	@Override
	public ArticleInfo getArticle(int aid) {
		return (ArticleInfo) sessionFactory.getCurrentSession().get(ArticleInfo.class, aid);
	}

	@Override
	public List<ArticleInfo> getArticleByUserId(int uId, int start, int maxCount) {
		String hql = "from ArticleInfo as a where a.user.userId = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, uId);
		query.setFirstResult(start);
		query.setMaxResults(maxCount);
		return query.list();
	}

	@Override
	public boolean addForward(RelArticleForward rel) {
		String id = (String) sessionFactory.getCurrentSession().save(rel);
		return id == null;
	}

	@Override
	public boolean praise(int userId, int articleId) {
		String sql = "insert into RelArticlePraise(UserId,ArticleId) values(?,?)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, userId);
		query.setInteger(1, articleId);

		int result = query.executeUpdate();
		return result > 0;
	}

	@Override
	public List<ArticleInfo> getFollowArticle(int uid, int page, int maxCount) {
		String hql = "select a from RelUserFollow as rel, ArticleInfo as a where rel.user.userId = ? and rel.follow.userId = a.user.userId";
		// String hql1 =
		// "select a,aptl from RelUserFollow as ruf,ArticlePublishTimeLine as aptl,ArticleInfo as a"
		// +
		// " where ruf.user.userId = ? and ruf.follow.userId = aptl.user.userId and aptl.article.articleId = a.articleId"
		// + " ORDER BY aptl.date";

		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		query.setInteger(0, uid);
		query.setFirstResult(page * maxCount);
		query.setMaxResults(maxCount);

		return query.list();
	}

	@Override
	public boolean addTimeLine(ArticlePublishTimeLine timeLine) {
		int id = (int) sessionFactory.getCurrentSession().save(timeLine);
		return id == 0;
	}

	@Override
	public List<ArticleInfo> getSquareArticle(int page, int maxCount) {
		String hql = "select a from ArticleInfo as a where a.articleId not in (select ac.article.articleId from ActivityInfo ac) order by a.date desc";
		// String hql = "select a from ArticleInfo as a  order by a.date desc";
		// String hql =
		// "select a,aptl from ArticlePublishTimeLine as aptl,ArticleInfo as a"
		// + " where aptl.article.articleId = a.articleId ORDER BY aptl.date";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		query.setFirstResult(page * maxCount);
		query.setMaxResults(maxCount);

		return query.list();
	}

	@Override
	public List<Object[]> getByTagAndLocation(String tag, String location, int page, int maxCount) {
		// System.out.println("Tag:"+tag+"-----location:"+location);
		// String hql =
		// "select a,u from UserInfo as u, ArticleInfo as a join a.tags as t where a.user.id = u.id and u.homeTown = ? and t.articles.id = a.id and t.content = ?";
		String sql = "select a.ID as aid,u.ID as uid from ARTICLEINFO as a, USERINFO as u, TAGINFO as t ,RELARTICLETAG as r where a.USERID = u.ID and u.homeTown like ? and t.ID = r.TAGID and r.ARTICLEID = a.ID and t.CONTENT = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);

		query.setString(0, "%" + location + "%");
		query.setString(1, tag);
		query.setFirstResult(page * maxCount);
		query.setMaxResults(maxCount);
		// System.out.println(query.getQueryString());
		// List<String> list = query.list();
		return query.list();// query.list();
	}

	@Override
	public List<Object[]> getHotArticles() {
		String sql = "select t.ID, rap.PICTURE,t.CONTENT,t.USECOUNT,count(t.CONTENT) "
				+ "from TAGINFO as t , ARTICLEINFO as a ,RELARTICLETAG as r,RELARTICLEPICTURE as rap "
				+ "where t.ID = r.TAGID and r.ARTICLEID = a.ID and a.ID = rap.ARTICLEID group by t.CONTENT order by t.USECOUNT desc";
		// String hql =
		// "select rap.PICTURE,t.CONTENT,t.USECOUNT from TAGINFO as t , ARTICLEINFO as a ,RELARTICLETAG as r,RELARTICLEPICTURE as rap where t.ID = r.TAGID and r.ARTICLEID = a.ID and a.ID = rap.ARTICLEID order by t.USECOUNT desc";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(3);

		return query.list();
	}

	@Override
	public boolean isParise(int userId, int articleId) {
		String sql = "select * from RelArticlePraise where UserId = ? and ArticleId = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, userId);
		query.setInteger(1, articleId);

		return query.uniqueResult() != null;
	}

	@Override
	public List<ArticleInfo> getArticleByTag(String tag, int page, int maxCount) {
		String hql = "select a from ArticleInfo as a join a.tags as t where t.content = ?";

		// String sql = "select a.* "
		// + "from ARTICLEINFO as a,TAGINFO as t,RELARTICLETAG as r "
		// +
		// "where a.ID = r.ARTICLEID and r.TAGID = t.ID and t.CONTENT = ? order by a.DATE desc";
		// String sql1 =
		// "select a.ID,a.USERID,a.CONTENT,a.DATE,a.PRAISECOUNT,a.COMMENTCOUNT,a.STATUS,a.URL,a.TITLE,a.ABBREVIATION,aptl.TYPE,aptl.USERID as SENDERID "
		// +
		// "from ARTICLEINFO as a, TAGINFO as t,RELARTICLETAG as r,ARTICLEPUBLISHTIMELINE as aptl "
		// +
		// "where aptl.ARTICLEID=a.ID and a.ID = r.ARTICLEID and r.TAGID = t.ID and t.CONTENT = ? order by aptl.DATE desc";
		// Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, tag);
		query.setFirstResult(page * maxCount);
		query.setMaxResults(maxCount);
		// System.out.println("tag: "+tag);
		// List<Object[]> list =
		// System.out.println(list.size());
		// List<Object[]> list = query.list();
		// List<ArticleInfo> aList = new ArrayList<ArticleInfo>();
		// for(Object[] obj:list){
		// Integer idS = (Integer)obj[0];
		// aList.add(new ArticleInfo(idS));
		// }
		return query.list();
	}

	@Override
	public boolean addCommentCount(String id) {
		String sql = "update ARTICLEINFO as a set a.COMMENTCOUNT = a.COMMENTCOUNT + 1 where a.ID = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, id);

		int result = query.executeUpdate();
		return result > 0;
	}

	@Override
	public List<String> getPicListById(int articleId) {
		String sql = "select r.Picture from RelArticlePicture as r where r.ArticleId = ? order by r.Picture";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, articleId);
		return query.list();
	}

	@Override
	public boolean uploadPicture(int articleId, String fileName) {
		String sql = "insert into RelArticlePicture(ArticleId,Picture) values(?,?)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, articleId);
		query.setString(1, fileName);

		return query.executeUpdate() > 0;
	}

	@Override
	public boolean deleteByUserId(String userId) {
		String hql = "delete from ArticleInfo as a where a.user.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		return query.executeUpdate() > 0;
	}

	@Override
	public List<ArticleInfo> getArticleByTag(List<String> tagList, int page, int maxCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tagList.size(); i++) {
			if (i == tagList.size() - 1) {
				sb.append("? ");
			} else
				sb.append("?, ");

		}
		String hql = "select a from ArticleInfo as a join a.tags as t where t.content in (" + sb + ")";
		//System.out.println("Hql:   " + hql);
		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		for (int i = 0; i < tagList.size(); i++) {
			String tag = tagList.get(i);
			query.setString(i, tag);
		}
		query.setFirstResult(page * maxCount);
		query.setMaxResults(maxCount);

		return query.list();
	}

	@Override
	public List<ArticleInfo> getDayExquisites(int page, int maxRequestCount) {

		return null;
	}

	@Override
	public List<ArticleInfo> getArticleByType(int type, int start, int maxResults) {
		String hql = "from ArticleInfo as a where a.type = ? order by a.date desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, type);
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.list();
	}

	@Override
	public List<ArticleInfo> getListByActivityId(int activityId, int start, int maxResults) {
		String hql = "select a from ArticleInfo as a  where a.activity.activityId = ? and a.type=? order by a.date desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, activityId);
		query.setInteger(1, ArticleType.Activity.getIndex());
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.list();
	}

	@Override
	public boolean setArticleType(int articleId, int type) {
		String hql = "update ArticleInfo as a set a.type=? where a.articleId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, type);
		query.setInteger(1, articleId);

		return query.executeUpdate() > 0;
	}

	@Override
	public boolean addRedisArticle(ArticleInfo aInfo) {
		if(redisDao==null)
			return false;
		Map<String, String> map = new HashMap<String, String>();
		map.put(ArticleInfo.USERID, aInfo.getUser().getUserId()+"");
		map.put(ArticleInfo.TYPE, aInfo.getType()+"");
		map.put(ArticleInfo.CONTENT, aInfo.getContent());
		map.put(ArticleInfo.REFERENCE, aInfo.getReference());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		//System.out.println(sdf.format(aInfo.getDate()));
		map.put(ArticleInfo.DATE, sdf.format(aInfo.getDate()));
		
		int colCount = getCollectionCount(aInfo.getArticleId());
		map.put(ArticleInfo.COLLECTIONCOUNT, ""+colCount);
		aInfo.setCollectionCount(colCount);
		int pCount = getPraiseCount(aInfo.getArticleId());
		map.put(ArticleInfo.PRAISECOUNT, ""+pCount);
		aInfo.setPraiseCount(pCount);
		
		int cCount = getCommentCount(aInfo.getArticleId());
		aInfo.setCommentCount(cCount);
		map.put(ArticleInfo.COMMENTCOUNT, ""+cCount);
		
		int bCount = aInfo.getBrowseCount();
		map.put(ArticleInfo.BROWSECOUNT, bCount+"");
		
		if(aInfo.getActivity()!=null){
			map.put(ArticleInfo.ACTIVITYID, aInfo.getActivity().getActivityId()+"");
		}
		
		if(aInfo.getTitle()!=null){
			map.put(ArticleInfo.TITLE, aInfo.getTitle());
		}
		
		if(aInfo.getRichText()!=null){
			map.put(ArticleInfo.RICHTEXT, aInfo.getRichText());
		}
		
		boolean result = redisDao.hmset(aInfo.getKey(),map)!=null;
		setKeyExpire(aInfo.getKey(),60*60*24*30);
		return result;
	}
	
	private int getCollectionCount(int articleId) {
		String sql = "select count(r.UserId) from RelArticleCollection as r where r.ArticleId=?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, articleId);
		String result = query.uniqueResult()+"";
		return Integer.parseInt(result);
	}

	@Override
	public int getCommentCount(int articleId) {
		String hql = "select count(c.commentId) from CommentInfo as c where c.article.articleId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, articleId);
		String result = query.uniqueResult()+"";
		return Integer.parseInt(result);
	}

	@Override
	public List<Object[]> getArticlePicByUserId(int userId, int page, int maxPictureResult) {
		String sql = "select rel.ArticleId,rel.Picture from UserInfo as u,ArticleInfo as a,RelArticlePicture as rel where "
				+ "rel.ArticleId = a.Id and a.UserId = u.Id and u.Id=? and a.Type!=11 order by a.Date desc";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, userId);
		query.setFirstResult(page * maxPictureResult);
		query.setMaxResults(maxPictureResult);

		return query.list();
	}

	@Override
	public void addRedisUserArticleList(int userId, int articleId) {
		if(redisDao==null)
			return ;
		String key = RedisInfo.USERARTICLELIST + userId;
		redisDao.lpush(key, "" + articleId);
	}

	@Override
	public void addRedisArticlePicture(int id, String fileName) {
		if(redisDao==null)
			return ;
		String key = RedisInfo.ARTICLEPICTURELIST + id;
		redisDao.rpush(key, fileName);
		
	}

	@Override
	public int addChartlet(ChartletInfo chartlet) {
		int id = (int) sessionFactory.getCurrentSession().save(chartlet);
		return id;
	}

	@Override
	public int addChartletPicture(int chartletId, int fileId) {
		String sql ="insert into RelChartletPicture(ChartletId,FileId) values(?,?)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, chartletId);
		query.setInteger(1, fileId);
		return query.executeUpdate();
	}

	@Override
	public List<ChartletInfo> getAllChartlet() {
		String hql = "from ChartletInfo as c order by c.chartletId desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	@Override
	public int addFile(FileInfo fileInfo) {
		
		return (int)sessionFactory.getCurrentSession().save(fileInfo);
	}

	@Override
	public int addChartletPicture(RelChartletPicture rel) {
		return (int)sessionFactory.getCurrentSession().save(rel);
	}

	@Override
	public boolean changeChartletPictureStatus(int pictureId, int status) {
		String hql = "update RelChartletPicture as r set r.status=? where r.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, status);
		query.setInteger(1, pictureId);
		return query.executeUpdate()>0;
	}

	@Override
	public boolean removeChartletPicture(int pictureId) {
		String hql = "delete from RelChartletPicture as r where r.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, pictureId);
		return query.executeUpdate()>0;
	}

	@Override
	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int start, int maxResults) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Criteria  criteria = sessionFactory.getCurrentSession().createCriteria(ArticleInfo.class);
		try {
			criteria.add(Restrictions.ge("date",sdf.parse(startDate)))
				.add(Restrictions.le("date",sdf.parse(endDate)))
				.setFirstResult(start)
				.setMaxResults(maxResults);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String hql = "from ArticleInfo as a where a.date>:startDate and a.date<:endDate order by a.date desc";
//		//String sql = "select a.* from ArticleInfo as a where a.Date>? and a.Date<?";
//		System.out.println(startDate);
//		System.out.println(endDate);
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		query.setString("startDate", "'"+startDate+"'");
//		query.setString("endDate", "'"+endDate+"'");
//		query.setFirstResult(start);
//		query.setMaxResults(maxResults);
		
		//return query.list();
		
		return criteria.list();
		
	}

	@Override
	public int getPraiseCount(int articleId) {
		String sql = "select count(r.UserId) from RelArticlePraise as r where r.ArticleId=?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, articleId);
		String result = query.uniqueResult()+"";
		return Integer.parseInt(result);
	}

	@Override
	public boolean changeArticleType(int articleId, int type) {
		String hql = "update ArticleInfo as a set a.type = ? where a.articleId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, type);
		query.setInteger(1, articleId);
		return query.executeUpdate()>0;
	}

	@Override
	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int type, int start, int maxResults) {
		String hql = "from ArticleInfo as a where a.date>? and a.date<? and a.type = ? order by a.date desc";
		//String sql = "select a.* from ArticleInfo as a where a.Date>? and a.Date<?";
		//System.out.println(startDate);
		//System.out.println(endDate);
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, startDate);
		query.setString(1, endDate);
		query.setInteger(2, type);
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		
		return query.list();
	}

	@Override
	public List<ArticleInfo> getArticleByUserId(int uid, List<Integer> typeList, int start, int maxCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < typeList.size(); i++) {
			if (i == typeList.size() - 1) {
				sb.append("a.type = ? ");
			} else
				sb.append("a.type = ? or ");

		}
		String hql = "from ArticleInfo as a where a.user.userId = ? and ("+sb+") order by a.date desc";
		//String hql = "select a from ArticleInfo as a join a.tags as t where t.content in (" + sb + ")";
		//System.out.println("Hql:   " + hql);
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, uid);
		for (int i = 0; i < typeList.size(); i++) {
			int type = typeList.get(i);
			query.setInteger(i+1, type);
		}
		query.setFirstResult(start);
		query.setMaxResults(maxCount);

		return query.list();
	}

	@Override
	public boolean deleteByActivityId(int activityId) {
		String hql = "delete from ArticleInfo as a where a.activity.activityId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, activityId);
		return query.executeUpdate()>0;
	}

	@Override
	public int getCountByType(int index) {
		String hql = "select count(a.articleId) from ArticleInfo as a where a.type=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, index);
		String count = query.uniqueResult()+"";
		return Integer.parseInt(count);
	}

	@Override
	public int getCountByTypes(List<Integer> list) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<list.size();i++){
			
			if(i<list.size()-1){
				builder.append(" a.type=? or");
			}
			else {
				builder.append(" a.type=?");
			}
		}
		String hql = "select count(a.articleId) from ArticleInfo as a where "+builder;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i=0;i<list.size();i++){
			query.setInteger(i, list.get(i));
		}
		String count = query.uniqueResult()+"";
		return Integer.parseInt(count);
	}

	@Override
	public List<ArticleInfo> getArticleByType(List<Integer> types, int start, int maxResults) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<types.size();i++){
			
			if(i<types.size()-1){
				builder.append(" a.type=? or");
			}
			else {
				builder.append(" a.type=?");
			}
		}
		String hql = "from ArticleInfo as a where "+builder+ " order by a.date desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i=0;i<types.size();i++){
			query.setInteger(i, types.get(i));
		}
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.list();
	}

	@Override
	public List<Object[]> getRedisArticlePicByUserId(int userId, int page, int maxPictureResult) {
		
		return null;
	}

	@Override
	public List<ArticleInfo> getAllListByActivityId(int activityId, int start, int maxResults) {
		String hql = "select a from ArticleInfo as a  where a.activity.activityId = ? and a.type!=? order by a.date desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, activityId);
		query.setInteger(1, ArticleType.Delete.getIndex());
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.list();
	}

	@Override
	public List<String> getRedisArticleByType(int types,int page,int maxResults) {
		//List<String> idList = redisDao.range(RedisInfo.getArticleTypeList(type), 0, 1)
		return null;
	}

	@Override
	public List<ArticleInfo> getRedisArticles(String key,int page, int maxResults) {
		if(redisDao==null)
			return null;
		List<String> idList = redisDao.lrange(key, page*maxResults, (page+1)*maxResults-1);
		List<ArticleInfo> list = new ArrayList<ArticleInfo>();
		if(idList!=null&&!idList.isEmpty()){
			for(String id:idList){
				Map<String, String> aMap = redisDao.hgetAll(ArticleInfo.getKey(id));
				if(aMap==null||aMap.isEmpty()){
					continue;
				}
				ArticleInfo article = new ArticleInfo();
				article.setArticleId(Integer.parseInt(id));
				article.setUser(new UserInfo(Integer.parseInt(aMap.get(ArticleInfo.USERID))));
				article.setType(Integer.parseInt(aMap.get(ArticleInfo.TYPE)));
				article.setContent(aMap.get(ArticleInfo.CONTENT));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				
				try {
					article.setDate(sdf.parse(aMap.get(ArticleInfo.DATE)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String collectionCount = aMap.get(ArticleInfo.COLLECTIONCOUNT);
				if(collectionCount!=null&&!collectionCount.equals("")){
					article.setCollectionCount(Integer.parseInt(collectionCount));
				}
				else{
					article.setCollectionCount(0);
				}
				article.setPraiseCount(Integer.parseInt(aMap.get(ArticleInfo.PRAISECOUNT)));
				article.setCommentCount(Integer.parseInt(aMap.get(ArticleInfo.COMMENTCOUNT)));
				
				String browserCount = aMap.get(ArticleInfo.BROWSECOUNT);
				if(browserCount!=null&&!browserCount.equals("")){
					article.setBrowseCount(Integer.parseInt(browserCount));
				}
				else {
					article.setBrowseCount(0);
				}
				
				String activityId = aMap.get(ArticleInfo.ACTIVITYID);
				if(activityId!=null){
					article.setActivity(new ActivityInfo(Integer.parseInt(activityId)));
				}
				
				String title = aMap.get(ArticleInfo.TITLE);
				if(title!=null){
					article.setTitle(title);
				}
				else{
					article.setTitle("");
				}
				
				String richText = aMap.get(ArticleInfo.RICHTEXT);
				if(richText!=null){
					article.setRichText(richText);
				}
				else {
					article.setRichText("");
				}
				
				list.add(article);
			}
		}else{
			return null;
		}
		return list;
	}
	
	@Override
	public void addRedisExquisitesList(int articleId) {
		if(redisDao==null)
			return;
		redisDao.lpush(RedisInfo.ARTICLEDAYEXQUISITELIST,articleId+"");
	}

	@Override
	public List<String> getRedisPictures(int articleId) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(RedisInfo.ARTICLEPICTURELIST+articleId, 0, -1);
	}

	@Override
	public boolean isRedisPraise(int userId, int articleId) {
		if(redisDao==null)
			return false;
		return redisDao.sismember(RedisInfo.USERPRAISESET+userId, articleId+"");
	}

	@Override
	public long addRedisPraise(int userId, int articleId) {
		if(redisDao==null)
			return 0;
		redisDao.hincrby(ArticleInfo.getKey(articleId),ArticleInfo.PRAISECOUNT,1);
		return redisDao.sadd(RedisInfo.USERPRAISESET+userId,articleId+"");
	}

	@Override
	public String getRedisArticleCount(String key) {
		if(redisDao==null)
			return null;
		return redisDao.get(key);
	}

	@Override
	public void setRedisArticleCount(String key, int count) {
		if(redisDao==null)
			return ;
		redisDao.set(key, count+"");
	}

	@Override
	public long addRedisArticleId(String key, int id) {
		if(redisDao==null)
			return 0;
		return redisDao.lpush(key, id+"");
	}

	@Override
	public List<String> getRedisArticleIds(String key, int page, int maxCount) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, page*maxCount, (page+1)*maxCount-1);
	}

	@Override
	public ArticleInfo getRedisArticle(int id) {
		if(redisDao==null)
			return null;
		Map<String, String> aMap = redisDao.hgetAll(ArticleInfo.getKey(id));
		if(aMap==null||aMap.isEmpty()){
			return null;
		}
		ArticleInfo article = new ArticleInfo();
		article.setArticleId(id);
		article.setUser(new UserInfo(Integer.parseInt(aMap.get(ArticleInfo.USERID))));
		article.setType(Integer.parseInt(aMap.get(ArticleInfo.TYPE)));
		article.setContent(aMap.get(ArticleInfo.CONTENT));
		article.setReference(aMap.get(ArticleInfo.REFERENCE));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		
		try {
			article.setDate(sdf.parse(aMap.get(ArticleInfo.DATE)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String collectionCount = aMap.get(ArticleInfo.COLLECTIONCOUNT);
		if(collectionCount!=null&&!collectionCount.equals("")){
			article.setCollectionCount(Integer.parseInt(collectionCount));
		}
		else{
			article.setCollectionCount(0);
		}
		article.setPraiseCount(Integer.parseInt(aMap.get(ArticleInfo.PRAISECOUNT)));
		article.setCommentCount(Integer.parseInt(aMap.get(ArticleInfo.COMMENTCOUNT)));
		String browserCount = aMap.get(ArticleInfo.BROWSECOUNT);
		if(browserCount!=null&&!browserCount.equals("")){
			article.setBrowseCount(Integer.parseInt(browserCount));
		}
		else {
			article.setBrowseCount(0);
		}
		String activityId = aMap.get(ArticleInfo.ACTIVITYID);
		if(activityId!=null){
			article.setActivity(new ActivityInfo(Integer.parseInt(activityId)));
		}
		
		String title = aMap.get(ArticleInfo.TITLE);
		if(title!=null){
			article.setTitle(title);
		}
		else {
			article.setTitle("");
		}
		
		String richText = aMap.get(ArticleInfo.RICHTEXT);
		if(richText!=null){
			article.setRichText(richText);
		}
		else {
			article.setRichText("");
		}
		
		return article;
	}

	@Override
	public long addRedisCommentCount(int articleId) {
		if(redisDao==null)
			return 0;
		return redisDao.hincrby(ArticleInfo.getKey(articleId), ArticleInfo.COMMENTCOUNT, 1);
	}

	@Override
	public void addRedisArticlesId(String key, int articleId) {
		if(redisDao==null)
			return;
		redisDao.lpush(key, articleId+"");
	}

	@Override
	public void addRedisArticleCount(String key) {
		if(redisDao==null)
			return ;
		redisDao.incr(key);
	}

	@Override
	public void addRedisUpdateList(int articleId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(RedisInfo.ARTICLEUPDATELIST,articleId+"");
	}

	@Override
	public void addRedisUserArticleCount(int userId) {
		if(redisDao==null)
			return ;
		redisDao.hincrby(UserInfo.getKey(userId), UserInfo.ARTICLECOUNT, 1);
	}

	@Override
	public void addRedisUserPraiseCount(int userId) {
		if(redisDao==null)
			return ;
		redisDao.hincrby(UserInfo.getKey(userId), UserInfo.PRAISECOUNT, 1);
	}

	@Override
	public void changeRedisArticleType(int articleId, int type) {
		if(redisDao==null)
			return ;
		redisDao.hset(ArticleInfo.getKey(articleId), ArticleInfo.TYPE, type+"");
	}

	@Override
	public void decRedisArticleCount(String key) {
		if(redisDao==null)
			return ;
		redisDao.decr(key);
	}

	@Override
	public void addRedisMagazineList(int articleId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(RedisInfo.ARTICLEMAGAZINELIST, articleId+"");
	}

	@Override
	public void addRedisActivityAllList(int activityId, int articleId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(RedisInfo.ACTIVITYARTICLEALLLIST+activityId, articleId+"");
	}

	@Override
	public void addRedisActivityAuditingList(int activityId, int articleId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(RedisInfo.ACTIVITYARTICLEAUDITINGLIST+activityId, articleId+"");
	}

	@Override
	public void removeRedisArticleList(String key, int articleId) {
		if(redisDao==null)
			return ;
		redisDao.lrem(key, 0, articleId+"");
	}

	@Override
	public boolean setKeyExpire(String key, int seconds) {
		if(redisDao==null)
			return false;
		return redisDao.expire(key,seconds);
	}

	@Override
	public boolean addRedisChartlet(ChartletInfo chartlet) {
		if(redisDao==null)
			return false;
		Map<String, String> map = new HashMap<String, String>();
		map.put(ChartletInfo.TITLE, chartlet.getTitle());
		map.put(ChartletInfo.TYPE, chartlet.getType()+"");
		map.put(ChartletInfo.STATUS, chartlet.getStatus()+"");
		
		return redisDao.hmset(chartlet.getKey(), map)!=null;
	}

	@Override
	public List<String> getRedisChartletIds(String key) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, 0, -1);
	}

	@Override
	public ChartletInfo getRedisChartlet(int chartletId) {
		if(redisDao==null)
			return null;
		Map<String, String> cMap = redisDao.hgetAll(ChartletInfo.getKey(chartletId));
		if(cMap==null||cMap.isEmpty()){
			return null;
		}
		ChartletInfo chartlet = new ChartletInfo();
		chartlet.setChartletId(chartletId);
		chartlet.setTitle(cMap.get(ChartletInfo.TITLE));
		chartlet.setStatus(Integer.parseInt(cMap.get(ChartletInfo.STATUS)));
		chartlet.setType(Integer.parseInt(cMap.get(ChartletInfo.TYPE)));
		
		return chartlet;
	}

	@Override
	public ChartletInfo getChartlet(int chartletId) {
		
		return (ChartletInfo)sessionFactory.getCurrentSession().get(ChartletInfo.class, chartletId);
	}

	@Override
	public void addRedisAllChartlet(String key, int chartletId) {
		if(redisDao==null)
			return ;
		redisDao.lpush(key, chartletId+"");
	}

	@Override
	public List<RelChartletPicture> getChartletPictures(int chartletId) {
		String hql = "from RelChartletPicture as rel where rel.chartlet.chartletId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, chartletId);
		return query.list();
	}

	@Override
	public List<String> getRedisChartletPictureIds(String key) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, 0, -1);
	}

	@Override
	public boolean addRedisChartletPicture(RelChartletPicture rel) {
		if(redisDao==null)
			return false;
		Map<String, String> map = new HashMap<String, String>();
		
		map.put(RelChartletPicture.CHARTLETID, rel.getChartlet().getChartletId()+"");
		map.put(RelChartletPicture.PICTURE, rel.getPicture());
		map.put(RelChartletPicture.STATUS, rel.getStatus()+"");
		
		return redisDao.hmset(rel.getKey(), map)!=null;
	}

	@Override
	public RelChartletPicture getChartletPicture(int relId) {
		return (RelChartletPicture)sessionFactory.getCurrentSession().get(RelChartletPicture.class, relId);
	}

	@Override
	public RelChartletPicture getRedisChartletPicture(int relId) {
		if(redisDao==null)
			return null;
		Map<String, String> cMap = redisDao.hgetAll(RelChartletPicture.getKey(relId));
		if(cMap==null||cMap.isEmpty()){
			return null;
		}
		RelChartletPicture rel = new RelChartletPicture();
		rel.setId(relId);
		rel.setPicture(cMap.get(RelChartletPicture.PICTURE));
		rel.setStatus(Integer.parseInt(cMap.get(ChartletInfo.STATUS)));
		rel.setChartlet(new ChartletInfo(Integer.parseInt(cMap.get(RelChartletPicture.CHARTLETID))));
		return rel;
	}

	@Override
	public void addRedisChartletPictures(String key, int id) {
		if(redisDao==null)
			return ;
		redisDao.lpush(key, id+"");
	}

	@Override
	public void removeRedisKey(String key) {
		if(redisDao==null)
			return ;
		redisDao.del(key);
	}

	@Override
	public void removeRedisChartletPicture(int chartletId, int pictureId) {
		if(redisDao==null)
			return ;
		redisDao.lrem(RedisInfo.CHARTLETPICTURELIST+chartletId, 0, pictureId+"");
	}

	@Override
	public void setRedisChartletPictureStatus(int pictureId, int status) {
		if(redisDao==null)
			return;
		redisDao.hset(RelChartletPicture.getKey(pictureId), RelChartletPicture.STATUS, status+"");
	}

	@Override
	public boolean deleteChartlet(int chartletId) {
		String hql = "delete from ChartletInfo as c where c.chartletId=:chartletId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("chartletId", chartletId);
		
		return query.executeUpdate()>0;
	}

	@Override
	public void removeRedisChartlet(int chartletId) {
		if(redisDao==null)
			return ;
		redisDao.del(ChartletInfo.getKey(chartletId));
	}

	@Override
	public boolean deleteChartletPictureByChartletId(int chartletId) {
		String hql = "delete from RelChartletPicture as r where r.chartlet.chartletId=:chartletId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("chartletId", chartletId);
		
		return query.executeUpdate()>0;
	}

	@Override
	public void removeRedisAllChartlet(String key, int chartletId) {
		if(redisDao==null)
			return ;
		redisDao.lrem(key, 0, chartletId+"");
	}

	@Override
	public void removeRedisUserArticleList(int userId, int articleId) {
		if(redisDao==null)
			return ;
		String key = RedisInfo.USERARTICLELIST + userId;
		redisDao.lrem(key, 0, articleId+"");
	}

	@Override
	public void decRedisUserArticleCount(int userId) {
		if(redisDao==null)
			return ;
		redisDao.hincrby(UserInfo.getKey(userId), UserInfo.ARTICLECOUNT, -1);
	}

	@Override
	public void addRedisUserArticleRList(int uid, int articleId) {
		if(redisDao==null)
			return ;
		String key = RedisInfo.USERARTICLELIST + uid;
		redisDao.rpush(key, "" + articleId);
	}

	@Override
	public void addRedisExquisitesRList(int articleId) {
		if(redisDao==null)
			return;
		redisDao.rpush(RedisInfo.ARTICLEDAYEXQUISITELIST,articleId+"");
	}

	@Override
	public void addRedisUpdateRList(int articleId) {
		if(redisDao==null)
			return ;
		redisDao.rpush(RedisInfo.ARTICLEUPDATELIST,articleId+"");
	}

	@Override
	public long addRedisArticleIdR(String key, int articleId) {
		if(redisDao==null)
			return 0;
		return redisDao.rpush(key, articleId+"");
	}

	@Override
	public void addRedisMagazineRList(int articleId) {
		if(redisDao==null)
			return ;
		redisDao.rpush(RedisInfo.ARTICLEMAGAZINELIST, articleId+"");
	}

	@Override
	public boolean isCollection(int userId, int articleId) {
		//String sql = "delete from ChartletInfo as c where c.chartletId=:chartletId";
		String sql = "select * from RelArticleCollection as rel where rel.UserId=:userId and rel.ArticleId=:articleId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger("userId", userId);
		query.setInteger("articleId", articleId);
		
		return query.list().size()>0;
	}

	@Override
	public List<ArticleInfo> getListByShowId(int showId,int type, int start, int maxResults) {
		String hql = "select a from ArticleInfo as a join a.activities as act where act.activityId =:showId and a.type=:type";
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("showId", showId);
		query.setInteger("type", type);
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		
		return query.list();
	}

	@Override
	public boolean collectArticle(int userId, int articleId) {
		String sql = "insert into RelArticleCollection(UserId,ArticleId) values(?,?)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, userId);
		query.setInteger(1, articleId);

		int result = query.executeUpdate();
		return result > 0;
	}

	@Override
	public boolean browseArticle(int articleId) {
		String hql = "update ArticleInfo as a set a.browseCount = a.browseCount+1 where a.articleId=:articleId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("articleId", articleId);
		
		return query.executeUpdate()>0;
	}

	@Override
	public List<ArticleInfo> getCollectionList(int userId, int start, int maxResult) {
		String hql = "select a from ArticleInfo as a join a.collectors as ac where ac.userId =:userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("userId", userId);
		query.setFirstResult(start);
		query.setMaxResults(maxResult);
		return query.list();
	}

	@Override
	public long addRedisBrowseCount(int articleId) {
		if(redisDao==null)
			return 0;
		return redisDao.hincrby(ArticleInfo.getKey(articleId), ArticleInfo.BROWSECOUNT, 1);
	}

	@Override
	public Set<String> getRedisFollowArticleIdSet(String key, int page, int maxCount) {
		if(redisDao==null)
			return null;
		
		return redisDao.zrevrange(key, page*maxCount, (page+1)*maxCount-1);
	}

	@Override
	public void addRedisSetArticleId(String key, double score, String member) {
		if(redisDao==null)
			return ;
		
		redisDao.zadd(key, score, member);
	}

	@Override
	public List<String> getRedisArticleIds(String key) {
		if(redisDao==null)
			return null;
		return redisDao.lrange(key, 0, -1);
	}

	@Override
	public List<ArticleInfo> getArticleByUserId(int userId, List<Integer> typeList) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < typeList.size(); i++) {
			if (i == typeList.size() - 1) {
				sb.append("a.type = ? ");
			} else
				sb.append("a.type = ? or ");

		}
		String hql = "from ArticleInfo as a where a.user.userId = ? and ("+sb+") order by a.date desc";
		//String hql = "select a from ArticleInfo as a join a.tags as t where t.content in (" + sb + ")";
		//System.out.println("Hql:   " + hql);
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userId);
		for (int i = 0; i < typeList.size(); i++) {
			int type = typeList.get(i);
			query.setInteger(i+1, type);
		}
		

		return query.list();
	}

	@Override
	public void deleteRedisKey(String key) {
		if(redisDao==null)
			return ;
		redisDao.del(key);
	}

	@Override
	public boolean setBrowseCount(int articleId, int count) {
		String hql = "update ArticleInfo as a set a.browseCount =:count where a.articleId=:articleId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("count", count);
		query.setInteger("articleId", articleId);
		
		return query.executeUpdate()>0;
	}

	@Override
	public void setRedisArticleInfo(String key, String field, String value) {
		if(redisDao==null)
			return ;
		//redisDao.hincrby(ArticleInfo.getKey(articleId),ArticleInfo.PRAISECOUNT,1);
		redisDao.hset(key, field, value);
	}

	@Override
	public long addRedisCollectionCount(int articleId) {
		if(redisDao==null)
			return 0;
		return redisDao.hincrby(ArticleInfo.getKey(articleId), ArticleInfo.COLLECTIONCOUNT, 1);
	}

	@Override
	public boolean addActivityArticleId(int activityId, int articleid) {
		String sql = "insert into RelActivityArticle(ActivityId,ArticleId) values(?,?)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, activityId);
		query.setInteger(1, articleid);
		
		return query.executeUpdate()>0;
	}

	@Override
	public boolean changeArticleShowId(int articleId, int showId) {
		String hql = "update ArticleInfo as a set a.activity.activityId =:showId where a.articleId =:articleId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("showId", showId);
		query.setInteger("articleId", articleId);
		
		return query.executeUpdate()>0;
	}

	@Override
	public List<ArticleInfo> getUserPublishList(List<Integer> roles,int start, int maxResult) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < roles.size(); i++) {
			if (i == roles.size() - 1) {
				sb.append("u.role = ? ");
			} else
				sb.append("u.role = ? or ");

		}
		String hql = "select a from ArticleInfo as a,UserInfo as u where ("+sb+") and a.user.userId = u.userId order by a.articleId desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		for (int i = 0; i < roles.size(); i++) {
			int type = roles.get(i);
			query.setInteger(i, type);
		}
		query.setFirstResult(start);
		query.setMaxResults(maxResult);
		
		return query.list();
	}

	@Override
	public void removeRedisZSetArticleId(String key, String member) {
		redisDao.zrem(key, member);
	}
}
