package com.stark.web.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.ArticlePublishTimeLine;
import com.stark.web.entity.ChartletInfo;
import com.stark.web.entity.CommentInfo;
import com.stark.web.entity.EnumBase;
import com.stark.web.entity.RelChartletPicture;
import com.stark.web.entity.UserGroup;
import com.stark.web.entity.EnumBase.ArticleType;
import com.stark.web.entity.EnumBase.ChartletStatus;
import com.stark.web.entity.EnumBase.ChartletType;
import com.stark.web.entity.EnumBase.NoticeStatus;
import com.stark.web.entity.EnumBase.NoticeType;
import com.stark.web.entity.NoticeInfo;
import com.stark.web.entity.TagInfo;
import com.stark.web.entity.UserInfo;
import com.stark.web.hunter.FileManager;
import com.stark.web.service.CommentManager;
import com.stark.web.service.IActivityManager;
import com.stark.web.service.IArticleManager;
import com.stark.web.service.ICommentManager;
import com.stark.web.service.INoticeManager;
import com.stark.web.service.ITagManager;
import com.stark.web.service.IUserManager;
import com.stark.web.service.WordFilter;

@Controller
@RequestMapping("/article")
public class ArticleController {

	private static int maxResults = 10;
	private static int maxResults2 = 20;
	private static String ourShareUrl = "http://192.168.1.102/StarkPet/article/outsideShare?articleId=";
	private static int maxPictureResult = 15;
	// private static final String HTTP_REG =
	// "http://([w-]+.)+[w-]+(/[w- ./?%&=]*)?";
	// private static final String HTTP_REG =
	// "(?<=http\\://)(?:[^.\\s]*?\\.)+(com|cn|net|org|biz|info|cc|tv)";
	private static final String HTTP_REG = "(((http|ftp|https|file)://)|((?<!((http|ftp|https|file)://))www\\.))" // 以http...或www开头
			+ ".*?" // 中间为任意内容，惰性匹配
			+ "(?=(&nbsp;|\\s|　|<br />|$|[<>]|[\u4e00-\u9fa5]))"; // 结束条件

	@Resource(name = "articleManager")
	private IArticleManager articleManager;

	@Resource(name = "tagManager")
	private ITagManager tagManager;

	@Resource(name = "noticeManager")
	private INoticeManager noticeManager;

	@Resource(name = "userManager")
	private IUserManager userManager;

	@Resource(name = "activityManager")
	private IActivityManager activityManager;

	@Resource(name = "commentManager")
	private ICommentManager commentManager;

	@RequestMapping("/publish.do")
	@ResponseBody
	public Map<String, Object> publish(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("==> /article/publish.do");
		//PrintWriter out = response.getWriter();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (multipartResolver.isMultipart(request)) {
			ArticleInfo article = new ArticleInfo();
			
			String activityId = request.getParameter("activityId");
			//System.out.println(activityId);
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			int userId = Integer.parseInt(multiRequest.getParameter("userId"));
			UserInfo user = userManager.getUser(userId);
			article.setUser(user);
			String content = multiRequest.getParameter("content");
			
			content = filterContent(content);
			
			article.setContent(content);
			article.setReference("");
			article.setDate(new Date());
			if(activityId!=null){
				article.setActivity(new ActivityInfo(Integer.parseInt(activityId)));
				article.setType(ArticleType.NoAuditingActivity.getIndex());
			}
			else {
				article.setType(EnumBase.ArticleType.Publish.getIndex());
			}
			
			int articleId = articleManager.addArticle(article);
			if(articleId<1){
				map.put("result", 0);
	
				return map;
			}
			map.put("articleId", articleId);
			map.put("outShareUrl", ourShareUrl+articleId);
			Iterator<String> iter = multiRequest.getFileNames();
			int i=0;
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				String name = file.getName();
				if (file != null&&!"".equals(name)) {
					String fileName = file.getOriginalFilename();
					if(fileName.equals(""))
						continue;
					//System.out.println("File Name:" + file.getName());
					String filepix = "";
					String rename = fileName;
					int index = name.indexOf("?");
					
					if(index>0){
						filepix = name.substring(index);
					}
					else {
						index = fileName.indexOf("?");
						rename = fileName.substring(0,index);
						filepix = fileName.substring(index);
					}
					System.out.println(rename);
					if(rename.endsWith(".gif")&&i==0){
						i++;
						String imageFormat = "gif";
						String gifName = "0suo101lue.gif";
						Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByFormatName(imageFormat);
						ImageReader imageReader = (ImageReader)imageReaders.next();
						imageReader.setInput(ImageIO.createImageInputStream(file.getInputStream()));
						//System.out.println(imageReader.getMinIndex());
						BufferedImage bi = imageReader.read(0);
						String path = FileManager.getArticlePicturePath(articleId, gifName);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(bi, imageFormat, baos);
						int size = baos.size();
						//FileManager.saveImage(bi,imageFormat,path);
						FileManager.upload(path, baos);
						
						//System.out.println("suolue");
						//InputStream is = new BufferedInputStream(bi);
						//ImageIO.createImageOutputStream(bi);
						articleManager.updatePicture(articleId, gifName+filepix);
					}
					i++;
					rename = i+rename;
					String path = FileManager.getArticlePicturePath(articleId, rename);
					//System.out.println("Path: "+path);

					try {
						//long size = file.getSize();
						FileManager.upload(path, file);
						// file.transferTo(localFile);
						articleManager.updatePicture(articleId, rename+filepix);
						
					} catch (IllegalStateException e) {
						e.printStackTrace();
						//out.print("<script language='javascript'>parent.callback('发布推文失败');</script>");
						map.put("result", 0);
						//out.flush();
						//out.close();
						return map;
						
					}
				} 
			}
		}
		
		map.put("result", 1);
		
		//out.print("<script language='javascript'>parent.callback('发布推文成功');</script>");
		//out.flush();
		//out.close();
		return map;
	}

	private String filterContent(String content) {
		WordFilter filter = new WordFilter();
		
		return filter.doFilter(content);
	}

	@RequestMapping("/getArticlePictures.do")
	@ResponseBody
	public Map<String, Object> getArticlePictures(int userId, int page) {
		System.out.println("==> /article/getArticlePictures.do?userId="+userId+"&page="+page);
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = articleManager.getArticlePicturesByUserId(userId, page, maxPictureResult);
		if(list==null){
			map.put("result", 0);
			return map;
		}
		map.put("result", 1);
		map.put("pictures", list);
		return map;
	}

	@RequestMapping("/getArticleDetial.do")
	@ResponseBody
	public Map<String, Object> getArticleDetial(int userId,int articleId) {
		System.out.println("==> /article/getArticleDetial.do?articleId="+articleId);
		ArticleInfo article = articleManager.getArticle(articleId);
		Map<String, Object> map = new HashMap<String, Object>();
		if(article==null){
			map.put("result", 0);
			return map;
		}
		map = articleToMap(article);
//		UserInfo user = userManager.getUser(article.getUser().getUserId());
		map.put("result", 1);
//		map.put("userId", user.getUserId());
//		map.put("name", user.getName());
//		map.put("headPic", user.getHeadUrl());
//		map.put("content", article.getContent());
//		List<String> picList = articleManager.getPicListById(articleId);
//		map.put("contentPics", picList);
//		map.put("commentCount", article.getCommentCount());
//		map.put("praiseCount", article.getPraiseCount());
		boolean ispraise = articleManager.isPraise(userId, articleId);
		map.put("praiseStatus", ispraise);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		List<UserInfo> praiseList = userManager.getPraiseUsers(articleId);
//		for (Iterator<UserInfo> iterator = praiseList.iterator(); iterator.hasNext();) {
//			UserInfo userInfo = iterator.next();
//			Map<String, Object> pMap = new HashMap<String, Object>();
//			pMap.put("userId", userInfo.getUserId());
//			pMap.put("headPic", userInfo.getHeadUrl());
//
//			list.add(pMap);
//		}
//		map.put("praiseList", list);
//
//		List<CommentInfo> commentList = commentManager.getCommentByArticleId(articleId);
//		map.put("comments", commentsToList(commentList));

		return map;
	}

	@RequestMapping("/report.do")
	@ResponseBody
	public Map<String, Object> report(int articleId) {
		System.out.println("==> /article/report.do?articleId="+articleId);
		Map<String, Object> map = new HashMap<String, Object>();
		int type = 0;
		ArticleInfo article = articleManager.getArticle(articleId);
		if(article.getType()==ArticleType.Publish.getIndex()){
			type = ArticleType.Report.getIndex();
		}
		else if(article.getType()==ArticleType.DayExquisite.getIndex()){
			type = ArticleType.DayExquisiteReport.getIndex();
		}
		else if(article.getType()==ArticleType.FashionMagazine.getIndex()){
			type = ArticleType.FashionMagazineReport.getIndex();
		}
		else if(article.getType()==ArticleType.ExquisiteMagazine.getIndex()){
			type = ArticleType.ExquisiteMagazineReport.getIndex();
		}
		else{
			map.put("result", 1);
			return map;
		}
		boolean result = articleManager.changeArticleType(articleId, type);
		
		map.put("result", result ? 1 : 0);
		return map;
	}

	@RequestMapping("/getExquisiteArticle.do")
	@ResponseBody
	public Map<String, Object> getExquisiteArticle(int userId) {
		System.out.println("==> /article/getExquisiteArticle.do?userId=" + userId);
		List<ActivityInfo> bannerActs = activityManager.getBannerActivity();
		List<ActivityInfo> topActs = activityManager.getTopRecommendActivity();
		List<ArticleInfo> dayExquisites = articleManager.getDayExquisites(0, maxResults);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> bannerList = new ArrayList<Map<String, Object>>();
		if(bannerActs==null||topActs==null||dayExquisites==null){
			map.put("result", 0);
			return map;
		}
		for (ActivityInfo act : bannerActs) {
			Map<String, Object> bannerMap = new HashMap<String, Object>();
			bannerMap.put("activityId", act.getActivityId());
			bannerMap.put("bannerPic", act.getBannerPicUrl());

			bannerList.add(bannerMap);
		}

		map.put("activities", bannerList);

		List<Map<String, Object>> topList = new ArrayList<Map<String, Object>>();
		for (ActivityInfo act : topActs) {
			Map<String, Object> topMap = new HashMap<String, Object>();
			topMap.put("activityId", act.getActivityId());
			topMap.put("picture", act.getBannerPicUrl());

			topList.add(topMap);
		}

		map.put("TopRecommend", topList);

		List<Map<String, Object>> articleList = articlesToList(userId, dayExquisites);

		map.put("dayExquisite", articleList);
		map.put("result", 1);
		return map;
	}

	@RequestMapping("getDayExquisiteArticles.do")
	@ResponseBody
	public Map<String, Object> getDayExquisiteArticle(int userId, int page) {
		System.out.println("==> /article/getDayExquisiteArticle.do?userId=" + userId + "&page=" + page);
		List<ArticleInfo> dayExquisites = articleManager.getDayExquisites(page, maxResults);
		Map<String,Object> map = new HashMap<String, Object>();
		if(dayExquisites==null){
			map.put("result", 0);
			return map;
		}
		List<Map<String, Object>> list = articlesToList(userId, dayExquisites);
//		for (ArticleInfo article : dayExquisites) {
//			Map<String, Object> dayMap = articleToMap(article);
//			boolean isPraise = articleManager.isPraise(userId, article.getArticleId());
//			dayMap.put("praiseStatus", isPraise ? 1 : 0);
//
//			list.add(dayMap);
//		}
		map.put("result", 1);
		map.put("articles", list);
		return map;
	}
	
	@RequestMapping("getDayExquisiteArticle.do")
	@ResponseBody
	public Map<String, Object> getDayExquisiteArticles(int userId, int page) {
		System.out.println("==> /article/getDayExquisiteArticle.do?userId=" + userId + "&page=" + page);
		List<ArticleInfo> dayExquisites = articleManager.getDayExquisites(page, maxResults);
		Map<String,Object> map = new HashMap<String, Object>();
		if(dayExquisites==null){
			map.put("result", 0);
			return map;
		}
		List<Map<String, Object>> list = articlesToList(userId, dayExquisites);
		map.put("result", 1);
		map.put("articles", list);

		return map;
	}

	@RequestMapping("/getUpdateArticle.do")
	@ResponseBody
	public Map<String, Object> getUpdateArticle(int userId, int page) {
		System.out.println("==> /article/getUpdateArticle.do?userId=" + userId + "&page=" + page);
		List<ArticleInfo> dayExquisites = articleManager.getUpdateArticle(page, maxResults);
		Map<String,Object> map = new HashMap<String, Object>();
		if(dayExquisites==null){
			map.put("result", 0);
			return map;
		}
		List<Map<String, Object>> list = articlesToList(userId, dayExquisites);
//		for (ArticleInfo article : dayExquisites) {
//			Map<String, Object> dayMap = ArticleManager.articleToMap(article);
//			boolean isPraise = articleManager.isPraise(userId, article.getArticleId());
//			dayMap.put("praiseStatus", isPraise ? 1 : 0);
//
//			list.add(dayMap);
//		}
		map.put("result", 1);
		map.put("articles", list);
		return map;
	}

	@RequestMapping("/getNewFashionMagazine.do")
	@ResponseBody
	public Map<String, Object> getNewFashionMagazine() {
		System.out.println("==> /article/getNewFashionMagazine.do");
		List<ArticleInfo> dayExquisites = articleManager.getNewFashionMagazine(0, 4);
		Map<String,Object> map = new HashMap<String, Object>();
		if(dayExquisites==null){
			map.put("result", 0);
			return map;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ArticleInfo article : dayExquisites) {
			Map<String, Object> dayMap = new HashMap<String, Object>();
			dayMap.put("articleId", article.getArticleId());
			dayMap.put("userId", article.getUser().getUserId());
			UserInfo user = userManager.getUser(article.getUser().getUserId());
			dayMap.put("name", user.getName());
			dayMap.put("headPic", user.getHeadUrl());
			dayMap.put("content", article.getContent());
			List<String> pics = articleManager.getPicListById(article.getArticleId());
			if(pics!=null&&!pics.isEmpty()){
				dayMap.put("picture", pics.get(0));
			}
			
			dayMap.put("sharedUrl", FileManager.getShareUrl(article.getArticleId()));
			list.add(dayMap);
		}
		map.put("result", 1);
		map.put("articles", list);
		return map;
	}
	
	@RequestMapping("/getMagazineArticles.do")
	@ResponseBody
	public Map<String, Object> getMagazineArticles(int userId,int page,HttpServletRequest request) {
		System.out.println("==> /article/getMagazineArticles.do?userId="+userId+"&page="+page);
		Map<String,Object> map = new HashMap<String, Object>();
		List<ArticleInfo> dayExquisites = articleManager.getNewFashionMagazine(page, maxResults);
		if(dayExquisites==null){
			map.put("result", 0);
			return map;
		}
	
		if(userId>0){
			List<Map<String, Object>> list = articlesToList(userId, dayExquisites);
			map.put("result", 1);
			map.put("articles", list);
		}
		else {
			map.put("result", 0);
		}
		//HttpSession session = request.getSession(true);
		//int userId = (int)session.getAttribute("adminId");
		//List<Map<String, Object>> list = articlesToList(userId, dayExquisites);
		//map.put("result", 1);
		//map.put("articles", list);
		return map;
	}

	@RequestMapping("/getListByActivityId.do")
	@ResponseBody
	public Map<String, Object> getListByActivityId(int activityId, int userId, int page) {
		System.out.println("==> /article/getListByActivityId.do?activityId=" + activityId + "&userId="+userId+"&page=" + page);
		List<ArticleInfo> articleInfos = articleManager.getListByActivityId(activityId, page, maxResults);
		Map<String, Object> map = new HashMap<String, Object>();
		if(articleInfos==null){
			map.put("result", 0);
			return map;
		}
		map.put("result", 1);
		if (page == 0) {
			ActivityInfo act = activityManager.getActivity(activityId);
			map.put("activityPic", act.getContentPicUrl());
		}
		List<Map<String, Object>> list = articlesToList(userId, articleInfos);
		//System.out.println(list.size());
		map.put("articles", list);
		return map;
	}
	
	@RequestMapping("/getAllListByActivityId.do")
	@ResponseBody
	public Map<String, Object> getAllListByActivityId(int activityId, int userId, int page) {
		System.out.println("==> /article/getAllListByActivityId.do?activityId=" + activityId + "&userId="+userId+"&page=" + page);
		List<ArticleInfo> articleInfos = articleManager.getAllListByActivityId(activityId, page, maxResults);
		Map<String, Object> map = new HashMap<String, Object>();
		if(articleInfos==null){
			map.put("result", 0);
			return map;
		}
		map.put("result", 1);
		if (page == 0) {
			ActivityInfo act = activityManager.getActivity(activityId);
			map.put("activityPic", act.getContentPicUrl());
		}
		List<Map<String, Object>> list = articlesToList(userId, articleInfos);
		//System.out.println(list.size());
		map.put("articles", list);
		return map;
	}

	public Map<String, Object> articleToMap(ArticleInfo article) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("articleId", article.getArticleId());
		map.put("type", article.getType());

		UserInfo user = userManager.getUser(article.getUser().getUserId());
		map.put("userId", user.getUserId());
		map.put("userName", user.getName());
		map.put("userRole", user.getRole());
		// String headPic = article.getUser().getHeadPic();
		map.put("headPic", user.getHeadUrl());
		map.put("content", article.getContent());
		map.put("reference", article.getReference());
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		if (article.getDate() != null) {
			map.put("date", format.format(article.getDate()));
		}
		map.put("praiseCount", article.getPraiseCount());
		// map.put("praiseStatus", 1);
		map.put("commentCount", article.getCommentCount());
		//map.put("url", article.getUrl());
		//map.put("title", article.getTitle());

		// map.put("tags", article.getTagsList());
		List<String> picList = articleManager.getPicListById(article.getArticleId());
//		List<String> pics = new ArrayList<String>();
//		for(String pic:picList){
//			System.out.println("pic: "+pic);
//			String temp = FileManager.getArticlePictureUrl(article.getArticleId(), pic);
//			pics.add(temp);
//		}
		map.put("pictures",picList);
		map.put("sharedUrl", FileManager.getShareUrl(article.getArticleId()));

		return map;
	}
	
	@RequestMapping("/add.do")
	public void add(ArticleInfo aInfo) {
		System.out.println("==> /article/add?" + aInfo);
		ArticlePublishTimeLine timeLine = new ArticlePublishTimeLine();
		timeLine.setArticle(aInfo);
		timeLine.setUser(new UserInfo(aInfo.getUser().getUserId()));
		timeLine.setType(0);
		// String id = articleManager.addArticle(aInfo);

	}

	@RequestMapping("/getFollowArticle.do")
	@ResponseBody
	public List<Map<String, Object>> getFollowArticle(int userId, int page) {
		System.out.println("==> /article/getFollowArticle?userId=" + userId + "&page=" + page);
		if (userId == 0) {
			return null;
		}
		// System.out.println("UserId:"+userId);
		// System.out.println(page);
		List<ArticleInfo> aList = articleManager.getFollowArticle(userId, page, maxResults);

		// System.out.println(aList.size());
		return articlesToList(userId, aList);
	}

	private List<Map<String, Object>> articlesToList(int userId, List<ArticleInfo> aList) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator<ArticleInfo> iterator = aList.iterator(); iterator.hasNext();) {
			ArticleInfo article = iterator.next();
			Map<String, Object> map = articleToMap(article);
			
			boolean flag = articleManager.isPraise(userId, article.getArticleId());
			map.put("praiseStatus", flag ? 1 : 0);
			
			list.add(map);
		}

		return list;
	}

	@RequestMapping("/getArticleByTag.do")
	@ResponseBody
	public List<Map<String, Object>> getArticleByTag(int userId, String tag, int page) {
		System.out.println("==> /article/getArticleByTag?userId=" + userId + "&tag=" + tag + "&page=" + page);
		List<ArticleInfo> aList = articleManager.getArticleByTag(tag, page, maxResults);
		// System.out.println(aList.size());
		// List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		return articlesToList(userId, aList);

	}

	@RequestMapping("/getArticleByTags.do")
	@ResponseBody
	public List<Map<String, Object>> getArticleByTags(int userId, String tag, String type, int page) {
		System.out.println("==> /article/getArticleByTags?userId=" + userId + "&tag=" + tag + "&type=" + type + "&page=" + page);
		List<String> tagList = new ArrayList<String>();
		tagList.add(tag);
		tagList.add(type);
		List<ArticleInfo> aList = articleManager.getArticleByTag(tagList, page, maxResults);
		System.out.println(aList.size());
		// List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		return articlesToList(userId, aList);
	}

	@RequestMapping("/getSquareArticle.do")
	@ResponseBody
	public List<Map<String, Object>> getSquareArticle(int userId, int page) {
		System.out.println("==> /article/getSquareArticle?userId=" + userId + "&page=" + page);
		if (userId == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		List<ArticleInfo> aList = articleManager.getSquareArticle(page, maxResults);

		return articlesToList(userId, aList);
	}

	@RequestMapping("/getFindArticle.do")
	@ResponseBody
	public Map<String, Object> getFindArticle() {
		System.out.println("==> /article/getFindArticle");
		Map<String, Object> map = new HashMap<String, Object>();

		List<ActivityInfo> activitys = activityManager.getAllStartUpActivity();
		// StringBuilder builder = new StringBuilder("[");
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		for (Iterator<ActivityInfo> iterator = activitys.iterator(); iterator.hasNext();) {
			ActivityInfo act = iterator.next();
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("activityId", act.getActivityId());
			map2.put("bannerPic", FileManager.getActivityPictureUrl(act.getActivityId(), act.getBannerPic()));
			if (act.getArticle().getPicList() != null && act.getArticle().getPicList().size() > 0) {
				String articlePic = act.getArticle().getPicList().get(0);
				map2.put("articlePic", articlePic);
			}

			maps.add(map2);
			// builder.append("{").append("\"id\":").append(act.getId()).append(",\"bannerPic\":").append(act.getBannerPic()).append("},");
		}
		map.put("activities", maps);

		// builder = new StringBuilder("[");
		maps = new ArrayList<Map<String, Object>>();
		List<TagInfo> tagsList = tagManager.getHotTags();
		for (Iterator<TagInfo> iterator = tagsList.iterator(); iterator.hasNext();) {
			TagInfo tag = iterator.next();
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("hotTag", tag.getContent());
			map2.put("userCount", tag.getUseCount());
			String picString = FileManager.getTagPictureUrl(tag.getTagId(), tag.getPicture());
			// System.out.println(picString);
			map2.put("picture", picString);
			// System.out.println(tag.getContent());

			List<ArticleInfo> articles = articleManager.getArticleByTag(tag.getContent(), 0, 9);

			List<String> userlist = new ArrayList<String>();
			List<String> picList = new ArrayList<String>();
			if (articles != null && articles.size() > 0) {

				for (int i = 0; i < articles.size() && i < 9; i++) {
					ArticleInfo artInfo = articles.get(i);
					artInfo = articleManager.getArticle(artInfo.getArticleId());
					UserInfo userInfo = userManager.getUser(artInfo.getUser().getUserId());
					if (userInfo != null && userInfo.getHeadPic() != null) {
						// System.out.println("====================="+userInfo);
						userlist.add("" + userInfo.getUserId());

						picList.add(userInfo.getHeadUrl());
					}

				}
			}
			map2.put("userList", userlist);
			map2.put("userPics", picList);
			maps.add(map2);
		}

		map.put("hotarticles", maps);
		return map;
	}

	@RequestMapping("/getActivityArticle.do")
	@ResponseBody
	public Map<String, Object> getActivityArticle(int activityId) {
		System.out.println("==> /article/getActivityArticle");
		ActivityInfo activity = activityManager.getActivity(activityId);
		ArticleInfo article = activity.getArticle();
		UserInfo user = activity.getUser();
		// List<String> tags = tagManager.getTagsByArticle(article.getId());
		List<CommentInfo> comments = commentManager.getCommentByArticleId(article.getArticleId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("articleId", article.getArticleId());
		map.put("userId", user.getUserId());
		map.put("userName", user.getName());
		map.put("headPic", user.getHeadUrl());
		map.put("content", article.getContent());
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		map.put("date", format.format(article.getDate()));
		// map.put("date", article.getDate());
		// if(article.getPicSet()!=null&&article.getPicSet().size()>0)
		// map.put("contentPic", article.getPicList().get(0));
		// map.put("praise", article.getPraiseCount());
		map.put("commentCount", article.getCommentCount());
		// map.put("praiseCount", article.getPraiseCount());
		// ArrayList<String> tags = new ArrayList<String>();
		// Set<TagInfo> tagSet = article.getTags();
		// System.out.println("Tag size"+tagSet.size());
		// for (Iterator<TagInfo> it = article.getTags().iterator();
		// it.hasNext();) {
		// String tag = it.next().getContent();
		// tags.add(tag);
		// }
		// map.put("tags", article.getTagsList());
		map.put("comments", CommentManager.ListToJson(comments));

		return map;
	}

	@RequestMapping("/getByTagAndLocation.do")
	@ResponseBody
	public List<Map<String, Object>> getByTagAndLocation(int userId, String tag, String location, int page) {
		System.out.println("==> /article/getByTagAndLocation?Tag=" + tag + "&location=" + location + "&page=" + page);
		List<Object[]> objs = articleManager.getByTagAndLocation(tag, location, page, maxResults);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (objs == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("result", "0");
			list.add(map);
			return list;
		}

		for (Object[] obj : objs) {
			// System.out.println(obj);
			ArticleInfo article = articleManager.getArticle(Integer.parseInt(obj[0] + ""));
			list.add(articleToMap(article));
		}

		return list;
	}

	@RequestMapping("/getArticlesByUserId.do")
	@ResponseBody
	public List<Map<String, Object>> getArticlesByUserId(int userId, int page) {
		System.out.println("==> /article/getArticlesByUserId?userId=" + userId + "&page=" + page);
		List<ArticleInfo> articles = articleManager.getArticleByUserId(userId, page, maxResults);
		if (articles == null)
			return null;
		
		return articlesToList(userId, articles);
		
	}

	@RequestMapping("/publish2.do")
	@ResponseBody
	public Map<String, Object> publish2(@RequestBody ArticleInfo article, HttpServletResponse response) {
		System.out.println("==> /publish2.do?" + article);
		// ArticlePublishTimeLine timeLine = new ArticlePublishTimeLine();
		// timeLine.setArticle(article);
		// timeLine.setUser(new UserInfo(article.getUser().getUserId()));
		// timeLine.setType(0);
		// timeLine.setDate(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		Set<TagInfo> tags = article.getTags();
		Set<String> atUserSet = article.getAtUser();
		// String url = article.getUrl();
		String content = article.getContent();
		article.setDate(new Date());
		article.setType(EnumBase.ArticleType.Publish.getIndex());
		Matcher matcher = Pattern.compile(HTTP_REG).matcher(content);
		int id = 0;
		if (matcher.find()) {
			String url = matcher.group().substring(0, matcher.group().length() - 1);
			article.setUrl(url);
			String html = articleManager.getOneHtml(url);
			String title = articleManager.getTitle(html);
			article.setTitle(title);
			// System.out.println(html);
			// System.out.println("Title:"+title);

			String cont = matcher.replaceAll(" ");
			System.out.println("Cont: " + cont);
			article.setContent(cont);
			id = articleManager.addArticle(article);
			if (id == 0) {
				map.put("result", 0);
				return map;
			}
			List<String> imageUrlList = articleManager.getImageUrl(html);
			List<String> imageSrcList = articleManager.getImageSrc(imageUrlList);
			// String path = FileManager.articlePictureStore +"/"+id;
			articleManager.Download(imageSrcList, id);

		} else {
			id = articleManager.addArticle(article);
			System.out.println("ArticleId:" + id);
			if (id == 0) {
				map.put("result", 0);
				return map;
			}
		}
		map.put("articleId", id);

		for (Iterator<TagInfo> iterator = tags.iterator(); iterator.hasNext();) {
			TagInfo tagInfo = iterator.next();
			TagInfo tag = tagManager.getTagByContent(tagInfo.getContent());
			if (tag != null) {
				// System.out.println(tag.getContent()+"------"+tag.getUseCount());
				tag.setUseCount(tag.getUseCount() + 1);
				tagManager.updateTag(tag);
				tagManager.addArticleTag(id, tag.getTagId());
			} else {
				Set<ArticleInfo> articles = new HashSet<ArticleInfo>();
				articles.add(article);
				tagInfo.setArticles(articles);
				tagManager.addTag(tagInfo);
			}
		}

		// articleManager.addTimeLine(timeLine);
		if (atUserSet != null) {
			for (Iterator<String> iterator = atUserSet.iterator(); iterator.hasNext();) {
				String userId = iterator.next();
				int senderId = article.getUser().getUserId();
				NoticeInfo notice = new NoticeInfo();
				notice.setUser(new UserInfo(Integer.parseInt(userId)));
				notice.setSender(new UserInfo(senderId));
				notice.setArticle(article);
				notice.setContent(article.getUser().getName() + "@了你");
				notice.setDate(new Date());
				notice.setType(NoticeType.At.getIndex());
				notice.setStatus(NoticeStatus.NoRead.getIndex());
				// System.out.println(userId);
				noticeManager.addNotice(notice);
			}
		}
		map.put("result", 1);
		return map;
	}

	@RequestMapping("/forward.do")
	@ResponseBody
	public Map<String, Object> forward(int userId, int articleId) {
		System.out.println("==> /article/forward.do?userId=" + userId + "&articleId=" + articleId);
		// RelArticleForward rel = new RelArticleForward();
		// rel.setArticle(new ArticleInfo(articleId));
		// rel.setUser(new UserInfo(userId));
		// rel.setDate(new Date());
		ArticleInfo article = new ArticleInfo();
		article.setUser(new UserInfo(userId));
		article.setDate(new Date());
		article.setContent("");
		article.setPraiseCount(0);
		article.setCommentCount(0);
		article.setStatus(EnumBase.ArticleStatus.Normal.getIndex());
		article.setType(EnumBase.ArticleType.Forward.getIndex());
		article.setUrl("");
		article.setTitle("");
		article.setAbbreviation("");
		article.setOriginalArticle(new ArticleInfo(articleId));

		int result = articleManager.addArticle(article);

		// ArticlePublishTimeLine timeLine = new ArticlePublishTimeLine();
		// timeLine.setArticle(new ArticleInfo(articleId));
		// timeLine.setUser(new UserInfo(userId));
		// timeLine.setType(1);
		// timeLine.setDate(new Date());
		// boolean result = articleManager.addTimeLine(timeLine);

		// String ret = "{\"result\":\""+(result!=0?1:0) +"\"}";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result != 0 ? 1 : 0);
		return map;
	}

	@RequestMapping("/praise.do")
	@ResponseBody
	public Map<String, Object> praise(int userId, int articleId) {
		System.out.println("==> /article/praise.do?userId=" + userId + "&articleId=" + articleId);
		boolean result = articleManager.praise(userId, articleId);
		if (result) {

			ArticleInfo article = articleManager.getArticle(articleId);
			//article.setPraiseCount(article.getPraiseCount() + 1);
			//articleManager.updateArticle(article);

			NoticeInfo notice = new NoticeInfo();
			notice.setUser(new UserInfo(article.getUser().getUserId()));
			notice.setSender(new UserInfo(userId));
			notice.setArticle(article);
			notice.setContent("赞了你");
			notice.setDate(new Date());
			notice.setType(NoticeType.Praise.getIndex());
			notice.setStatus(NoticeStatus.NoRead.getIndex());
			//System.out.println(userId);
			noticeManager.addNotice(notice);
		}
		// String ret = "{\"result\":\""+(result?1:0) +"\"}";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result ? 1 : 0);
		return map;
	}
	
	@RequestMapping("addPraises.do")
	@ResponseBody
	public Map<String, Object> addPraises(@RequestBody UserGroup userPraises){
		System.out.println("==> /user/addPraises.do?" + userPraises);
		Map<String, Object> map = new HashMap<String, Object>();
		if(userPraises==null){
			map.put("result", 0);
			return map;
		}
		map.put("result", 1);
		Set<Integer> praises = userPraises.getUsersId();
		int articleId = userPraises.getObjectId();
		int count =0;
		if(praises!=null&&articleId!=0){
			for(int userId:praises){
				boolean result = articleManager.isPraise(userId, articleId);
				if(result){
					continue;
				}
				count++;
				result = articleManager.praise(userId, articleId);
				if (result) {
					ArticleInfo article = articleManager.getArticle(articleId);

					NoticeInfo notice = new NoticeInfo();
					notice.setUser(new UserInfo(article.getUser().getUserId()));
					notice.setSender(new UserInfo(userId));
					notice.setArticle(article);
					notice.setContent("赞了你");
					notice.setDate(new Date());
					notice.setType(NoticeType.Praise.getIndex());
					notice.setStatus(NoticeStatus.NoRead.getIndex());
					//System.out.println(userId);
					noticeManager.addNotice(notice);
				}
			}
		}
		map.put("count", count);
		return map;
	}

	@RequestMapping("/addArticle.do")
	public void addArticle(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			//Map<String, Object> map = new HashMap<String, Object>();
			int userId = Integer.parseInt(request.getParameter("userId"));
			String activityId = request.getParameter("activityId");
			
			if (multipartResolver.isMultipart(request)) {
				
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				
				ArticleInfo article = new ArticleInfo();
				if(activityId!=null){
					article.setActivity(new ActivityInfo(Integer.parseInt(activityId)));
					article.setType(ArticleType.NoAuditingActivity.getIndex());
				}else{
					String articleType = request.getParameter("articleType");
					if(articleType!=null){
						article.setType(Integer.parseInt(articleType));
					}
					else {
						article.setType(EnumBase.ArticleType.Publish.getIndex());
					}
				}
				
				UserInfo user = userManager.getUser(userId);
				article.setUser(user);
				
				String content = multiRequest.getParameter("content");
				
				content = filterContent(content);
				article.setContent(content);
				article.setDate(new Date());
				article.setReference(multiRequest.getParameter("reference"));
				int articleId = articleManager.addArticle(article);
				if(articleId<1){
					//map.put("result", 0);
					out.print("<script language='javascript'>parent.callback('0','0');</script>");
					//map.put("result", 0);
					out.flush();
					out.close();
					return ;
				}
				Iterator<String> iter = multiRequest.getFileNames();
				int i=0;
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile((String) iter.next());
					String name = file.getName();
					if (file != null&&!"".equals(name)) {
						try {
						String fileName = file.getOriginalFilename();
						if(fileName.equals(""))
							continue;
						//System.out.println("File Name:" + file.getName());
						//Random ran = new Random(1000);
						//System.out.println(ran.nextInt());
						
						//System.out.println("Path: "+path);
						int index = name.indexOf("?");
						String filepix = "";
						if(index>0){
							filepix = name.substring(index);
						}
						//System.out.println(fileName);
						if(fileName.endsWith(".gif")&&i==0){
							i++;
							String imageFormat = "gif";
							String gifName = "0suo101lue.gif";
							Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByFormatName(imageFormat);
							ImageReader imageReader = (ImageReader)imageReaders.next();
							imageReader.setInput(ImageIO.createImageInputStream(file.getInputStream()));
							//System.out.println(imageReader.getMinIndex());
							BufferedImage bi = imageReader.read(0);
							String path = FileManager.getArticlePicturePath(articleId, gifName);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(bi, imageFormat, baos);
							//int size = baos.size();
							//FileManager.saveImage(bi,imageFormat,path);
							FileManager.upload(path, baos);
							
							//System.out.println("suolue");
							//InputStream is = new BufferedInputStream(bi);
							//ImageIO.createImageOutputStream(bi);
							articleManager.updatePicture(articleId, gifName+filepix);
						}
						
						fileName = i+fileName;
						i++;
						String path = FileManager.getArticlePicturePath(articleId, fileName);
						
						//System.out.println("filepix:"+filepix);
							//long size = file.getSize();
							FileManager.upload(path, file);
							//System.out.println("picture");
							// file.transferTo(localFile);
							articleManager.updatePicture(articleId, fileName+filepix);
							
						} catch (Exception e) {
							//System.out.println("dddddde");
							e.printStackTrace();
							out.print("<script language='javascript'>parent.callback('2','"+userId+"');</script>");
							//map.put("result", 0);
							out.flush();
							out.close();
							//return map;
							
						}
					} 
				}
			}
			
			out.print("<script language='javascript'>parent.callback('1','"+userId+"');</script>");
			out.flush();
			out.close();
			
		} catch (IOException e) {
			//System.out.println("ccccccc");
			e.printStackTrace();
			
		}
		out.print("<script language='javascript'>parent.callback('0','0');</script>");
		//map.put("result", 0);
		out.flush();
		out.close();
		//map.put("result", 1);
		
	}

	@RequestMapping("uploadArticlePicture.do")
	@ResponseBody
	public Map<String, Object> uploadArticlePicture(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("==> /article/uploadArticlePicture.do");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		Map<String, Object> map = new HashMap<String, Object>();
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			Iterator<String> iter = multiRequest.getFileNames();
			// String name = (String)multiRequest.getAttribute("name");
			// name = multiRequest.getParameter("name");
			// System.out.println(name);
			int articleId = Integer.parseInt(multiRequest.getParameter("articleId"));
			// System.out.println("Upload ArticleId:  "+articleId);

			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					System.out.println("File Name:" + file.getOriginalFilename());
					String path = FileManager.getArticlePicturePath(articleId, fileName);

					File localFile = new File(path);
					if (localFile.exists()) {
						localFile.delete();
					}
					File dir = new File(localFile.getAbsolutePath());
					if (!dir.exists()) {
						dir.mkdirs();
					}

					try {
						FileManager.upload(path, file);
						// file.transferTo(localFile);
						articleManager.updatePicture(articleId, fileName);
					} catch (IllegalStateException e) {
						e.printStackTrace();
						map.put("result", 0);
						return map;
					}
				} else {
					map.put("result", 0);
					return map;
				}

			}
		}
		map.put("result", 1);
		return map;
	}

	@RequestMapping("outsideShare.do")
	public String outsideShare(int articleId, HttpServletRequest request) {
		System.out.println("==> /article/outsideShare?articleId=" + articleId);
		if (articleId == 0)
			return "";
		ArticleInfo article = articleManager.getArticle(articleId);
		UserInfo user = userManager.getUser(article.getUser().getUserId());
		
		List<String> picList = articleManager.getPicListById(article.getArticleId());
//		List<String> pics = new ArrayList<String>();
//		for(String pic:picList){
//			String temp = FileManager.getArticlePictureUrl(article.getArticleId(), pic);
//			pics.add(temp);
//		}
		//map.put("pictures",pics);
		request.setAttribute("article", article);
		request.setAttribute("user", user);
		request.setAttribute("pictures", picList);
		//System.out.println(pics.size());
		return "outShare";
	}

	@RequestMapping("createChartlet.do")
	@ResponseBody
	public Map<String, Object> createChartlet(@RequestBody ChartletInfo chartlet){
		System.out.println("==> /article/createChartlet?type=" + chartlet.getType()+"&title="+chartlet.getTitle());
		chartlet.setStatus(ChartletStatus.Normal.getIndex());
		int id = articleManager.addChartlet(chartlet);
		Map<String, Object> map = new HashMap<String, Object>();
		if(id>0){
			map.put("result", 1);
			map.put("chartletId", id);
			map.put("title", chartlet.getTitle());
		}
		else {
			map.put("result", 0);
		}
		
		return map;
	}
	
	@RequestMapping("addChartletPicture.do")
	@ResponseBody
	public void addChartletPicture(HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("==> /article/addChartletPicture.do");
		PrintWriter out = response.getWriter();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//Map<String, Object> map = new HashMap<String, Object>();
		int chartletId = Integer.parseInt(request.getParameter("chartletId"));
		String chartletUrl = "";
		int picId = 0;
		//System.out.println(chartletId);
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			
			Iterator<String> iter = multiRequest.getFileNames();
			if(!iter.hasNext()){
				out.print("<script language='javascript'>parent.callbackChartlet('0');</script>");
				//map.put("result", 0);
				out.flush();
				out.close();
			}
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null&&!"".equals(file.getName())) {
					String fileName = file.getOriginalFilename();
					if(fileName.equals(""))
						continue;
					//System.out.println("File Name:" + file.getName());
					//FileInfo fileInfo = new FileInfo();
					//fileInfo.setName(fileName);
					//fileInfo.setStatus(ChartletStatus.Normal.getIndex());
					Random random = new Random();
					int next = random.nextInt(1000);
					//System.out.println(" "+next);
					fileName = next+fileName;
					String path = FileManager.getChartletPicturePath(chartletId, fileName);
					RelChartletPicture rel = new RelChartletPicture();
					rel.setChartlet(new ChartletInfo(chartletId));
					rel.setPicture(fileName);
					rel.setStatus(ChartletStatus.Normal.getIndex());
					///System.out.println("Path: "+path);
					//int fileId = articleManager.addFile(fileInfo);
					try {
						FileManager.upload(path, file);
						// file.transferTo(localFile);
						
						picId = articleManager.addChartletPicture(rel);
						
						chartletUrl = FileManager.getChartletPictureUrl(chartletId, fileName);
					} catch (IllegalStateException e) {
						e.printStackTrace();
						out.print("<script language='javascript'>parent.callbackChartlet('0');</script>");
						//map.put("result", 0);
						out.flush();
						out.close();
						//return map;
					}
				} 
			}
		}
		else {
			out.print("<script language='javascript'>parent.callbackChartlet('0');</script>");
			out.flush();
			out.close();
		}
		
		//map.put("result", 1);
		out.print("<script language='javascript'>parent.callbackChartlet('"+chartletUrl+"','"+chartletId+"','"+picId+"');</script>");
		out.flush();
		out.close();
	}

	@RequestMapping("/getChartlets.do")
	@ResponseBody
	public Map<String, Object> getChartlets(){
		System.out.println("==> /article/getChartlets.do");
		List<ChartletInfo> chartlets = articleManager.getAllChartlet();
		for(ChartletInfo chartlet:chartlets){
			List<RelChartletPicture> pictures = articleManager.getChartletPictures(chartlet.getChartletId());
			chartlet.setPicList(pictures);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(chartlets!=null){
			map.put("result", 1);
			List<Map<String, Object>> words = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> pictures = new ArrayList<Map<String,Object>>();
			for(ChartletInfo chartlet : chartlets){
				Map<String, Object> chartMap = new HashMap<String, Object>();
				chartMap.put("title", chartlet.getTitle());
				chartMap.put("status", chartlet.getStatus());
				//map.put("urls", chartlet.getPicList());
				List<Map<String, Object>> urlMaps = new ArrayList<Map<String,Object>>();
				for(RelChartletPicture url: chartlet.getPicList()){
					Map<String, Object> urlMap = new HashMap<String, Object>();
					urlMap.put("url", url.getPicUrl(chartlet.getChartletId()));
					urlMap.put("status", url.getStatus());
					urlMaps.add(urlMap);
				}
				chartMap.put("urls", urlMaps);
				if(chartlet.getType()==ChartletType.Word.getIndex()){
					words.add(chartMap);
				}
				if(chartlet.getType()==ChartletType.Picture.getIndex()){
					pictures.add(chartMap);
				}
 			}
			map.put("words", words);
			map.put("pictures", pictures);
		}
		
		return map;
	}

	@RequestMapping("/changeChartletPictureStatus.do")
	@ResponseBody
	public Map<String, Object> changeChartletPictureStatus(int pictureId,int status){
		System.out.println("==> /article/changeChartletPictureStatus.do?pictureId="+pictureId+"&status="+status);
		Map<String, Object> map = new HashMap<String, Object>();
		if(pictureId==0){
			map.put("result", 0);
			return map;
		}
		boolean result = articleManager.changeChartletPictureStatus(pictureId,status);
		if(result){
			map.put("result", 1);
		}else {
			map.put("result", 0);
		}
		
		return map;
	}
	
	@RequestMapping("/removeChartletPicture.do")
	@ResponseBody
	public Map<String, Object> removeChartletPicture(int pictureId){
		System.out.println("==> /article/removeChartletPicture.do?pictureId="+pictureId);
		Map<String, Object> map = new HashMap<String, Object>();
		if(pictureId==0){
			map.put("result", 0);
			return map;
		}
		
		boolean result = articleManager.removeChartletPicture(pictureId);
		if(result){
			map.put("result", 1);
		}else {
			map.put("result", 0);
		}
		return map;
	}
	
	@RequestMapping("/getArticlesByDate.do")
	@ResponseBody
	public Map<String, Object> getArticlesByDate(String date,int type,int userId,int page,HttpServletRequest request){
		System.out.println("==> /article/getArticlesByDate.do?date="+date+"&userId="+userId+"&type="+type+"&page="+page);
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Map<String, Object> map = new HashMap<String, Object>();
		//Date startDate = sdf.parse(date+" 00:00:00");
		//Date endDate = sdf.parse(date+" 23:59:59");
		String startDate = date+" 00:00:00";
		String endDate = date+" 23:59:59";
		List<ArticleInfo> articleInfos = new ArrayList<ArticleInfo>();
		if(type==100){
			articleInfos = articleManager.getArticlesByDate(startDate,endDate, page, maxResults);
		}else {
			articleInfos = articleManager.getArticlesByDate(startDate, endDate,type, page, maxResults);
		}
		
		if(articleInfos==null){
			map.put("result", 0);
			return map;
		}

		if(userId>0){
			List<Map<String, Object>> list = articlesToList(userId, articleInfos);
			//System.out.println(list.size());
			map.put("result", 1);
			map.put("articles", list);
			//System.out.println("userId:"+userId);
		}
		else {
			map.put("result", 0);
		}
		return map;
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public Map<String, Object> delete(int articleId){
		return changeArticleType(articleId, ArticleType.Delete.getIndex());
	}
	
	@RequestMapping("/changeArticleType.do")
	@ResponseBody
	public Map<String, Object> changeArticleType(int articleId,int type){
		System.out.println("==> /article/changeArticleType.do?articleId="+articleId+"&type="+type);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = articleManager.changeArticleType(articleId,type);
		map.put("result", result?1:0);
		if(result){
			String typeStr = ArticleType.getName(type);
			map.put("type", typeStr);
		}
		
		return map;
	}
	
	@RequestMapping("deleteChartlet.do")
	@ResponseBody
	public Map<String, Object> deleteChartlet(int chartletId){
		System.out.println("==> /article/changeArticleType.do?chartletId="+chartletId);
		boolean result = articleManager.deleteChartlet(chartletId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result?1:0);
		return map;
	}
	
	@RequestMapping("richText.do")
	public void richText(){
		
	}
	
	@RequestMapping("getRecommendList2.0.do")
	@ResponseBody
	public Map<String,Object> getRecommendList2(int userId,int page){
		Map<String,Object> map = new HashMap<String,Object>();
		map = articleManager.getRecommendList(userId,page,maxResults2);
		return map;
	}
	
	@RequestMapping("getShowArticleList2.0.do")
	@ResponseBody
	public Map<String,Object> getShowArticleList2(int showId,int userId,int page){
		Map<String,Object> map = articleManager.getShowArticleList(showId,userId,page,maxResults2);
		return map;
	}
	
	@RequestMapping("getArticleInfo2.0.do")
	@ResponseBody
	public Map<String,Object> getArticleInfo2(int articleId,int userId){
		Map<String,Object> map = articleManager.getArticleInfo(articleId,userId);
		
		return map;
	}
	
	@RequestMapping("collectArticle2.0.do")
	@ResponseBody
	public Map<String,Object> collectArticle2(int userId,int articleId){
		boolean result = articleManager.collectArticle(userId,articleId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", result?1:0);
		return map;
	}
	
	@RequestMapping("browseArticle2.0.do")
	@ResponseBody
	public Map<String,Object> browseArticle2(int articleId){
		boolean result = articleManager.browseArticle(articleId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", result?1:0);
		return map;
	}
}
