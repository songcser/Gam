package com.stark.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class WebManager {
	private static Logger logger = Logger.getLogger(WebManager.class);
	private final static String masterSecret = "adec968251666c5982c3cce3";
	private final static String appKey = "408183a3a7fd461efc860cda";
	private final static String ALERT = "hello world";
	
	public static void JPush(){
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload = buildPushObject_android_tag_alertWithTitle();
		 
		 try {
	            PushResult result = jpushClient.sendPush(payload);
	            logger.info("Got result - " + result);

	        } catch (APIConnectionException e) {
	            // Connection error, should retry later
	        	logger.error("Connection error, should retry later", e);

	        } catch (APIRequestException e) {
	            // Should review the error, and fix the request
	        	logger.error("Should review the error, and fix the request", e);
	        	logger.info("HTTP Status: " + e.getStatus());
	        	logger.info("Error Code: " + e.getErrorCode());
	        	logger.info("Error Message: " + e.getErrorMessage());
	        }
	}
	
	public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }
	
	public static PushPayload test(){
		return PushPayload.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.alias("93"))
				.setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                 .setMessage(Message.content("hello world"))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)
                         .build())
                 .build();
	}
	
	public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                 .setMessage(Message.content(""))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)
                         .build())
                 .build();
    }
	
	public static PushPayload buildPushObject_android_tag_alertWithTitle() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias("93"))
                .setNotification(Notification.ios("hello", null))
                .build();
    }
	
	public static SimpleDateFormat getDateFormat(){
		return new SimpleDateFormat("yyyyMMddHHmmss");
	}
	public static String getOneHtml(String htmlUrl) {
		// System.out.println("Html Url:" +htmlUrl);
		URL url;
		String temp;
		final StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlUrl);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			InputStreamReader input = new InputStreamReader(connection.getInputStream(), "utf-8");
			final BufferedReader in = new BufferedReader(input);// 读取网页全部内容
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (final MalformedURLException me) {
			System.out.println("你输入的URL格式有问题！请仔细输入");
			me.getMessage();
			return "你输入的URL格式有问题！请仔细输入";
			// throw me;
		} catch (final IOException e) {
			e.printStackTrace();
			// throw e;
		}
		return sb.toString();
	}
	
	public static String getTitle(String str) {
		String regex;
		String title = "";
		final List<String> list = new ArrayList<String>();
		regex = "<title>.*?</title>";
		final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		final Matcher ma = pa.matcher(str);
		while (ma.find()) {
			list.add(ma.group());
		}
		for (int i = 0; i < list.size(); i++) {
			title = title + list.get(i);
		}
		return outTag(title);
	}
	
	public static String getBody(String str) {
		String regex;
		String title = "";
		final List<String> list = new ArrayList<String>();
		regex = "<body.*?</body>";
		final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		final Matcher ma = pa.matcher(str);
		while (ma.find()) {
			list.add(ma.group());
		}
		for (int i = 0; i < list.size(); i++) {
			title = title + list.get(i);
		}
		return outTag(title);
	}
	
	private static String outTag(final String s) {
		return s.replaceAll("<.*?>", "");
	}
}
