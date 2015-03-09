package com.stark.web.entity;

import java.util.Date;

import com.stark.web.hunter.FileManager;

public class PetInfo {
	
	private int petId;
	
	private UserInfo user;
	private String name;
	private String petType;
	private int type;
	private int sex;
	private Date birthday;
	private String headPic;
	
	//getter and setter
	public int getPetId() {
		return petId;
	}
	public void setPetId(int petId) {
		this.petId = petId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getHeadPic() {
		return headPic;
	}
	public String getHeadPicUrl(){
		if(headPic==null){
			return FileManager.getPetDefaultPictureUrl();
		}
		return FileManager.getPetPictureUrl(petId, headPic);
	}
	
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public String getPetType() {
		return petType;
	}
	public void setPetType(String petType) {
		this.petType = petType;
	}
	
}
