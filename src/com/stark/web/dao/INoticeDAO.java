package com.stark.web.dao;

import java.util.List;

import com.stark.web.entity.NoticeInfo;

public interface INoticeDAO {

	public int addNotice(NoticeInfo nInfo);
	
	public boolean updateNotice(NoticeInfo nInfo);
	
	public boolean deleteNotice(int nid);
	
	public NoticeInfo getNotice(int id);
	
	public List<NoticeInfo> getNoticeByUser(int uid);

	public boolean updateStatus(int noticeId, int status);

	public List<NoticeInfo> getLastNotice(int userId, int type);

	public boolean addRedisNotice(NoticeInfo nInfo);

	public void addRedisUserList(int userId, int noticeId);

	public List<String> getRedisNoticeIds(String key);

	public NoticeInfo getRedisNotice(int id);

	public void setRedisUserNoticeStatus(int userId, int status);

	public int getRedisUserNoticeStatus(int userId);

	public void removeRedisUserList(int userId, int noticeId);

	public void updateRedisNotice(String key, String field, String value);
}
