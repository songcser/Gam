package com.stark.web.entity;

import com.stark.web.service.FileManager;

public class RelChartletPicture {
	
	private static final String key = "Chartlet:Picture:Hash:";
	public static final String CHARTLETID = "ChartletId";
	public static final String PICTURE = "Picture";
	public static final String STATUS = "Status";

	private int id;
	private ChartletInfo chartlet;
	private String picture;
	private int status;
	
	public String getKey(){
		return key+id;
	}
	
	public static String getKey(int id){
		return key+id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ChartletInfo getChartlet() {
		return chartlet;
	}
	public void setChartlet(ChartletInfo chartlet) {
		this.chartlet = chartlet;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getPicUrl(int chartletId){
		return FileManager.getChartletPictureUrl(chartletId, picture);
	}
}
