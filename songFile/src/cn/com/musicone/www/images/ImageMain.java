/**
 * 
 */
package cn.com.musicone.www.images;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.StringHolder;

import cn.com.musicone.www.base.utils.BaseMD5Util;
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
public class ImageMain {
	protected static final Logger logger = LogManager.getLogger(ImageMain.class);
	public static ImageService imageService = new ImageServiceImpl();
	
	//// test
	public static void main(String[] args) {
		try {
			AliyunOSSUtil.init();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		MybatisUtil.init();
		start();
	}
	
	public static void start(){
		List<ImageModel> lists = null;
		//// 专辑图片
		lists = imageService.queryAlbumImages();
		todoImage(lists);
		//// 歌手图片
		lists = imageService.querySingerImages();
		todoImage(lists);
		//// banner图片
		lists = imageService.queryBannerImages();
		todoImage(lists);
		//// 书籍图片
		lists = imageService.queryBookImages();
		todoImage(lists);
		//// 歌单环境标签 图片
		lists = imageService.queryEnvironmentImages();
		todoImage(lists);
		//// 歌单 图片
		lists = imageService.querySonglistImages();
		todoImage(lists);
		//// 专辑风格图片
		lists = imageService.queryStyleImages();
		todoImage(lists);
	}
	
	/**
	 * 处理查询出来的图片数据集
	 * @param lists
	 */
	public static void todoImage(List<ImageModel> lists){
		if(lists == null){
			return ;
		}
		for (ImageModel imageModel : lists) {
			if(imageModel == null){
				continue;
			}
			if(StringUtils.isBlank(imageModel.getImg()) && StringUtils.isBlank(imageModel.getIcon())){
				continue;
			}
			List<ImageModel> result = copyImage(imageModel);
			if(result == null){
				continue;
			}
			for (ImageModel data : result) {
				try {
					imageService.save(data);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					continue;
				}
			}
		}
	}
	
	public static List<ImageModel> copyImage(ImageModel img){
		List<ImageModel> result = null;
		if(img == null){
			return result;
		}
		String bucket = AliyunOSSUtil.OSS_BUCKET;
		String key = img.getImg();
		ImageModel data = copyImage(bucket, key);
		if(data != null){
			if(result == null){
				result = new ArrayList<ImageModel>();
			}
			result.add(data);
		}
		key = img.getIcon();
		data = copyImage(bucket, key);
		if(data != null){
			if(result == null){
				result = new ArrayList<ImageModel>();
			}
			result.add(data);
		}
		return result;
	}
	
	public static ImageModel copyImage(String bucket,String key) {
		if(StringUtils.isBlank(key)){
			return null;
		}
		ImageModel data = new ImageModel();
		data.setImg(key);
		if(AliyunOSSUtil.isExistObject(AliyunOSSUtil.OSS_IMG_BUCKET, key)){
			data.setStatus(5);
			return data;
		}
		String message = "获取阿里云的图片[ bucket = "+ bucket +" ,key = " + key + " ] ";
		OSSObject obj = AliyunOSSUtil.getOSSObject(bucket, key);
		if(obj == null){
			logger.debug(message + "   失败 ");
			data.setStatus(6);
		}else{
			logger.debug(message + "   OK ");
			ObjectMetadata meta = obj.getObjectMetadata();
			long size = meta.getContentLength();
			data.setSize(size);
			////
			InputStream objectContent = obj.getObjectContent();
			String md5 = null;
			try {
				md5 = BaseMD5Util.getMd5ByFile(objectContent, size);
			} catch (Exception e) {
				logger.debug(message + " MD5  失败");
				e.printStackTrace();
			}
			try {
				objectContent.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			data.setMd5(md5);
			//// copy image to target bucket
			StringHolder strh = new StringHolder();
			boolean flag = AliyunOSSUtil.copyFile(bucket, key, AliyunOSSUtil.OSS_IMG_BUCKET, key, strh);
			if(!flag){
				logger.debug(message + " 复制  失败");
				data.setStatus(7);
			}else{
				logger.debug(message + " 复制  OK");
			}
		}
		return data;
	}

}
