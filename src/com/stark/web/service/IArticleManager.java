package com.stark.web.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.ArticlePublishTimeLine;
import com.stark.web.entity.ChartletInfo;
import com.stark.web.entity.DialogueInfo;
import com.stark.web.entity.FileInfo;
import com.stark.web.entity.RelArticleForward;
import com.stark.web.entity.RelChartletPicture;
import com.stark.web.entity.UserInfo;

public interface IArticleManager {
	
	public int addArticle(ArticleInfo aInfo);
	
	public boolean updateArticle(ArticleInfo aInfo);
	
	public ArticleInfo getArticle(int articleId);
	
	public List<ArticleInfo> getArticleByUserId(int uid, int page, int maxRequestCount);
	
	public List<ArticleInfo> getFollowArticle(int userId, int page, int maxRequestCount);
	
	public List<ArticleInfo> getArticleByTag(String tag, int page, int maxRequestCount);

	public boolean addForward(RelArticleForward rel);

	public boolean praise(int userId, int articleId);

	public boolean addTimeLine(ArticlePublishTimeLine timeLine);

	public List<ArticleInfo> getSquareArticle(int page, int maxRequestCount);

	public List<Object[]> getHotArticles();

	public List<Object[]> getByTagAndLocation(String tag, String location,int page,int maxRequestCount);

	public boolean isPraise(int userId, int articleId);

	public boolean addCommentCount(int articleId);
	
	public String getOneHtml(final String htmlUrl) ;
	
	public String getTitle(final String str);

	public List<String> getPicListById(int articleId);

	public String getImg(String html);

	//public boolean uploadPicture(int articleId, String fileName);

	public boolean deleteByUserId(String userId);
	
	public List<String> getImageUrl(String html);
	
	public  List<String> getImageSrc(List<String> listImageUrl);
	
	public void Download(List<String> listImgSrc,int id);
	
	public boolean downloadPic(String url,String path);

	public List<ArticleInfo> getArticleByTag(List<String> tagList,int page,int maxRequestCount);

	public boolean updatePicture(int id, String fileName);

	public List<ArticleInfo> getDayExquisites(int page, int maxResults);

	public List<ArticleInfo> getUpdateArticle(int page, int maxResults);

	public List<ArticleInfo> getNewFashionMagazine(int page, int maxResults);

	public List<ArticleInfo> getListByActivityId(int activityId, int page,int maxResults);

	public boolean setArticleType(int articleId, int type);

	public List<Object[]> getArticlePicByUserId(int userId, int page, int maxPictureResult);

	public int addChartlet(ChartletInfo chartlet);

	public int addChartletPicture(int chartletId, int fileId);

	public List<ChartletInfo> getAllChartlet();

	public int addFile(FileInfo fileInfo);

	public int addChartletPicture(RelChartletPicture rel);

	public boolean changeChartletPictureStatus(int pictureId, int status);

	public boolean removeChartletPicture(int pictureId);

	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int page, int maxResults);

	public int getPraiseCount(int articleId);

	public boolean changeArticleType(int articleId, int type);

	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int type, int page, int maxResults);

	public boolean deleteByActivityId(int activityId);

	public int getExquisiteCount();

	public int getMagazineCount();

	public int getActivityArticleCount();

	public int getNoAuditingCount();

	public int getdeleteCount();

	public int getPublishCount();

	public Map<String, Object> getArticlePicturesByUserId(int userId, int page,int maxPictureResult);

	public List<ArticleInfo> getAllListByActivityId(int activityId, int page, int maxResults);

	public int getReportCount();

	public ChartletInfo getChartlet(int chartletId);

	public List<RelChartletPicture> getChartletPictures(int chartletId);

	RelChartletPicture getRelChartletPicture(int relId);

	public boolean deleteChartlet(int chartletId);
	
	public Map<String, Object> test(String test);

	public Map<String, Object> getRecommendList(int userId, int page,int maxResults);

	//public Map<String, Object> articlesToMap(List<ArticleInfo> articles, int userId);

	public Map<String, Object> getShowArticleList(int showId, int userId, int page, int maxResults2);

	public Map<String, Object> getArticleInfo(int articleId, int userId);

	public boolean collectArticle(int userId, int articleId);

	public boolean browseArticle(int articleId);

	public Map<String, Object> getFollowArticleList(int userId, int page, int maxResults2);

	public Map<String, Object> getCollectionPictures(int userId, int page, int maxPictureResult);

	public List<ArticleInfo> getAllArticleByUserId(int followId);

	public void addSetArticleId(String key, int score, String member);

	List<Map<String, Object>> articlesToPictureList(List<ArticleInfo> articles);

	Map<String, Object> articlesToMap(List<ArticleInfo> articles, int userId);

	public Map<String, Object> getNoAuditingRecommendList(int userId, int page, int maxResults2);

	public boolean setBrowseCount(int articleId, int count);

	public Map<String, Object> getNoAuditingShowArticles(int showId, int userId, int page, int maxResults2);

	public Map<String, Object> getMomentList(int userId, int page, int maxResults);

	public Map<String, Object> getNoAuditingMomentList(int userId, int page, int maxResults2);

	public Map<String, Object> getDeleteList(int page, int maxResults);

	public boolean moveArticleToRecommend(int articleId, int showId);

	public Map<String, Object> getCommonPublishList(int page, int maxResults);

	public Map<String, Object> getOperPublishList(int page, int maxResults2);

	public void removeSetArticleId(String key, String member);

	public boolean setBubbleCoordinate(int bubbleId, String flag, int value);

	public List<ChartletInfo> getBubbleList();

	public int addDialogue(DialogueInfo dialogue);

	public List<DialogueInfo> getDialogueListByChartletId(int chartletId);

	public boolean deleteDialogue(int dialogueId);

	public List<ChartletInfo> getDialogueList();

	public ChartletInfo getUserChartlet();

	public void setValue(String key,String flag);

	public String getValue(String key);

	public void createArticleHtml();

	public void addArticleCount(String key);

	public void addArticleCount(String key, int size);

	public void descArticleCount(String string, int size);

	public void shieldRecommend(int articleId);

	public void shieldMoment(int articleId);

	public void shieldShow(int articleId);

	

}
