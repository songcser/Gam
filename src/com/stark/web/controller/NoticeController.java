package com.stark.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.stark.web.define.EnumBase;
import com.stark.web.define.EnumBase.NoticeStatus;
import com.stark.web.define.EnumBase.NoticeType;
import com.stark.web.define.EnumBase.UserRole;
import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.NoticeInfo;
import com.stark.web.entity.UserInfo;
import com.stark.web.service.IActivityManager;
import com.stark.web.service.IArticleManager;
import com.stark.web.service.INoticeManager;
import com.stark.web.service.IUserManager;
import com.stark.web.service.WebManager;

@Controller
@RequestMapping("notice")
public class NoticeController {

	@Resource(name = "noticeManager")
	private INoticeManager noticeManager;

	@Resource(name = "userManager")
	private IUserManager userManager;
	
	@Resource(name = "articleManager")
	private IArticleManager articleManager;
	
	@Resource(name = "activityManager")
	private IActivityManager activityManager;
	
	@Resource(name="threadPool")
	private ThreadPoolTaskExecutor threadPool;
	
	@Resource
	private WebManager webManager;

	@RequestMapping("/add")
	public void add(NoticeInfo nInfo) {
		noticeManager.addNotice(nInfo);
	}

	@RequestMapping("/getLastNotice.do")
	@ResponseBody
	public List<Map<String, Object>> getLastNotice(int userId, int type) {
		System.out.println("/notice/getLastNotice?userId=" + userId + "&type=" + type);
		List<NoticeInfo> nList = noticeManager.getLastNotice(userId, type);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		for (Iterator<NoticeInfo> iterator = nList.iterator(); iterator.hasNext();) {
			Map<String, Object> map = new HashMap<String, Object>();
			NoticeInfo notice = iterator.next();
			map.put("noticeId", notice.getNoticeId());
			map.put("type", notice.getType());
			String contentString = notice.getContent();
			if(contentString==null)
				map.put("content", "");
			else {
				map.put("content", contentString);
			}
			map.put("userId", notice.getUser().getUserId());
			map.put("userName", notice.getUser().getName());
			map.put("headPic", notice.getUser().getHeadUrl());
			// map.put("articleId", notice.getArticle().getArticleId());
			map.put("status", notice.getStatus());
			map.put("date", format.format(notice.getDate()));
			map.put("senderId", notice.getSender().getUserId());
			map.put("senderName", notice.getSender().getName());
			map.put("senderHeadPic", notice.getSender().getHeadUrl());

			if (notice.getType() == EnumBase.NoticeType.Comment.getIndex() || notice.getType() == EnumBase.NoticeType.Praise.getIndex()) {
				int articleId = notice.getArticle().getArticleId();
				List<String> pics = articleManager.getPicListById(articleId);
				map.put("articleId", articleId);
				//System.out.println(article.getPicList().size());
				if(pics!=null&&!pics.isEmpty()){
					map.put("thumbImg",pics.get(0));
				}
				
			}
			list.add(map);
		}
		return list;
	}

	@RequestMapping("/update.do")
	public void update(NoticeInfo nInfo) {
		System.out.println("/notice/update");
		noticeManager.updateNotice(nInfo);
	}

	@RequestMapping("/updateStatus.do")
	public String updateStatus(int noticeId, int status) {
		System.out.println("/notice/updateStatus?noticeId=" + noticeId + "&status=" + status);
		boolean result = noticeManager.updateStatus(noticeId, status);
		return "{\"result\":\"" + (result ? 1 : 0) + "\"}";
	}

	@RequestMapping("/delete.do")
	public String delete(int id) {
		System.out.println("/notice/delete?id=" + id);
		boolean result = noticeManager.deleteNotice(id);

		String ret = "{\"result\":\"" + (result ? 1 : 0) + "\"}";

		return ret;
	}

	@RequestMapping("/getById.do")
	public void getById(String id) {
		// NoticeInfo nInfo = noticeManager.getNotice(id);
	}

	@RequestMapping("/getListByUserId.do")
	@ResponseBody
	public Map<String, Object> getListByUserId(int userId) {
		System.out.println("/notice/getListByUserId?uid=" + userId);
		Map<String, Object> mainMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<NoticeInfo> nList = noticeManager.getNoticeByUserId(userId);
		if(nList==null){
			mainMap.put("result", 0);
			return mainMap;
		}
		noticeManager.setUserNoticeStatus(userId,0);
		//int status = noticeManager.getUserNoticeStatus(userId);
		//mainMap.put("status", status);
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		for (Iterator<NoticeInfo> iterator = nList.iterator(); iterator.hasNext();) {
			Map<String, Object> map = new HashMap<String, Object>();
			NoticeInfo notice = iterator.next();
			UserInfo sender = userManager.getUser(notice.getSender().getUserId());
			map.put("noticeId", notice.getNoticeId());
			map.put("type", notice.getType());
			String contentString = notice.getContent();
			if(contentString==null)
				map.put("content", "");
			else {
				map.put("content", contentString);
			}
			if (notice.getType() == EnumBase.NoticeType.Comment.getIndex() || notice.getType() == EnumBase.NoticeType.Praise.getIndex()) {
				int articleId = notice.getArticle().getArticleId();
				List<String> pics = articleManager.getPicListById(articleId);
				//System.out.println(pics.size());
				map.put("articleId", articleId);
				//System.out.println(article.getPicList().size());
				if(pics!=null&&!pics.isEmpty()){
					map.put("thumbImg", pics.get(0));
				}
				
			}
			
			map.put("status", notice.getStatus());
			map.put("date", format.format(notice.getDate()));
			if (notice.getType() != EnumBase.NoticeType.System.getIndex()) {
				
				map.put("senderId",sender.getUserId());
				map.put("senderName", sender.getName());
				map.put("senderHeadPic", sender.getHeadUrl());
			}
			noticeManager.updateStatus(notice.getNoticeId(),NoticeStatus.Readed.getIndex());
			list.add(map);
		}
		mainMap.put("result", 1);
		mainMap.put("notices", list);
		return mainMap;
	}

	@RequestMapping("systemNotice.do")
	public void systemNotice(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("/notice/systemNotice.do");
		final String content = request.getParameter("systemArticle");
		final int userId = Integer.parseInt(request.getParameter("userId"));
		final String showFlag = request.getParameter("showFlag");
		final String activityId = request.getParameter("activityId");
		final UserInfo sender = new UserInfo(userId);
		final List<UserInfo> userList = userManager.getAllUser();
		threadPool.execute(new Runnable() {
			public void run() {
				for (Iterator<UserInfo> iterator = userList.iterator(); iterator.hasNext();) {
					UserInfo userInfo = iterator.next();
					int role = userInfo.getRole() ;
					if (role!= EnumBase.UserRole.Normal.getIndex()&&role!=EnumBase.UserRole.Mark.getIndex()) {
						continue;
					}
					NoticeInfo notice = new NoticeInfo();
					notice.setUser(userInfo);
					notice.setSender(sender);
					//notice.setArticle(new ArticleInfo());
					notice.setContent(content);
					notice.setDate(new Date());
					notice.setType(EnumBase.NoticeType.System.getIndex());
					notice.setStatus(EnumBase.NoticeStatus.NoRead.getIndex());

					noticeManager.addNotice(notice);
				}
			}
		});
		//System.out.println(userList.size());
		int showId = 0;
		if(showFlag.equals("1")&&activityId!=null){
			showId = Integer.parseInt(activityId);
			ActivityInfo activity = activityManager.getActivity(showId);
			webManager.pushToAllExtShow(content,showId,activity.getType());
		}
		else {
			webManager.pushToUser( userId, NoticeType.System.getIndex(), content);
		}
		
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>parent.callbackSystem('发布完成');</script>");
		out.flush();
		out.close();
	}

	@RequestMapping("systemNotice1")
	public void systemNotice1(HttpServletRequest request, HttpServletResponse response) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			Iterator<String> iter = multiRequest.getFileNames();

			NoticeInfo notice = new NoticeInfo();
			String content = multiRequest.getParameter("systemArticle");
			notice.setContent(content);
			notice.setDate(new Date());
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = "demoUpload" + file.getOriginalFilename();
					System.out.println("File Name:" + file.getOriginalFilename());
					String path = "/home/stark/Temp/" + fileName;

					File localFile = new File(path);

					try {
						file.transferTo(localFile);
					} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
					}
				}

			}

		}
	}
	
	@RequestMapping("test.do")
	public void test(int page){
	}
	
	@RequestMapping("publish.do")
	@ResponseBody
	public Map<String,Object> publish(@RequestBody final NoticeInfo notice){
		UserInfo user = userManager.getUser(notice.getUser().getUserId());
		notice.setUser(user);
		notice.setDate(new Date());
		notice.setStatus(EnumBase.NoticeStatus.NoRead.getIndex());
		//final int id =0 ;
		noticeManager.addNotice(notice);
		webManager.pushToUser( notice.getUser().getUserId(),notice.getType(), notice.getContent());
		
		Map<String,Object> map = new HashMap<String,Object>();
		//WebManager.JPush();
		//WebManager.pushToUser(notice.getUser().getUserId(), notice.getContent());
		map.put("result", 1);
		SimpleDateFormat sdf = WebManager.getDateFormat();
		map.put("date", sdf.format(new Date()));
		return map;
	}
	
	@RequestMapping("publishWithShow.do")
	@ResponseBody
	public Map<String,Object> publishWithShow(int showId,@RequestBody NoticeInfo notice){
		notice.setDate(new Date());
		return null;
	}
	
	@RequestMapping("/getListByUserId2.do")
	@ResponseBody
	public Map<String, Object> getListByUserId2(int userId) {
		Map<String, Object> mainMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<NoticeInfo> nList = noticeManager.getNoticeByUserId(userId);
		if(nList==null){
			mainMap.put("result", 0);
			return mainMap;
		}
		
		//int status = noticeManager.getUserNoticeStatus(userId);
		//mainMap.put("status", status);
		noticeToList(nList,userId);
		
		mainMap.put("result", 1);
		mainMap.put("notices", list);
		return mainMap;
	}
	
	@RequestMapping("getCommentNoticeList2.0.do")
	@ResponseBody
	public Map<String,Object> getCommentNoticeList2(int userId){
		Map<String, Object> mainMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<NoticeInfo> nList = noticeManager.getCommentNotice(userId);
		if(nList==null){
			mainMap.put("result", 0);
			return mainMap;
		}
		
		noticeToList(nList,userId);
		
		mainMap.put("result", 1);
		mainMap.put("notices", list);
		return mainMap;
	}
	
	@RequestMapping("getPraiseNoticeList2.0.do")
	@ResponseBody
	public Map<String,Object> getPraiseNoticeList2(int userId){
		Map<String, Object> mainMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<NoticeInfo> nList = noticeManager.getPraiseNotice(userId);
		if(nList==null){
			mainMap.put("result", 0);
			return mainMap;
		}
		
		noticeToList(nList,userId);
		
		mainMap.put("result", 1);
		mainMap.put("notices", list);
		return mainMap;
	}
	
	@RequestMapping("getNoticeList2.0.do")
	@ResponseBody
	public Map<String,Object> getNoticeList2(int userId){
		Map<String, Object> mainMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<NoticeInfo> nList = noticeManager.getNoticeList(userId);
		if(nList==null){
			mainMap.put("result", 0);
			return mainMap;
		}
		
		noticeToList(nList,userId);
		
		mainMap.put("result", 1);
		mainMap.put("notices", list);
		return mainMap;
	}
	
	

	private List<Map<String,Object>> noticeToList(List<NoticeInfo> nList,int userId){
		UserInfo user = userManager.getUser(userId);
		int role = user.getRole();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		for (Iterator<NoticeInfo> iterator = nList.iterator(); iterator.hasNext();) {
			Map<String, Object> map = new HashMap<String, Object>();
			NoticeInfo notice = iterator.next();
			UserInfo sender = userManager.getUser(notice.getSender().getUserId());
			map.put("noticeId", notice.getNoticeId());
			map.put("type", notice.getType());
			String contentString = notice.getContent();
			if(contentString==null)
				map.put("content", "");
			else {
				map.put("content", contentString);
			}
			if (notice.getType() == EnumBase.NoticeType.Comment.getIndex() || notice.getType() == EnumBase.NoticeType.Praise.getIndex()) {
				int articleId = notice.getArticle().getArticleId();
				List<String> pics = articleManager.getPicListById(articleId);
				//System.out.println(pics.size());
				map.put("articleId", articleId);
				//System.out.println(article.getPicList().size());
				if(pics!=null&&!pics.isEmpty()){
					map.put("thumbImg", pics.get(0));
				}
				
			}
			
			map.put("status", notice.getStatus());
			map.put("date", format.format(notice.getDate()));
			if (notice.getType() != EnumBase.NoticeType.System.getIndex()) {
				
				map.put("senderId",sender.getUserId());
				map.put("senderName", sender.getName());
				map.put("senderHeadPic", sender.getHeadUrl());
			}
			if(role!=UserRole.Normal.getIndex()&&role!=UserRole.Mark.getIndex()){
				noticeManager.updateStatus(notice.getNoticeId(),NoticeStatus.Readed.getIndex());
			}
			list.add(map);
			
		}
		if(role!=UserRole.Normal.getIndex()&&role!=UserRole.Mark.getIndex()){
			//noticeManager.updateStatus(notice.getNoticeId(),NoticeStatus.Readed.getIndex());
			noticeManager.setUserNoticeStatus(userId,0);
		}
		return list;
	}
}
