package com.stark.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.CommentInfo;
import com.stark.web.entity.EnumBase.ActivityStatus;
import com.stark.web.service.FileManager;
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
		System.out.println("==> /activity/addActivity?");
		PrintWriter out = response.getWriter();
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(multipartResolver.isMultipart(request)){
			MultipartHttpServletRequest  multiRequest = (MultipartHttpServletRequest)request;
			
			ActivityInfo activity = new ActivityInfo();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String offDate = multiRequest.getParameter("offlineDate");
			String subject = multiRequest.getParameter("subject");
			int type = Integer.parseInt(multiRequest.getParameter("activityType"));
			
			activity.setBannerPic("");
			activity.setContentPic("");
			activity.setOffDate(sdf.parse(offDate));
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
					if(fileName=="")
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
					
					if(name.equals("bnner")){
						//activity.setBannerPic(fileName);
						fileName ="bnner"+fileName;
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
	
}
