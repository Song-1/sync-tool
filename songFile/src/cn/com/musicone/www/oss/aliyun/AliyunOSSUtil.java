package cn.com.musicone.www.oss.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.StringHolder;

import cn.com.musicone.www.base.utils.BaseMD5Util;
import cn.com.musicone.www.base.utils.FileUtils;
import cn.com.musicone.www.base.utils.HttpClientUtil;
import cn.com.musicone.www.base.utils.StringUtil;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * @ClassName: AliyunOSSUtil
 * @Description: 阿里云开放存储服务OSS操作辅助类
 * @author Jelly.Liu
 * @date 2014年11月17日 下午1:49:14
 * 
 */
public class AliyunOSSUtil {
	protected static final Logger logger = LogManager
			.getLogger(AliyunOSSUtil.class);
	protected static OSS client = null;
	protected static String ACCESS_ID = null;
	protected static String ACCESS_KEY = null;
	protected static String OSS_ENDPOINT = null;
	public static String OSS_BUCKET = "cherrytime";
	public static String OSS_IMG_BUCKET = "songimage";
	public static long PART_SIZE = 512 * 1024L; // 每个Part的大小，最小为1MB
	public static int CONCURRENCIES = 20; // 上传Part的并发线程数。
	public static int RE_CONNECT_COUNTS = 3;

	protected static boolean isInitFlag = false;

	/**
	 * 初始化aliyun oss key bucket等属性
	 * @throws Exception
	 */
	public static void init() throws Exception {
		Properties p = FileUtils.loadProperties("aliyun_oss_config.properties");
		if (p != null) {
			ACCESS_ID = p.getProperty("aliyun.oss.accesskeyId");
			ACCESS_KEY = p.getProperty("aliyun.oss.accesskeySecret");
			OSS_ENDPOINT = p.getProperty("aliyun.oss.host");
			OSS_BUCKET = p.getProperty("aliyun.oss.bucket");
			String partSizeStr = p
					.getProperty("aliyun.oss.multipart.upload.partsize");
			String concurrencies = p
					.getProperty("aliyun.oss.multipart.upload.concurrencies");
			String re_connect_counts = p
					.getProperty("aliyun.oss.client.exception.re.connect.counts");
			long partSize = StringUtil.formateLongStr(partSizeStr);
			int threads = StringUtil.formateIntStr(concurrencies);
			int reConnectCounts = StringUtil.formateIntStr(re_connect_counts);
			PART_SIZE = partSize == 0L ? PART_SIZE : partSize;
			CONCURRENCIES = threads == 0 ? CONCURRENCIES : threads;
			RE_CONNECT_COUNTS = reConnectCounts == 0 ? RE_CONNECT_COUNTS
					: reConnectCounts;
			initClient();
		}
	}
	
	public static OSS getOSS() {
		return new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
	}

	public static void initClient() {
		ClientConfiguration config = new ClientConfiguration();
		config.setSocketTimeout(30000);
		config.setMaxErrorRetry(3);
		config.setConnectionTimeout(30000);
		config.setMaxConnections(100); // /http最大连接数据
		client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY, config);
		isInitFlag = true;
	}

	public static OSS getOSSClient() {
		if (!isInitFlag) {
			initClient();
		}
		return client;
	}

	public static String getDeafultBucket() {
		return OSS_BUCKET;
	}

	public static boolean isExistObject(String bucket, String key)
			throws ClientException, OSSException {
		return isExistObject(bucket, key, 0);
	}

	/**
	 * 判断阿里云服务器指定bucket下面是否存在此key值的文件 .<br>
	 * 
	 * @param bucket
	 * @param key
	 * @return
	 */
	public static boolean isExistObject(String bucket, String key, int times)
			throws ClientException, OSSException {
		if (!isInitFlag) {
			initClient();
		}
		String url = getGenerateURL(bucket, key);
		int code = HttpClientUtil.doGet(url, null);
		if(code == 200){
			return true;
		}
		return false;
	}

	
	/**
	 * 判断阿里云服务器指定bucket下面是否存在此key值的文件 .<br>
	 * 
	 * @param bucket
	 * @param key
	 * @return
	 */
	public static boolean isExistObject1(String bucket, String key){
		OSSObject object = null ;
		try {
			OSS ossClient = getOSSClient();
			// 新建GetObjectRequest
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
			// 获取0~100字节范围内的数据
			getObjectRequest.setRange(0, 100);
			// 获取Object，返回结果为OSSObject对象
			object = ossClient.getObject(getObjectRequest);
		} catch (Exception e) {
			return false;
		}
		if (object != null) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * 上传文件到OSS
	 * 
	 * @param key
	 * @param file
	 * @param contentType
	 * @param md5Value
	 * @return PutObjectResult
	 * @throws OSSException
	 * @throws ClientException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static PutObjectResult uploadFile(String bucket, String key,
			File file, String contentType, String md5Value) throws IOException,
			FileNotFoundException, OSSException, ClientException, Exception {
		if (file == null) {
			return null;
		}
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			return putObject(bucket, key, input, file.length(), contentType,
					md5Value);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (OSSException e) {
			throw e;
		} catch (ClientException e) {
			throw e;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw e;
				} finally {
					input = null;
				}
			}
		}
	}

	/**
	 * 实现:上传文件到OSS
	 * 
	 * @param key
	 * @param input
	 * @param size
	 *            文件大小 (小于5G)
	 * @param contentType
	 * @param md5Value
	 * @return PutObjectResult
	 * @throws OSSException
	 * @throws ClientException
	 * 
	 */
	public static PutObjectResult putObject(String bucket, String key,
			InputStream input, long size, String contentType, String md5Value)
			throws Exception {
		if (!isInitFlag) {
			initClient();
		}
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(size);
		if (StringUtil.isNotBlank(contentType)) {
			objectMeta.setContentType(contentType);
		}
		objectMeta.addUserMetadata("MD5", md5Value);
		PutObjectResult result = getOSSClient().putObject(bucket, key, input,
				objectMeta);
		return result;
	}

	/**
	 * 实现复制阿里云文件
	 * 
	 * @param bucket
	 * @param key
	 * @param targetBucket
	 * @param targetKey
	 * @param strh
	 * @return boolean
	 */
	public static boolean copyFile(String bucket, String key,
			String targetBucket, String targetKey, StringHolder strh) {
//		if (StringUtils.isBlank(bucket)) {
//			strh.value = "源bucket为空";
//			return false;
//		}
//		if (StringUtils.isBlank(key)) {
//			strh.value = "源key为空";
//			return false;
//		}
//		if (!isExistObject(bucket, key)) {
//			strh.value = "源文件不存在";
//			return false;
//		}
//		if (StringUtils.isBlank(targetBucket)) {
//			strh.value = "目标bucket为空";
//			return false;
//		}
//		if (StringUtils.isBlank(targetKey)) {
//			strh.value = "目标key为空";
//			return false;
//		}
//		if (isExistObject(targetBucket, targetKey)) {
//			strh.value = "目标文件已经存在";
//			return true;
//		}
		try{
			CopyObjectResult result = getOSSClient().copyObject(bucket, key,
					targetBucket, targetKey);
			strh.value = "复制成功 :: " + result.getETag();
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			strh.value = "复制失败 :: " + e.getMessage();
			return true;
		}
	}
	public static boolean copyFile(String bucket, String key,
			String targetBucket, String targetKey, String contentType,String md5) {
		try{
			CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, key,targetBucket, targetKey);
			// 设置新的Metadata
			ObjectMetadata meta = new ObjectMetadata();
			if(StringUtils.isNotBlank(contentType)){
				meta.setContentType(contentType);
			}
			if(StringUtils.isNotBlank(md5)){
				meta.addUserMetadata("MD5", md5);
			}
			copyObjectRequest.setNewObjectMetadata(meta);
			CopyObjectResult result = getOSSClient().copyObject(copyObjectRequest);
			logger.debug("copy object etag ::: " + result.getETag());
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return true;
		}
	}

	/**
	 * 简单的读取Object
	 * @param bucket
	 * @param key
	 * @return OSSObject
	 */
	public static OSSObject getOSSObject(String bucket, String key){
		return getOSSObject(bucket, key, 0);
	}
	
	/**
	 * 简单的读取Object
	 * @param bucket
	 * @param key
	 * @param times
	 * @return OSSObject
	 */
	public static OSSObject getOSSObject(String bucket, String key,int times) {
		if (StringUtils.isBlank(bucket)) {
			return null;
		}
		if (StringUtils.isBlank(key)) {
			return null;
		}
		try{
			OSSObject object = getOSSClient().getObject(bucket, key);
			return object;
		}catch(OSSException e){
			logger.error(e.getErrorCode(),e);
		}catch(ClientException e){
//			if (times > RE_CONNECT_COUNTS) {
				logger.error(e.getMessage(),e);
//			}else{
//				return getOSSObject(bucket, key, times + 1);
//			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	// // test main function
	public static void main(String[] args) throws Exception {
		init();
		String key = "images/album/201501/26/谨此献给亲爱的邓丽君.jpg";
//		StringHolder strh = new StringHolder();
//		boolean flag = copyFile(OSS_BUCKET, key, OSS_IMG_BUCKET, key, strh);
//		if (flag) {
//			System.out.println("OK " + strh.value);
//		} else {
//			System.out.println("ERROR " + strh.value);
//		}
		OSSObject obj = getOSSObject(OSS_BUCKET, key);
		if(obj == null){
			System.out.println("file not found");
		}else{
			ObjectMetadata meta = obj.getObjectMetadata();
			System.out.println(meta.getUserMetadata());
			long size = meta.getContentLength();
			////
			InputStream objectContent = obj.getObjectContent();
			String md5 = BaseMD5Util.getMd5ByFile(objectContent, size);
			System.out.println(" mdr = " + md5 );
			objectContent.close();
		}
	}
	
	/**
	 * 获取阿里云的签名URL
	 * @param key
	 * @param bucket
	 * @return String
	 */
	public static String getGenerateURL(String bucket,String key){
		long times = 600000;
		return getGenerateURL( bucket,key, times);
	}
	
	/**
	 * 获取阿里云的签名URL
	 * 
	 * @param key
	 * @param bucket
	 * @param times
	 *            超时时间(单位为毫秒)
	 * @return String
	 */
	public static String getGenerateURL(String bucket,String key, long times) {
		String url = null;
		if(StringUtils.isBlank(key) || StringUtils.isBlank(bucket) || times <= 0){
			return url;
		}
		times = new Date().getTime() + times;
		Date expiration = new Date(times);
		OSS client = getOSSClient();
		try{
			URL urls = client.generatePresignedUrl(bucket, key, expiration);
			if(null != urls){
				url = urls.toString();
			}
		}catch(ClientException e){
			logger.error(e.getMessage(),e);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return url;
	}

}
