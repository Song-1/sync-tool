/**
 * 
 */
package cn.com.musicone.www.images;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.StringHolder;

import cn.com.musicone.www.base.utils.HttpClientUtil;
import cn.com.musicone.www.base.utils.HttpResponseData;
import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.images.service.ImageService;
import cn.com.musicone.www.images.service.impl.ImageServiceImpl;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;

import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

/**
 * @author Administrator
 *
 */
public class CopyOssImage {
	protected static final Logger logger = LogManager
			.getLogger(CopyOssImage.class);
	public static ImageService imageService = new ImageServiceImpl();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AliyunOSSUtil.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String bucket = "cherrytime";
		List<String> datas = listOssDatas(bucket,"享CD/");
		listOssDatas(bucket,datas);
//		datas = listOssDatas(bucket,"");
//		listOssDatas(bucket,datas);
	}
	
	public static void listOssDatas(String bucket,List<String> datas){
		if(datas == null){
			return;
		}
		for (String prefix : datas) {
			System.out.println(prefix);
//			 List<String> prefixs = listOssDatas(bucket,prefix);
//			 listOssDatas(bucket, prefixs);
		}
	}
	
	public static List<String> listOssDatas(String bucket,String prefix){
		List<String> commonPrefixs = new ArrayList<String>();
		listOssDatas(bucket, prefix, "", commonPrefixs);
		return commonPrefixs;
	}
	
	public static void listOssDatas(String bucket,String prefix,String marker,List<String> commonPrefixs){
		if(commonPrefixs == null){
			commonPrefixs = new ArrayList<String>();
		}
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucket);
		// "/" 为文件夹的分隔符
		listObjectsRequest.setDelimiter("/");
		// 列出目录下的所有文件和文件夹
		listObjectsRequest.setPrefix(prefix);
		listObjectsRequest.setMarker(marker);
		listObjectsRequest.setMaxKeys(500);
		try{
			ObjectListing listing = AliyunOSSUtil.getOSSClient().listObjects(listObjectsRequest);
//			List<OSSObjectSummary> objectSummarys = listing.getObjectSummaries();
//			if(objectSummarys != null){
//				for (OSSObjectSummary objectSummary : objectSummarys) {
//				    String key = objectSummary.getKey();
//				    logger.info("文件 :::: " + key);
////				    boolean flag = validateIsImg(key);
////				    if(!flag){
////				    	continue;
////				    }
//////				    logger.info("图片 ::: "+key);
////				    flag = validateImg(key);
////				    if(flag){
////				    	logger.debug("复制图片[bucket = " + bucket + " , prefix = " + key + " ]   已存在 " );
////				    	continue;
////				    }
////			    	StringHolder strh = new StringHolder();
////			    	flag = AliyunOSSUtil.copyFile(bucket, key, img_bucket, key, strh);
////			    	if(flag){
////			    		logger.debug("复制图片[bucket = " + bucket + " , prefix = " + key + " ]   成功 " );
////			    	}else{
////			    		logger.error("复制图片[bucket = " + bucket + " , prefix = " + key + " ]   失败  ::  " + strh.value);
////			    	}
//				}
//			}
			List<String> datas = listing.getCommonPrefixes();
			if(datas != null && !datas.isEmpty()){
				commonPrefixs.addAll(datas);
			}
			marker = listing.getNextMarker();
			if(StringUtils.isNotBlank(marker) && listing.isTruncated()){
				listOssDatas(bucket, prefix, marker, commonPrefixs);
			}
		}catch(Exception e){
			logger.error("获取[bucket = " + bucket + " , prefix = " + prefix + " ]    失败  ");
			logger.error(e.getMessage(),e);
		}
	}
	private static boolean validateIsImg(String key){
		if(StringUtils.isBlank(key)){
			return false;
		}
		String str = key.trim().toLowerCase();
		if(str.endsWith(".jpg")){
			return true;
		}
		return false;
	}
	private static String img_bucket = "songimage";
	private static String host = "http://songimage.oss-cn-hangzhou.aliyuncs.com";
	private static boolean validateImg(String key){
		if(StringUtils.isBlank(key)){
			return false;
		}
		String url = host + "/" + StringUtil.encodeStr(key, null);
		HttpResponseData result = HttpClientUtil.doGet(url);
		if(result == null){
			return false;
		}
		if(HttpStatus.SC_OK == result.getCode()){
			return true;
		}
		return false;
	}

}
