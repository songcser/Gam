package com.stark.web.entity;

import java.util.Date;

public class DialogueInfo {

	private static String key = "Dialogue:Info:Hash:";
	public static String DIALOGUEID = "DialogueId";
	public static String USERID = "UserId";
	public static String CHARTLETID = "ChartletId";
	public static String CONTENT = "Content";
	public static String NUMBER = "Number";
	public static String DATE = "Date";
	
	private int dialogueId;
	private UserInfo user;
	private ChartletInfo chartlet;
	private String content;
	private int number;
	private Date date;
	public int getDialogueId() {
		return dialogueId;
	}
	public void setDialogueId(int dialogueId) {
		this.dialogueId = dialogueId;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public ChartletInfo getChartlet() {
		return chartlet;
	}
	public void setChartlet(ChartletInfo chartlet) {
		this.chartlet = chartlet;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getKey() {
		return key+dialogueId;
	}
	public static String getKey(int id) {
		return key+id;
	}
	
}
