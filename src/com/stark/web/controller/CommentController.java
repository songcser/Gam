package com.stark.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stark.web.define.EnumBase.NoticeStatus;
import com.stark.web.define.EnumBase.NoticeType;
import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.CommentInfo;
import com.stark.web.entity.NoticeInfo;
import com.stark.web.entity.UserInfo;
import com.stark.web.service.CommentManager;
import com.stark.web.service.IArticleManager;
import com.stark.web.service.ICommentManager;
import com.stark.web.service.INoticeManager;
import com.stark.web.service.IUserManager;
import com.stark.web.service.WebManager;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	private static final int maxResults = 20;
	@Resource(name="commentManager")
	private ICommentManager commentManager;
	
	@Resource(name="noticeManager")
	private INoticeManager noticeManager;
	
	@Resource(name="articleManager")
	private IArticleManager articleManager;
	
	@Resource(name="userManager")
	private IUserManager userManager;
	
	@Resource
	private WebManager webManager;
	
	@RequestMapping("/publish.do")
	@ResponseBody
	public Map<String, Object> publish(@RequestBody CommentInfo comment){
		comment.setDate(new Date());
		int id = commentManager.addComment(comment);
		articleManager.addCommentCount(comment.getArticle().getArticleId());
		Set<String> atUsers = comment.getAtUsers();
		Map<String, Object> map = new HashMap<String, Object>();
		if(id!=0){
			map.put("result", 1);
			map.put("commentId", id);
			//articleManager.addCommentCount(""+comment.getArticle().getArticleId());
			int articleUserId = comment.getArticle().getUser().getUserId();
			int commentUserId = comment.getUser().getUserId();
			if(articleUserId!=commentUserId){
				NoticeInfo notice = new NoticeInfo();
				notice.setUser(new UserInfo(articleUserId));
				notice.setSender(new UserInfo(commentUserId));
				notice.setArticle(new ArticleInfo(comment.getArticle().getArticleId()));
				notice.setContent("回复了你");
				notice.setDate(new Date());
				notice.setType(NoticeType.Comment.getIndex());
				notice.setStatus(NoticeStatus.NoRead.getIndex());
				//System.out.println(userId);
				noticeManager.addNotice(notice);
				webManager.pushToUser( notice.getUser().getUserId(),notice.getType());
			}
			
			
			if(atUsers!=null){
				//System.out.println("atUser:"+atUsers.size());
				for (Iterator<String> iterator = atUsers.iterator(); iterator.hasNext();) {
					String userid = iterator.next();
					NoticeInfo notice = new NoticeInfo();
					notice.setUser(new UserInfo(Integer.parseInt(userid)));
					notice.setSender(new UserInfo(comment.getUser().getUserId()));
					notice.setArticle(new ArticleInfo(comment.getArticle().getArticleId()));
					notice.setContent("评论了你");
					notice.setDate(new Date());
					notice.setType(NoticeType.Comment.getIndex());
					notice.setStatus(NoticeStatus.NoRead.getIndex());
					//System.out.println(userId);
					noticeManager.addNotice(notice);
					webManager.pushToUser( notice.getUser().getUserId(),notice.getType());
				}
			}
		}
		else {
			map.put("result", 0);
		}
		
		
		return map;
	}
	
	@RequestMapping("/getById.do")
	public void getById(String id){
		//CommentInfo cInfo = commentManager.getComment(id);
	}
	
	@RequestMapping("/getByArticleId.do")
	@ResponseBody
	public Map<String, Object> getListByArticleId(int articleId,int page){
		System.out.println("==> /comment/getArticleComment?articleId="+articleId+"&page="+page);
		List<CommentInfo> cList = commentManager.getCommentByArticleId(articleId,page,maxResults);
		Map<String,Object> map = new HashMap<String, Object>();
		if(cList==null){
			map.put("result", 0);
		}
		map.put("result", 1);
		ArticleInfo article = articleManager.getArticle(articleId);
		map.put("count", article.getCommentCount());
		List<Map<String,Object>> list = commentsToList(cList);
		map.put("articles", list);
		
		return map;
	}

	@RequestMapping("/deleteComment.do")
	public String deleteComment(int commentId){
		System.out.println("==> /comment/deleteComment?commentId"+commentId);
		boolean result = commentManager.deleteComment(commentId);
		String ret = "{\"result\":\""+(result?1:0) +"\"}";
		return ret;
	}
	
	private List<Map<String, Object>> commentsToList(List<CommentInfo> cList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator<CommentInfo> iterator = cList.iterator(); iterator.hasNext();) {
			Map<String, Object> map = new HashMap<String, Object>();
			CommentInfo commentInfo = iterator.next();
			UserInfo user = userManager.getUser(commentInfo.getUser().getUserId());
			map.put("commentId", commentInfo.getCommentId());
			map.put("userId", user.getUserId());
			map.put("userName", user.getName());
			map.put("headPic", user.getHeadUrl());
			map.put("content", commentInfo.getContent());
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			map.put("date", format.format(commentInfo.getDate()));
			list.add(map);
			// json.append(commentInfo.toJson()).append(",");
		}
		return list;
	}
}
