package com.stark.web.dao;

import java.util.ArrayList;

import com.stark.web.entity.PetInfo;

public interface IPetDAO {
	
	public String addPet(PetInfo pInfo);
	
	public boolean updatePet(PetInfo pInfo);
	
	public boolean deletePet(String pid);
	
	public ArrayList<PetInfo> getPets(String uid);

	public boolean addPetHeadPic(String petId, String fileName);

	public boolean deletePetByUserId(String userId);

	public PetInfo getPet(int id);
}
