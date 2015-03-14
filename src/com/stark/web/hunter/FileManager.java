package com.stark.web.hunter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.baidu.ueditor.PathFormat;
import com.stark.web.define.AppInfo;
import com.stark.web.define.BaseState;
import com.stark.web.define.EnumBase;
import com.stark.web.define.MultiState;
import com.stark.web.define.State;

public class FileManager {

	private static Logger logger = Logger.getLogger(FileManager.class);
	private static OSSClient ossClient;
	private final static String bucketName = "starktest";
	private final static String shareUrl = "http://www.uha.so/";
	//private static String url = "http://192.168.10.107/";
	private final static String url = "http://"+bucketName+".oss-cn-hangzhou.aliyuncs.com/";
	private final static String localPath = "/home/stark/FileStore/";
	private final static String webIcon = "http://www.uha.so/icon/uha.ico";
	
	private final static String accessKeyId = "mjTkL5di0yWjHbnU";
	private final static String accessKeySecret = "7X6ievrxmiTWiXbxl3fH03VTUnuNnh";
	private final static String endPoint  ="http://oss.aliyuncs.com";
	
	public final static String articlePictureStore = "Article/images/";
	public final static String petPictureStore = "Pet/images/";
	public final static String userPictureStore = "User/images/";
	public final static String activityPictureStore = "Activity/images/";
	public final static String tagPictureStore = "Tag/images/";
	public final static String chartletPictureStore = "Chartlet/images/";
	
	public final static String userPictureUrl = url+"User/images/";
	public final static String articlePictureUrl = url+ "Article/images/";
	public final static String petPictureUrl = url+"Pet/images/";
	public final static String tagPictureUrl = url+"Tag/images/";
	public final static String activityPictureUrl = url+"Activity/images/";
	public final static String chartletPictureUrl = url+"Chartlet/images/";
	
	private String dir = null;
	private String rootPath = null;
	private String[] allowFiles = null;
	private int count = 0;
	
	public FileManager ( Map<String, Object> conf ) {

		this.rootPath = (String)conf.get( "rootPath" );
		this.dir = this.rootPath + (String)conf.get( "dir" );
		this.allowFiles = this.getAllowFiles( conf.get("allowFiles") );
		this.count = (Integer)conf.get( "count" );
		
	}
	
	public State listFile ( int index ) {
		
		File dir = new File( this.dir );
		State state = null;

		if ( !dir.exists() ) {
			return new BaseState( false, AppInfo.NOT_EXIST );
		}
		
		if ( !dir.isDirectory() ) {
			return new BaseState( false, AppInfo.NOT_DIRECTORY );
		}
		
		Collection<File> list = FileUtils.listFiles( dir, this.allowFiles, true );
		
		if ( index < 0 || index > list.size() ) {
			state = new MultiState( true );
		} else {
			Object[] fileList = Arrays.copyOfRange( list.toArray(), index, index + this.count );
			state = this.getState( fileList );
		}
		
		state.putInfo( "start", index );
		state.putInfo( "total", list.size() );
		
		return state;
		
	}
	
	private State getState ( Object[] files ) {
		
		MultiState state = new MultiState( true );
		BaseState fileState = null;
		
		File file = null;
		
		for ( Object obj : files ) {
			if ( obj == null ) {
				break;
			}
			file = (File)obj;
			fileState = new BaseState( true );
			fileState.putInfo( "url", PathFormat.format( this.getPath( file ) ) );
			state.addState( fileState );
		}
		
		return state;
		
	}
	
	private String getPath ( File file ) {
		
		String path = file.getAbsolutePath();
		
		return path.replace( this.rootPath, "/" );
		
	}
	
	private String[] getAllowFiles ( Object fileExt ) {
		
		String[] exts = null;
		String ext = null;
		
		if ( fileExt == null ) {
			return new String[ 0 ];
		}
		
		exts = (String[])fileExt;
		
		for ( int i = 0, len = exts.length; i < len; i++ ) {
			
			ext = exts[ i ];
			exts[ i ] = ext.replace( ".", "" );
			
		}
		
		return exts;
		
	}
	
	public static String getWebIcon(){
		return webIcon;
	}
	
	public static String getOssUrl(){
		return url;
	}
	
	public static String getShareUrl(int articleId){
		return shareUrl+"StarkPet/article/outsideShare.do?articleId="+articleId;
	}
	
	public static String getChartletPicturePath(int chartletId, String fileName) {
		String path = chartletPictureStore+chartletId+"/"+  fileName;
		
		return path;
	}
	
	public static String getChartletPictureUrl(int chartletId,String fileName){
		return chartletPictureUrl + chartletId + "/" + fileName;
	}
	
	public static String getPetPictureUrl(int petId, String headPic) {
		
		return petPictureUrl + petId + "/" + headPic;
	}
	
	public static String getPetDefaultPictureUrl() {
		
		return petPictureUrl + "/default/pet.png";
	}

	public static String getActivityPictureUrl(int activityId, String bannerPic) {
		return activityPictureUrl+activityId+"/"+bannerPic;
	}
	
	public static String getActivityPicturePath(int activityId, String fileName) {
		String path = activityPictureStore+activityId+"/"+  fileName;
		
		return path;
	}
	
	public static String getArticlePicturePath(int id, String fileName){
		String path = articlePictureStore+id+"/"+  fileName;
		
		return path;
	}
	
	public static String getUserPicturePath(int userId,String fileName){
		String path = userPictureStore+userId+"/"+  fileName;
		return path;
	}
	
	public static String getUserPictureUrl(int userId, String headPic) {
		return userPictureUrl+userId+"/"+headPic;
	}
	
	public static String getArticlePictureUrl(int articleId, String picture) {
		return articlePictureUrl+articleId+"/"+picture;
	}
	
	public static String getPetPicturePath(String id, String fileName) {
		String path = petPictureStore+id+"/"+  fileName;
		
		return path;
	}
	
	public static String getTagPicturePath(int id,String fileName){
		String path = tagPictureStore+id+"/"+  fileName;

		return path;
	}
	
	public static String getTagPictureUrl(int tagId,String fileName){
		
		return tagPictureUrl + tagId + "/" + fileName;
	}

	public static String getUserDefaultPicture(int sex) {
		
		if(sex == EnumBase.Sex.male.getIndex())
			return userPictureUrl+"default/male.png";
		else 
			return userPictureUrl+"default/female.png";
	}

	private static OSSClient getClient() {
		if(ossClient==null){
			ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
		}
		
		return ossClient;
	}
	
	public static String putObject(String bucketName,String key,InputStream input,long length){
		PutObjectResult result = null;
		try{
			OSSClient client = getClient();
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(length);
			result = client.putObject(bucketName, key, input, meta);
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		finally{
			if(input!=null){
				try {
					input.close();
					input =null;
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				
			}
		}
		return result.getETag();
	}

	public static boolean upload(String path,MultipartFile file){
		
		try {
			//uploadLocal(path,file.getInputStream());
			uploadoss(path,file.getInputStream(),file.getSize());
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void uploadLocal(String fileName,InputStream input){
		fileName = localPath+fileName;
		File dir = new File(fileName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		dir.delete();
		FileOutputStream fs = null;
		try {
			fs= new FileOutputStream(fileName);
			byte[] buffer =new byte[1024*1024];
	        int byteread = 0; 
	        while ((byteread=input.read(buffer))!=-1)
	        {
	           fs.write(buffer,0,byteread);
	           
	        } 
	        fs.flush();
	       
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			 try {
				 if(fs!=null){
					 fs.close();
					 fs=null;
				 }
				 if(input!=null){
					 input.close();
					 input=null;
				 }
				
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		     
		}
	        
	}
	
	public static String uploadoss(String fileName,InputStream input,long length){
		
		return putObject(bucketName, fileName, input, length);
	}
	
	public static void upload(String path, URL uri) {
		try {
			//uploadLocal(path, uri.openStream());
			uploadoss(path,uri.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void uploadoss(String path, InputStream input) {
		OSSClient client = getClient();
		InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, path);
		InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);
		final int partSize = 1024 * 1024 * 1;
		File partFile = new File(localPath+path);
		//File tmpDir = FileUtils.getTempDirectory();
		//String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		//File partFile = new File(tmpDir,tmpFileName);
		System.out.println(partFile.length());
		int partCount = (int) (partFile.length() / partSize);
		if (partFile.length() % partSize != 0){
		    partCount++;
		}
		List<PartETag> partETags = new ArrayList<PartETag>();
		try {
			
			for(int i = 0; i < partCount; i++){
				 // 跳到每个分块的开头
			    long skipBytes = partSize * i;
			    input.skip(skipBytes);

			    // 计算每个分块的大小
			    long size = partSize < partFile.length() - skipBytes ?
			            partSize : partFile.length() - skipBytes;

			    // 创建UploadPartRequest，上传分块
			    UploadPartRequest uploadPartRequest = new UploadPartRequest();
			    uploadPartRequest.setBucketName(bucketName);
			    uploadPartRequest.setKey(path);
			    uploadPartRequest.setUploadId(initiateMultipartUploadResult.getUploadId());
			    uploadPartRequest.setInputStream(input);
			    uploadPartRequest.setPartSize(size);
			    uploadPartRequest.setPartNumber(i + 1);
			    UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);

			    // 将返回的PartETag保存到List中。
			    partETags.add(uploadPartResult.getPartETag());
			}
			
			CompleteMultipartUploadRequest completeMultipartUploadRequest =
			        new CompleteMultipartUploadRequest(bucketName, path, initiateMultipartUploadResult.getUploadId(), partETags);

			// 完成分块上传
			CompleteMultipartUploadResult completeMultipartUploadResult =
			        client.completeMultipartUpload(completeMultipartUploadRequest);
			
			// 打印Object的ETag
			logger.info("ETag:"+completeMultipartUploadResult.getETag());
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			// 关闭文件
			try {
				if(input!=null){
					input.close();
					input=null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void multipartUpload(String path,MultipartFile file){
		OSSClient client = getClient();
		InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, path);
		InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);
		
		// 设置每块为 5M
		final int partSize = 1024 * 1024 * 5;

		File partFile = new File("/path/to/file.zip");

		// 计算分块数目
		int partCount = (int) (partFile.length() / partSize);
		if (partFile.length() % partSize != 0){
		    partCount++;
		}

		// 新建一个List保存每个分块上传后的ETag和PartNumber
		List<PartETag> partETags = new ArrayList<PartETag>();

		for(int i = 0; i < partCount; i++){
		    // 获取文件流
		    FileInputStream fis;
		    
			try {
				fis = new FileInputStream(partFile);
				 // 跳到每个分块的开头
			    long skipBytes = partSize * i;
			    fis.skip(skipBytes);

			    // 计算每个分块的大小
			    long size = partSize < partFile.length() - skipBytes ?
			            partSize : partFile.length() - skipBytes;

			    // 创建UploadPartRequest，上传分块
			    UploadPartRequest uploadPartRequest = new UploadPartRequest();
			    uploadPartRequest.setBucketName(bucketName);
			    uploadPartRequest.setKey(path);
			    uploadPartRequest.setUploadId(initiateMultipartUploadResult.getUploadId());
			    uploadPartRequest.setInputStream(fis);
			    uploadPartRequest.setPartSize(size);
			    uploadPartRequest.setPartNumber(i + 1);
			    UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);

			    // 将返回的PartETag保存到List中。
			    partETags.add(uploadPartResult.getPartETag());

			    // 关闭文件
			    fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		CompleteMultipartUploadRequest completeMultipartUploadRequest =
		        new CompleteMultipartUploadRequest(bucketName, path, initiateMultipartUploadResult.getUploadId(), partETags);

		// 完成分块上传
		CompleteMultipartUploadResult completeMultipartUploadResult =
		        client.completeMultipartUpload(completeMultipartUploadRequest);

		// 打印Object的ETag
		System.out.println(completeMultipartUploadResult.getETag());

	}
	
	public static String getMd5ByFile(MultipartFile file) {
        String value = null;
       
		try {
			 FileInputStream in = (FileInputStream)file.getInputStream();
			try {
		    	
		        MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.getSize());
		        MessageDigest md5 = MessageDigest.getInstance("MD5");
		        md5.update(byteBuffer);
		        BigInteger bi = new BigInteger(1, md5.digest());
		        value = bi.toString(16);
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		       if(null != in) {
		    	   try {
		           in.close();
		           } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    
		return value;
    }

	public static void delete(String path) {
		//System.out.println("delete: "+path);
		File file = new File(localPath+path);
		if(file.exists()){
			file.delete();
		}
	}

	public static void saveImage(BufferedImage bi,String imageFormat, String fileName) {
		fileName = localPath+fileName;
		File dir = new File(fileName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		dir.delete();
		
		//GifEncoder encoder = new GifEncoder(bi,new FileOutputStream(file));
		//encoder.encode();  
		FileOutputStream fs = null;
		try {
			 fs= new FileOutputStream(fileName);
			ImageIO.write(bi, imageFormat, fs);   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(fs!=null){
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fs = null;
			}
		}
	}

	public static void upload(String path, ByteArrayOutputStream baos) {
		//InputStream localIS = new ByteArrayInputStream(baos.toByteArray());
		InputStream ossIS = new ByteArrayInputStream(baos.toByteArray());
		//uploadLocal(path, localIS);
		uploadoss(path,ossIS,baos.size());
	}

	public static void uploadoss2(String path, InputStream input) {
		OSSClient client = getClient();
		InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, path);
		InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);
		final int partSize = 1024 * 1024 * 1;
		//File partFile = new File(localPath+path);
		//File tmpDir = FileUtils.getTempDirectory();
		//String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		//File partFile = new File(tmpDir,tmpFileName);
		//System.out.println(partFile.length());
		//int partCount = (int) (partFile.length() / partSize);
		//if (partFile.length() % partSize != 0){
		//    partCount++;
		//}
		List<PartETag> partETags = new ArrayList<PartETag>();
		try {
			int i=0;
			long skip = partSize;
			System.out.println(partSize);
			while(true){
				// FileInputStream fis = new FileInputStream(partFile);
			//for(int i = 0; i < partCount; i++){
				 // 跳到每个分块的开头
				 long skipBytes = partSize * i;
				 skip = input.skip(skipBytes);
				 long size = skip==0?partSize:skip;
			    // 计算每个分块的大小
			  //  long size = partSize < partFile.length() - skipBytes ?
			   //         partSize : partFile.length() - skipBytes;
			    
			    // 创建UploadPartRequest，上传分块
			    UploadPartRequest uploadPartRequest = new UploadPartRequest();
			    uploadPartRequest.setBucketName(bucketName);
			    uploadPartRequest.setKey(path);
			    uploadPartRequest.setUploadId(initiateMultipartUploadResult.getUploadId());
			    uploadPartRequest.setInputStream(input);
			    uploadPartRequest.setPartSize(size);
			    uploadPartRequest.setPartNumber(i + 1);
			    System.out.println(skip +"===="+partSize);
			    UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);

			    // 将返回的PartETag保存到List中。
			    partETags.add(uploadPartResult.getPartETag());
			    
			    if(skip!=0&&skip<partSize){
			    	break;
			    }
			    System.out.println("Size: "+skip);
			    i++;
			//}
			}
			CompleteMultipartUploadRequest completeMultipartUploadRequest =
			        new CompleteMultipartUploadRequest(bucketName, path, initiateMultipartUploadResult.getUploadId(), partETags);

			// 完成分块上传
			CompleteMultipartUploadResult completeMultipartUploadResult =
			        client.completeMultipartUpload(completeMultipartUploadRequest);
			
			// 打印Object的ETag
			logger.info("ETag:"+completeMultipartUploadResult.getETag());
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(input!=null){
				try {
					input.close();
					input=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}
