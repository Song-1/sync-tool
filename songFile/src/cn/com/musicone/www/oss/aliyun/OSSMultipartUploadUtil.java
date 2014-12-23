/**
 * 
 */
package cn.com.musicone.www.oss.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.BaseMD5Util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

/**
 * @author Administrator
 *
 */
public class OSSMultipartUploadUtil {
//	protected static final Logger logger = LogManager.getLogger(OSSMultipartUploadUtil.class);
//
//	/**
//	 * 大文件上传.<br>
//	 * <pre>
//	 * 	通过Multipart的方式上传一个大文件
//	 * 	要上传文件的大小必须大于一个Part允许的最小大小，即5MB。
//	 * </pre>
//	 * @param bucketName
//	 * @param key
//	 * @param uploadFile
//	 * @param md5Value
//	 * @throws Exception
//	 * @throws InterruptedException
//	 */
//	public static void uploadBigFile(String bucketName,String key, File uploadFile,String md5Value) throws Exception {
//		OSS client = AliyunOSSUtil.getOSSClient();
//		long PART_SIZE = AliyunOSSUtil.PART_SIZE;
//		int partCount = calPartCount(uploadFile,PART_SIZE);
//		if (partCount <= 1) {
//			AliyunOSSUtil.uploadFile(bucketName,key, uploadFile, null, md5Value);
//			throw new IllegalArgumentException("要上传文件的大小必须大于一个Part的字节数："+ PART_SIZE);
//		}
//		String uploadId = initMultipartUpload(client, bucketName, key);
//		ExecutorService pool = Executors.newFixedThreadPool(AliyunOSSUtil.CONCURRENCIES);
//		List<PartETag> eTags = Collections.synchronizedList(new ArrayList<PartETag>());
//		for (int i = 0; i < partCount; i++) {
//			long start = PART_SIZE * i;
//			long curPartSize = PART_SIZE < uploadFile.length() - start ? PART_SIZE : uploadFile.length() - start;
//			UploadPartParam param = new UploadPartParam();
//			param.setBucket(bucketName);
//			param.seteTags(eTags);
//			param.setKey(key);
//			param.setPartId(i + 1);
//			param.setSize(curPartSize);
//			param.setStart(PART_SIZE * i);
//			param.setUploadFile(uploadFile);
//			param.setUploadId(uploadId);
//			pool.execute(new UploadPartThread(param));
//		}
//		pool.shutdown();
//		while (!pool.isTerminated()) {
//			pool.awaitTermination(5, TimeUnit.SECONDS);
//		}
//		if (eTags.size() != partCount) {
//			throw new IllegalStateException("Multipart上传失败，有Part未上传成功。");
//		}
//		completeMultipartUpload(client, bucketName, key, uploadId, eTags);
//	}
//
//	// 根据文件的大小和每个Part的大小计算需要划分的Part个数。
//	private static int calPartCount(File f,long PART_SIZE) {
//		int partCount = (int) (f.length() / PART_SIZE);
//		if (f.length() % PART_SIZE != 0) {
//			partCount++;
//		}
//		return partCount;
//	}
//
//	// 初始化一个Multi-part upload请求。
//	private static String initMultipartUpload(OSSClient client,
//			String bucketName, String key) throws OSSException, ClientException {
//		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
//		InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(initUploadRequest);
//		String uploadId = initResult.getUploadId();
//		return uploadId;
//	}
//
//	// 完成一个multi-part请求。
//	private static void completeMultipartUpload(OSSClient client,
//			String bucketName, String key, String uploadId, List<PartETag> eTags)
//			throws OSSException, ClientException {
//		// 为part按partnumber排序
//		Collections.sort(eTags, new Comparator<PartETag>() {
//			public int compare(PartETag arg0, PartETag arg1) {
//				PartETag part1 = arg0;
//				PartETag part2 = arg1;
//				return part1.getPartNumber() - part2.getPartNumber();
//			}
//		});
//		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, eTags);
//		client.completeMultipartUpload(completeMultipartUploadRequest);
//	}
//	
//	
//
//	protected static class UploadPartThread implements Runnable {
//		private UploadPartParam param;
//		public UploadPartThread(UploadPartParam param) {
//			this.param = param;
//		}
//		
//		public UploadPartRequest createUploadPartRequest(InputStream in){
//			UploadPartRequest uploadPartRequest = new UploadPartRequest();
//			uploadPartRequest.setBucketName(param.getBucket());
//			uploadPartRequest.setKey(param.getKey());
//			uploadPartRequest.setUploadId(param.getUploadId());
//			uploadPartRequest.setInputStream(in);
//			uploadPartRequest.setPartSize(param.getSize());
//			uploadPartRequest.setPartNumber(param.getPartId());
//			return uploadPartRequest;
//		}
//		public void upload(UploadPartRequest uploadPartRequest,OSSClient client,int connects)throws Exception{
//			try{
//				UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
//				param.geteTags().add(uploadPartResult.getPartETag());
//				logger.debug(param + " ::: " + uploadPartResult.getETag());
//				logger.debug(param + " ::: " + uploadPartResult.getPartETag().getETag());
//			}catch(ClientException e){
//				if(connects >= 3 ){
//					throw e;
//				}
//				AliyunOSSUtil.initClient();
//				client = AliyunOSSUtil.getOSSClient();
//				upload(uploadPartRequest, client,connects + 1);
//			}
//		}
//		@Override
//		public void run() {
//			InputStream in = null;
//			OSSClient client = AliyunOSSUtil.getOSSClient();
//			try {
//				in = new FileInputStream(param.getUploadFile());
//				in.skip(param.getStart());
//				String md5 = BaseMD5Util.getMd5ByFile(in, param.getSize());
//				logger.debug(param + " ::: md5 :::  " + md5);
//				UploadPartRequest uploadPartRequest = createUploadPartRequest(in);
//				upload(uploadPartRequest, client, 1);
//			} catch (Exception e) {
//				logger.error("大文件上传失败::" + param);
//				logger.error(e.getMessage(),e);
//			} finally {
//				if (in != null){
//					try {
//						in.close();
//					} catch (Exception e) {
//						logger.error(e.getMessage(),e);
//					}finally{
//						in = null;
//					}
//				}
//			}
//		}
//	}
//	
//	protected static class UploadPartParam{
//		private File uploadFile;
//		private String bucket;
//		private String key;
//		private long start;
//		private long size;
//		private List<PartETag> eTags;
//		private int partId;
//		private String uploadId;
//		public File getUploadFile() {
//			return uploadFile;
//		}
//		public void setUploadFile(File uploadFile) {
//			this.uploadFile = uploadFile;
//		}
//		public String getBucket() {
//			return bucket;
//		}
//		public void setBucket(String bucket) {
//			this.bucket = bucket;
//		}
//		
//		public String getKey() {
//			return key;
//		}
//		public void setKey(String key) {
//			this.key = key;
//		}
//		public long getStart() {
//			return start;
//		}
//		public void setStart(long start) {
//			this.start = start;
//		}
//		public long getSize() {
//			return size;
//		}
//		public void setSize(long size) {
//			this.size = size;
//		}
//		public List<PartETag> geteTags() {
//			return eTags;
//		}
//		public void seteTags(List<PartETag> eTags) {
//			this.eTags = eTags;
//		}
//		public int getPartId() {
//			return partId;
//		}
//		public void setPartId(int partId) {
//			this.partId = partId;
//		}
//		public String getUploadId() {
//			return uploadId;
//		}
//		public void setUploadId(String uploadId) {
//			this.uploadId = uploadId;
//		}
//		@Override
//		public String toString() {
//			return "文件块上传参数 [bucket=" + bucket + ", key=" + key + ", uploadId=" + uploadId + ", partId=" + partId + ", size=" + size + "]";
//		}
//	}
//
}
