package com.stark.web.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.stark.web.dao.ArticleDAO;
import com.stark.web.entity.EnumBase.*;
import com.stark.web.entity.ArticleInfo;
import com.stark.web.entity.EnumBase;
import com.stark.web.entity.NoticeInfo;
import com.stark.web.entity.PetInfo;
import com.stark.web.entity.RelUserFollow;
import com.stark.web.entity.RelUserFriend;
import com.stark.web.entity.UserGroup;
import com.stark.web.entity.UserInfo;
import com.stark.web.hunter.FileManager;
import com.stark.web.service.IActivityManager;
import com.stark.web.service.IArticleManager;
import com.stark.web.service.INoticeManager;
import com.stark.web.service.IPetManager;
import com.stark.web.service.ITagManager;
import com.stark.web.service.IUserManager;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource(name = "userManager")
	private IUserManager userManager;

	@Resource(name = "petManager")
	private IPetManager petManager;

	@Resource(name = "tagManager")
	private ITagManager tagManager;

	@Resource(name = "noticeManager")
	private INoticeManager noticeManager;

	@Resource(name = "articleManager")
	private IArticleManager articleManager;
	
	@Resource(name = "activityManager")
	private IActivityManager activityManager;

	// @Resource(name="redisManager")
	// private IRedisManager redisManager;

	private static int maxRequestFollowCount = 20;
	private static int maxPictureResult = 15;
	private static int maxMeetingCount = 8;

	// private static int maxRequestFriendCount = 20;

	@PostConstruct
	public void init() {

	}

	@RequestMapping("/test.do")
	public String test() {
		// IRedisManager redisManager = new RedisManager();
		// redisManager.save();
		return "result";
	}

	@RequestMapping("/getVerificationCode.do")
	@ResponseBody
	public Map<String, Object> getVerificationCode(String phoneNumber, String verificationCode) {

		System.out.println("/user/getVerificationCode.do?phoneNumber=" + phoneNumber+"&verificationCode="+verificationCode);
		//System.out.println("verifacationCode:" + verificationCode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 1);
		return map;
	}

	@RequestMapping("/register.do")
	@ResponseBody
	public Map<String,Object> register(@RequestBody UserInfo user){
		Map<String,Object> map = new HashMap<String, Object>();
		//UserInfo user = new UserInfo();
		//String name = request.getParameter("name");
		//System.out.println(name);
		//user.setName(name);
		//int sex = Integer.parseInt(request.getParameter("sex"));
		//user.setSex(sex);
		user.setStatus(UserStatus.OffLine.getIndex());
		
		user.setLastLogonDate(new Date());
		
		//user.setHomeTown(request.getParameter("homeTown"));
		//user.setHeadPic(request.getParameter("headPic"));
		user.setPassword("");
		user.setEmail("");
		//user.setPhoneNumber("");
		//user.setSignature("");
		UserInfo isUser = null;
		user.setRole(UserRole.Normal.getIndex());
		
		if(user.getQqOpenId()!=null){
			isUser = userManager.isExistQQOpenId(user.getQqOpenId());
			user.setWeChatOpenId("");
			user.setSinaOpenId("");
		}
		else if(user.getSinaOpenId()!=null){
			isUser = userManager.isExistSinaOpenId(user.getSinaOpenId());
			user.setQqOpenId("");
			user.setWeChatOpenId("");
		}
		else if(user.getWeChatOpenId()!=null){
			isUser = userManager.isExistWeChatOpenId(user.getWeChatOpenId());
			user.setQqOpenId("");
			user.setSinaOpenId("");
		}
		//boolean result1 = userManager.getUserHeadPic(isUser.getUserId(),user.getHeadPic());
		if(isUser==null){
			int userId = userManager.addUser(user);
			if(userId<=0){
				map.put("result", 0);
			}
			//System.out.println(user.getHeadPic());
			String result = null;
			if(!user.getHeadPic().equals("")){
				result = userManager.getUserHeadPic(userId,user.getHeadPic());
			}
			
			if(result==null){
				user.setHeadPic("");
				map.put("result", 2);
				map.put("userId", userId);
				map.put("name", user.getName());
				map.put("homeTown",user.getHomeTown());
				map.put("sex", user.getSex());
				
				return map;
			}
			else {
				map.put("result", 3);
				map.put("userId", userId);
				map.put("name", user.getName());
				map.put("headPic", FileManager.getUserPictureUrl(userId, result));
				map.put("homeTown", user.getHomeTown());
				map.put("sex", user.getSex());
			}
		}
		else {
			
			map = userManager.userToMap(isUser);
			map.put("result", 1);
//			map.put("userId", isUser.getUserId());
//			map.put("name", isUser.getName());
//			map.put("sex", isUser.getSex());
//			String homeTown = isUser.getHomeTown();
//			if(homeTown==null){
//				map.put("homeTown", "");
//			}
//			else {
//				map.put("homeTown", isUser.getHomeTown());
//			}
//			map.put("headPic", isUser.getHeadUrl());
//			map.put("", value)
		}
		
		return map;
	}
	
	@RequestMapping("/register2.do")
	@ResponseBody
	public Map<String, Object> register2(@RequestBody UserInfo user) {
		System.out.println("==> /user/register?" + user);
		Set<PetInfo> pList = (Set<PetInfo>) user.getPets();
		Map<String, Object> mapList = new HashMap<String, Object>();
		// user.setDoorplate(++maxDoorplate);
		user.setRole(UserRole.Normal.getIndex());
		int userId = userManager.addUser(user);
		if (userId == 0) {
			mapList.put("result", 0);
			return mapList;
		}

		UserInfo nuser = userManager.getUser(userId);

		mapList = userManager.userToMap(nuser);

		mapList.put("result", 1);

		for (Iterator<PetInfo> it = pList.iterator(); it.hasNext();) {
			PetInfo pet = (PetInfo) it.next();
			pet.setUser(user);
			String petType = pet.getPetType();
			int type = CatPetType.getIndex(petType);
			if (type == -1) {
				type = DogPetType.getIndex(petType);
				if (type == -1) {
					type = OtherPetType.getIndex(petType);

				}
			}
			pet.setType(type);
			String petId = petManager.addPet(pet);
			mapList.put(pet.getName(), petId);
		}

		List<UserInfo> mustUsers = userManager.getMustFollowUserList();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (mustUsers != null) {
			for (Iterator<UserInfo> iterator = mustUsers.iterator(); iterator.hasNext();) {
				UserInfo userInfo = iterator.next();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userInfo.getUserId());
				map.put("name", userInfo.getName());
				map.put("doorplate", userInfo.getDoorplate());
				map.put("headPic", userInfo.getHeadUrl());
				Set<PetInfo> pets = userInfo.getPets();
				List<String> petList = new ArrayList<String>();
				for (Iterator<PetInfo> iter = pets.iterator(); iter.hasNext();) {
					PetInfo petInfo = iter.next();
					petList.add(petInfo.getName());
				}
				map.put("pets", petList);
				list.add(map);
			}
			mapList.put("mustFollow", list);
		}

		list = new ArrayList<Map<String, Object>>();
		List<UserInfo> recommendUsers = userManager.getRecommendUserList();
		if (recommendUsers != null) {
			for (Iterator<UserInfo> iterator = recommendUsers.iterator(); iterator.hasNext();) {
				UserInfo userInfo = iterator.next();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userInfo.getUserId());
				map.put("name", userInfo.getName());
				map.put("doorplate", userInfo.getDoorplate());
				map.put("headPic", userInfo.getHeadUrl());
				Set<PetInfo> pets = userInfo.getPets();
				List<String> petList = new ArrayList<String>();
				for (Iterator<PetInfo> iter = pets.iterator(); iter.hasNext();) {
					PetInfo petInfo = iter.next();
					petList.add(petInfo.getName());
				}
				map.put("pets", petList);
				list.add(map);
			}
			mapList.put("recommendFollow", list);
		}

		return mapList;
	}

	@RequestMapping("/addUser.do")
	@ResponseBody
	public Map<String,Object> addUser(HttpServletRequest request,  HttpServletResponse response) {
		//Set<PetInfo> pList = (Set<PetInfo>) user.getPets();
		Map<String,Object> map = new HashMap<String,Object>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			UserInfo user = new UserInfo();

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			user.setName(multiRequest.getParameter("name"));
			user.setPassword(multiRequest.getParameter("password"));
			//user.setPhoneNumber(multiRequest.getParameter("phoneNumber"));

			//int role = Integer.parseInt(multiRequest.getParameter("role"));
			user.setRole(EnumBase.UserRole.Normal.getIndex());
			int sex = Integer.parseInt(multiRequest.getParameter("sex"));
			user.setSex(sex);
			user.setStatus(UserStatus.OffLine.getIndex());
			//String birthday = multiRequest.getParameter("birthday");
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//user.setBirthday(sdf.parse(birthday));
			user.setHomeTown(multiRequest.getParameter("homeTown"));
			user.setEmail(multiRequest.getParameter("email"));
			user.setLastLogonDate(new Date());
			user.setHeadPic("");
			user.setQqOpenId("");
			user.setSinaOpenId("");
			user.setWeChatOpenId("");
			//user.setSignature(multiRequest.getParameter("signature"));
			//user.setFollowingCount(0);
			//user.setFriendCount(0);
			//user.setFansCount(0);
			//user.setLastLogonDate(new Date());
			Iterator<String> iter = multiRequest.getFileNames();
			int userId = userManager.addUser(user);
			if(userId<=0){
				map.put("result", 0);
				return map;
			}
			user.setUserId(userId);
			
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {

					String fileName = file.getOriginalFilename();
					if (fileName.equals(""))
						continue;
					// System.out.println("File Name:"+file.getOriginalFilename());

					// System.out.println(path);
					try {
						String path = "";
						
						path = FileManager.getUserPicturePath(userId, fileName);
						// System.out.println("UserPath: "+path);
						user.setHeadPic(fileName);
						userManager.addUserHeadPic(userId, fileName);
						// index++;
						// System.out.println();
						// File localFile = new File(path);
						FileManager.upload(path, file);
						// file.transferTo(localFile);

					} catch (IllegalStateException e) {
						map.put("result", 0);
						e.printStackTrace();
						return map;
					}
				}

			}
			map = userManager.userToMap(user);
		}
		
		//String result = "{\"status\":\"" + 1 + "\"}";
		map.put("result", 1);
		return map;
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Map<String, Object> Update(HttpServletRequest request, HttpServletResponse response) {
		//PrintWriter out = response.getWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// name = multiRequest.getParameter("name");
			int userId = Integer.parseInt(multiRequest.getParameter("userId"));
			UserInfo user = userManager.getUser(userId);
			//System.out.println(multiRequest.getParameter("name"));
			user.setName(multiRequest.getParameter("name"));
			int sex = Integer.parseInt(multiRequest.getParameter("sex"));
			user.setSex(sex);
			user.setStatus(UserStatus.OffLine.getIndex());
			
			user.setHomeTown(multiRequest.getParameter("homeTown"));
			user.setPassword("");
			user.setPhoneNumber("");
			user.setSignature("");
			
			Iterator<String> iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {

					String fileName = file.getOriginalFilename();
					if (fileName.equals(""))
						continue;
					// System.out.println("File Name:"+file.getOriginalFilename());
					String nameStr = file.getName();
					if (nameStr.equals(""))
						continue;
					// System.out.println(path);
					try {
						
						user.setHeadPic(fileName);
						String path = FileManager.getUserPicturePath(userId, fileName);
						String oldPath = user.getHeadPic();
						if(oldPath!=null&&!oldPath.equals("")){
							FileManager.delete(FileManager.getUserPicturePath(user.getUserId(), oldPath));
						}
						FileManager.upload(path, file);
						//userManager.addUserHeadPic(userId, fileName);
						
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}

			}

			if(userManager.updateUser(user)){
				map.put("headPic", FileManager.getUserPictureUrl(user.getUserId(), user.getHeadPic()));
				map.put("result", 1);
			}
			else{
				map.put("result", 0);
			}
		}
		else {
			map.put("result", 0);
		}
		return map;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/update2.do")
	@ResponseBody
	public Map<String, Object> Update(@RequestBody UserInfo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo oldUser = userManager.getUser(user.getUserId());
		if (oldUser == null) {
			map.put("result", 0);
		}
		oldUser.setName(user.getName());
		// oldUser.setBirthday(user.getBirthday());
		oldUser.setHomeTown(user.getHomeTown());
		oldUser.setSex(user.getSex());
		// oldUser.setType(user.getType());
		userManager.updateUser(oldUser);

		map.put("result", 1);
		// String ret = "{\"result\":\""+(result?1:0) +"\"}";
		return map;
	}

	@RequestMapping("/getUserInfo.do")
	@ResponseBody
	public Map<String, Object> getUserInfo(int userId) {
		UserInfo user = userManager.getUser(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user == null) {
			map.put("result", "0");
			return map;
		}

		map = userManager.userToMap(user);
		map.put("result", 1);
		List<ArticleInfo> alist = articleManager.getArticleByUserId(userId, 0, maxPictureResult);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(ArticleInfo article:alist){
			Map<String, Object> picMap = new HashMap<String, Object>();
			picMap.put("articleId", article.getArticleId());
			List<String> picList = articleManager.getPicListById(article.getArticleId());
			//System.out.println(picList.size());
			if(picList==null||picList.isEmpty()){
				continue;
			}
			picMap.put("url",  picList.get(0));
			list.add(picMap);
		}
		//List<Map<String, Object>> list = articleManager.getArticlePicturesByUserId(userId, 0,maxPictureResult);
		map.put("pictureUrl", list);
		return map;
	}

	@RequestMapping("/getOperationInfo.do")
	@ResponseBody
	public Map<String, Object> getOperationInfo(int userId) {
		UserInfo user = userManager.getUser(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user == null) {
			map.put("result", "0");
			return map;
		}
		// DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		map = userManager.userToMap(user);
		map.put("result", 1);
		map.put("password", user.getPassword());
		map.put("role", user.getRole());
		// map.put("birthday1", format.format(user.getBirthday()));
		return map;
	}

	@RequestMapping("/getOtherUserInfo.do")
	@ResponseBody
	public Map<String, Object> getOtherUserInfo(int userId, int otherId) {
		UserInfo user = userManager.getUser(otherId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user == null) {
			map.put("result", "0");
			return map;
		}

		map = userManager.userToMap(user);
		map.put("result", 1);
		boolean follow = userManager.isFollow(userId, otherId);
		map.put("followStatus", follow ? 1 : 0);

		//List<Map<String, Object>> list = articleManager.getArticlePicturesByUserId(userId, 0,maxPictureResult);
		List<ArticleInfo> alist = articleManager.getArticleByUserId(otherId, 0, maxPictureResult);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(ArticleInfo article:alist){
			Map<String, Object> picMap = new HashMap<String, Object>();
			picMap.put("articleId", article.getArticleId());
			List<String> picList = articleManager.getPicListById(article.getArticleId());
			//System.out.println(picList.size());
			if(picList==null||picList.isEmpty()){
				continue;
			}
			picMap.put("url",  picList.get(0));
			list.add(picMap);
		}
		//List<Map<String, Object>> list = articleManager.getArticlePicturesByUserId(userId, 0,maxPictureResult);
		map.put("pictureUrl", list);
		//map.put("pictureUrl", list);

		return map;
	}

	@RequestMapping("/getUserByDoorplate.do")
	@ResponseBody
	public Map<String, Object> getUserByDoorplate(int doorplate) {
		UserInfo user = userManager.getUserByDoorplate(doorplate);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user == null) {
			map.put("result", "0");
			return map;
		}

		map = userManager.userToMap(user);
		map.put("result", 1);
		return map;
	}
	
	@RequestMapping("/getFollowList.do")
	@ResponseBody
	public Map<String, Object> getFollowList(int userId, int page) {
		System.out.println("==> /user/getFollowList?userId=" + userId + "&page=" + page);
		Map<String, Object> fmap = new HashMap<String, Object>();
		List<UserInfo> uList = userManager.getFollowList(userId, page, maxRequestFollowCount);
		if(uList.isEmpty()||uList==null){
			fmap.put("result", 1);
			fmap.put("count", 0);
			return fmap;
		}
		fmap.put("result", 1);
		int count = userManager.getFollowCount(userId);
		fmap.put("count", count);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator<UserInfo> iterator = uList.iterator(); iterator.hasNext();) {
			UserInfo userInfo = (UserInfo) iterator.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userInfo.getUserId());
			map.put("name", userInfo.getName());
			// map.put("doorplate", userInfo.getDoorplate());
			map.put("headPic", userInfo.getHeadUrl());

			list.add(map);
		}
		
		fmap.put("follows", list);
		return fmap;
	}

	@RequestMapping("/getFansList.do")
	@ResponseBody
	public Map<String, Object> getFansList(int userId, int page) {
		System.out.println("==> /user/getFansList?userId=" + userId + "&page=" + page);
		Map<String, Object> fmap = new HashMap<String, Object>();
		List<UserInfo> uList = userManager.getFansList(userId, page, maxRequestFollowCount);
		if(uList.isEmpty()||uList==null){
			fmap.put("result", 1);
			fmap.put("count", 0);
			return fmap;
		}
		fmap.put("result", 1);
		int count = userManager.getFansCount(userId);
		fmap.put("count", count);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator<UserInfo> iterator = uList.iterator(); iterator.hasNext();) {
			UserInfo userInfo = (UserInfo) iterator.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userInfo.getUserId());
			map.put("name", userInfo.getName());
			boolean isFollow = userManager.isFollow(userInfo.getUserId(),userId);
			map.put("followStatus", isFollow ? 1 : 0);
			map.put("headPic", userInfo.getHeadUrl());

			list.add(map);
		}
		fmap.put("fans", list);
		return fmap;
	}

	@RequestMapping("/toAddUser.do")
	public String toAddUser() {
		System.out.println("==> /user/toAddUser.do");
		return "/Test";
	}

	@RequestMapping("/requestFriend.do")
	@ResponseBody
	public String requestFriend(int userid, int friendid) {
		UserInfo user = new UserInfo(userid);
		UserInfo friend = new UserInfo(friendid);
		// UserInfo user = userManager.getUser(userid);
		// UserInfo friend = userManager.getUser(friendid);

		Date requestDate = new Date();
		RelUserFriend rel = new RelUserFriend();
		rel.setUser(user);
		rel.setFriend(friend);
		rel.setRequestDate(requestDate);
		rel.setStatus(FriendStatus.Request.getIndex());

		userManager.addFriend(rel);

		String result = "{\"status\":\"" + 1 + "\"}";

		return result;
	}

	@RequestMapping("/agreeFriend.do")
	public void agreeFriend(String relId) {
		RelUserFriend rel = userManager.getRelUserFriend(relId);

		rel.setStatus(FriendStatus.Agree.getIndex());

		RelUserFollow follow1 = new RelUserFollow();
		follow1.setUser(rel.getUser());
		follow1.setFollow(rel.getFriend());
		userManager.addFollow(follow1);

		RelUserFollow follow2 = new RelUserFollow();
		follow2.setUser(rel.getFriend());
		follow2.setUser(rel.getUser());
		userManager.addFollow(follow2);
	}

	@RequestMapping("/denyFriend.do")
	public void denyFriend(String relId) {
		RelUserFriend rel = userManager.getRelUserFriend(relId);

		rel.setStatus(FriendStatus.Deny.getIndex());
	}

	@RequestMapping("/deleteFriend.do")
	public void deleteFriend(String relId) {
		RelUserFriend rel = userManager.getRelUserFriend(relId);

		userManager.deleteFriend(rel);
	}

	@RequestMapping("/follow.do")
	@ResponseBody
	public Map<String, Object> follow(int userId, int followId) {
		System.out.println("==> /user/follow?userId=" + userId + "&followId=" + followId);
		RelUserFollow rel = new RelUserFollow();
		rel.setUser(new UserInfo(userId));
		rel.setFollow(new UserInfo(followId));
		rel.setDate(new Date());

		boolean result = userManager.addFollow(rel);
		
		if (result) {
			NoticeInfo notice = new NoticeInfo();
			notice.setUser(new UserInfo(followId));
			notice.setSender(new UserInfo(userId));
			//notice.setArticle(article);
			notice.setContent("关注了你");
			notice.setDate(new Date());
			notice.setType(NoticeType.Follow.getIndex());
			notice.setStatus(NoticeStatus.NoRead.getIndex());
			
			noticeManager.addNotice(notice);
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);

		return map;
	}

	@RequestMapping("/removeFollow.do")
	@ResponseBody
	public  Map<String, Object>  removeFollow(int userId, int followId) {
		System.out.println("==> /user/removeFollow&userId=" + userId + "&followId=" + followId);
		//RelUserFollow rel = new RelUserFollow();
		//rel.setUser(new UserInfo(userId));
		//rel.setFollow(new UserInfo(followId));

		boolean result = userManager.removeFollow(userId, followId);

		//String ret = "{\"result\":\"" + (result ? 1 : 0) + "\"}";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		return map;
	}

	@RequestMapping("/login.do")
	@ResponseBody
	public Map<String, Object> login(String phoneNumber, String password) {
		UserInfo user = userManager.verificationUser(phoneNumber, password);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user != null) {
			map = userManager.userToMap(user);
			map.put("result", 1);
			/*
			 * ObjectMapper objectMapper = new ObjectMapper(); try { String json
			 * = objectMapper.writeValueAsString(user);
			 * System.out.println(json); return json; } catch (IOException e) {
			 */

		} else {
			map.put("result", "0");
		}
		return map;
	}
	
	@RequestMapping("/logout.do")
	@ResponseBody
	public Map<String,Object> logout(int userId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = userManager.getUser(userId);
		user.setLastLogonDate(new Date());
		boolean result = userManager.updateUser(user);
		userManager.logout(userId);
		map.put("result", result?1:0);
		return map;
	}
	
	@RequestMapping("/validateEmail.do")
	@ResponseBody
	public Map<String,Object> validateEmail(String email){
		boolean result = userManager.verificationEmail(email);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", result?0:1);
		
		return map;
	}
	
	@RequestMapping("/loginEmail.do")
	@ResponseBody
	public Map<String, Object> loginEmail(String email, String password) {
		UserInfo user = userManager.verificationEmail(email, password);
		Map<String, Object> map = new HashMap<String, Object>();
		if (user != null) {
			map = userManager.userToMap(user);
			map.put("result", 1);
			/*
			 * ObjectMapper objectMapper = new ObjectMapper(); try { String json
			 * = objectMapper.writeValueAsString(user);
			 * System.out.println(json); return json; } catch (IOException e) {
			 */

		} else {
			map.put("result", "0");
		}
		return map;
	}

	@RequestMapping("/updateUser.do")
	@ResponseBody
	public Map<String, Object> updateUser(UserInfo uInfo) {
		System.out.println("==> /user/updateUser?" + uInfo);
		UserInfo user = userManager.getUser(uInfo.getUserId());
		if (uInfo.getName() != null &&!uInfo.getName().equals("")) {
			user.setName(uInfo.getName());
		}
		user.setSex(uInfo.getSex());
		user.setType(uInfo.getType());
		user.setPhoneNumber(uInfo.getPhoneNumber());
		user.setBirthday(uInfo.getBirthday());
		user.setSignature(uInfo.getSignature());
		user.setHomeTown(uInfo.getHomeTown());
		boolean result = userManager.updateUser(uInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		return map;
	}

	@RequestMapping("/getRandCode.do")
	public void getRandCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("image/jpeg");

		int width = 60;
		int height = 25;
		Random random = new Random();
		Color back = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
		Color front = new Color(255 - back.getRed(), 255 - back.getGreen(), 255 - back.getBlue());

		String old = "23456789abcdefghijkmnpqrstuvwxyz";
		StringBuffer sb = new StringBuffer();
		int j = 0;

		for (int i = 0; i < 4; i++) {
			j = random.nextInt(old.length());
			sb.append(old.substring(j, j + 1));
		}
		String code = sb.toString();
		// request.getSession().setAttribute("rand", code);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

		Graphics2D g = bi.createGraphics();
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g.setColor(back);
		g.fillRect(0, 0, width, height);
		g.setColor(front);
		g.drawString(code, 8, 20);

		for (int i = 0, n = random.nextInt(20); i < n; i++) {
			g.fillRect(random.nextInt(width), random.nextInt(height), 1, 1);
		}

		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		// PrintWriter out = null;
		try {
			// out = response.getWriter();
			// ServletOutputStream so=response.getOutputStream();
			// JPEGImageEncoder
			// je=JPEGCodec.createJPEGEncoder(jpegOutputStream);
			// je.encode(bi);

			captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			ServletOutputStream responseOutputStream = response.getOutputStream();
			responseOutputStream.write(captchaChallengeAsJpeg);
			responseOutputStream.flush();
			responseOutputStream.close();
			// so.flush();
			// so.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/upload.do")
	public void upload(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("==> /user/upload");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			Iterator<String> iter = multiRequest.getFileNames();
			String name = (String) multiRequest.getAttribute("name");
			name = multiRequest.getParameter("name");
			System.out.println(name);
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

	@RequestMapping("/addOperator.do")
	public void addOperator(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
		PrintWriter out = response.getWriter();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			UserInfo user = new UserInfo();

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			// String name = (String)multiRequest.getAttribute("name");
			// name = multiRequest.getParameter("name");
			// System.out.println(name);
			
			
			user.setName(multiRequest.getParameter("name"));
			user.setPassword(multiRequest.getParameter("password"));
			user.setHeadPic("");

			int role = Integer.parseInt(multiRequest.getParameter("role"));
			user.setRole(role);
			int sex = Integer.parseInt(multiRequest.getParameter("sex"));
			user.setSex(sex);
			user.setStatus(UserStatus.OffLine.getIndex());
			user.setHomeTown(multiRequest.getParameter("homeTown"));
			user.setLastLogonDate(new Date());
			user.setEmail("");
			
			user.setQqOpenId("");
			user.setSinaOpenId("");
			user.setWeChatOpenId("");
			
			Iterator<String> iter = multiRequest.getFileNames();
			
			int userId = userManager.addUser(user);
			if(userId<=0){
				out.print("<script language='javascript'>parent.callback2('0');</script>");
				out.flush();
				out.close();
				return;
			}
			
			String uId = multiRequest.getParameter("userId");
			if(user!=null){
				userManager.addChildUser(Integer.parseInt(uId),userId);
			}
			//String petId = petManager.addPet(pet);
			// int index = 0;
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {

					String fileName = file.getOriginalFilename();
					if (fileName.equals(""))
						continue;
					try {
						String path = "";
						String name = file.getName();
						if (name.equals("headPic")) {
							path = FileManager.getUserPicturePath(userId, fileName);
							//user.setHeadPic(fileName);
							userManager.addUserHeadPic(userId, fileName);
						
						}
						FileManager.upload(path, file);
						out.print("<script language='javascript'>parent.callback2('1');</script>");
					} catch (IllegalStateException e) {
						e.printStackTrace();
						out.print("<script language='javascript'>parent.callback2('0');</script>");
					}
				}

			}

		}
		out.flush();
		out.close();
	}

	@RequestMapping("/updateOperator.do")
	public void updateOperator(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
		System.out.println("==> /user/updateOperator.do");
		PrintWriter out = response.getWriter();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {

			// UserInfo user = new UserInfo();

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			// String name = (String)multiRequest.getAttribute("name");
			// name = multiRequest.getParameter("name");
			int userId = Integer.parseInt(multiRequest.getParameter("userId"));
			UserInfo user = userManager.getUser(userId);
			// System.out.println(name);
			user.setName(multiRequest.getParameter("name"));
			String password = multiRequest.getParameter("password");
			if (password != null && !password.equals("")) {
				user.setPassword(password);
			}

			//user.setPhoneNumber(multiRequest.getParameter("phoneNumber"));
			int role = Integer.parseInt(multiRequest.getParameter("role"));
			user.setRole(role);
			int sex = Integer.parseInt(multiRequest.getParameter("sex"));
			user.setSex(sex);
			user.setStatus(UserStatus.OffLine.getIndex());
			
			user.setHomeTown(multiRequest.getParameter("homeTown"));
			
			Iterator<String> iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {

					String fileName = file.getOriginalFilename();
					if (fileName.equals(""))
						continue;
					// System.out.println("File Name:"+file.getOriginalFilename());
					String nameStr = file.getName();
					if (nameStr.equals(""))
						continue;
					// System.out.println(path);
					try {
						String path = "";
						if (nameStr.equals("headPic")) {
							path = FileManager.getUserPicturePath(userId, fileName);
							user.setHeadPic(fileName);
							userManager.addUserHeadPic(userId, fileName);
						}
						FileManager.upload(path, file);
						
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}

			}

			if(userManager.updateUser(user)){
				out.print("<script language='javascript'>parent.callbackUpdate('更新用户成功');</script>");
				
			}
			else{
				out.print("<script language='javascript'>parent.callbackUpdate('更新用户完成');</script>");
			}
			//if (flag)
			//	petManager.updatePet(pet);

		}
		out.flush();
		out.close();
		
	}

	@RequestMapping("/modifyPassword.do")
	@ResponseBody
	public Map<String, Object> modifyPassword(String phoneNumber, String password) {
		System.out.println("/user/modifyPassword?phoneNumber=" + phoneNumber + "&password=" + password);
		UserInfo user = userManager.getUserByPhoneNumber(phoneNumber);

		Map<String, Object> map = new HashMap<String, Object>();
		if (user != null) {
			user.setPassword(password);

			userManager.updateUser(user);

			map = userManager.userToMap(user);
			map.put("result", 1);
		} else {
			map.put("result", "0");
		}
		return map;
	}

	@RequestMapping("/qqOpenIdLogin.do")
	@ResponseBody
	public Map<String, Object> qqOpenIdLogin(String qqOpenId) {
		System.out.println("==> /user/qqOpenIdLogin?qqOpenId=" + qqOpenId);
		UserInfo user = userManager.getUserByQQOpenId(qqOpenId);

		Map<String, Object> map;

		if (user != null) {
			map = userManager.userToMap(user);
			map.put("result", "1");
		} else {
			map = new HashMap<String, Object>();
			map.put("result", "0");
		}
		return map;
	}

	@RequestMapping("/sinaOpenIdLogin.do")
	@ResponseBody
	public Map<String, Object> sinaOpenIdLogin(String sinaOpenId) {
		System.out.println("==> /user/sinaOpenIdLogin?sinaOpenId=" + sinaOpenId);
		UserInfo user = userManager.getUserBySinaOpenId(sinaOpenId);

		Map<String, Object> map;

		if (user != null) {
			map = userManager.userToMap(user);
			map.put("result", "1");
		} else {
			map = new HashMap<String, Object>();
			map.put("result", "0");
		}

		return map;
	}

	@RequestMapping("/deleteRecommend.do")
	@ResponseBody
	public Map<String, Object> deleteRecommend(String userId) {
		System.out.println("==> /user/deleteRecommend?userId=" + userId);
		boolean result = userManager.deleteUser(userId);

		Map<String, Object> map = new HashMap<String, Object>();
		if (result) {
			map.put("result", "1");
		} else
			map.put("result", "0");

		return map;
	}

	@RequestMapping("/addFollows.do")
	@ResponseBody
	public Map<String, Object> addFollows(@RequestBody UserGroup userFollows) {
		System.out.println("==> /user/addFollows.do?" + userFollows);
		Map<String, Object> map = new HashMap<String, Object>();
		if (userFollows != null) {
			Set<Integer> follows = userFollows.getUsersId();
			if (follows != null && userFollows.getObjectId() != 0) {
				map.put("result", "1");
				int followId = userFollows.getObjectId();
				for (Iterator<Integer> iter = follows.iterator(); iter.hasNext();) {
					Integer userId = iter.next();
					RelUserFollow rel = new RelUserFollow();
					rel.setUser(new UserInfo(userId));
					rel.setFollow(new UserInfo(followId));
					rel.setDate(new Date());
					boolean isfollow = userManager.isFollow(userId, followId);
					if(isfollow){
						continue;
					}
					boolean result = userManager.addFollow(rel);
					if (result) {
						NoticeInfo notice = new NoticeInfo();
						notice.setUser(new UserInfo(followId));
						notice.setSender(new UserInfo(userId));
						//notice.setArticle(article);
						notice.setContent("关注了你");
						notice.setDate(new Date());
						notice.setType(NoticeType.Follow.getIndex());
						notice.setStatus(NoticeStatus.NoRead.getIndex());
						
						noticeManager.addNotice(notice);
						
					}
				}
			} else {
				map.put("result", "0");
			}
		} else {
			map.put("result", "0");
			//System.out.println("add follows error");
		}

		return map;
	}

	@RequestMapping("/addFateFriend.do")
	@ResponseBody
	public List<Map<String, Object>> addFateFriend(String userId) {
		System.out.println("==> /user/addFateFriend?userId=" + userId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (userId != null) {
			List<UserInfo> userList = userManager.getFateFriend(userId);
			if (userList != null) {

				for (Iterator<UserInfo> iter = userList.iterator(); iter.hasNext();) {
					Map<String, Object> map = new HashMap<String, Object>();
					UserInfo user = iter.next();
					map.put("userId", user.getUserId());
					map.put("name", user.getName());
					map.put("headPic", user.getHeadUrl());
					map.put("pets", user.getPetTypeList());
					list.add(map);
				}
			}

		} else {
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("result", "0");
			list.add(userMap);
		}
		return list;
	}

	@RequestMapping("/uploadUserPicture.do")
	@ResponseBody
	public Map<String, Object> uploadUserPicture(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("==> /user/uploadUserPicture");
		Map<String, Object> map = new HashMap<String, Object>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			// Map<String, Object> map = new HashMap<String, Object>();
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			Iterator<String> iter = multiRequest.getFileNames();
			// String articleId = multiRequest.getParameter("userId");

			while (iter.hasNext()) {
				// System.out.println("upload");
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					System.out.println("File Name:" + file.getOriginalFilename());
					String path = "";
					int index = fileName.indexOf("user");
					if (index == 0) {
						index = fileName.indexOf(".");
						System.out.println("index: " + index);
						System.out.println("end: " + fileName.length());
						String id = fileName.substring(4, index);
						System.out.println("Id: " + id);

						path = FileManager.getUserPicturePath(Integer.parseInt(id), fileName);
						userManager.addUserHeadPic(Integer.parseInt(id), fileName);
					}
					index = -1;
					index = fileName.indexOf("pet");
					if (index == 0) {
						index = fileName.indexOf(".");
						String id = fileName.substring(3, index);
						path = FileManager.getPetPicturePath(id, fileName);
						petManager.addPetHeadPic(id, fileName);
					}

					// File localFile = new File(path);
					try {
						FileManager.upload(path, file);
						// file.transferTo(localFile);
						// articleManager.uploadPicture(articleId,fileName);
					} catch (IllegalStateException e) {
						map.put("result", 0);

						e.printStackTrace();
						return map;
					}
				}

			}
			map.put("result", 1);
			return map;
		}
		map.put("result", 0);
		return map;

	}

	@RequestMapping("/delUser.do")
	@ResponseBody
	public Map<String, Object> delUser(String userId) {
		System.out.println("==> /user/delUser?userId=" + userId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (userId != null) {
			
			//boolean result = petManager.deletePetByUserId(userId);
			//if (!result) {
			//	System.out.println("delete pet error");
				// return "{\"result\":\""+0 +"\"}";
			//}
			// result = noticeManager.deleteByUserId(userId);
			articleManager.deleteByUserId(userId);
			//if (!result) {
			//	System.out.println("delete article error");
				// return "{\"result\":\""+0 +"\"}";
			//}

			boolean result = userManager.deleteUser(userId);
			if (!result) {
				System.out.println("delete user error");
				map.put("result", 0);
				return map;
			}
			map.put("result", 1);
			return map;
		}
		map.put("result", 0);
		return map;
	}

	@RequestMapping("/modifyAdmin.do")
	public void modifyAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("==> /user/modifyAdmin");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		PrintWriter out = response.getWriter();
		String headUrl = "";
		if (multipartResolver.isMultipart(request)) {

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// System.out.println(user);
			Iterator<String> iter = multiRequest.getFileNames();
			// String name = (String)multiRequest.getAttribute("name");
			//String name = multiRequest.getParameter("name");
			int userId = Integer.parseInt(multiRequest.getParameter("userId"));
			//userManager.updateUserName(userId, name);
			// System.out.println(name);
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					if (fileName.equals(""))
						continue;
					// System.out.println("File Name:"+file.getOriginalFilename());
					String path = FileManager.getUserPicturePath(userId, fileName);
					
					try {
					FileManager.upload(path, file);
					userManager.addUserHeadPic(userId, fileName);
					headUrl = FileManager.getUserPictureUrl(userId, fileName);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}

			}

		}
		out.print("<script language='javascript'>parent.callback('"+headUrl+"');</script>");
		//map.put("result", 0);
		out.flush();
		out.close();
	}

	@RequestMapping("uploadUserHeadPic.do")
	@ResponseBody
	public Map<String, Object> uploadUserHeadPic(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("==> /user/uploadUserHeadPic.do");
		Map<String, Object> map = new HashMap<String, Object>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			//String name = multiRequest.getParameter("name");
			int userId = Integer.parseInt(multiRequest.getParameter("userId"));
			//userManager.updateUserName(userId, name);
			// System.out.println(name);
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					if (fileName.equals(""))
						continue;
					// System.out.println("File Name:"+file.getOriginalFilename());
					String path = FileManager.getUserPicturePath(userId, fileName);

					//File localFile = new File(path);
					userManager.addUserHeadPic(userId, fileName);
					try {
						if(FileManager.upload(path, file))
							map.put("result", 1);
						else 
							map.put("result", 0);

					} catch (IllegalStateException e) {
						map.put("result", 0);
						e.printStackTrace();
					}
				}

			}

		}
		else {
			map.put("result", 0);
		}
		return map;
	}

	@RequestMapping("follows.do")
	public void follows(HttpServletRequest request, HttpServletResponse response){
		String[] ids = request.getParameterValues("userGroupBox");
		for(String userId:ids){
			System.out.println(userId);
		}
	}
	
	@RequestMapping("isChildUser.do")
	@ResponseBody
	public Map<String,Object> isChildUser(int userId,int childId){
		boolean ischild = userManager.isChildUser(userId,childId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", ischild?1:0);
		return map;
	}
	
	@RequestMapping("getMeetList2.0.do")
	@ResponseBody
	public Map<String,Object> getMeetList2(int userId,int sex){
		Map<String,Object> map = new HashMap<String,Object>();
		List<UserInfo> uList = userManager.getMeetList(userId,sex,maxMeetingCount);
		if(uList==null){
			map.put("result", 0);
			return map;
		}
		map = usersToMap(uList,3);
		map.put("result", 1);
		return map;
	}
	
	private Map<String, Object> usersToMap(List<UserInfo> uList,int picSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(UserInfo user:uList){
			Map<String,Object> uMap = userManager.userToMap(user);
			if(picSize>0){
				List<Map<String,Object>> pics = articleManager.getArticlePicturesByUserId(user.getUserId(), 0, picSize);
				uMap.put("articlePics", pics);
			}
			
			list.add(uMap);
		}
		map.put("users", list);
		return map;
	}
}
