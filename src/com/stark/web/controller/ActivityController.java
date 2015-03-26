package com.stark.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.stark.web.define.EnumBase.ActivityStatus;
import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.CommentInfo;
import com.stark.web.hunter.FileManager;
import com.stark.web.service.IActivityManager;
import com.stark.web.service.IArticleManager;
import com.stark.web.service.ICommentManager;

@Controller
@RequestMapping("/activity")
public class ActivityController {
	
	@Resource(name="activityManager")
	private IActivityManager activityManager;
	
	@Resource(name="commentManager")
	private ICommentManager commentManager;
	
	@Resource(name="articleManager")
	private IArticleManager articleManager;
	
	@RequestMapping("/publish.do")
	public Map<String, Object> publish(@RequestBody ActivityInfo activity){
		System.out.println("==> /activity/publish?"+activity);
		int id = activityManager.addActivity(activity);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 1);
		map.put("activityId",id);
		
		return map;
	}
	
	@RequestMapping("delete.do")
	@ResponseBody
	public Map<String, Object> delete(int activityId){
		System.out.println("==> /activity/delete?activityId="+activityId);
		//articleManager.deleteByActivityId(activityId);
		boolean result = activityManager.setActivityStatus(activityId,ActivityStatus.Delete.getIndex());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result?1:0);
		
		return map;
	}
	
	@RequestMapping("getActivity.do")
	@ResponseBody
	public Map<String, Object> getActivity(int activityId){
		System.out.println("==> /activity/getActivity?activityId="+activityId);
		ActivityInfo activity = activityManager.getActivity(activityId);
		List<CommentInfo> aList = commentManager.getCommentByArticleId(activity.getArticle().getArticleId());
		StringBuilder cms = new StringBuilder("[");
		
		int index = 0;
		for (Iterator<CommentInfo> iterator = aList.iterator(); iterator.hasNext()&&index<20;index ++) {
			CommentInfo commentInfo = iterator.next();
			cms.append(commentInfo.toJson()).append(",");
		}
		cms.append("]");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",activity.getUser().getUserId());
		map.put("userName", activity.getUser().getName());
		map.put("subject",activity.getSubject());
		//map.put("content", activity.getArticle().getContent());
		//map.put("Picture", activity.getArticle().getPicList());
		map.put("comments", cms);
		
		return map;
	}

	@RequestMapping("/addActivity.do")
	public void addActivity(HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException{
		PrintWriter out = response.getWriter();
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(multipartResolver.isMultipart(request)){
			MultipartHttpServletRequest  multiRequest = (MultipartHttpServletRequest)request;
			
			ActivityInfo activity = new ActivityInfo();
			
			String offDate = multiRequest.getParameter("offlineDate");
			String subject = multiRequest.getParameter("subject");
			int type = Integer.parseInt(multiRequest.getParameter("activityType"));
			
			String order = multiRequest.getParameter("order");
			if(order!=null){
				activity.setOrder(Integer.parseInt(order));
			}
			
			activity.setBannerPic("");
			activity.setContentPic("");
			if(offDate!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				activity.setOffDate(sdf.parse(offDate));
			}
			else {
				activity.setOffDate(new Date());
			}
			activity.setSubject(subject);
			activity.setType(type);
			Iterator<String>  iter = multiRequest.getFileNames();
			//int articleId = articleManager.addArticle(article);
			int activityId = activityManager.addActivity(activity);
			if(activityId<=0){
				out.print("<script language='javascript'>parent.callback('0');</script>");
				out.flush();
				out.close();
				return;
			}
			while(iter.hasNext()){
				MultipartFile file = multiRequest.getFile((String)iter.next());
				if(file != null){
					String fileName =  file.getOriginalFilename();
					if(fileName.equals(""))
						continue;
					String path = "";
					String name = file.getName();
					//System.out.println("Name: "+name);
					int index = name.indexOf("?");
					String filepix = "";
					if(index>0){
						filepix = name.substring(index);
						name = name.substring(0,index);
						//System.out.println(name);
					}
					
					if(name.equals("cover")){
						//activity.setBannerPic(fileName);
						fileName ="cover"+fileName;
						path = FileManager.getActivityPicturePath(activityId,fileName);
						activityManager.addBannerPic(activityId,fileName+filepix);
						//System.out.println("Path"+path);
					}
					else if(name.equals("content")){
						fileName = "content" + fileName;
						path = FileManager.getActivityPicturePath(activityId, fileName);
						activityManager.addContentPic(activityId,fileName+filepix);
						
					}
					
					try {
						FileManager.upload(path, file);
						//file.transferTo(localFile);
					} catch (IllegalStateException e) {
						//e.printStackTrace();
						out.print("<script language='javascript'>parent.callback('0');</script>");
						out.flush();
						out.close();
						return;
					}
				}
				
			}
			
		}
		out.print("<script language='javascript'>parent.callback('1');</script>");
		out.flush();
		out.close();
	}
	
	@RequestMapping("getShowList2.0.do")
	@ResponseBody
	public Map<String,Object> getShowList2(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<ActivityInfo> list = activityManager.getAllShowList();
		if(list==null){
			map.put("result", 0);
			return map;
		}
		List<Map<String,Object>> lm = new ArrayList<Map<String,Object>>();
		for(ActivityInfo act:list){
			Map<String,Object> am = new HashMap<String,Object>();
			am.put("showId", act.getActivityId());
			am.put("showTitle", act.getSubject());
			am.put("showPic", act.getBannerPicUrl());
			am.put("showType", act.getType());
		}
		
		return map;
	}
	
	@RequestMapping("uploadPicture.do")
	public void uploadPicture(HttpServletRequest request, HttpServletResponse response) throws IOException{
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		PrintWriter out = response.getWriter();
		String picUrl = "";
		if (multipartResolver.isMultipart(request)) {

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			Iterator<String> iter = multiRequest.getFileNames();
			// String name = (String)multiRequest.getAttribute("name");
			//String name = multiRequest.getParameter("name");
			//int userId = Integer.parseInt(multiRequest.getParameter("userId"));
			int activityId = Integer.parseInt(multiRequest.getParameter("activityId"));
			String activityType = multiRequest.getParameter("activityType");
			//userManager.updateUserName(userId, name);
			// System.out.println(name);
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					String name = file.getName();
					System.out.println(name);
					if (fileName.equals("")||"".equals(name))
						continue;
					// System.out.println("File Name:"+file.getOriginalFilename());
					int index = name.indexOf("?");
					String filepix = "";
					if(index>0){
						filepix = name.substring(index);
					}
					String path = FileManager.getActivityPicturePath(activityId, fileName);
					
					try {
					FileManager.upload(path, file);
					if("banner".equals(activityType)){
						activityManager.addBannerPic(activityId, fileName+filepix);
						//activityManager.uploadBannerPic(activityId,fileName);
					}
					else if("content".equals(activityType)){
						activityManager.addContentPic(activityId, fileName+filepix);
					}
					//userManager.addUserHeadPic(userId, fileName);
					picUrl = FileManager.getActivityPictureUrl(activityId, fileName+filepix);
					//headUrl = FileManager.getUserPictureUrl(userId, fileName);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}

			}

		}
		out.print("<script language='javascript'>parent.callbackUpload('"+picUrl+"');</script>");
		//map.put("result", 0);
		out.flush();
		out.close();
	}
}
