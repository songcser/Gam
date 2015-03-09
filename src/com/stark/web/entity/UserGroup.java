package com.stark.web.entity;

import java.util.HashSet;
import java.util.Set;

public class UserGroup {
	private int objectId;
	private Set<Integer> usersId = new HashSet<Integer>();
	
	
	
	public Set<Integer> getUsersId() {
		return usersId;
	}
	public void setUsersId(Set<Integer> usersId) {
		this.usersId = usersId;
	}
	@Override
	public String toString() {
		return "UserGroup [objectId=" + objectId + ", usersId=" + usersId + "]";
	}
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	
	
}
