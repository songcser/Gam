package com.stark.web.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.stark.web.entity.EnumBase;
import com.stark.web.entity.TagInfo;

public class TagDAO implements ITagDAO {

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public boolean addTag(TagInfo tInfo) {
		sessionFactory.getCurrentSession().save(tInfo);
		return false;
	}

	@Override
	public boolean updateTag(TagInfo tInfo) {
		sessionFactory.getCurrentSession().update(tInfo);
		
		return true;
	}

	@Override
	public boolean deleteTag(TagInfo tInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TagInfo getTag(String id) {
		return (TagInfo)sessionFactory.getCurrentSession().get(TagInfo.class, id);
	}

	@Override
	public List<String> getTagsByArticle(int aid) {
		//String sql = "select t.content from TAGINFO as t,ARTICLEINFO as a ,RELARTICLETAG as r where a.id = ? and a.id = r.ARTICLEID and r.TAGID = t.id";
		String hql = "select t.content from TagInfo as t,ArticleInfo as a where a.articleId= ? and a.tags.tagId = t.tagId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		//Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, aid);
		return query.list();
	}
	@Override
	public TagInfo getTagByContent(String content) {
		String hql = "from TagInfo as t where t.content = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, content);
		return (TagInfo) query.uniqueResult();
	}
	@Override
	public boolean addArticleTag(int articleId, int tagId) {
		System.out.println("ArticleId: "+articleId+"------------TagId: "+tagId);
		String sql = "insert into RELARTICLETAG(ARTICLEID,TAGID) values(?,?)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger(0, articleId);
		query.setInteger(1, tagId);
		int result = query.executeUpdate();
		System.out.println("result:"+result);
		return result>0;
	}
	@Override
	public List<TagInfo> getHotTags() {
		String hql = "from TagInfo as t where t.status = ? order by t.useCount desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, EnumBase.TagStatus.Online.getIndex());
		query.setFirstResult(0);
		query.setMaxResults(9);
		return query.list();
	}
	@Override
	public boolean addTagPicture(String tagId, String fileName) {
		String hql = "update TagInfo as t set t.picture = ? where t.tagId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, fileName);
		query.setString(1, tagId);
		return query.executeUpdate()>0;
	}

}
