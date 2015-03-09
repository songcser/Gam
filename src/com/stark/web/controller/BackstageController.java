package com.stark.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.BackupInfo;
import com.stark.web.entity.ChartletInfo;
import com.stark.web.entity.EnumBase;
import com.stark.web.entity.RelChartletPicture;
import com.stark.web.entity.EnumBase.ActivityType;
import com.stark.web.entity.EnumBase.ArticleType;
import com.stark.web.entity.UserInfo;
import com.stark.web.entity.EnumBase.UserRole;
import com.stark.web.hunter.FileManager;
import com.stark.web.service.IActivityManager;
import com.stark.web.service.IArticleManager;
import com.stark.web.service.IUserManager;

@Controller
@RequestMapping("/backstage")
public class BackstageController {
	
	@Resource(name = "userManager")
	private IUserManager userManager;
	
	@Resource(name = "activityManager")
	private IActivityManager activityManager;
	
	@Resource(name = "articleManager")
	private IArticleManager articleManager;
	
	@Resource(name = "backupInfo")
	private BackupInfo backupInfo;
	
	@RequestMapping("/login.do")
	public String login( HttpServletRequest request){
		request.setAttribute("error", false);
		return "/adminLogin";
	}
	
	@RequestMapping("/adminLogin.do")
	public String adminLogin(String name, String password,int adminRole, HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		System.out.println("==> /backstage/adminLogin?name=" + name + "&password=" + password+"&adminRole="+adminRole);
		//int adminId = userManager.isAdminByDoorplate(name, password);
		UserInfo user = new UserInfo();
		if(adminRole==UserRole.Admin.getIndex()){
			user = userManager.isAdmin(name, password);
		}
		else if(adminRole == UserRole.Operatior.getIndex()){
			//System.out.println("adminRole="+adminRole);
			user = userManager.isOperatior(name,password);
			//System.out.println(user);
		}
		else {
			request.setAttribute("error", true);
			return "/adminLogin";
		}
		if (user != null) {
			int operCount = userManager.getOperatiorCount();
			int simCount = userManager.getSimulationCount();
			int norCount = userManager.getNormalCount();
			int publishCount = articleManager.getPublishCount();
			int exquisiteCount = articleManager.getExquisiteCount();
			int magazineCount = articleManager.getMagazineCount();
			int activityCount = articleManager.getActivityArticleCount();
			int noAuditingCount = articleManager.getNoAuditingCount();
			int deleteCount = articleManager.getdeleteCount();
			int reportCount = articleManager.getReportCount();
			
			Cookie userIdCookie =new Cookie("userId",user.getUserId()+"");
			userIdCookie.setMaxAge(60*60*12*10);
			Cookie userNameCookie =new Cookie("userName",URLEncoder.encode(user.getName(),"UTF-8"));
			userNameCookie.setMaxAge(60*60*12*10);
			Cookie userRoleCookie =new Cookie("userRole",user.getRole()+"");
			userRoleCookie.setMaxAge(60*60*12*10);
			
			response.addCookie(userIdCookie);
			//System.out.println("add cookie");
			response.addCookie(userNameCookie);
			response.addCookie(userRoleCookie);
			//HttpSession session = request.getSession(true);
			//session.setAttribute("adminId", user.getUserId());
			//session.setAttribute("adminName", user.getName());
			//session.setAttribute("adminRole", user.getRole());
			
			//UserInfo user = userManager.getUser(adminId);
			request.setAttribute("webIcon", FileManager.getWebIcon());
			request.setAttribute("user", user);
			request.setAttribute("operatiorCount", operCount);
			request.setAttribute("simulationCount", simCount);
			request.setAttribute("normalCount", norCount);
			request.setAttribute("publishCount", publishCount);
			request.setAttribute("exquisiteCount", exquisiteCount);
			request.setAttribute("magazineCount", magazineCount);
			request.setAttribute("activityCount", activityCount);
			request.setAttribute("noAuditingCount", noAuditingCount);
			request.setAttribute("deleteCount", deleteCount);
			request.setAttribute("reportCount", reportCount);
			
			return "/main";
		}
//		PrintWriter out = response.getWriter();
//		out.print("<script language='javascript'>parent.callback();</script>");
//		out.flush();
//		out.close();
		request.setAttribute("error", true);
		return "/adminLogin";
	}
	
	@RequestMapping("/operatorManage.do")
	public String operationManage(HttpServletRequest request) {
		System.out.println("==> /backstage/operatorManage");
		//List<UserInfo> operators = userManager.getOperatiors();
		Cookie cookies[]=request.getCookies();
		String view = request.getParameter("view");
		
		int userId = 0;
		int role=-1;
		for(Cookie cookie : cookies){
			//System.out.println("Cookie: "+cookie.getName());
			
			if(cookie.getName().equals("userId")){
				userId =Integer.parseInt(cookie.getValue());
				request.setAttribute("userId", userId);
				 
			}
			else if(cookie.getName().equals("userName")){
				try {
					request.setAttribute("userName", URLDecoder.decode(cookie.getValue(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			else if(cookie.getName().equals("userRole")){
				String userRole = cookie.getValue();
				request.setAttribute("userRole", userRole);
				role = Integer.parseInt(userRole);
				
				
			}
		}
		
		List<UserInfo> operators = null;
		if(view!=null&&view.equals("all")){
			operators = userManager.getOperatiors();
		}
		else if(view!=null&&userId>0&&view.equals("self")){
			operators = userManager.getChildUser(userId);
		}
		else{
			if(userId>0&&role == EnumBase.UserRole.Admin.getIndex()){
				operators = userManager.getOperatiors();
			}
			else if(userId>0&&role==EnumBase.UserRole.Operatior.getIndex()){
				operators = userManager.getChildUser(userId);
			}
		}
		
		if(operators!=null){
			request.setAttribute("operations", operators);
		}
		
		//if(operators!=null){
		//	request.setAttribute("operations", operators);
			
			//HttpSession session = request.getSession(true);
			//int userId = (int)session.getAttribute("adminId");
			//String userName = (String)session.getAttribute("adminName");
			//int userRole = (int)session.getAttribute("adminRole");
			
			//request.setAttribute("userName", userName);
			//request.setAttribute("userRole", userRole);
			
		//}
		request.setAttribute("webIcon", FileManager.getWebIcon());
		request.setAttribute("roles", UserRole.values());
		

		return "operation";
	}
	
	@RequestMapping("/articleManage.do")
	public String articleManage(HttpServletRequest request) {
		System.out.println("==> /backstage/publishManage");
		List<UserInfo> operators = userManager.getOperatiors();
		List<ChartletInfo> chartlets = articleManager.getAllChartlet();
		//System.out.println(chartlets.size());
		for(ChartletInfo chartlet:chartlets){
			//System.out.println(chartlet);
			List<RelChartletPicture> pictures = articleManager.getChartletPictures(chartlet.getChartletId());
			//chartlet.setPicSet(new HashSet<RelChartletPicture>(pictures));
			//System.out.println(pictures.size());
			chartlet.setPicList(pictures);
		}
		//List<ActivityInfo> activitys = activityManager.getAllStartUpActivity();
		//List<UserInfo> recommends = userManager.getRecommendUserList();
		//List<TagInfo> tags = tagManager.getHotTags();
		//HttpSession session = request.getSession(true);
		Cookie cookies[]=request.getCookies();
		for(Cookie cookie : cookies){
			//System.out.println("Cookie: "+cookie.getName());
			if(cookie.getName().equals("userId")){
				request.setAttribute("userId", Integer.parseInt(cookie.getValue()));
			}
			else if(cookie.getName().equals("userName")){
				try {
					request.setAttribute("userName", URLDecoder.decode(cookie.getValue(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			else if(cookie.getName().equals("userRole")){
				request.setAttribute("userRole", cookie.getValue());
			}
		}
		//int userId = (int)session.getAttribute("adminId");
		//String userName = (String)session.getAttribute("adminName");
		//int userRole = (int)session.getAttribute("adminRole");
		//request.setAttribute("userId", userId);
		//request.setAttribute("userName", userName);
		//request.setAttribute("userRole", userRole);
		request.setAttribute("operations", operators);
		request.setAttribute("roles", UserRole.values());
		request.setAttribute("chartlets", chartlets);
		request.setAttribute("articleTypes", ArticleType.values());
		request.setAttribute("webIcon", FileManager.getWebIcon());
		
		//request.setAttribute("recommends", recommends);
		//request.setAttribute("tags", tags);

		// request.setAttribute("adminId",
		// request.getSession(true).getAttribute("adminId"));
		return "articleManage";
	}
	
	@RequestMapping("/activityManage.do")
	public String activitManage(HttpServletRequest request){
		System.out.println("==> /backstage/activityManage");
		List<ActivityInfo> activitys = activityManager.getAllActivity();
		List<UserInfo> operators = userManager.getOperatiors();
		//List<UserInfo> recommends = userManager.getRecommendUserList();
		//List<TagInfo> tags = tagManager.getHotTags();
		//request.setAttribute("operations", operators);
		request.setAttribute("types", ActivityType.values());
		request.setAttribute("activitys", activitys);
		
		Cookie cookies[]=request.getCookies();
		for(Cookie cookie : cookies){
			//System.out.println("Cookie: "+cookie.getName());
			if(cookie.getName().equals("userId")){
				request.setAttribute("userId", Integer.parseInt(cookie.getValue()));
			}
			else if(cookie.getName().equals("userName")){
				try {
					request.setAttribute("userName", URLDecoder.decode(cookie.getValue(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			else if(cookie.getName().equals("userRole")){
				request.setAttribute("userRole", cookie.getValue());
			}
		}
		
		//HttpSession session = request.getSession(true);
		//int userId = (int)session.getAttribute("adminId");
		//String userName = (String)session.getAttribute("adminName");
		//int userRole = (int)session.getAttribute("adminRole");
		//request.setAttribute("userId", userId);
		//request.setAttribute("userName", userName);
		//request.setAttribute("userRole", userRole);
		request.setAttribute("operations", operators);
		request.setAttribute("webIcon", FileManager.getWebIcon());
		//request.setAttribute("tags", tags);

		// request.setAttribute("adminId",
		// request.getSession(true).getAttribute("adminId"));
		return "activityManage";
	}
	
	@RequestMapping("/toMain.do")
	public String toMain(HttpServletRequest request) {
		System.out.println("==> /backstage/toMain.do");
		//javax.servlet.http.HttpSession session = request.getSession(true);
		Cookie cookies[]=request.getCookies();
		for(Cookie cookie : cookies){
			//System.out.println("Cookie: "+cookie.getName());
			if(cookie.getName().equals("userId")){
				UserInfo user = userManager.getUser(Integer.parseInt(cookie.getValue()));
				request.setAttribute("user", user);
			}
			
		}
		int operCount = userManager.getOperatiorCount();
		int simCount = userManager.getSimulationCount();
		int norCount = userManager.getNormalCount();
		int publishCount = articleManager.getPublishCount();
		int exquisiteCount = articleManager.getExquisiteCount();
		int magazineCount = articleManager.getMagazineCount();
		int activityCount = articleManager.getActivityArticleCount();
		int noAuditingCount = articleManager.getNoAuditingCount();
		int deleteCount = articleManager.getdeleteCount();
		int reportCount = articleManager.getReportCount();
		
		//UserInfo user = userManager.getUser(adminId);
		request.setAttribute("webIcon", FileManager.getWebIcon());
		//request.setAttribute("user", user);
		request.setAttribute("operatiorCount", operCount);
		request.setAttribute("simulationCount", simCount);
		request.setAttribute("normalCount", norCount);
		request.setAttribute("publishCount", publishCount);
		request.setAttribute("exquisiteCount", exquisiteCount);
		request.setAttribute("magazineCount", magazineCount);
		request.setAttribute("activityCount", activityCount);
		request.setAttribute("noAuditingCount", noAuditingCount);
		request.setAttribute("deleteCount", deleteCount);
		request.setAttribute("reportCount", reportCount);
		//request.setAttribute("user", user);
		request.setAttribute("webIcon", FileManager.getWebIcon());
		return "/main";

	}

	@RequestMapping("systemManage.do")
	public String systemManage(HttpServletRequest request){
		//System.out.println(request.getContextPath());
		//request.get
		//System.out.println(backupInfo);
		request.setAttribute("backup", backupInfo);
		return "systemManage";
	}
	
	@RequestMapping("backup.do")
	public void backup(HttpServletRequest request){
		System.out.println(request.getContextPath());
	}
	
	@RequestMapping("publishManage.do")
	public String publishManage(HttpServletRequest request){
		request.setAttribute("webIcon", FileManager.getWebIcon());
		return "publishManage";
	}
}
