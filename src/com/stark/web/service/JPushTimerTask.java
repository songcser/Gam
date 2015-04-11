package com.stark.web.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import cn.jpush.api.JPushClient;

import com.stark.web.entity.UserInfo;

public class JPushTimerTask {

	private static Logger logger = Logger.getLogger(JPushTimerTask.class);
	
	@Resource(name = "userManager")
	private IUserManager userManager;
	
	public void doTask(){
		//System.out.println("####################################");
		Set<String> idSet = userManager.getLogoutUser();
		
		//JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
//		WebManager.JPush();
//		for(String id:idSet){
//			//System.out.println(id);
//			UserInfo user = userManager.getUser(Integer.parseInt(id));
//			if(user!=null){
//				//System.out.println(user);
//				SimpleDateFormat sdf = WebManager.getDateFormat();
//				Date currentDate = new Date();
//				Calendar calendar = Calendar.getInstance();
//				calendar.setTime(currentDate);
//				int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
//				calendar.setTime(user.getLastLogonDate());
//				int logoutDay = calendar.get(Calendar.DAY_OF_YEAR);
//				int dayDiff = currentDay-logoutDay;
//				if(dayDiff>5){
//					
//				}
//			}
//			else {
//				logger.error("get user null");
//			}
//		}
	}

}
