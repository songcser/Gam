package com.stark.web.dao;

import java.util.List;
import java.util.Set;

import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.ArticlePublishTimeLine;
import com.stark.web.entity.ChartletInfo;
import com.stark.web.entity.FileInfo;
import com.stark.web.entity.RelArticleForward;
import com.stark.web.entity.RelChartletPicture;

public interface IArticleDAO {
    
    	
	public int addArticle(ArticleInfo aInfo);
	
	public boolean addRedisArticle(ArticleInfo raInfo);
	
	public boolean updateArticle(ArticleInfo aInfo);
	
	public ArticleInfo getArticle(int aid);
	
	public List<ArticleInfo> getArticleByUserId(int uid, int start, int maxCount);

	public boolean addForward(RelArticleForward rel);

	public boolean praise(int userId, int articleId);

	public List<ArticleInfo> getFollowArticle(int uid, int page, int maxCount);

	public boolean addTimeLine(ArticlePublishTimeLine timeLine);

	public List<ArticleInfo> getSquareArticle(int page, int maxCount);

	public List<Object[]> getByTagAndLocation(String tag, String location,int page,int maxCount);

	public List<Object[]> getHotArticles();

	public boolean isParise(int userId, int articleId);

	public List<ArticleInfo> getArticleByTag(String tag, int page, int maxCount);
	
	public  boolean addCommentCount(String id);

	public List<String> getPicListById(int articleId);

	public boolean uploadPicture(int id, String fileName);

	public boolean deleteByUserId(String userId);

	public List<ArticleInfo> getArticleByTag(List<String> tagList,int page,int maxCount);

	public List<ArticleInfo> getDayExquisites(int page, int maxResults);

	public List<ArticleInfo> getArticleByType(int type, int start,int maxResults);

	public List<ArticleInfo> getListByActivityId(int activityId, int page,int maxResults);

	public boolean setArticleType(int articleId, int type);

	public List<Object[]> getArticlePicByUserId(int userId, int page, int maxPictureResult);

	public void addRedisUserArticleList(int userId, int articleId);

	public void addRedisArticlePicture(int id, String fileName);

	public int addChartlet(ChartletInfo chartlet);

	public int addChartletPicture(int chartletId, int fileId);

	public List<ChartletInfo> getAllChartlet();

	public int addFile(FileInfo fileInfo);

	public int addChartletPicture(RelChartletPicture rel);

	public boolean changeChartletPictureStatus(int pictureId, int status);

	public boolean removeChartletPicture(int pictureId);

	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int page, int maxResults);

	public int getPraiseCount(int articleId);
	
	public int getCommentCount(int articleId) ;

	public boolean changeArticleType(int articleId, int type);

	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int type, int page, int maxResults);

	public List<ArticleInfo> getArticleByUserId(int uid, List<Integer> typeList, int start, int maxCount);

	public boolean deleteByActivityId(int activityId);

	public int getCountByType(int index);

	public int getCountByTypes(List<Integer> list);

	public List<ArticleInfo> getArticleByType(List<Integer> types, int start, int maxResults);

	public List<Object[]> getRedisArticlePicByUserId(int userId, int page, int maxPictureResult);

	public List<ArticleInfo> getAllListByActivityId(int activityId, int page, int maxResults);

	public List<String> getRedisArticleByType(int type, int page, int maxResults);

	public List<ArticleInfo> getRedisArticles(String key,int page, int maxResults);

	public List<String> getRedisPictures(int articleId);

	public boolean isRedisPraise(int userId, int articleId);

	public long addRedisPraise(int userId, int articleId);

	public String getRedisArticleCount(String key);

	public void setRedisArticleCount(String key, int count);

	public long addRedisArticleId(String key, int id);

	public List<String> getRedisArticleIds(String key, int page, int maxResults);

	public ArticleInfo getRedisArticle(int id);

	public long addRedisCommentCount(int articleId);

	public void addRedisArticlesId(String key, int articleId);

	public void addRedisArticleCount(String key);

	public void addRedisExquisitesList(int articleId);

	public void addRedisUpdateList(int articleId);

	public void addRedisUserArticleCount(int userId);

	public void addRedisUserPraiseCount(int userId);

	public void changeRedisArticleType(int articleId, int type);

	public void decRedisArticleCount(String key);

	public void addRedisMagazineList(int articleId);

	public void addRedisActivityAllList(int activityId, int articleId);

	public void addRedisActivityAuditingList(int activityId, int articleId);

	public void removeRedisArticleList(String key, int articleId);

	public boolean setKeyExpire(String key, int expire);

	public boolean addRedisChartlet(ChartletInfo chartlet);

	public List<String> getRedisChartletIds(String key);

	public ChartletInfo getRedisChartlet(int chartletId);

	public ChartletInfo getChartlet(int chartletId);

	public void addRedisAllChartlet(String key, int chartletId);

	public List<RelChartletPicture> getChartletPictures(int chartletId);

	public List<String> getRedisChartletPictureIds(String key);

	public boolean addRedisChartletPicture(RelChartletPicture rel);

	public RelChartletPicture getChartletPicture(int relId);

	public RelChartletPicture getRedisChartletPicture(int relId);

	public void addRedisChartletPictures(String key, int id);

	public void removeRedisKey(String key);

	public void removeRedisChartletPicture(int chartletId, int pictureId);

	public void setRedisChartletPictureStatus(int pictureId, int status);

	public boolean deleteChartlet(int chartletId);

	public void removeRedisChartlet(int chartletId);

	public boolean deleteChartletPictureByChartletId(int chartletId);

	public void removeRedisAllChartlet(String key, int chartletId);

	public void removeRedisUserArticleList(int userId, int articleId);

	public void decRedisUserArticleCount(int userId);

	public void addRedisUserArticleRList(int uid, int articleId);

	public void addRedisExquisitesRList(int articleId);

	public void addRedisUpdateRList(int articleId);

	public long addRedisArticleIdR(String string, int articleId);

	public void addRedisMagazineRList(int articleId);

	public boolean isCollection(int userId, int articleId);

	public List<ArticleInfo> getListByShowId(int showId,int type, int start, int maxResults);

	public boolean collectArticle(int userId, int articleId);

	public boolean browseArticle(int articleId);

	public List<ArticleInfo> getCollectionList(int userId, int page, int maxResult);

	public long addRedisBrowseCount(int articleId);

	public Set<String> getRedisFollowArticleIdSet(String string, int page, int maxResult);

	public void addRedisSetArticleId(String key, double score,String member);

	public List<String> getRedisArticleIds(String string);

	public List<ArticleInfo> getArticleByUserId(int userId, List<Integer> list);

	public void deleteRedisKey(String key);

	public boolean setBrowseCount(int articleId, int count);

	public void setRedisArticleInfo(String key, String field, String value);

	public long addRedisCollectionCount(int articleId);

	public boolean addActivityArticleId(int activityId, int id);

	public boolean changeArticleShowId(int articleId, int showId);

	public List<ArticleInfo> getUserPublishList(List<Integer> roles,int start, int maxResult);

	public void removeRedisZSetArticleId(String key, String member);

	public boolean setBubbleCoordinate(int bubbleId, String flag, int value);

	public List<ChartletInfo> getChartletByType(int type);

}
