package com.stark.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.stark.web.dao.INoticeDAO;
import com.stark.web.define.EnumBase.NoticeType;
import com.stark.web.define.EnumBase.UserRole;
import com.stark.web.define.RedisInfo;
import com.stark.web.define.EnumBase.NoticeStatus;
import com.stark.web.entity.NoticeInfo;
import com.stark.web.entity.UserInfo;

public class NoticeManager implements INoticeManager{

	private INoticeDAO noticeDao;
	
	public void setNoticeDao(INoticeDAO noticeDao){
		this.noticeDao = noticeDao;
	}
	
	@Resource
	private WebManager webManager;
	
	@Override
	public int addNotice(NoticeInfo nInfo) {
		int id = noticeDao.addNotice(nInfo);
		if(id>0){
			noticeDao.addRedisNotice(nInfo);
			noticeDao.addRedisUserList(nInfo.getUser().getUserId(),nInfo.getNoticeId());
			noticeDao.setRedisUserNoticeStatus(nInfo.getUser().getUserId(),1);
			//int type = nInfo.getType();
			//if(type==NoticeType.At.getIndex()||type==NoticeType.Follow.getIndex()||type==NoticeType.Praise.getIndex()||type==NoticeType.Comment.getIndex()){
				//if(nInfo.getUser().getRole()==UserRole.Normal.getIndex()){
					//WebManager.pushToUser(nInfo.getUser().getUserId(),type, nInfo.getContent());
				//}
				
			//}
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
		boolean result = noticeDao.updateStatus(noticeId,status);
		if(result){
			noticeDao.updateRedisNotice(NoticeInfo.getKey(noticeId),NoticeInfo.STATUS,status+"");
			if(status==NoticeStatus.Readed.getIndex()){
				NoticeInfo notice = getNotice(noticeId);
				noticeDao.removeRedisUserList(notice.getUser().getUserId(),noticeId);
			}
		}
		return result;
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

	@Override
	public void setUserNoticeStatus(int userId, int i) {
		noticeDao.setRedisUserNoticeStatus(userId, i);
	}

	@Override
	public void addSeeNotice(int uId, int senderId) {
		NoticeInfo notice = new NoticeInfo();
		notice.setUser(new UserInfo(uId));
		notice.setSender(new UserInfo(senderId));
		//notice.setArticle(null);
		notice.setContent("看了你");
		notice.setDate(new Date());
		notice.setType(NoticeType.See.getIndex());
		notice.setStatus(NoticeStatus.NoRead.getIndex());
		//System.out.println(userId);
		addNotice(notice);
		webManager.pushToUser(uId,NoticeType.See.getIndex());		
	}

}
