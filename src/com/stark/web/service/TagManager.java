package com.stark.web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.stark.web.dao.ITagDAO;
import com.stark.web.entity.TagInfo;

public class TagManager implements ITagManager{

	private ITagDAO tagDao;
	
	public void setTagDao(ITagDAO tagDao){
		this.tagDao = tagDao;
	}
	@Override
	public boolean addTag(TagInfo tInfo) {
		return tagDao.addTag(tInfo);
	}

	@Override
	public boolean updateTag(TagInfo tInfo) {
		return tagDao.updateTag(tInfo);
	}

	@Override
	public TagInfo getTag(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTagsByArticle(int aid) {
		// TODO Auto-generated method stub
		return tagDao.getTagsByArticle(aid);
	}
	@Override
	public TagInfo getTagByContent(String content) {
		return tagDao.getTagByContent(content);
	}
	@Override
	public boolean addArticleTag(int articleId, int tagId) {
		
		return tagDao.addArticleTag(articleId,tagId);
	}
	@Override
	public List<TagInfo> getHotTags() {
		return tagDao.getHotTags();
	}
	@Override
	public List<String> getTagsList(Set<TagInfo> tags) {
		List<String> tagList = new ArrayList<String>();
		//Set<TagInfo> tagSet = article.getTags();
		//System.out.println("Tag size"+tagSet.size());
		for (Iterator<TagInfo> it = tags.iterator(); it.hasNext();) {
			String tag =  it.next().getContent();
			tagList.add(tag);
		}
		return tagList;
	}
	@Override
	public boolean addTagPicture(String tagId, String fileName) {
		return tagDao.addTagPicture(tagId,fileName);
	}

}
