package com.stark.web.service;

import java.util.List;
import java.util.Set;

import com.stark.web.entity.TagInfo;

public interface ITagManager {
	public boolean addTag(TagInfo tInfo);
	
	public boolean updateTag(TagInfo tInfo);
	
	public TagInfo getTag(String id);
	
	public List<String> getTagsByArticle(int articleId);

	public TagInfo getTagByContent(String content);

	public boolean addArticleTag(int articleId, int tagId);

	public List<TagInfo> getHotTags();
	
	public List<String> getTagsList(Set<TagInfo> tags);

	public boolean addTagPicture(String tagId, String fileName);
}
