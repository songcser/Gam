package com.stark.web.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CommentListInfo extends ArrayList<CommentInfo>{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		List<String> json = new ArrayList<String>();
		for (Iterator<CommentInfo> iterator = this.iterator(); iterator.hasNext();) {
			CommentInfo c = (CommentInfo) iterator.next();
			json.add(c.toString());
		}
		return json.toString();
	}
	
}
