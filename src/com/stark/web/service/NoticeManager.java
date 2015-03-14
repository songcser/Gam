package com.stark.web.service;

import java.util.ArrayList;
import java.util.List;

import com.stark.web.dao.INoticeDAO;
import com.stark.web.define.RedisInfo;
import com.stark.web.entity.NoticeInfo;

public class NoticeManager implements INoticeManager{

	private INoticeDAO noticeDao;
	
	public void setNoticeDao(INoticeDAO noticeDao){
		this.noticeDao = noticeDao;
	}
	
	@Override
	public int addNotice(NoticeInfo nInfo) {
		int id = noticeDao.addNotice(nInfo);
		if(id>0){
			noticeDao.addRedisNotice(nInfo);
			noticeDao.addRedisUserList(nInfo.getUser().getUserId(),nInfo.getNoticeId());
			noticeDao.setRedisUserNoticeStatus(nInfo.getUser().getUserId(),1);
		}
		return id;
	}

	@Override
	public boolean updateNotice(NoticeInfo nInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteNotice(int id) {
		return noticeDao.deleteNotice(id);
	}

	@Override
	public NoticeInfo getNotice(int id) {
		NoticeInfo notice = noticeDao.getRedisNotice(id);
		if(notice==null){
			notice = noticeDao.getNotice(id);
			if(notice!=null){
				noticeDao.addRedisNotice(notice);
			}
		}
		return notice;
	}

	@Override
	public List<NoticeInfo> getNoticeByUserId(int uid) {
		List<String> ids = noticeDao.getRedisNoticeIds(RedisInfo.USERNOTICELIST+uid);
		List<NoticeInfo> list = new ArrayList<NoticeInfo>();
		noticeDao.setRedisUserNoticeStatus(uid, 0);
		if(ids!=null&&!ids.isEmpty()){
			for(String id:ids){
				NoticeInfo notice = getNotice(Integer.parseInt(id));
				if(notice!=null){
					list.add(notice);
				}
			}
			
			return list;
		}
		
		list = noticeDao.getNoticeByUser(uid);
		if(list!=null){
			int size = list.size();
			for(int i=size-1;i>=0;i--){
				NoticeInfo notice = list.get(i);
				noticeDao.addRedisNotice(notice);
				noticeDao.addRedisUserList(notice.getUser().getUserId(), notice.getNoticeId());
			}
		}
		return list;
	}

	@Override
	public boolean updateStatus(int noticeId, int status) {
		return noticeDao.updateStatus(noticeId,status);
	}

	@Override
	public boolean deleteByUserId(int userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NoticeInfo> getLastNotice(int userId, int type) {
		
		return noticeDao.getLastNotice(userId,type);
	}

	@Override
	public int getUserNoticeStatus(int userId) {
		
		return noticeDao.getRedisUserNoticeStatus(userId);
	}

}
