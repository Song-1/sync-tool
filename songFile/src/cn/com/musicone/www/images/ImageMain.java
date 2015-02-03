/**
 * 
 */
package cn.com.musicone.www.images;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.HttpClientUtil;
import cn.com.musicone.www.base.utils.HttpResponseData;
import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.images.model.ImageModel;
import cn.com.musicone.www.images.service.ImageService;
import cn.com.musicone.www.images.service.impl.ImageServiceImpl;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;

import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

/**
 * @author Administrator
 *
 */
public class ImageMain {
	protected static final Logger logger = LogManager
			.getLogger(ImageMain.class);
	private static String img_bucket = "songimage";
	private static String bucket = "cherrytime";
	private static String host = "http://songimage.oss-cn-hangzhou.aliyuncs.com";
	public static ImageService imageService = new ImageServiceImpl();
	public static String[] keys = { "201501/", "Cherry Time/", "E:/", "H:/" };

	// public static String[] keys =
	// {"bigfile_upload_test/","book/","cherrytime_special_recommend/","cherrytimemusic/"};
	// public static String[] keys =
	// {"cherrytime乐库/","editor/","enjoyCD/","firmware/","makesound/","radio/"};
	// public static String[] keys =
	// {"software/","songs/","test/","享CD/","学外语/","童趣乐园/"};
	// public static String[] keys = {"icon/","images/","imgs/","singer/"};

	// // test
	public static void main(String[] args) {
		try {
			AliyunOSSUtil.init();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		MybatisUtil.init();
		// 获取oss上的图片
		start();
	}

	public static void start() {
		for (String key : keys) {
			listOssDatas(bucket, key, "");
		}
	}

	// public static void listOssDatas(String bucket, List<String> datas) {
	// if (datas == null) {
	// return;
	// }
	// for (String prefix : datas) {
	// List<String> prefixs = listOssDatas(bucket, prefix);
	// listOssDatas(bucket, prefixs);
	// }
	// }
	//
	// public static List<String> listOssDatas(String bucket, String prefix) {
	// List<String> commonPrefixs = new ArrayList<String>();
	// listOssDatas(bucket, prefix, "", commonPrefixs);
	// return commonPrefixs;
	// }

	public static void listOssDatas(String bucket, String prefix, String marker) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucket);
		// "/" 为文件夹的分隔符
		listObjectsRequest.setDelimiter("/");
		// 列出目录下的所有文件和文件夹
		listObjectsRequest.setPrefix(prefix);
		listObjectsRequest.setMarker(marker);
		listObjectsRequest.setMaxKeys(500);
		try {
			logger.debug("开始遍历文件夹:::" + prefix + " marker ::: " + marker);
			long start = System.currentTimeMillis();
			ObjectListing listing = AliyunOSSUtil.getOSSClient().listObjects(
					listObjectsRequest);
			long end = System.currentTimeMillis();
			System.out.println("list datas cost times :::: " + (end - start)
					+ " ms ");
			List<OSSObjectSummary> objectSummarys = listing
					.getObjectSummaries();
			if (objectSummarys != null) {
				for (OSSObjectSummary objectSummary : objectSummarys) {
					String key = objectSummary.getKey();
					logger.info("文件 :::: " + key);
					boolean flag = validateIsImg(key);
					if (!flag) {
						continue;
					}
					ImageModel data = new ImageModel();
					data.setImg(key);
					data.setSize(objectSummary.getSize());
					// data.setRemark(objectSummary.getETag());
					data.setMd5(objectSummary.getETag());
					copyImage(data);
				}
			}
			List<String> datas = listing.getCommonPrefixes();
			if (datas != null && !datas.isEmpty()) {
				for (String str : datas) {
					listOssDatas(bucket, str, "");
				}
			}
			marker = listing.getNextMarker();
			if (StringUtils.isNotBlank(marker) && listing.isTruncated()) {
				listOssDatas(bucket, prefix, marker);
			}
		} catch (Exception e) {
			logger.error("获取[bucket = " + bucket + " , prefix = " + prefix
					+ " ]    失败  ");
			logger.error(e.getMessage(), e);
			ImageModel data = new ImageModel();
			data.setImg(prefix);
			data.setStatus(8);
			try {
				imageService.save(data);
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
	}

	private static void copyImage(ImageModel data) {
		long start = System.currentTimeMillis();
		try {
			if (data == null) {
				return;
			}
			String key = data.getImg();
			if (StringUtils.isBlank(key)) {
				return;
			}
			if (validateImg(key)) {
				data.setStatus(5);
				imageService.save(data);
				return;
			}
			String md5 = null;
			String contentType = "image/jpeg";
			String str = key.trim().toLowerCase();
			if (str.endsWith(".jpg")) {
				contentType = "image/jpeg";
			} else if (str.endsWith(".png")) {
				contentType = "image/png";
			}
			String message = "复制 key ::: " + key + "  ";
			// long startO = System.currentTimeMillis();
			// OSSObject obj = AliyunOSSUtil.getOSSObject(bucket, key);
			// long endO = System.currentTimeMillis();
			// System.out.println("get oss  object cost times ::: " + (endO -
			// startO) + " ms ");
			// if (obj != null) {
			// ObjectMetadata meta = obj.getObjectMetadata();
			// long size = meta.getContentLength();
			// InputStream objectContent = obj.getObjectContent();
			// long startmd5 = System.currentTimeMillis();
			// try {
			// if( objectContent != null){
			// md5 = BaseMD5Util.getMd5ByFile(objectContent, size);
			// }
			// } catch (Exception e) {
			// logger.debug(message + " 计算MD5  失败");
			// logger.error(e.getMessage(), e);
			// }
			// try {
			// if( objectContent != null){
			// objectContent.close();
			// }
			// } catch (IOException e) {
			// logger.error(e.getMessage(), e);
			// } finally {
			// objectContent = null;
			// }
			// data.setMd5(md5);
			// long endmd5= System.currentTimeMillis();
			// System.out.println("count object md5  value cost times ::: " +
			// (endmd5 - startmd5) + " ms ");
			// }
			boolean flag = AliyunOSSUtil.copyFile(bucket, key, img_bucket, key,
					contentType, md5);
			if (flag) {
				logger.debug(message + " 复制  OK");
				data.setStatus(0);
			} else {
				logger.debug(message + " 复制  失败");
				data.setStatus(7);
			}
			imageService.save(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		long end = System.currentTimeMillis();
		System.out.println("cost times :::::" + (end - start) + " ms ");
	}

	private static boolean validateIsImg(String key) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		String str = key.trim().toLowerCase();
		if (str.endsWith(".jpg")) {
			return true;
		} else if (str.endsWith(".png")) {
			return true;
		}
		return false;
	}

	private static boolean validateImg(String key) {
//		if (StringUtils.isBlank(key)) {
//			return false;
//		}
//		key = StringUtil.encodeStr(key, null);
//		key = StringUtils.replace(key, "%2F", "/");
//		String url = host + "/" + key;
//		HttpResponseData result = HttpClientUtil.doGet(url);
//		if (result == null) {
//			return false;
//		}
//		if (HttpStatus.SC_OK == result.getCode()) {
//			return true;
//		}
		return false;
	}

}
