package com.stark.web.service;

import java.util.List;

import com.stark.web.entity.NoticeInfo;

public interface INoticeManager {
	public int addNotice(NoticeInfo nInfo);
	
	public boolean updateNotice(NoticeInfo nInfo);
	
	public boolean deleteNotice(int id);
	
	public NoticeInfo getNotice(int id);
	
	public List<NoticeInfo> getNoticeByUserId(int uid);

	public boolean updateStatus(int noticeId, int status);

	public boolean deleteByUserId(int userId);

	public List<NoticeInfo> getLastNotice(int userId, int type);

	public int getUserNoticeStatus(int userId);

	public void setUserNoticeStatus(int userId, int i); 
}
