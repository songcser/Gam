package com.stark.web.entity;

import com.stark.web.hunter.FileManager;

public class RelChartletPicture {
	
	private static final String key = "Chartlet:Picture:Hash:";
	public static final String CHARTLETID = "ChartletId";
	public static final String PICTURE = "Picture";
	public static final String STATUS = "Status";
	public static final String COORDINATEX = "coordinateX";
	public static final String COORDINATEY = "coordinateY";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	private int id;
	private ChartletInfo chartlet;
	private String picture;
	private int status;
	private int coordinateX;
	private int coordinateY;
	private int width;
	private int height;
	
	public int getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
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

	public int getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}
}
