package cn.com.musicone.www.oss.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.FileUtils;
import cn.com.musicone.www.base.utils.StringUtil;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
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
	protected static final Logger logger = LogManager.getLogger(AliyunOSSUtil.class);
	protected static OSS client = null;
	protected static String ACCESS_ID = null;
	protected static String ACCESS_KEY = null;
	protected static String OSS_ENDPOINT = null;
	protected static String OSS_BUCKET = "cherrytime";
	public static long PART_SIZE = 512 * 1024L; // 每个Part的大小，最小为1MB
	public static int CONCURRENCIES = 20; // 上传Part的并发线程数。
	public static int RE_CONNECT_COUNTS = 3;

	protected static boolean isInitFlag = false;

	public static void init() throws Exception {
		Properties p = FileUtils.loadProperties("aliyun_oss_config.properties");
		if (p != null) {
			ACCESS_ID = p.getProperty("aliyun.oss.accesskeyId");
			ACCESS_KEY = p.getProperty("aliyun.oss.accesskeySecret");
			OSS_ENDPOINT = p.getProperty("aliyun.oss.host");
			OSS_BUCKET = p.getProperty("aliyun.oss.bucket");
			String partSizeStr =  p.getProperty("aliyun.oss.multipart.upload.partsize");
			String concurrencies =  p.getProperty("aliyun.oss.multipart.upload.concurrencies");
			String re_connect_counts =  p.getProperty("aliyun.oss.client.exception.re.connect.counts");
			long partSize = StringUtil.formateLongStr(partSizeStr);
			int threads = StringUtil.formateIntStr(concurrencies);
			int reConnectCounts = StringUtil.formateIntStr(re_connect_counts);
			PART_SIZE = partSize == 0L ? PART_SIZE : partSize;
			CONCURRENCIES = threads == 0 ? CONCURRENCIES : threads;
			RE_CONNECT_COUNTS = reConnectCounts == 0 ? RE_CONNECT_COUNTS : reConnectCounts;
			initClient();
		}
	}
	
	protected static void initClient(){
		ClientConfiguration config = new ClientConfiguration();
		config.setSocketTimeout(30000);
		config.setMaxErrorRetry(3);
		config.setConnectionTimeout(30000);
		config.setMaxConnections(100); ///http最大连接数据
		client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY,config);
		isInitFlag = true;
	}
	
	public static OSS getOSSClient(){
		if(!isInitFlag){
			initClient();
		}
		return client;
	}
	
	public static String getDeafultBucket(){
		return OSS_BUCKET;
	}
	public static boolean isExistObject(String bucket, String key)throws ClientException,OSSException{
		return isExistObject(bucket, key, 0);
	}
	/**
	 * 判断阿里云服务器指定bucket下面是否存在此key值的文件 .<br>
	 * 
	 * @param bucket
	 * @param key
	 * @return
	 */
	public static boolean isExistObject(String bucket, String key,int times)throws ClientException,OSSException{
		if (!isInitFlag) {
			initClient();
		}
		boolean flag = true;
		try {
			getOSSClient().getObject(bucket, key);
		} catch (OSSException e) {
			String errorCode = e.getErrorCode();
			if ("NoSuchKey".equalsIgnoreCase(errorCode)) {
				flag = false;
				return flag;
			}
			throw e;
		} catch (ClientException e) {
			if(times > RE_CONNECT_COUNTS){
				throw e;
			}
			isExistObject(bucket, key,times + 1);
		}
		return flag;
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
	public static PutObjectResult uploadFile(String bucket,String key, File file,
			String contentType, String md5Value) throws IOException,FileNotFoundException,OSSException,ClientException,Exception {
		if (file == null) {
			return null;
		}
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			return putObject(bucket,key, input, file.length(), contentType, md5Value);
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
	public static PutObjectResult putObject(String bucket,String key, InputStream input,
			long size, String contentType, String md5Value)
			throws Exception {
		if (!isInitFlag) {
			initClient();
		}
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(size);
		if(StringUtil.isNotBlank(contentType)){
			objectMeta.setContentType(contentType);
		}
		objectMeta.addUserMetadata("MD5", md5Value);
		PutObjectResult result = getOSSClient().putObject(bucket, key, input, objectMeta);
		return result;
	}

	// // test main function
	public static void main(String[] args) throws Exception {
//		if (!isInitFlag) {
//			init();
//		}
//		System.out.println(ACCESS_ID);
//		System.out.println(ACCESS_KEY);
//		System.out.println(OSS_ENDPOINT);
//		System.out.println(OSS_BUCKET);
		System.out.println(PART_SIZE);
	}

}
