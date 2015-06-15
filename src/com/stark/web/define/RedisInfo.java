package com.stark.web.define;

public class RedisInfo {
	public static final String ARTICLETYPELIST = "Article:Type:List:";							//推文类型列表
	public static final String ARTICLEDAYEXQUISITELIST = "Article:DayExquisite:List";				//每日精选推文列表
	public static final String ARTICLEUPDATELIST = "Article:Update:List";							//更新推文列表
	public static final String ARTICLEPUBLISHCOUNT = "Article:Publish:Count";						//用户发布推文数量
	public static final String ARTICLEEXQUISITECOUNT = "Article:Exquisite:Count";					//每日精选推文数量
	public static final String ARTICLEMAGAZINECOUNT = "Article:Magazine:Count";					//杂志推文数量
	public static final String ARTICLEACTIVITYCOUNT = "Article:Activity:Count";					//活动中推文数量
	public static final String ARTICLENOAUDITINGCOUNT = "Article:NoAuditing:Count";				//未审核活动推文数量
	public static final String ARTICLEDELETECOUNT = "Article:Delete:Count";						//删除推文的数量
	public static final String ARTICLEREPORTCOUNT = "Article:Report:Count";						//被举报的推文数量
	public static final String ARTICLEMAGAZINELIST = "Article:Magazine:List";						//杂志推文列表
	public static final String ARTICLEPICTURELIST = "Article:Picture:List:";						//推文图片列表
	public static final String ARTICLEDATELIST = "Article:Date:List:";							//推文按日期列表
	public static final String ARTICLECOMMENTLIST = "Article:Comment:List:";						//推文评论列表
	public static final String ARTICLERECOMMENDLIST = "Article:Recommend:List";					//推文推荐列表
	public static final String ARTICLENOAUDITINGRECOMMENDLIST = "Article:NoAuditing:Recommend:List"; //未审核推荐推文列表
	public static final String ARTICLEMOMENTLIST = "Article:Moment:List";							//瞬间推文列表
	public static final String ARTICLENOAUDITINGMOMENTLIST = "Article:NoAuditing:Moment:List";			//未审核瞬间推文列表
	public static final String ARTICLEDELETELIST = "Article:Delete:List";							//删除的推文列表
	public static final String ARTICLECOMMONLIST = "Article:Common:List";							//普通用户发布推文列表
	public static final String ARTICLEOPERTORLIST = "Article:Opertor:List";							//运营人员发布推文列表
	public static final String ARTICLEPRAISELIST = "Article:Praise:List:";							//推文赞过的用户列表
	
	public static final String USERARTICLELIST = "User:Article:List:";								//用户发布推文列表
	public static final String USEROSLIST = "User:OS:List";											//运营人员和模拟用户列表
	public static final String USERPRAISESET = "User:Praise:Set:";									//用户赞的推文集
	public static final String USERPRAISEARTICLE = "User:Praise:Article:";							//用户赞推文标识
	public static final String USERFOLLOWZSET = "User:Follow:ZSet:";								//用户关注列表
	public static final String USERFOLLOWFLAG = "User:Follow:Flag:";								//用户关注标识
	public static final String USERFANSZSET = "User:Fans:ZSet:";									//用户粉丝列表
	public static final String OPERATIORCOUNT = "User:Operatior:Count";							//运营人员数量
	public static final String SIMULATIONCOUNT = "User:Simulation:Count";							//模拟用户数量
	public static final String NORMALCOUNT = "User:Normal:Count";									//普通用户数量
	public static final String USERORGANIZATIONCOUNT = "User:Organization:Count";					//机构组织数量
	public static final String USERNOTICELIST = "User:Notice:List:";								//用户通知列表
	public static final String USERQQOPENID = "User:QQOpenId:";										//QQ用户Id
	public static final String USERSINAOPENID = "User:SinaOpenId:";							//新浪用户Id
	public static final String USERWECHATOPENID = "User:WeChatOpenId:";							//微信用户Id
	public static final String USERNOTICESTATUS = "User:Notice:Status:";							//用户通知状态
	public static final String USERCHILDLIST = "User:Child:List:";								//子用户列表
	public static final String USEREMAILPASSWORD = "User:EmailPassword:";						//用户邮箱密码
	public static final String USERLOGINSET = "User:Login:Set";								//用户最新登录集
	public static final String USEREMAILSET = "User:Email:Set";								//用户邮箱集
	public static final String USERCOLLECTIONLIST = "User:Collection:List:";				//用户收藏推文列表
	public static final String USERCOLLECTIONARTICLE="User:Collection:Article:";			//用户收藏推文标识
	public static final String USERFOLLOWARTICLEZSET = "User:Follow:Article:ZSet:";			//用户关注推文集
	public static final String USERADMINPASSWORD = "User:Admin:Password:";					//管理员密码
	public static final String USEROPERATIORPASSWORD = "User:Operatior:Password:";			//运营人员密码
	public static final String USERNAMELIST = "User:Name:List:";							//用户名字id列表
	public static final String USERMARKLIST = "User:Mark:List";								//标记用户列表
	public static final String USERNORMALLIST = "User:Normal:List";								//普通用户有序集
	public static final String USERALLSET = "User:ALL:Set";										//所有用户集
	public static final String USERORGANIZATIONLIST = "User:Organization:List";					//机构用户列表
	public static final String USERFOLLOWARTICLECOUNT = "User:Follow:Article:Count:";			//用户关注列表的数量
	
	public static final String ACTIVITYBANNERLIST="Activity:Banner:List";							//活动banner列表
	public static final String ACTIVITYTOPLIST = "Activity:Top:List";								//活动置顶推荐列表
	public static final String ACTIVITYARTICLEALLLIST = "Activity:Article:All:List:";				//活动所有推文列表
	public static final String ACTIVITYARTICLEAUDITINGLIST = "Activity:Article:Auditing:List:"; 	//活动中审核通过的推文列表
	public static final String ACTIVITYALLLIST = "Activity:All:List";								//所有活动列表
	public static final String ACTIVITYORDERZSET = "Activity:Order:ZSet";							//节目有序集
	public static final String ACTIVITYNOAUDITINGLIST = "Activity:Article:NoAuditing:List:";		//活动中没有审核的推文列表
	public static final String ACTIVITYONLINEZSET = "Activity:Online:ZSet";							//上线节目单有序集
	
	public static final String CHARTLETALLLIST = "Chartlet:All:List";								//贴图列表
	public static final String CHARTLETPICTURELIST = "Chartlet:Picture:List:";					//贴图图片列表
	public static final String CHARTLETBUBBLELIST = "Chartlet:Bubble:List";						//气泡框列表
	public static final String CHARTLETDIALOGUEZSET = "Chartlet:Dialogue:ZSet:";					//台词有序集
	public static final String CHARTLETDIALOGUELIST = "Chartlet:Dialogue:List";					//贴图台词列表
	public static final String CHARTLETDIALOGUEUSER = "Chartlet:Dialogue:User";					//用户台词系列ID
	
	public static final String OPENPICTUREFLAG = "Open:Picture:Flag";
	
}
