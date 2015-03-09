package com.stark.web.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChartletInfo {
	
	private static String key = "Chartlet:Info:Hash:";
	public static String CHARTLETID = "ChartletId";
	public static String TYPE = "Type";
	public static String TITLE = "Title";
	public static String STATUS = "Status";
	
	private int chartletId;
	private int type;
	private String title;
	private int status;
	private Set<RelChartletPicture> picSet = new HashSet<RelChartletPicture>();
	private List<RelChartletPicture> picList = new ArrayList<RelChartletPicture>();
	//private Set<String> picSet;
	
	public ChartletInfo(){
		
	}
	
	public String getKey(){
		return key+chartletId;
	}
	
	public static String getKey(int chartletId){
		return key+chartletId;
	}
	
	public ChartletInfo(int chartletId) {
		this.chartletId = chartletId;
	}
	public int getChartletId() {
		return chartletId;
	}
	public void setChartletId(int chartletId) {
		this.chartletId = chartletId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/*
	public List<String> getPicList(){
		List<String> list = new ArrayList<String>();
		for (Iterator<FileInfo> iterator = fileSet.iterator(); iterator.hasNext();) {
			FileInfo pic = iterator.next();
			list.add(FileManager.getChartletPictureUrl(chartletId, pic.getName()));
		}
		//System.out.println(list.size());
		return list;
	}*/
	public Set<RelChartletPicture> getPicSet() {
		return picSet;
	}
	public void setPicSet(Set<RelChartletPicture> picSet) {
		this.picSet = picSet;
	}

	public List<RelChartletPicture> getPicList() {
		return picList;
	}

	public void setPicList(List<RelChartletPicture> picList) {
		this.picList = picList;
	}
	
}
