package com.stark.web.upload;

import com.baidu.ueditor.PathFormat;
import com.stark.web.define.AppInfo;
import com.stark.web.define.BaseState;
import com.stark.web.define.FileType;
import com.stark.web.define.State;
import com.stark.web.hunter.FileManager;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class BinaryUploader {

	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		FileItemStream fileStream = null;
		
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			FileItemIterator iterator = upload.getItemIterator(request);
			
			while (iterator.hasNext()) {
				fileStream = iterator.next();

				if (!fileStream.isFormField())
					break;
				fileStream = null;
			}

			if (fileStream == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			String originFileName = fileStream.getName();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			String physicalPath = (String) conf.get("rootPath") + savePath;

			InputStream is = fileStream.openStream();
			State storageState = StorageManager.saveFileByInputStream(is,
					physicalPath, maxSize);
			is.close();

			if (storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
	
	public static final State saveOSS2(HttpServletRequest request, Map<String, Object> conf){
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(multipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while(iter.hasNext()){
				MultipartFile file = multiRequest.getFile((String) iter.next());
				String name = file.getName();
				if (file != null&&!"".equals(name)) {
					String fileName = file.getOriginalFilename();
					if(fileName.equals(""))
						continue;
					String path ="html/images"+fileName;
					try {
						//long size = file.getSize();
						FileManager.upload(path, file);
						// file.transferTo(localFile);
						State state = new BaseState(true);
						state.putInfo("url", "");
						return state;
					} catch (IllegalStateException e) {
						e.printStackTrace();
						//out.print("<script language='javascript'>parent.callback('发布推文失败');</script>");
						//out.flush();
						//out.close();
						return null;
						
					}
				} 
			}
		}
		return null;
	}

	public static final State saveOSS(HttpServletRequest request, Map<String, Object> conf){
		//FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			//FileItemIterator iterator = upload.getItemIterator(request);
			List<FileItem> items = upload.parseRequest(request);
			if(items==null){
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}
			FileItem fileItem = null;
			for(int i=0;i<items.size();i++){
				fileItem = items.get(i);
				if(!fileItem.isFormField())
					break;
				fileItem = null;
			}
			
			String savePath = (String) conf.get("savePath");
			//String originFileName = fileStream.getName();
			String originFileName = fileItem.getName();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			//long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);
			//InputStream is = fileStream.openStream();
			InputStream is = fileItem.getInputStream();
			//State storageState = StorageManager.saveFileByInputStream(is,	physicalPath, maxSize);
			State storageState = new BaseState(true);
			//FileManager.uploadoss2(savePath, is);
			FileManager.uploadoss(savePath, is, fileItem.getSize());
			//is.close();
			
			if (storageState.isSuccess()) {
				String imgUrl = FileManager.getOssUrl();
				storageState.putInfo("url", imgUrl+savePath);
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}
}
