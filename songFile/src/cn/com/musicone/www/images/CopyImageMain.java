/**
 * 
 */
package cn.com.musicone.www.images;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.BaseMD5Util;
import cn.com.musicone.www.base.utils.HttpClientUtil;
import cn.com.musicone.www.base.utils.HttpResponseData;
import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.images.model.ImageModel;
import cn.com.musicone.www.images.service.ImageService;
import cn.com.musicone.www.images.service.impl.ImageServiceImpl;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;

import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

/**
 * @author Administrator
 *
 */
public class CopyImageMain {
	protected static final Logger logger = LogManager
			.getLogger(CopyImageMain.class);
	public static ImageService imageService = new ImageServiceImpl();
	private static String bucket = AliyunOSSUtil.OSS_BUCKET;
	private static String img_bucket = "songimage";
	private static String host = "http://songimage.oss-cn-hangzhou.aliyuncs.com";

	// // main test
	public static void main(String[] args) throws Exception {
		AliyunOSSUtil.init();
		MybatisUtil.init();
		start();
	}
	
	public static boolean validateKey(String key){
		if(StringUtils.isBlank(key)){
			return false;
		}
		String[] keys = ImageMain.keys;
		for (String str : keys) {
			if(StringUtils.isBlank(str)){
				continue;
			}
			if (key.startsWith(str)) {
				return true;
			}
		}
		return false;
	}

	public static void start() {
		List<ImageModel> lists = null;
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lists = imageService.queryImages();
			todoImage(lists);
		}
	}

	/**
	 * 处理查询出来的图片数据集
	 * 
	 * @param lists
	 */
	private static void todoImage(List<ImageModel> lists) {
		if (lists == null) {
			logger.debug("待处理图片数据集为空");
			return;
		}
		for (ImageModel imageModel : lists) {
			if (imageModel == null) {
				continue;
			}
			if (StringUtils.isBlank(imageModel.getImg())) {
				continue;
			}
			ImageModel result = copyImage(imageModel);
			if (result == null) {
				continue;
			}
			try {
				imageService.updateImage(result);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private static ImageModel copyImage(ImageModel data) {
		if (data == null) {
			return null;
		}
		String message = "";
		String key = data.getImg();
		if(StringUtils.isBlank(key)){
			return data;
		}
		if(!validateKey(key)){
			return null;
		}
		if (validateImg(key)) {
			message = "获取阿里云的图片[ bucket = " + img_bucket + " ,key = " + key
					+ " ] ";
			logger.debug(message + "   存在  ");
			data.setStatus(5);
		} else {
			message = "获取阿里云的图片[ bucket = " + bucket + " ,key = " + key + " ] ";
			OSSObject obj = AliyunOSSUtil.getOSSObject(bucket, key);
			if (obj == null) {
				logger.debug(message + "   失败 ");
				data.setStatus(6);
			} else {
				logger.debug(message + "   OK ");
				ObjectMetadata meta = obj.getObjectMetadata();
				long size = meta.getContentLength();
				InputStream objectContent = obj.getObjectContent();
				String md5 = null;
				try {
					md5 = BaseMD5Util.getMd5ByFile(objectContent, size);
				} catch (Exception e) {
					logger.debug(message + " MD5  失败");
					logger.error(e.getMessage(), e);
				}
				try {
					objectContent.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				} finally {
					objectContent = null;
				}
				data.setMd5(md5);
				String contentType = "image/jpeg";
				String str = key.trim().toLowerCase();
				if (str.endsWith(".jpg")) {
					contentType = "image/jpeg";
				}else if(str.endsWith(".png")){
					contentType = "image/png";
				}
				boolean flag = AliyunOSSUtil.copyFile(bucket, key, img_bucket,
						key,contentType,md5);
				if (flag) {
					logger.debug(message + " 复制  OK");
					data.setStatus(0);
				} else {
					logger.debug(message + " 复制  失败");
					data.setStatus(7);
				}
			}
		}
		return data;
	}

	private static boolean validateImg(String key) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		String url = host + "/" + StringUtil.encodeStr(key, null);
		HttpResponseData result = HttpClientUtil.doGet(url);
		if (result == null) {
			return false;
		}
		if (HttpStatus.SC_OK == result.getCode()) {
			return true;
		}
		return false;
	}

}
