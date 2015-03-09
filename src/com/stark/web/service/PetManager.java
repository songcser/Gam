package com.stark.web.service;

import java.util.ArrayList;

import com.stark.web.dao.IPetDAO;
import com.stark.web.entity.PetInfo;

public class PetManager implements IPetManager{

	private IPetDAO petDao;
	
	public void setPetDao(IPetDAO petDao){
		this.petDao=petDao;
		
	}
	@Override
	public String addPet(PetInfo pInfo) {
		return petDao.addPet(pInfo);
	}

	@Override
	public boolean updatePet(PetInfo pInfo) {
		
		return petDao.updatePet(pInfo);
	}

	@Override
	public boolean deletePet(String pid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PetInfo getPet(int id) {
		// TODO Auto-generated method stub
		return petDao.getPet(id);
	}

	@Override
	public ArrayList<PetInfo> getPets(String uid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean addPetHeadPic(String petId, String fileName) {
		return petDao.addPetHeadPic(petId,fileName);
	}
	@Override
	public boolean deletePetByUserId(String userId) {
		return petDao.deletePetByUserId(userId);
	}

}
