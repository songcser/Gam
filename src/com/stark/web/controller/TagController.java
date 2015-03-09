package com.stark.web.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.stark.web.entity.TagInfo;
import com.stark.web.hunter.FileManager;
import com.stark.web.service.ITagManager;

@Controller
@RequestMapping("/tag")
public class TagController {
	
	@Resource(name="tagManager")
	private ITagManager tagManager;
	
	@RequestMapping("/add")
	public void add(TagInfo tInfo){
		tagManager.addTag(tInfo);
	}
	
	@RequestMapping("/getHotTag")
	public void getHotTag(){
		
	}
	
	@RequestMapping("/addPicture.do")
	public void addPicture(HttpServletRequest request,HttpServletResponse response){
		System.out.println("/tag/addPicture");
		
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(multipartResolver.isMultipart(request)){
			
			MultipartHttpServletRequest  multiRequest = (MultipartHttpServletRequest)request;
			
			//System.out.println(user);
			Iterator<String>  iter = multiRequest.getFileNames();
			//String name = (String)multiRequest.getAttribute("name");
			//name = multiRequest.getParameter("name");
			//System.out.println(name);
			while(iter.hasNext()){
				MultipartFile file = multiRequest.getFile((String)iter.next());
				
				if(file != null){
					String tagId = file.getName();
					if(tagId.equals("")||file.getOriginalFilename().equals(""))
						continue;
					String fileName = file.getOriginalFilename();
					System.out.println("File Name:"+file.getOriginalFilename());
					String path = FileManager.getTagPicturePath(Integer.parseInt(tagId), fileName);
					System.out.println("Tag Path: "+path);
					//File localFile = new File(path);
					
					try {
						tagManager.addTagPicture(tagId,fileName);
						FileManager.upload(path, file);
						//file.transferTo(localFile);
//						InputStream stream = file.getInputStream();
//						 FileOutputStream fs=new FileOutputStream(path);
//					     byte[] buffer =new byte[1024*1024];
//					     int bytesum = 0;
//					        int byteread = 0; 
//					        while ((byteread=stream.read(buffer))!=-1)
//					        {
//					           bytesum+=byteread;
//					           fs.write(buffer,0,byteread);
//					           
//					        } 
//					        fs.flush();
//					        fs.close();
//					        stream.close();      
//						
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
	}
	
	@RequestMapping("/addPicture2.do")
	public void addPicture2(HttpServletRequest request,HttpServletResponse response){
		System.out.println("/addPicture2");
		response.setContentType("text/html");  
		try {
			//PrintWriter out = response.getWriter();
			
			final long MAX_SIZE = 1024 * 1024 * 1024;
			DiskFileItemFactory dfif = new DiskFileItemFactory();  
			dfif.setSizeThreshold(4096);
			dfif.setRepository(new File("/home/stark/FileStore"));
			ServletFileUpload sfu = new ServletFileUpload(dfif);  
			 sfu.setSizeMax(MAX_SIZE);  
			 
			 List<?> listFiles = null;  
			 
			 listFiles = sfu.parseRequest(request);  
			 System.out.println("file size:"+listFiles.size());
			 for(int i=0;i<listFiles.size();i++){
				 FileItem fileItem = (FileItem)listFiles.get(i);
				 if(null!=fileItem.getName()){
					 String tagId = fileItem.getName();
					 String fileName = fileItem.getFieldName();
					 String path = FileManager.getTagPicturePath(Integer.parseInt(tagId), fileName);
					 File saveFile = new File(path);
					 System.out.println("File Name:"+fileName);
					 tagManager.addTagPicture(tagId,fileName);
					 fileItem.write(saveFile);
				 }
			 }
			 
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
