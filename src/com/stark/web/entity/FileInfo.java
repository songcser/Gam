package com.stark.web.entity;

import java.util.HashSet;
import java.util.Set;

public class FileInfo {
	
	private int fileId;
	private String name;
	private String path;
	private int width;
	private int height;
	private int size;
	private int status;
	private Set<ChartletInfo> chartlets = new HashSet<ChartletInfo>();
	
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Set<ChartletInfo> getChartlets() {
		return chartlets;
	}
	public void setChartlets(Set<ChartletInfo> chartlets) {
		this.chartlets = chartlets;
	}
}
