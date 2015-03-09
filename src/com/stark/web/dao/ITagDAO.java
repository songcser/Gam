package com.stark.web.dao;

import java.util.List;

import com.stark.web.entity.TagInfo;

public interface ITagDAO {
	public boolean addTag(TagInfo tInfo);
	
	public boolean updateTag(TagInfo tInfo);
	
	public boolean deleteTag(TagInfo tInfo);
	
	public TagInfo getTag(String id);
	
	public List<String> getTagsByArticle(int aid);

	public TagInfo getTagByContent(String content);

	public boolean addArticleTag(int articleId, int tagId);

	public List<TagInfo> getHotTags();

	public boolean addTagPicture(String tagId, String fileName);
}
