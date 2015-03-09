package com.stark.web.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.stark.web.entity.PetInfo;

public class PetDAO implements IPetDAO{

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public String addPet(PetInfo pInfo) {
		int id = (int) sessionFactory.getCurrentSession().save(pInfo);
		return id+"";
	}

	@Override
	public boolean updatePet(PetInfo pInfo) {
		sessionFactory.getCurrentSession().update(pInfo);
		return true;
	}

	@Override
	public boolean deletePet(String pid) {
		String hql = "delete from PetInfo as p where p.petId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, pid);
		
		return query.executeUpdate()>0;
	}

	@Override
	public PetInfo getPet(int id) {
		PetInfo petInfo = (PetInfo) sessionFactory.getCurrentSession().get(PetInfo.class, id);
		return petInfo;
	}

	@Override
	public ArrayList<PetInfo> getPets(String uid) {
		String hql = "from PetInfo a where a.user.userId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, uid);
		//sessionFactor
		return null;
	}
	@Override
	public boolean addPetHeadPic(String petId, String fileName) {
		String hql = "update PetInfo as p set p.headPic = ? where p.petId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, fileName);
		query.setString(1, petId);
		return query.executeUpdate()>0;
	}
	@Override
	public boolean deletePetByUserId(String userId) {
		String hql = "delete from PetInfo as p where p.user.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		
		return query.executeUpdate()>0;
	}

}
