package com.stark.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.stark.web.service.JPushTimerTask;

public class CustomListener implements ServletContextListener {
	private Timer timer = null;
	@Override
	public void contextInitialized(ServletContextEvent event) {
		timer = new Timer(true);
		Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制时  
        calendar.set(Calendar.MINUTE, 0);       // 控制分  
        calendar.set(Calendar.SECOND, 0);       // 控制秒  
  
        Date time = calendar.getTime();         // 得出执行任务的时间,此处为今天的12：00：00  
       // timer.scheduleAtFixedRate(new JPushTimerTask() , time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行  
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if(timer!=null){
			timer.cancel();
		}
	}

}
