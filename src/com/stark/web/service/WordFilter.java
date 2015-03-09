package com.stark.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordFilter implements IFilter {

	private static ArrayList<String> words =null;
	
	public WordFilter(){
		if(words==null){
			words = new ArrayList<String>();
			InputStream is=WordFilter.class.getClassLoader().getResourceAsStream("filterwords");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	            	words.add(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	if(is!=null)
	            		is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
	}
	
	@Override
	public String doFilter(String str) {
		if(words==null){
			getWords();
		}
		//System.out.println(str);
		for(String word:words){
			//System.out.println(word);
			int len = word.length();
			StringBuilder sb = new StringBuilder("*");
			for(int i=1;i<len;i++){
				sb.append("*");
			}
			//System.out.println(sb);
			
			str = str.replace(word,sb);
		}
		str = str.replace("这个家伙太懒了，什么都没写耶！", "他什么都没写");
		//System.out.println(str);
		return str;
	}

	public static List<String> getWords() {
		if(words==null){
			words = new ArrayList<String>();
			InputStream is=WordFilter.class.getClassLoader().getResourceAsStream("filterwords");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	            	words.add(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	if(is!=null)
	            		is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
		return words;
	}

}
