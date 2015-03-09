package com.stark.web.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.stark.web.service.FileManager;

public class UserInfo {
	
	private static String key = "User:Info:Hash:";
	
	public static final String NAME = "Name";
	public static final String ROLE = "Role";
	public static final String SEX = "Sex";
	public static final String PASSWORD = "Password";
	public static final String HOMETOWN = "HomeTown";
	public static final String HEADPIC = "HeadPic";
	public static final String FOLLOWINGCOUNT = "FollowingCount";
	public static final String FANSCOUNT = "FansCount";
	public static final String ARTICLECOUNT = "ArticleCount";
	public static final String QQOPENID = "QQOpenId";
	public static final String SINAOPENID = "SinaOpenId";
	public static final String WECHATOPENID = "WeChatOpenId";
	public static final String PRAISECOUNT = "PraiseCount";
	public static final String NOTICESTATUS = "NoticeStatus";
	public static final String LASTLOGONDATE = "LastLogonDate";
	public static final String EMAIL = "Email";
	
	
	private int  userId;
	private String name;
	private String password;
	private long doorplate;
	private String phoneNumber;
	private int role;
	private int sex;
	private int status;
	private Date birthday;
	private String homeTown;
	private String signature;
	private String email;
	private int followingCount;
	private int friendCount;
	private int fansCount;
	private int articleCount;
	private int praiseCount;
	private Date lastLogonDate;
	private Set<String> type = new HashSet<String>();
	private Set<PetInfo> pets = new HashSet<PetInfo>();
	private Set<ArticleInfo> praiseArticle = new HashSet<ArticleInfo>();
	private Set<UserInfo> childUser = new HashSet<UserInfo>();
	private String headPic;
	private String qqOpenId;
	private String sinaOpenId;
	private String weChatOpenId;
	private int noticeStatus;
	
	//private String headUrl;
	
	public UserInfo(){}
	
	public UserInfo(int userId){
		this.userId = userId;
	}
	
	public UserInfo(int userId,String headPic) {
	    this.userId = userId;
	    this.headPic = headPic;
	}
	
	public String getKey() {
		return key+userId;
	}
	
	public static String getKey(int userId){
		return key+ userId;
	}
	//getter and setter
	public String getName() {
		return name;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getDoorplate() {
		return doorplate;
	}
	public void setDoorplate(long doorplate) {
		this.doorplate = doorplate;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getHomeTown() {
		return homeTown;
	}
	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public int getFollowingCount() {
		return followingCount;
	}
	public void setFollowingCount(int followingCount) {
		this.followingCount = followingCount;
	}
	public int getFriendCount() {
		return friendCount;
	}
	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}
	public int getFansCount() {
		return fansCount;
	}
	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
	public Date getLastLogonDate() {
		return lastLogonDate;
	}
	public void setLastLogonDate(Date lastLogonDate) {
		this.lastLogonDate = lastLogonDate;
	}
	public String getHeadPic() {
		
		return headPic;
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	public Set<PetInfo> getPets() {
		return pets;
	}
	public void setPets(Set<PetInfo> pets) {
		this.pets = pets;
	}
	
	public String toJson(){
		return "";
	}

	public Set<ArticleInfo> getPraiseArticle() {
		return praiseArticle;
	}

	public void setPraiseArticle(Set<ArticleInfo> praiseArticle) {
		this.praiseArticle = praiseArticle;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSinaOpenId() {
		return sinaOpenId;
	}

	public void setSinaOpenId(String sinaOpenId) {
		this.sinaOpenId = sinaOpenId;
	}

	public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getHeadUrl() {
		if(headPic==null|"".equals(headPic)){
			return FileManager.getUserDefaultPicture(sex);
		}
		else 
			return FileManager.getUserPictureUrl(userId, headPic);
		
	}

	

	public Set<String> getType() {
		return type;
	}

	public void setType(Set<String> type) {
		this.type = type;
	}

	public List<String> getPetTypeList() {
		List<String> list = new ArrayList<String>();
		for (Iterator<PetInfo> iter = pets.iterator(); iter.hasNext();) {
			PetInfo petInfo = iter.next();
			String type = EnumBase.DogPetType.getName(petInfo.getType());
			if(type==null){
				type = EnumBase.CatPetType.getName(petInfo.getType());
				if(type==null){
					type = EnumBase.OtherPetType.getName(petInfo.getType());
				}
			}
			list.add(type);
		}
		return list;
	}
	
	
	
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", name=" + name + ", password=" + password + ", doorplate=" + doorplate + ", phoneNumber=" + phoneNumber
				+ ", role=" + role + ", sex=" + sex + ", status=" + status + ", birthday=" + birthday + ", homeTown=" + homeTown + ", signature=" + signature
				+ ", followingCount=" + followingCount + ", friendCount=" + friendCount + ", fansCount=" + fansCount + ", articleCount=" + articleCount
				+ ", praiseCount=" + praiseCount + ", lastLogonDate=" + lastLogonDate + ", type=" + type + ", pets=" + pets + ", praiseArticle="
				+ praiseArticle + ", headPic=" + headPic + ", qqOpenId=" + qqOpenId + ", sinaOpenId=" + sinaOpenId + ", weChatOpenId=" + weChatOpenId
				+ ", noticeStatus=" + noticeStatus + "]";
	}

	public int getArticleCount() {
	    return articleCount;
	}

	public void setArticleCount(int articleCount) {
	    this.articleCount = articleCount;
	}

	public String getWeChatOpenId() {
		return weChatOpenId;
	}

	public void setWeChatOpenId(String weChatOpenId) {
		this.weChatOpenId = weChatOpenId;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getNoticeStatus() {
		return noticeStatus;
	}

	public void setNoticeStatus(int noticeStatus) {
		this.noticeStatus = noticeStatus;
	}

	public Set<UserInfo> getChildUser() {
		return childUser;
	}

	public void setChildUser(Set<UserInfo> childUser) {
		this.childUser = childUser;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

}
