package com.stark.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.stark.web.define.EnumBase;
import com.stark.web.define.EnumBase.ActivityType;
import com.stark.web.define.EnumBase.ArticleType;
import com.stark.web.define.EnumBase.UserRole;
import com.stark.web.define.RedisInfo;
import com.stark.web.entity.ActivityInfo;
import com.stark.web.entity.BackupInfo;
import com.stark.web.entity.ChartletInfo;
import com.stark.web.entity.RelChartletPicture;
import com.stark.web.entity.UserInfo;
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
	public String adminLogin(String name, String password, HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		//int adminId = userManager.isAdminByDoorplate(name, password);
		UserInfo user = new UserInfo();
		String role = request.getParameter("adminRole");
		if(role==null){
			return "/adminLogin";
		}
		int adminRole = Integer.parseInt(role);
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
			
			request.setAttribute("user", user);
			List<UserInfo> lastUsers = userManager.getLastUsers(20);
			request.setAttribute("lastUser", lastUsers);
			addCount(request);
			
			return "/home";
		}

		request.setAttribute("error", true);
		return "/adminLogin";
	}
	
	@RequestMapping("/operatorManage.do")
	public String operationManage(HttpServletRequest request) {
		System.out.println("==> /backstage/operatorManage");
		//List<UserInfo> operators = userManager.getOperatiors();
		String view = request.getParameter("view");
		
		UserInfo user = getLoginUser(request);
		
		if(user==null){
			return "/adminLogin";
		}
		int userId = user.getUserId();
		int role = user.getRole();
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
			request.setAttribute("userList", operators);
		}
		
		request.setAttribute("user", user);
		request.setAttribute("webIcon", FileManager.getWebIcon());
		request.setAttribute("roles", UserRole.values());
		

		return "user";
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
		if(cookies==null){
			return "/adminLogin";
		}
		boolean isLogin = false;
		for(Cookie cookie : cookies){
			//System.out.println("Cookie: "+cookie.getName());
			if(cookie.getName().equals("userId")){
				request.setAttribute("userId", Integer.parseInt(cookie.getValue()));
				isLogin = true;
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
		if(!isLogin){
			return "/adminLogin";
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
		if(cookies==null){
			return "/adminLogin";
		}
		boolean isLogin = false;
		for(Cookie cookie : cookies){
			//System.out.println("Cookie: "+cookie.getName());
			if(cookie.getName().equals("userId")){
				request.setAttribute("userId", Integer.parseInt(cookie.getValue()));
				isLogin = true;
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
		if(!isLogin){
			return "/adminLogin";
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
		Cookie cookies[]=request.getCookies();
		if(cookies==null){
			return "/adminLogin";
		}
		boolean isLogin = false;
		for(Cookie cookie : cookies){
			//System.out.println("Cookie: "+cookie.getName());
			if(cookie.getName().equals("userId")){
				UserInfo user = userManager.getUser(Integer.parseInt(cookie.getValue()));
				request.setAttribute("user", user);
				isLogin = true;
			}
			
		}
		if(!isLogin){
			return "/adminLogin";
		}
		List<UserInfo> lastUsers = userManager.getLastUsers(20);
		String openFileUrl = FileManager.getOpeFileUrl();
		request.setAttribute("openPictueUrl", openFileUrl);
		request.setAttribute("lastUser", lastUsers);
		addCount(request);
		
		return "/home";

	}
	
	private void addCount(HttpServletRequest request){
		int operCount = userManager.getOperatiorCount();
		int simCount = userManager.getSimulationCount();
		int norCount = userManager.getNormalCount();
		int orgCount = userManager.getOrganizationCount();
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
		request.setAttribute("organizationCount", orgCount);
		request.setAttribute("publishCount", publishCount);
		request.setAttribute("exquisiteCount", exquisiteCount);
		request.setAttribute("magazineCount", magazineCount);
		request.setAttribute("activityCount", activityCount);
		request.setAttribute("noAuditingCount", noAuditingCount);
		request.setAttribute("deleteCount", deleteCount);
		request.setAttribute("reportCount", reportCount);
		//request.setAttribute("user", user);
		request.setAttribute("webIcon", FileManager.getWebIcon());
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
		UserInfo user = getLoginUser(request);
		if(user==null){
			return "adminLogin";
		}
		request.setAttribute("webIcon", FileManager.getWebIcon());
		List<UserInfo> operators = userManager.getOperatiors();
		List<ActivityInfo> acList = activityManager.getAllShowList();
		request.setAttribute("showList", acList);
		request.setAttribute("operations", operators);
		return "publish";
	}
	
	@RequestMapping("searchByName.do")
	public String searchByName(String name,HttpServletRequest request){
		UserInfo user = getLoginUser(request);
		if(user==null){
			return "adminLogin";
		}
		List<UserInfo> operators = userManager.getOperatiors();
		List<UserInfo> users = userManager.getUserByName(name);
		if(users!=null){
			request.setAttribute("userList", users);
		}
		request.setAttribute("operations", operators);
		request.setAttribute("webIcon", FileManager.getWebIcon());
		request.setAttribute("roles", UserRole.values());
		return "user";
	}
	
	@RequestMapping("markUser.do")
	public String getMarkUser(HttpServletRequest request){ 
		UserInfo user = getLoginUser(request);
		if(user==null){
			return "adminLogin";
		}
		List<UserInfo> operators = userManager.getOperatiors();
		List<UserInfo> users = userManager.getMarkUsers();
		if(users!=null){
			
			request.setAttribute("userList", users);
		}
		
		request.setAttribute("operations", operators);
		request.setAttribute("webIcon", FileManager.getWebIcon());
		request.setAttribute("roles", UserRole.values());
		return "user";
	}
	
	@RequestMapping("recommend.do")
	public String recommend(HttpServletRequest request){
		UserInfo user = getLoginUser(request);
		if(user==null){
			return "/adminLogin";
		}
		List<UserInfo> operators = userManager.getOperatiors();
		
		request.setAttribute("operations", operators);
		request.setAttribute("user", user);
		return "recommend";
	}
	
	private UserInfo getLoginUser(HttpServletRequest request){
		Cookie cookies[]=request.getCookies();
		if(cookies==null){
			return null;
		}
		UserInfo user = null;
		for(Cookie cookie : cookies){
			if(cookie.getName().equals("userId")){
				int userId = Integer.parseInt(cookie.getValue());
				user = userManager.getUser(userId);
				request.setAttribute("user", user);
			}
			
		}
		
		return user;
	}
	
	@RequestMapping("showUser.do")
	public String showUser(int userId,HttpServletRequest request){
		if(getLoginUser(request)==null){
			return "/adminLogin";
		}
		UserInfo user = userManager.getUser(userId);
		List<UserInfo> list = new ArrayList<UserInfo>();
		list.add(user);
		List<UserInfo> operators = userManager.getOperatiors();
		request.setAttribute("operations", operators);
		request.setAttribute("userList", list);
		request.setAttribute("roles", UserRole.values());
		return "user";
	}
	
	@RequestMapping("showManage.do")
	public String showManage(HttpServletRequest request){
		UserInfo user = getLoginUser(request);
		if(user==null){
			return "/adminLogin";
		}
		List<ActivityInfo> acList = activityManager.getAllShowList();
		List<ActivityType> types = new ArrayList<ActivityType>();
		types.add(ActivityType.Join);
		types.add(ActivityType.NoJoin);
		List<UserInfo> operators = userManager.getOperatiors();
		request.setAttribute("showList", acList);
		request.setAttribute("types", types);
		request.setAttribute("operations", operators);
		request.setAttribute("user", user);
		
		return "show";
	}
	
	@RequestMapping("moment.do")
	public String moment(HttpServletRequest request){
		UserInfo user = getLoginUser(request);
		if(user==null){
			return "/adminLogin";
		}
		List<UserInfo> operators = userManager.getOperatiors();
		List<ActivityInfo> acList = activityManager.getAllShowList();
		request.setAttribute("operations", operators);
		request.setAttribute("showList", acList);
		request.setAttribute("user", user);
		return "moment";
	}
	
	@RequestMapping("chartlet.do")
	public String chartlet(HttpServletRequest request){
		if(getLoginUser(request)==null){
			return "/adminLogin";
		}
		List<ChartletInfo> chartlets = articleManager.getAllChartlet();
		for(ChartletInfo chartlet:chartlets){
			List<RelChartletPicture> pictures = articleManager.getChartletPictures(chartlet.getChartletId());
			chartlet.setPicList(pictures);
		}
		List<ChartletInfo> dialogues = articleManager.getDialogueList();
		ChartletInfo uds = articleManager.getUserChartlet();
		
		request.setAttribute("dialogues", dialogues);
		request.setAttribute("chartlets", chartlets);
		request.setAttribute("userDialogues", uds);
		return "chartlet";
	}
	
	@RequestMapping("notice.do")
	public String notice(HttpServletRequest request){
		if(getLoginUser(request)==null){
			return "/adminLogin";
		}
		List<ActivityInfo> acList = activityManager.getAllShowList();
		request.setAttribute("showList", acList);
		return "notice";
	}
	
	@RequestMapping("changeOpenPicture.do")
	public void changeOpenPicture(HttpServletRequest request, HttpServletResponse response) throws IOException{
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
			//userManager.updateUserName(userId, name);
			// System.out.println(name);
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					if (fileName.equals(""))
						continue;
					
					// System.out.println("File Name:"+file.getOriginalFilename());
					String path = FileManager.getOpenFilePath();
					
					FileManager.deleteOss(path);
					picUrl = FileManager.getOpeFileUrl();
					try {
					FileManager.upload(path, file);
					//userManager.addUserHeadPic(userId, fileName);
					//headUrl = FileManager.getUserPictureUrl(userId, fileName);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}

			}

		}
		out.print("<script language='javascript'>parent.callbackOpenFile('"+picUrl+"');</script>");
		//map.put("result", 0);
		out.flush();
		out.close();
	}
	@RequestMapping("getOpenPicture.do")
	@ResponseBody
	public Map<String,Object> getOpenPicture(){
		Map<String,Object> map = new HashMap<String,Object>();
		//map.put("openPictureUrl", FileManager.getOpeFileUrl());
		String key = RedisInfo.OPENPICTUREFLAG;
		String value = articleManager.getValue(key);
		if(value==null){
			value="0";
			articleManager.setValue(key,"0");
		}
		map.put("flag", value);
		return map;
	}
	
	@RequestMapping("setOpenPictureFlag.do")
	public void setOpenPictureFlag(String flag){
		String key = RedisInfo.OPENPICTUREFLAG;
		articleManager.setValue(key,flag);
	}
	
}
