package com.stark.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.stark.web.define.EnumBase.NoticeType;
import com.stark.web.define.EnumBase.ThirdSharing;
import com.stark.web.hunter.FileManager;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

@Component
public class WebManager {
	private static Logger logger = Logger.getLogger(WebManager.class);
	private final static String masterSecret = "adec968251666c5982c3cce3";
	private final static String appKey = "408183a3a7fd461efc860cda";
	private final static String ALERT = "hello world";
	
	private final static String weChatAppId = "wx4b4d99253b8e39fc";
	private final static String weChatAppSecret = "1aa4c17e61d10b6690d3ccbafff9f974";
	//private final static String REDIRECT_URI = "http://www.uha.so/StrakPet/article/outShare.do?articleId=123";
	private final static String SCOPE = "snsapi_userinfo";
	
	
	public static String  getCodeRequest = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	private static String get_access_token_url="https://api.weixin.qq.com/sns/oauth2/access_token?" +
	        "appid=APPID" +
	        "&secret=SECRET&" +
	        "code=CODE&grant_type=authorization_code";
	
	private static String get_userinfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	@Resource(name="threadPool")
	private ThreadPoolTaskExecutor threadPool;
	
	public void setThreadPool(ThreadPoolTaskExecutor threadPool){
		this.threadPool = threadPool;
	}
	
	public void JPush(){
		final JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		final PushPayload payload = buildPushObject_android_tag_alertWithTitle();
		toPush(jpushClient,payload);
		
	}
	
	private static JPushClient getPushClient(){
		return new JPushClient(masterSecret, appKey, 3);
	}
	
	private  void toPush(final JPushClient jpushClient,final PushPayload payload){
		threadPool.execute(new Runnable() {
			public void run() {
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
		});
		
	}
	
	public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }
	
	public static PushPayload test(){
		return PushPayload.newBuilder()
				.setPlatform(Platform.all())
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
	
	public  void pushToAll(String content){
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload = pushToAll_all(content);
		
		toPush(jpushClient,payload);
	}
	
	private static PushPayload pushToAll_all(String content){
		return PushPayload.alertAll(content);
	}
	
	public  void pushToAllExtShow(String content, int showId,int showType) {
		JPushClient jpushClient = getPushClient();
		Map<String,String> map = new HashMap<String,String>();
		map.put("type", NoticeType.Show.getIndex()+"");
		map.put("showId", showId+"");
		map.put("showType", showType+"");
		PushPayload payload = pushExtra(content,map);
		
		toPush(jpushClient,payload);
	}
	
	private static PushPayload pushExtra(String content,Map<String,String> extra){
		return PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(content)
                                .setSound("happy")
                                .addExtras(extra)
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        		.setAlert(content)
                        		.addExtras(extra)
                        		.build())
                        .build())
                 .build();
	}

	public  void pushToUser(int userId,String content){
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload = pushToUser_ios_android(userId,content);
		
		toPush(jpushClient,payload);
	}
	
	public void pushToUser(int userId, int type) {
		JPushClient jpushClient = getPushClient();
		Map<String,String> map = new HashMap<String,String>();
		map.put("type", type+"");
		String content = "有通知";
		if(type==NoticeType.Comment.getIndex()){
			content="有人评论了你";
		}
		else if(type==NoticeType.Praise.getIndex()){
			content="有人赞了你";
		}
		else if(type==NoticeType.Follow.getIndex()){
			content="有人关注了你";
		}
		PushPayload payload = pushUserExtra(userId,content,map);
		toPush(jpushClient,payload);
	}
	
	public static PushPayload pushToUser_ios_android(int userId,String content){
		return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(userId+""))
                .setNotification(Notification.alert(content))
                .build();
	}
	public  void pushToUser(int userId, int type, String content) {
		JPushClient jpushClient = getPushClient();
		Map<String,String> map = new HashMap<String,String>();
		map.put("type", type+"");
		PushPayload payload = pushUserExtra(userId,content,map);
		
		toPush(jpushClient,payload);
	}
	
	private static PushPayload pushUserExtra(int userId,String content, Map<String, String> extra) {
		return PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(userId+""))
				.setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(content)
                                .setSound("happy")
                                .addExtras(extra)
                                .incrBadge(1)
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        		.setAlert(content)
                        		.addExtras(extra)
                        		.build())
                        .build())
                 .build();
	}

	public static PushPayload pushShowToUser(int userId,String content,int showId){
		return PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(userId+""))
				.setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("showId", ""+showId)
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        		.setAlert(content)
                        		.addExtra("showId",""+showId)
                        		.build())
                        .build())
                 .setMessage(Message.content(content))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)
                         .build())
                 .build();
	}
	
	public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
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
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("190"))
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
	
	public static String getHtmlArticlePattern(String str) {
		String regex;
		String title = "";
		final List<String> list = new ArrayList<String>();
		regex = "<article.*?</article>";
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
	
	public static String getHtmlArticle(String str){
		int beginIndex = str.indexOf("<article>");
		int endIndex = str.indexOf("</article>");
		
		return str.substring(beginIndex, endIndex);
	}
	
	private static String outTag(final String s) {
		return s.replaceAll("<.*?>", "");
	}

	public static void Oauth2WeiXin(String code){
		 get_access_token_url=get_access_token_url.replace("APPID", weChatAppId);
	     get_access_token_url=get_access_token_url.replace("SECRET", weChatAppSecret);
	     get_access_token_url=get_access_token_url.replace("CODE", code);
	     
	     String json=getUrl(get_access_token_url);
	     
	     JSONObject jsonObject=JSONObject.fromObject(json);
	     String access_token=jsonObject.getString("access_token");
	     String openid=jsonObject.getString("openid");
	        
	     get_userinfo=get_userinfo.replace("ACCESS_TOKEN", access_token);
	     get_userinfo=get_userinfo.replace("OPENID", openid);
	        
	     String userInfoJson=getUrl(get_userinfo);
	        
	     JSONObject userInfoJO=JSONObject.fromObject(userInfoJson);
	        
	     String user_openid=userInfoJO.getString("openid");
	     System.out.println(user_openid);
	     String user_nickname=userInfoJO.getString("nickname");
	     System.out.println(user_nickname);
	     String user_sex=userInfoJO.getString("sex");
	     System.out.println(user_sex);
	     String user_province=userInfoJO.getString("province");
	     System.out.println(user_province);
	     String user_city=userInfoJO.getString("city");
	     System.out.println(user_city);
	     String user_country=userInfoJO.getString("country");
	     System.out.println(user_country);
	     String user_headimgurl=userInfoJO.getString("headimgurl");
	     System.out.println(user_headimgurl);
	}
	
	public static JSONObject getAccessToken(String code){
		 get_access_token_url=get_access_token_url.replace("APPID", weChatAppId);
	     get_access_token_url=get_access_token_url.replace("SECRET", weChatAppSecret);
	     get_access_token_url=get_access_token_url.replace("CODE", code);
	     
	     String json=getUrl(get_access_token_url);
	     System.out.println(json);
	     JSONObject jsonObject=JSONObject.fromObject(json);
	     
	     return jsonObject;
	}
	
	public static JSONObject getOauthUserInfo(String openid,String access_token){
		get_userinfo=get_userinfo.replace("ACCESS_TOKEN", access_token);
	     get_userinfo=get_userinfo.replace("OPENID", openid);
	        
	     String userInfoJson=getUrl(get_userinfo);
	        
	     JSONObject userInfoJO=JSONObject.fromObject(userInfoJson);
	     
	     return userInfoJO;
	}

	public static String getUrl(String url){
        String result = null;
        try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(url);
            // 获取当前客户端对象
            @SuppressWarnings({ "resource", "deprecation" })
			HttpClient httpClient = new DefaultHttpClient();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            
            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result= EntityUtils.toString(response.getEntity(),"utf-8");
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	public static String getCodeRequest(String redirectUri){ 
        String result = null; 
        
        getCodeRequest  = getCodeRequest.replace("APPID", urlEnodeUTF8(weChatAppId)); 
        getCodeRequest  = getCodeRequest.replace("REDIRECT_URI",urlEnodeUTF8(redirectUri)); 
        getCodeRequest = getCodeRequest.replace("SCOPE", SCOPE); 
        String state = FileManager.toGUID();
        getCodeRequest = getCodeRequest.replace("STATE", state); 

        result = getCodeRequest; 
        return result; 
    } 


    public static String urlEnodeUTF8(String str){ 
        String result = str; 
        try { 
            result = URLEncoder.encode(str,"UTF-8"); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 

        return result; 
    }

	public static String getRedirectUri(int userId, int articleId, int shareFrom) {
		return  "http://stark.tunnel.mobi/StarkPet/article/outShareOAuth.do?articleId="+articleId+"&userId="+userId+"&shareFrom="+shareFrom;
	}

	public static String getSecondUrl(int userId,int articleId,int shareFrom,String fromOpenId,String toOpenId) {
		return "/article/outShareOAuth.do?articleId="+articleId+"&userId="+userId+"&shareFrom="+shareFrom+"&fromOpenId="+fromOpenId+"&toOpenId="+toOpenId;
	}

	public static String getAgentShareUrl(int articleId) {
		
		String redirectUri = WebManager.getRedirectUri(articleId);
		String oauth_url = WebManager.getCodeRequest(redirectUri);
		return oauth_url;
	}

	private static String getRedirectUri(int articleId) {
		return  "http://stark.tunnel.mobi/StarkPet/article/outShareOAuth.do?articleId="+articleId+"&userId=USERID&shareFrom=SHAREFROM";
	}

	public static String getWeChatAppId() {
		return weChatAppId;
	}

	public static String getWeChatAppSecret() {
		return weChatAppSecret;
	} 
}
