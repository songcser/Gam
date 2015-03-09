package com.stark.web.entity;


public class BackupInfo {
	
	private int status;
	private String startDate;
	private String lastDate;
	private String path;
	private int saveCount;
	private int frequency;
	private String date;
	private String time;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
		
	}
	public int getSaveCount() {
		return saveCount;
	}
	public void setSaveCount(int saveCount) {
		this.saveCount = saveCount;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	@Override
	public String toString() {
		return "BackupInfo [status=" + status + ", startDate=" + startDate + ", lastDate=" + lastDate + ", path=" + path + ", saveCount=" + saveCount
				+ ", frequency=" + frequency + ", date=" + date + ", time=" + time + "]";
	}
}
