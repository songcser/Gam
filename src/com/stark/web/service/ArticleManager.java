package com.stark.web.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.stark.web.dao.IArticleDAO;
import com.stark.web.dao.IUserDAO;
import com.stark.web.define.EnumBase.UserRole;
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
import com.stark.web.hunter.FileManager;

public class ArticleManager implements IArticleManager {

	private IArticleDAO articleDao;
	
	@Resource
	private IUserDAO userDao;
	
	@Resource
	private IUserManager userManager;
	
	@Resource
	private IActivityManager activityManager;

	// 获取img标签正则
	// private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?> ";
	private static final String IMGURL_REG = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	// 获取src路径的正则
	// private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
	public static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";

	private static final String IMGURLS_REG = "(?x)(src|SRC|background|BACKGROUND)=('|\")/?(([\\w-]+/)*([\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";

	public void setArticleDao(IArticleDAO articleDao) {
		this.articleDao = articleDao;
	}
	
	public void setUserManager(IUserManager userManager){
		this.userManager = userManager;
	}
	
	public void setActivityManager(IActivityManager activityManager){
		this.activityManager = activityManager;
	}
	
	@Override
	public int addArticle(ArticleInfo aInfo) {
		int id = articleDao.addArticle(aInfo);
		if (id > 0) {
			aInfo.setArticleId(id);
			aInfo.setBrowseCount(0);
			
			if(aInfo.getActivity()!=null){
				articleDao.addActivityArticleId(aInfo.getActivity().getActivityId(),id);
			}
			
			articleDao.addRedisArticle(aInfo);
			articleDao.addRedisUserArticleList(aInfo.getUser().getUserId(),aInfo.getArticleId());
			articleDao.addRedisUpdateList(id);
			articleDao.addRedisUserArticleCount(aInfo.getUser().getUserId());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			articleDao.addRedisArticleId(RedisInfo.ARTICLEDATELIST+sdf.format(aInfo.getDate()), id);
			
			//int userId = aInfo.getUser().getUserId();
			
			int type = aInfo.getType();
			if(type==ArticleType.Publish.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEPUBLISHCOUNT);
				articleDao.addRedisArticleId(RedisInfo.ARTICLEMOMENTLIST, id);
			}
			else if(type==ArticleType.Forward.getIndex()){
				
			}
			else if(type==ArticleType.DayExquisite.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
				articleDao.addRedisExquisitesList(id);
			}
			else if(type == ArticleType.FashionMagazine.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.addRedisMagazineList(id);
			}
			else if(type == ArticleType.ExquisiteMagazine.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.addRedisExquisitesList(id);
				articleDao.addRedisMagazineList(id);
			}
			else if(type == ArticleType.Report.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEREPORTCOUNT);
			}
			else if(type == ArticleType.DayExquisiteReport.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEREPORTCOUNT);
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
			}
			else if(type == ArticleType.FashionMagazineReport.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEREPORTCOUNT);
				articleDao.addRedisMagazineList(id);
			}
			else if(type == ArticleType.ExquisiteMagazineReport.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEREPORTCOUNT);
				articleDao.addRedisExquisitesList(id);
				articleDao.addRedisMagazineList(id);
			}
			else if(type == ArticleType.NoAuditingActivity.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLENOAUDITINGCOUNT);
				articleDao.addRedisActivityAllList(aInfo.getActivity().getActivityId(),id);
				
			}
			else if(type == ArticleType.Activity.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEACTIVITYCOUNT);
				articleDao.addRedisActivityAllList(aInfo.getActivity().getActivityId(),id);
				articleDao.addRedisActivityAuditingList(aInfo.getActivity().getActivityId(),aInfo.getArticleId());
			}
			else if(type == ArticleType.Delete.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEDELETECOUNT);
			}
			else if(type == ArticleType.ExquisiteNoAuditing.getIndex()){
				articleDao.addRedisArticleId(RedisInfo.ARTICLENOAUDITINGRECOMMENDLIST, aInfo.getArticleId());
			}
			else if(type== ArticleType.CommonNoAuditing.getIndex()){
				articleDao.addRedisArticleId(RedisInfo.ARTICLENOAUDITINGMOMENTLIST, id);
			}
		}

		return id;
	}
	
	

	public IUserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDAO userDao) {
		this.userDao = userDao;
	}
	@Override
	public boolean updateArticle(ArticleInfo aInfo) {
		return articleDao.updateArticle(aInfo);
	}

	@Override
	public ArticleInfo getArticle(int aid) {
		ArticleInfo article = articleDao.getRedisArticle(aid);
		if(article==null){
			article = articleDao.getArticle(aid);
			if(article!=null){
				articleDao.addRedisArticle(article);
			}
		}
		return article;
	}

	@Override
	public List<ArticleInfo> getArticleByUserId(int uid, int page, int maxCount) {
		List<String> ids = articleDao.getRedisArticleIds(RedisInfo.USERARTICLELIST+uid,page,maxCount);
		List<ArticleInfo> alist = new ArrayList<ArticleInfo>();
		UserInfo user = userManager.getUser(uid);
		int size = 0;
		if(ids!=null&&!ids.isEmpty()){
			size=ids.size();
			for(String id:ids){
				ArticleInfo article = getArticle(Integer.parseInt(id));
				if(article!=null){
					alist.add(article);
				}
			}
			if(size==maxCount||user.getArticleCount()==page*maxCount+size)
				return alist;
		}
		List<Integer> list = getCommonArticleTypeList();
		
		List<ArticleInfo> tlist = articleDao.getArticleByUserId(uid,list, page*maxCount+size, maxCount-size);
		if(tlist!=null){
			//size = alist.size();
			for(int i=0;i<tlist.size();i++){
				ArticleInfo article = tlist.get(i);
				setArticleCount(article);
				alist.add(article);
				articleDao.addRedisArticle(article);
				articleDao.addRedisUserArticleRList(uid,article.getArticleId());
			}
			
		}
		return  alist;
	}

	@Override
	public List<ArticleInfo> getFollowArticle(int uid, int page, int maxCount) {
		return articleDao.getFollowArticle(uid, page, maxCount);
	}

	@Override
	public List<ArticleInfo> getArticleByTag(String tag, int page, int maxCount) {
		return articleDao.getArticleByTag(tag, page, maxCount);
	}

	@Override
	public boolean addForward(RelArticleForward rel) {
		return articleDao.addForward(rel);
	}

	@Override
	public boolean praise(int userId, int articleId) {
		boolean result = articleDao.praise(userId, articleId);
		//System.out.println(result);
		if(result){
			articleDao.addRedisPraise(userId, articleId);
			ArticleInfo article = getArticle(articleId);
			articleDao.addRedisUserPraiseCount(article.getUser().getUserId());
			
		}
		return result;
	}

	@Override
	public boolean addTimeLine(ArticlePublishTimeLine timeLine) {

		return articleDao.addTimeLine(timeLine);
	}

	@Override
	public List<ArticleInfo> getSquareArticle(int page, int maxCount) {
		return articleDao.getSquareArticle(page, maxCount);
	}

	@Override
	public List<Object[]> getHotArticles() {
		return articleDao.getHotArticles();
	}

	@Override
	public List<Object[]> getByTagAndLocation(String tag, String location, int page, int maxCount) {
		return articleDao.getByTagAndLocation(tag, location, page, maxCount);
	}

	@Override
	public boolean isPraise(int userId, int articleId) {
		boolean result = articleDao.isRedisPraise(userId,articleId);
		if(!result){
			result = articleDao.isParise(userId, articleId);
			if(result){
				articleDao.addRedisPraise(userId,articleId);
			}
		}
		return result;
	}

	public String getOneHtml(String htmlUrl) {
		// System.out.println("Html Url:" +htmlUrl);
		URL url;
		String temp;
		final StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlUrl);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			InputStreamReader input = new InputStreamReader(connection.getInputStream(), "utf-8");
			final BufferedReader in = new BufferedReader(input);// 读取网页全部内容
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (final MalformedURLException me) {
			System.out.println("你输入的URL格式有问题！请仔细输入");
			me.getMessage();
			// throw me;
		} catch (final IOException e) {
			e.printStackTrace();
			// throw e;
		}
		return sb.toString();
	}

	@Override
	public String getTitle(String str) {
		String regex;
		String title = "";
		final List<String> list = new ArrayList<String>();
		regex = "<title>.*?</title>";
		final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		final Matcher ma = pa.matcher(str);
		while (ma.find()) {
			list.add(ma.group());
		}
		for (int i = 0; i < list.size(); i++) {
			title = title + list.get(i);
		}
		return outTag(title);
	}

	public String getImg(String html) {
		return "";
	}

	/***
	 * 获取ImageUrl地址
	 * 
	 * @param HTML
	 * @return
	 */
	public List<String> getImageUrl(String HTML) {
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
		List<String> listImgUrl = new ArrayList<String>();
		int i = 0;
		while (matcher.find() && i < 8) {
			i++;
			listImgUrl.add(matcher.group());
			// System.out.println("url:   "+matcher.group());
		}

		if (i == 0) {
			matcher = Pattern.compile(IMGURLS_REG).matcher(HTML);
			while (matcher.find() && i < 8) {
				i++;
				listImgUrl.add(matcher.group());
			}
		}
		System.out.println("Size  " + listImgUrl.size());
		return listImgUrl;
	}

	/***
	 * 获取ImageSrc地址
	 * 
	 * @param listImageUrl
	 * @return
	 */
	public List<String> getImageSrc(List<String> listImageUrl) {
		List<String> listImgSrc = new ArrayList<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}

	public void Download(List<String> listImgSrc, int id) {
		System.out.println("Image Size: " + listImgSrc.size());
		for (String url : listImgSrc) {
			String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
			System.out.println("Url: " + url);
			try {
				String path = FileManager.getArticlePicturePath(id, imageName);
				System.out.println(path);
				URL uri = new URL(url);
				FileManager.upload(path, uri);
				// InputStream in = uri.openStream();
				// FileOutputStream fo = new FileOutputStream(new File(path));
				// byte[] buf = new byte[1024];
				// int length = 0;
				// System.out.println("开始下载:" + url);
				// while ((length = in.read(buf, 0, buf.length)) != -1) {
				// fo.write(buf, 0, length);
				// }
				// in.close();
				// fo.close();
				System.out.println(imageName + "下载完成");

				updatePicture(id, imageName);
				return;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}

	}

	public boolean downloadPic(String url, String path) {

		try {
			URL uri = new URL(url);
			InputStream in = uri.openStream();
			FileOutputStream fo = new FileOutputStream(new File(path));
			byte[] buf = new byte[1024];
			int length = 0;
			System.out.println("开始下载:" + url);
			while ((length = in.read(buf, 0, buf.length)) != -1) {
				fo.write(buf, 0, length);
			}
			in.close();
			fo.close();
			System.out.println(path + "下载完成");
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println("下载失败");
			return false;
		}

	}

	private String outTag(final String s) {
		return s.replaceAll("<.*?>", "");
	}

	@Override
	public List<String> getPicListById(int articleId) {
		List<String> picList = articleDao.getRedisPictures(articleId);
		List<String> pictures = new ArrayList<String>();
		if(picList==null||picList.isEmpty()){
			picList =articleDao.getPicListById(articleId);
			if(picList!=null){
				int size = picList.size();
				for(int i=0;i<size;i++){
					String pic = picList.get(i);
					articleDao.addRedisArticlePicture(articleId, pic);
					String temp = FileManager.getArticlePictureUrl(articleId, pic);
					pictures.add(temp);
				}
			}
		}
		else{
			for(String pic:picList){
				String temp = FileManager.getArticlePictureUrl(articleId, pic);
				pictures.add(temp);
			}
			//return picList;
		}
		return pictures;
	}
	
	@Override
	public boolean updatePicture(int id, String fileName) {
		boolean result = articleDao.uploadPicture(id, fileName);
		if(result){
			articleDao.addRedisArticlePicture(id,fileName);
		}
		return result;
	}

	@Override
	public boolean deleteByUserId(String userId) {

		return articleDao.deleteByUserId(userId);
	}

	@Override
	public List<ArticleInfo> getArticleByTag(List<String> tagList, int page, int maxCount) {
		return articleDao.getArticleByTag(tagList, page, maxCount);
	}

	@Override
	public List<ArticleInfo> getDayExquisites(int page, int maxResults) {
		List<ArticleInfo> list = articleDao.getRedisArticles(RedisInfo.ARTICLEDAYEXQUISITELIST, page, maxResults);
		
		if(list==null||list.isEmpty()||list.size()<maxResults){
			int size = 0;
			if(list!=null){
				size=list.size();
			}
			else {
				list = new ArrayList<ArticleInfo>();
			}
			List<Integer> types = new ArrayList<Integer>();
			types.add(ArticleType.DayExquisite.getIndex());
			types.add(ArticleType.ExquisiteMagazine.getIndex());
			types.add(ArticleType.DayExquisiteReport.getIndex());
			types.add(ArticleType.ExquisiteMagazineReport.getIndex());
			
			List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
			if(tlist!=null){
				for(int i=0;i<tlist.size();i++){
					ArticleInfo article = tlist.get(i);
					articleDao.addRedisArticle(article);
					articleDao.addRedisExquisitesRList(article.getArticleId());
					list.add(article);
					setArticleCount(article);
				}
			}
		}
		
		return list;
	}

	@Override
	public List<ArticleInfo> getUpdateArticle(int page, int maxResults) {
		
		List<ArticleInfo> list = articleDao.getRedisArticles(RedisInfo.ARTICLEUPDATELIST, page, maxResults);;
		if(list==null||list.isEmpty()||list.size()<maxResults){
			int size = 0;
			if(list!=null){
				size=list.size();
			}
			else {
				list = new ArrayList<ArticleInfo>();
			}
			List<Integer> types = new ArrayList<Integer>();
			types.add(ArticleType.DayExquisite.getIndex());
			types.add(ArticleType.ExquisiteMagazine.getIndex());
			types.add(ArticleType.DayExquisiteReport.getIndex());
			types.add(ArticleType.Publish.getIndex());
			types.add(ArticleType.Forward.getIndex());
			types.add(ArticleType.FashionMagazine.getIndex());
			types.add(ArticleType.Report.getIndex());
			types.add(ArticleType.FashionMagazineReport.getIndex());
			types.add(ArticleType.ExquisiteMagazineReport.getIndex());
			
			List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
			if(tlist!=null){
				for(int i=0;i<tlist.size();i++){
					ArticleInfo article = tlist.get(i);
					articleDao.addRedisArticle(article);
					articleDao.addRedisUpdateRList(article.getArticleId());
					list.add(article);
					setArticleCount(article);
				}
			}
		}
		
		return list;
	}

	@Override
	public List<ArticleInfo> getNewFashionMagazine(int page, int maxResults) {
		List<ArticleInfo> list = articleDao.getRedisArticles(RedisInfo.ARTICLEMAGAZINELIST, page, maxResults);
		if(list==null||list.isEmpty()||list.size()<maxResults){
			int size = 0;
			if(list!=null){
				size=list.size();
			}
			else {
				list=new ArrayList<ArticleInfo>();
			}
			List<Integer> types = new ArrayList<Integer>();
			types.add(ArticleType.ExquisiteMagazine.getIndex());
			types.add(ArticleType.FashionMagazine.getIndex());
			types.add(ArticleType.FashionMagazineReport.getIndex());
			types.add(ArticleType.ExquisiteMagazineReport.getIndex());
			
			List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
			if(tlist!=null&&!tlist.isEmpty()){
				for(int i=0;i<tlist.size();i++){
					ArticleInfo article = tlist.get(i);
					articleDao.addRedisArticle(article);
					articleDao.addRedisMagazineRList(article.getArticleId());
					setArticleCount(article);
					list.add(article);
				}
			}
		}
		
		return list;
	}

	@Override
	public List<ArticleInfo> getListByActivityId(int activityId, int page, int maxResults) {
		List<String> ids = articleDao.getRedisArticleIds(RedisInfo.ACTIVITYARTICLEAUDITINGLIST+activityId, page, maxResults);
		List<ArticleInfo> list = new ArrayList<ArticleInfo>();
		int size = 0;
		if(ids!=null&&!ids.isEmpty()){
			size = ids.size();
			for(String id:ids){
				ArticleInfo article = getArticle(Integer.parseInt(id));
				list.add(article);
			}
			if(ids.size()==maxResults)
				return list;
		}
		
		List<ArticleInfo> tlist = articleDao.getListByActivityId(activityId, page*maxResults+size, maxResults-size);
		if(tlist!=null&&!tlist.isEmpty()){
			for(int i=0;i<tlist.size();i++){
				ArticleInfo article = tlist.get(i);
				articleDao.addRedisArticle(article);
				articleDao.addRedisArticleIdR(RedisInfo.ACTIVITYARTICLEAUDITINGLIST+activityId, article.getArticleId());
				setArticleCount(article);
				list.add(article);
			}
		}
		return list;
	}

	@Override
	public boolean setArticleType(int articleId, int type) {
		return articleDao.setArticleType(articleId, type);
	}

	@Override
	public List<Object[]> getArticlePicByUserId(int userId, int page, int maxPictureResult) {
		return articleDao.getArticlePicByUserId(userId, page, maxPictureResult);
	}

	@Override
	public int addChartlet(ChartletInfo chartlet) {
		int chartletId = articleDao.addChartlet(chartlet);
		if(chartletId>0){
			articleDao.addRedisChartlet(chartlet);
			articleDao.addRedisAllChartlet(RedisInfo.CHARTLETALLLIST, chartletId);
		}
		return chartletId;
	}

	@Override
	public int addChartletPicture(int chartletId, int fileId) {
		
		return articleDao.addChartletPicture(chartletId,fileId);
	}
	
	@Override
	public ChartletInfo getChartlet(int chartletId){
		ChartletInfo chartlet = articleDao.getRedisChartlet(chartletId);
		if(chartlet==null){
			chartlet = articleDao.getChartlet(chartletId);
			if(chartlet!=null){
				articleDao.addRedisChartlet(chartlet);
			}
		}
		
		return chartlet;
	}
	
	@Override
	public List<ChartletInfo> getAllChartlet() {
		List<String> ids = articleDao.getRedisChartletIds(RedisInfo.CHARTLETALLLIST);
		List<ChartletInfo> list = new ArrayList<ChartletInfo>();
		if(ids!=null&&!ids.isEmpty()){
			for(String id : ids){
				ChartletInfo chartlet = getChartlet(Integer.parseInt(id));
				if(chartlet!=null)
					list.add(chartlet);
			}
			return list;
		}
		
		list = articleDao.getAllChartlet();
		if(list!=null){
			int size = list.size();
			for(int i=size-1;i>=0;i--){
				ChartletInfo chartlet = list.get(i);
				articleDao.addRedisChartlet(chartlet);
				articleDao.addRedisAllChartlet(RedisInfo.CHARTLETALLLIST,chartlet.getChartletId());
			}
		}
		return list;
	}

	@Override
	public int addFile(FileInfo fileInfo) {
		return articleDao.addFile(fileInfo);
	}

	@Override
	public int addChartletPicture(RelChartletPicture rel) {
		int relId = articleDao.addChartletPicture(rel);
		if(relId>0){
			articleDao.addRedisChartletPicture(rel);
			articleDao.addRedisChartletPictures(RedisInfo.CHARTLETPICTURELIST+rel.getChartlet().getChartletId(), relId);
		}
		return relId;
	}

	@Override
	public boolean changeChartletPictureStatus(int pictureId, int status) {
		boolean result = articleDao.changeChartletPictureStatus(pictureId,status);
		if(result){
			articleDao.setRedisChartletPictureStatus(pictureId,status);
		}
		return result;
	}

	@Override
	public boolean removeChartletPicture(int pictureId) {
		boolean result = articleDao.removeChartletPicture(pictureId);
		if(result){
			RelChartletPicture rel = getRelChartletPicture(pictureId);
			articleDao.removeRedisKey(RelChartletPicture.getKey(pictureId));
			articleDao.removeRedisChartletPicture(rel.getChartlet().getChartletId(),pictureId);
			String path = FileManager.getChartletPicturePath(rel.getChartlet().getChartletId(), rel.getPicture());
			FileManager.delete(path);
		}
		return result;
	}

	@Override
	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int page, int maxResults) {
		String date = startDate.substring(0,10);
		String key = RedisInfo.ARTICLEDATELIST+date;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		List<ArticleInfo> list = new ArrayList<ArticleInfo>();
		int size=0;
		if(ids!=null&&!ids.isEmpty()){
			size = ids.size();
			for(String id:ids){
				ArticleInfo article = getArticle(Integer.parseInt(id));
				if(article!=null){
					list.add(article);
				}
			}
			if(size==maxResults)
				return list;
		}
		List<ArticleInfo> tlist = articleDao.getArticlesByDate(startDate, endDate,page*maxResults+size, maxResults-size);
		System.out.println(tlist.size());
		if(tlist!=null&&!tlist.isEmpty()){
			for(int i=0;i<tlist.size();i++){
				ArticleInfo article = tlist.get(i);
				articleDao.addRedisArticle(article);
				//setArticleCount(article);
				list.add(article);
				long len = articleDao.addRedisArticleIdR(key, article.getArticleId());
				if(len == 1){
					articleDao.setKeyExpire(key,60*60*24*10);
				}
			}
		}
		return list;
	}
	
	@Override
	public List<ArticleInfo> getArticlesByDate(String startDate, String endDate, int type, int page, int maxResults) {
		String date = startDate.substring(0,10);
		List<Integer> types = getTypeList(type);
		List<String> ids = articleDao.getRedisArticleIds(RedisInfo.ARTICLEDATELIST+date, page, maxResults);
		List<ArticleInfo> list = new ArrayList<ArticleInfo>();
		int size=0;
		if(ids!=null&&!ids.isEmpty()){
			size = ids.size();
			for(String id:ids){
				ArticleInfo article = getArticle(Integer.parseInt(id));
				if(article!=null){
					int atype = article.getType();
					for(int t:types){
						if(t==atype){
							list.add(article);
						}
					}
				}
			}
			if(size==maxResults)
				return list;
		}
		List<ArticleInfo> tlist = articleDao.getArticlesByDate(startDate, endDate,page*maxResults+size, maxResults-size);
		if(tlist!=null&&!tlist.isEmpty()){
			for(int i=0;i<tlist.size();i++){
				ArticleInfo article = tlist.get(i);
				//setArticleCount(article);
				articleDao.addRedisArticle(article);
				if(article!=null){
					int atype = article.getType();
					for(int t:types){
						if(t==atype){
							list.add(article);
						}
					}
				}
				long len = articleDao.addRedisArticleIdR(RedisInfo.ARTICLEDATELIST+date, article.getArticleId());
				if(len == 1){
					articleDao.setKeyExpire(RedisInfo.ARTICLEDATELIST+date,60*60*24*10);
				}
//				if(article.getType()!=type){
//					list.remove(i);
//					i--;
//					size--;
//				}
			}
		}
		return list;
	}
	
	private List<Integer> getTypeList(int type) {
		List<Integer> types = new ArrayList<Integer>();
		types.add(type);
		if(type==ArticleType.DayExquisite.getIndex()){
			types.add(ArticleType.ExquisiteMagazine.getIndex());
			types.add(ArticleType.DayExquisiteReport.getIndex());
			types.add(ArticleType.ExquisiteMagazineReport.getIndex());
			types.add(ArticleType.ExquisiteNoAuditing.getIndex());
			types.add(ArticleType.CommonExquisite.getIndex());
			types.add(ArticleType.ActivityExquisite.getIndex());
		}
		else if(type==ArticleType.Publish.getIndex()){
			types.add(ArticleType.Publish.getIndex());
			types.add(ArticleType.CommonNoAuditing.getIndex());
		}
		return types;
	}

	@Override
	public int getPraiseCount(int articleId) {
		return articleDao.getPraiseCount(articleId);
	}

	@Override
	public boolean changeArticleType(int articleId, int type) {
		ArticleInfo article = getArticle(articleId);
		int oldType = article.getType();
		if(type==oldType){
			return true;
		}
		boolean result = articleDao.changeArticleType(articleId,type);
		if(result){
			articleDao.changeRedisArticleType(articleId,type);
			
			if(oldType==ArticleType.Publish.getIndex()){
				articleDao.decRedisArticleCount(RedisInfo.ARTICLEPUBLISHCOUNT);
				articleDao.removeRedisArticleList(RedisInfo.ARTICLEMOMENTLIST, articleId);
			}
			else if(oldType==ArticleType.DayExquisite.getIndex()||oldType==ArticleType.DayExquisiteReport.getIndex()){
				articleDao.decRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
				articleDao.removeRedisArticleList(RedisInfo.ARTICLEDAYEXQUISITELIST, articleId);
				articleDao.removeRedisArticleList(RedisInfo.ARTICLERECOMMENDLIST, articleId);
			}
			else if(oldType==ArticleType.FashionMagazine.getIndex()||oldType==ArticleType.FashionMagazineReport.getIndex()){
				articleDao.decRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.removeRedisArticleList(RedisInfo.ARTICLEMAGAZINELIST, articleId);
			}
			else if(oldType==ArticleType.ExquisiteMagazine.getIndex()||oldType==ArticleType.ExquisiteMagazineReport.getIndex()){
				articleDao.decRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
				articleDao.decRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.removeRedisArticleList(RedisInfo.ARTICLEDAYEXQUISITELIST, articleId);
				articleDao.removeRedisArticleList(RedisInfo.ARTICLEMAGAZINELIST, articleId);
			}
			else if(oldType==ArticleType.NoAuditingActivity.getIndex()){
				articleDao.decRedisArticleCount(RedisInfo.ARTICLENOAUDITINGCOUNT);
				articleDao.removeRedisArticleList(RedisInfo.ACTIVITYNOAUDITINGLIST+article.getActivity().getActivityId(), articleId);
			}
			else if(oldType==ArticleType.Activity.getIndex()){
				if(type==ArticleType.Delete.getIndex()){
					articleDao.decRedisArticleCount(RedisInfo.ARTICLEACTIVITYCOUNT);
					articleDao.removeRedisArticleList(RedisInfo.ACTIVITYARTICLEALLLIST+article.getActivity().getActivityId(), articleId);
					articleDao.removeRedisArticleList(RedisInfo.ACTIVITYARTICLEAUDITINGLIST+article.getActivity().getActivityId(), articleId);
				}
			}
			else if(oldType==ArticleType.ExquisiteNoAuditing.getIndex()){
				articleDao.removeRedisArticleList(RedisInfo.ARTICLENOAUDITINGRECOMMENDLIST,articleId);
			}
			else if(oldType==ArticleType.CommonNoAuditing.getIndex()){
				articleDao.removeRedisArticleList(RedisInfo.ARTICLENOAUDITINGMOMENTLIST, articleId);
			}
			
			
			if(type==ArticleType.Publish.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEPUBLISHCOUNT);
				articleDao.addRedisArticleId(RedisInfo.ARTICLEMOMENTLIST, articleId);
			}
			else if(type==ArticleType.DayExquisite.getIndex()||type==ArticleType.DayExquisiteReport.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
				articleDao.addRedisExquisitesList(articleId);
				articleDao.addRedisArticleId(RedisInfo.ARTICLERECOMMENDLIST, articleId);
			}
			else if(type==ArticleType.FashionMagazine.getIndex()||type==ArticleType.FashionMagazineReport.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.addRedisMagazineList(articleId);
			}
			else if(type==ArticleType.ExquisiteMagazine.getIndex()||type==ArticleType.ExquisiteMagazineReport.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
				articleDao.addRedisExquisitesList(articleId);
				articleDao.addRedisMagazineList(articleId);
			}
			else if(type==ArticleType.Report.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEREPORTCOUNT);
			}
			else if(type==ArticleType.Activity.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEACTIVITYCOUNT);
				articleDao.addRedisActivityAuditingList(article.getActivity().getActivityId(), articleId);
			}
			else if(type==ArticleType.Delete.getIndex()){
				articleDao.addRedisArticleCount(RedisInfo.ARTICLEDELETECOUNT);
				articleDao.removeRedisArticleList(RedisInfo.ARTICLEUPDATELIST,articleId);
				articleDao.removeRedisUserArticleList(article.getUser().getUserId(), articleId);
				articleDao.decRedisUserArticleCount(article.getUser().getUserId());
				articleDao.addRedisArticleId(RedisInfo.ARTICLEDELETELIST, articleId);
			}
			else if(type==ArticleType.ActivityExquisite.getIndex()){
				articleDao.addRedisArticleId(RedisInfo.ARTICLERECOMMENDLIST, articleId);
			}
		}
		return result;
	}

	@Override
	public boolean deleteByActivityId(int activityId) {
		return articleDao.deleteByActivityId(activityId);
	}

	@Override
	public int getExquisiteCount() {
		String count = articleDao.getRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT);
		if(count==null||count.equals("")){
			List<Integer> list = new ArrayList<Integer>();
			list.add(ArticleType.DayExquisite.getIndex());
			list.add(ArticleType.ExquisiteMagazine.getIndex());
			list.add(ArticleType.DayExquisiteReport.getIndex());
			list.add(ArticleType.ExquisiteMagazineReport.getIndex());
			int c = articleDao.getCountByTypes(list);
			articleDao.setRedisArticleCount(RedisInfo.ARTICLEEXQUISITECOUNT,c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public int getMagazineCount() {
		String count = articleDao.getRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT);
		if(count==null||count.equals("")){
			List<Integer> list = new ArrayList<Integer>();
			list.add(ArticleType.FashionMagazine.getIndex());
			list.add(ArticleType.ExquisiteMagazine.getIndex());
			list.add(ArticleType.FashionMagazineReport.getIndex());
			list.add(ArticleType.ExquisiteMagazineReport.getIndex());
			int c =  articleDao.getCountByTypes(list);
			articleDao.setRedisArticleCount(RedisInfo.ARTICLEMAGAZINECOUNT, c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public int getActivityArticleCount() {
		String count = articleDao.getRedisArticleCount(RedisInfo.ARTICLEACTIVITYCOUNT);
		if(count==null||count.equals("")){
			int c =  articleDao.getCountByType(ArticleType.Activity.getIndex());
			articleDao.setRedisArticleCount(RedisInfo.ARTICLEACTIVITYCOUNT, c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public int getNoAuditingCount() {
		String count = articleDao.getRedisArticleCount(RedisInfo.ARTICLENOAUDITINGCOUNT);
		if(count==null||count.equals("")){
			int c = articleDao.getCountByType(ArticleType.NoAuditingActivity.getIndex());
			articleDao.setRedisArticleCount(RedisInfo.ARTICLENOAUDITINGCOUNT, c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public int getdeleteCount() {
		String count = articleDao.getRedisArticleCount(RedisInfo.ARTICLEDELETECOUNT);
		if(count==null||count.equals("")){
			int c = articleDao.getCountByType(ArticleType.Delete.getIndex());
			articleDao.setRedisArticleCount(RedisInfo.ARTICLEDELETECOUNT, c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public int getPublishCount() {
		String count = articleDao.getRedisArticleCount(RedisInfo.ARTICLEPUBLISHCOUNT);
		if(count==null||count.equals("")){
			int c = articleDao.getCountByType(ArticleType.Publish.getIndex());
			articleDao.setRedisArticleCount(RedisInfo.ARTICLEPUBLISHCOUNT, c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public Map<String, Object> getArticlePicturesByUserId(int userId, int page,int maxPictureResult) {
		List<ArticleInfo> articles = getArticleByUserId(userId,page,maxPictureResult);  
		//System.out.println(articles.size());
		Map<String,Object> map = new HashMap<String,Object>();
		if(articles==null){
			map.put("result", 0);
			return map;
		}
		map.put("result", 1);
		List<Map<String, Object>> list = articlesToPictureList(articles);
		map.put("pictures", list);
		//articleDao.getRedisArticlePicByUserId(userId,page,maxPictureResult);
		return map;
	}

	@Override
	public List<Map<String, Object>> articlesToPictureList(List<ArticleInfo> articles) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		List<Object[]> picList = getArticlePicByUserId(userId, page, maxPictureResult);
		for(ArticleInfo article:articles){
			List<String> pList = getPicListById(article.getArticleId());
			if(pList.size()<=0){
				continue;
			}
			Map<String, Object> picMap = new HashMap<String, Object>();
			int articleId = article.getArticleId();
			picMap.put("articleId", articleId);
			picMap.put("url", pList.get(0));
			
			list.add(picMap);
		}
		return list;
	}

	@Override
	public List<ArticleInfo> getAllListByActivityId(int activityId, int page, int maxResults) {
		List<String> ids = articleDao.getRedisArticleIds(RedisInfo.ACTIVITYARTICLEALLLIST+activityId, page, maxResults);
		List<ArticleInfo> list = new ArrayList<ArticleInfo>();
		int size=0;
		if(ids!=null&&!ids.isEmpty()){
			size=ids.size();
			for(String id:ids){
				ArticleInfo article = getArticle(Integer.parseInt(id));
				list.add(article);
			}
			if(size==maxResults)
				return list;
		}
		
		List<ArticleInfo> tlist = articleDao.getAllListByActivityId(activityId, page*maxResults+size, maxResults-size);
		if(tlist!=null&&!tlist.isEmpty()){
			for(int i=0;i<tlist.size();i++){
				ArticleInfo article = tlist.get(i);
				//setArticleCount(article);
				articleDao.addRedisArticle(article);
				articleDao.addRedisArticleIdR(RedisInfo.ACTIVITYARTICLEALLLIST+activityId, article.getArticleId());
				list.add(article);
			}
		}
		return list;
	}

	@Override
	public int getReportCount() {
		String count = articleDao.getRedisArticleCount(RedisInfo.ARTICLEREPORTCOUNT);
		if(count==null||count.equals("")){
			List<Integer> list = new ArrayList<Integer>();
			list.add(ArticleType.Report.getIndex());
			list.add(ArticleType.DayExquisiteReport.getIndex());
			list.add(ArticleType.FashionMagazineReport.getIndex());
			list.add(ArticleType.ExquisiteMagazineReport.getIndex());
			int c =  articleDao.getCountByTypes(list);
			articleDao.setRedisArticleCount(RedisInfo.ARTICLEREPORTCOUNT, c);
			return c;
		}
		return Integer.parseInt(count);
	}

	@Override
	public boolean addCommentCount(int articleId) {
		return articleDao.addRedisCommentCount(articleId)>0;
	}

	@Override
	public List<RelChartletPicture> getChartletPictures(int chartletId) {
		List<String> picIds = articleDao.getRedisChartletPictureIds(RedisInfo.CHARTLETPICTURELIST+chartletId);
		List<RelChartletPicture> list = new ArrayList<RelChartletPicture>();
		if(picIds!=null&&!picIds.isEmpty()){
			for(String id:picIds){
				RelChartletPicture rel = getRelChartletPicture(Integer.parseInt(id));
				//System.out.println(rel.getPicture());
				if(rel!=null){
					list.add(rel);
				}
			}
			return list;
		}
		list = articleDao.getChartletPictures(chartletId);
		if(list!=null&&!list.isEmpty()){
			int size = list.size();
			for(int i=size-1;i>=0;i--){
				RelChartletPicture rel = list.get(i);
				articleDao.addRedisChartletPicture(rel);
				articleDao.addRedisChartletPictures(RedisInfo.CHARTLETPICTURELIST+chartletId,rel.getId());
			}
		}
		return list;
	}

	@Override
	public RelChartletPicture getRelChartletPicture(int relId) {
		RelChartletPicture rel = articleDao.getRedisChartletPicture(relId);
		if(rel==null){
			rel = articleDao.getChartletPicture(relId);
			if(rel!=null){
				articleDao.addRedisChartletPicture(rel);
			}
		}
		return rel;
	}
	
	private void setArticleCount(ArticleInfo article){
		int count = articleDao.getCommentCount(article.getArticleId());
		article.setCommentCount(count);
		count = articleDao.getPraiseCount(article.getArticleId());
		article.setPraiseCount(count);
	}

	@Override
	public boolean deleteChartlet(int chartletId) {
		List<String> picIds = articleDao.getRedisChartletPictureIds(RedisInfo.CHARTLETPICTURELIST+chartletId);
		if(picIds!=null&&!picIds.isEmpty()){
			for(String id : picIds){
				removeChartletPicture(Integer.parseInt(id));
			}
		}
		boolean result = articleDao.deleteChartlet(chartletId);
		if(result){
			articleDao.removeRedisChartlet(chartletId);
			articleDao.removeRedisAllChartlet(RedisInfo.CHARTLETALLLIST,chartletId);
		}
		return result;
	}

	@Override
	public Map<String, Object> getRecommendList(int userId,int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.ARTICLERECOMMENDLIST;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,userId);
		
		List<Integer> types = new ArrayList<Integer>();
		types.add(ArticleType.DayExquisite.getIndex());
		types.add(ArticleType.DayExquisiteReport.getIndex());
		types.add(ArticleType.CommonExquisite.getIndex());
		types.add(ArticleType.ActivityExquisite.getIndex());
		types.add(ArticleType.ExquisiteMagazineReport.getIndex());
		List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
		
		listToListAndAddRedisId(key,tlist,articles);
		
		return articlesToMap(articles,userId);
	}

	private boolean listToListAndAddRedisId(String key,List<ArticleInfo> fromList,List<ArticleInfo> toList) {
		if(fromList==null){
			return false;
		}
		for(int i=0;i<fromList.size();i++){
			ArticleInfo article = fromList.get(i);
			articleDao.addRedisArticle(article);
			articleDao.addRedisArticleIdR(key, article.getArticleId());
			//setArticleCount(article);
			toList.add(article);
		}
		return true;
	}

	private int idsToArticleList(List<String> ids, List<ArticleInfo> list) {
		if(ids!=null&&!ids.isEmpty()){
    		for(String id:ids){
    			ArticleInfo article = getArticle(Integer.parseInt(id));
    			if(article!=null){
    				list.add(article);
    			}
    		}
    		return ids.size();
		}
		return 0;
	}

	@Override
	public Map<String, Object> articlesToMap(List<ArticleInfo> articles, int userId) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> aList = new ArrayList<Map<String,Object>>();
		for(ArticleInfo article:articles){
			
			aList.add(articleToMap(article,userId));
		}
		map.put("articles", aList);
		map.put("result", 1);
		return map;
	}
	
	private Map<String,Object> articleToMap(ArticleInfo article,int userId){
		Map<String,Object> aMap = new HashMap<String,Object>();
		UserInfo user = userManager.getUser(article.getUser().getUserId());
		int articleId = article.getArticleId();
		aMap.put("articleId", articleId);
		aMap.put("type", article.getType());
		aMap.put("typeStr", ArticleType.getName(article.getType()));
		aMap.put("userId", user.getUserId());
		DateFormat df = WebManager.getDateFormat();
		Date date = article.getDate();
		if(date!=null){
			aMap.put("date", df.format(date));
		}
		
		aMap.put("name", user.getName());
		aMap.put("headPic", user.getHeadUrl());
		aMap.put("userRole", UserRole.getName(user.getRole()));
		String title = article.getTitle();
		if(title!=null){
			aMap.put("title", title);
		}
		else{
			aMap.put("title", "");
		}
		aMap.put("content", article.getContent());
		aMap.put("reference", article.getReference());
		ActivityInfo act = article.getActivity();
		if(act!=null){
			act = activityManager.getActivity(act.getActivityId());
			aMap.put("showId", act.getActivityId());
			aMap.put("showTitle", act.getSubject());
			aMap.put("showType", act.getType());
		}
		else{
			aMap.put("showId", 0);
			aMap.put("showTitle", "");
			aMap.put("showType", 0);
		}
		
		if(userId>0){
			boolean flag = articleDao.isCollection(userId,articleId);
			aMap.put("collection", flag?1:0);
			flag = false;
			flag = isPraise(userId, articleId);
			aMap.put("praiseStatus", flag ? 1 : 0);
			
			flag = false;
			flag = userManager.isFollow(userId, user.getUserId());
			aMap.put("followStatus", flag ? 1 : 0);
		}
		else{
			aMap.put("collection", 0);
			aMap.put("praiseStatus",  0);
		}
		
		aMap.put("collectionCount", article.getCollectionCount());
		aMap.put("praiseCount", article.getPraiseCount());
		aMap.put("commentCount", article.getCommentCount());
		
		List<String> pics = getPicListById(articleId);
		aMap.put("pictures", pics);
		aMap.put("browseCount", article.getBrowseCount());
		aMap.put("url", article.getUrl());
		aMap.put("shareUrl", FileManager.getShareUrl(articleId));
		//aMap.put("articles", aMap);
		return aMap;
	}
	
	@Override
	public Map<String, Object> getShowArticleList(int showId, int userId, int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.ACTIVITYARTICLEAUDITINGLIST+showId;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,userId);
		
		List<ArticleInfo> alist = articleDao.getListByShowId(showId,ArticleType.Activity.getIndex(),page*maxResults+size,maxResults-size);
		
		listToListAndAddRedisId(key,alist,articles);
		
		return articlesToMap(articles,userId);
	}
	
	@Override
	public Map<String, Object> getArticleInfo(int articleId, int userId) {
		ArticleInfo article = getArticle(articleId);
		Map<String,Object> map = new HashMap<String,Object>();
		if(article==null){
			map.put("result", 0);
		}
		map = articleToMap(article,userId);
		map.put("result", 1);
		return map;
	}
	
	@Override
	public boolean collectArticle(int userId, int articleId) {
		boolean result = articleDao.collectArticle(userId,articleId);
		if(result){
			String key = RedisInfo.USERCOLLECTIONLIST+userId;
			articleDao.addRedisArticleIdR(key, articleId);
			articleDao.addRedisCollectionCount(articleId);
		}
		return result;
	}
	
	@Override
	public boolean browseArticle(int articleId) {
		boolean result = articleDao.browseArticle(articleId);
		if(result){
			articleDao.addRedisBrowseCount(articleId);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getFollowArticleList(int userId, int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.USERFOLLOWARTICLEZSET+userId;
		Set<String> ids = articleDao.getRedisFollowArticleIdSet(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,userId);
		
		List<ArticleInfo> alist = articleDao.getFollowArticle(userId, page*maxResults+size,maxResults-size);
		if(alist==null){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("result", 0);
			return map;
		}
		
		listToZSetAndAddRedisId(key,alist,articles);
		
		return articlesToMap(articles,userId);
	}
	
	private boolean listToZSetAndAddRedisId(String key, List<ArticleInfo> fromList, List<ArticleInfo> toList) {
		if(fromList==null){
			return false;
		}
		for(int i=0;i<fromList.size();i++){
			ArticleInfo article = fromList.get(i);
			articleDao.addRedisArticle(article);
			int articleId = article.getArticleId();
			
			addSetArticleId(key,articleId,articleId+"");
			//setArticleCount(article);
			toList.add(article);
		}
		return true;
	}

	public void addSetArticleId(String key, int score, String member) {
		articleDao.addRedisSetArticleId(key, score,member);
	}

	private int idsToArticleList(Set<String> ids, List<ArticleInfo> list) {
		if(ids!=null&&!ids.isEmpty()){
    		for(String id:ids){
    			ArticleInfo article = getArticle(Integer.parseInt(id));
    			if(article!=null){
    				list.add(article);
    			}
    		}
    		return ids.size();
		}
		return 0;
	}

	@Override
	public Map<String, Object> getCollectionPictures(int userId, int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		Map<String, Object> map = new HashMap<String,Object>();
		String key = RedisInfo.USERCOLLECTIONLIST+userId;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		UserInfo user = userManager.getUser(userId);
		if(size==maxResults||user.getArticleCount()==page*maxResults+size){
			List<Map<String, Object>> list = articlesToPictureList(articles);
			map.put("result", 1);
			map.put("pictures", list);
			return map;
		}
			
		List<ArticleInfo> alist = articleDao.getCollectionList(userId,page*maxResults+size,maxResults-size);
		if(alist!=null&&!alist.isEmpty()){
			
			listToListAndAddRedisId(key,alist,articles);
		}
		map.put("result", 1);
		List<Map<String, Object>> list = articlesToPictureList(articles);
		map.put("pictures", list);
		return map;
		
	}
	
	@Override
	public List<ArticleInfo> getAllArticleByUserId(int userId) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.USERARTICLELIST+userId;
		List<String> ids = articleDao.getRedisArticleIds(key);
		
		int size = idsToArticleList(ids,articles);
		UserInfo user = userManager.getUser(userId);
		if(user.getArticleCount()==size)
			return articles;
		
		List<Integer> list = getCommonArticleTypeList();
		
		List<ArticleInfo> alist = articleDao.getArticleByUserId(userId, list);
		articleDao.deleteRedisKey(key);
		listToListAndAddRedisId(key,alist,articles);
		
		return alist;
	}
	
	private List<Integer> getCommonArticleTypeList(){
		List<Integer> list = new ArrayList<Integer>(); 
		list.add(ArticleType.Publish.getIndex());
		list.add(ArticleType.Forward.getIndex());
		list.add(ArticleType.DayExquisite.getIndex());
		list.add(ArticleType.FashionMagazine.getIndex());
		list.add(ArticleType.ExquisiteMagazine.getIndex());
		list.add(ArticleType.Report.getIndex());
		list.add(ArticleType.DayExquisiteReport.getIndex());
		list.add(ArticleType.FashionMagazineReport.getIndex());
		list.add(ArticleType.ExquisiteMagazineReport.getIndex());
		list.add(ArticleType.NoAuditingActivity.getIndex());
		list.add(ArticleType.Activity.getIndex());
		
		return list;
	}

	
	@Override
	public Map<String, Object> getNoAuditingRecommendList(int userId, int page, int maxResults) {
		String key = RedisInfo.ARTICLENOAUDITINGRECOMMENDLIST;
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,userId);
		
		List<Integer> types = new ArrayList<Integer>();
		//types.add(ArticleType.DayExquisite.getIndex());
		//types.add(ArticleType.DayExquisiteReport.getIndex());
		//types.add(ArticleType.CommonExquisite.getIndex());
		types.add(ArticleType.ExquisiteNoAuditing.getIndex());
		List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
		
		listToListAndAddRedisId(key,tlist,articles);
		
		return articlesToMap(articles,userId);
	}

	
	@Override
	public boolean setBrowseCount(int articleId, int count) {
		boolean result = articleDao.setBrowseCount(articleId,count);
		if(result){
			String key = ArticleInfo.getKey(articleId);
			articleDao.setRedisArticleCount(key,ArticleInfo.BROWSECOUNT,count);
		}
		return result;
	}

	@Override
	public Map<String, Object> getNoAuditingShowArticles(int showId, int userId, int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.ACTIVITYNOAUDITINGLIST+showId;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,userId);
		
		List<ArticleInfo> alist = articleDao.getListByShowId(showId,ArticleType.NoAuditingActivity.getIndex(),page*maxResults+size,maxResults-size);
		
		listToListAndAddRedisId(key,alist,articles);
		
		return articlesToMap(articles,userId);
	}

	@Override
	public Map<String, Object> getMomentList(int userId, int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.ARTICLEMOMENTLIST;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,userId);
		
		List<Integer> types = new ArrayList<Integer>();
		types.add(ArticleType.Publish.getIndex());
		List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
		
		listToListAndAddRedisId(key,tlist,articles);
		
		return articlesToMap(articles,userId);
	}

	@Override
	public Map<String, Object> getNoAuditingMomentList(int userId, int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.ARTICLENOAUDITINGMOMENTLIST;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,userId);
		
		List<Integer> types = new ArrayList<Integer>();
		types.add(ArticleType.CommonNoAuditing.getIndex());
		List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
		
		listToListAndAddRedisId(key,tlist,articles);
		
		return articlesToMap(articles,userId);
	}

	@Override
	public Map<String, Object> getDeleteList(int page, int maxResults) {
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		String key = RedisInfo.ARTICLEDELETELIST;
		List<String> ids = articleDao.getRedisArticleIds(key, page, maxResults);
		int size = idsToArticleList(ids,articles);
		if(size==maxResults)
			return articlesToMap(articles,0);
		
		List<Integer> types = new ArrayList<Integer>();
		types.add(ArticleType.Delete.getIndex());
		List<ArticleInfo> tlist = articleDao.getArticleByType(types, page*maxResults+size, maxResults-size);
		
		listToListAndAddRedisId(key,tlist,articles);
		
		return articlesToMap(articles,0);
	}
}
