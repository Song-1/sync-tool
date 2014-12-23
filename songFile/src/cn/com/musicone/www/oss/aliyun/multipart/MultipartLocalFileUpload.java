/**
 * 
 */
package cn.com.musicone.www.oss.aliyun.multipart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.aliyun.OSSBucketException;
import cn.com.musicone.www.oss.aliyun.OSSKeyException;
import cn.com.musicone.www.oss.aliyun.OssValidateUtil;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PartSummary;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

/**
 * @author Administrator
 *
 */
public class MultipartLocalFileUpload {
	protected static final Logger logger = LogManager.getLogger(MultipartLocalFileUpload.class);
	private String bucket;
	private String key;
	private String md5;
	private File file;
	private String message;
	private long partSize = AliyunOSSUtil.PART_SIZE;
	private int CONCURRENCIES = AliyunOSSUtil.CONCURRENCIES;
	protected List<PartETag> eTags = new ArrayList<PartETag>();
	protected String uploadId;
	private int reUploadPartCounts = 3;
	private int reuploadFile = 0;

	public MultipartLocalFileUpload() {
	}

	public MultipartLocalFileUpload(String bucket, String key, String md5,
			File file) {
		this.bucket = bucket;
		this.key = key;
		this.md5 = md5;
		this.file = file;
	}

	/**
	 * 校验本地分块上传对象的参数是否正确,默认不正确false,否则正确true
	 * 
	 * @return boolean
	 */
	public boolean validateParams() {
		boolean flag = false;
		try {
			OssValidateUtil.validateOssBucket(getBucket());
			OssValidateUtil.validateOssKey(getKey());
			if (file != null && file.exists()) {
				flag = true;
			} else {
				message = "本地上传文件不存在";
			}
		} catch (OSSBucketException e) {
			message = e.getMessage();
			logger.error(message);
		} catch (OSSKeyException e) {
			message = e.getMessage();
			logger.error(message);
		}
		return flag;
	}

	public ObjectMetadata createObjectMetadata() {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.length());
		if (StringUtils.isNotBlank(md5)) {
			objectMeta.addUserMetadata("MD5", md5);
		}
		return objectMeta;
	}

	public boolean upload() {
		boolean flag = validateParams();
		if (!flag) {
			return false;
		}
		try {
			flag = AliyunOSSUtil.isExistObject(bucket, key);
			if(flag){
				message = "文件已经存在";
				return true;
			}
			int partCount = (int) (file.length() / partSize);
			if ((file.length() % partSize) != 0) {
				partCount++;
			}
			if (partCount <= 1) {
				message = "文件小于分块大小,采用put上传";
				AliyunOSSUtil.uploadFile(bucket, key, file, null, md5);
				return true;
			}
			// // 获取Oss客户端
			OSS client = AliyunOSSUtil.getOSSClient();
			ObjectMetadata objectMetadata = createObjectMetadata();
			uploadId = MultipartUploadUtil
					.createOrGetMultipartUploadRequest(client, bucket, key,
							objectMetadata, true);
			if (StringUtils.isBlank(uploadId)) {
				message = "获取OSS上传事件的uploadId为空";
				return false;
			}
			uploadPart(partCount, client);
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	public void uploadPart(int partCount,OSS client)throws Exception{
		eTags = new ArrayList<PartETag>();
		List<PartSummary> parts = MultipartUploadUtil.getPartList(client, bucket, key, uploadId);
		ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);
		partsLoop : for (int i = 0; i < partCount; i++) {
			int partNumber = i + 1;
			if(parts != null){
				for(PartSummary part : parts){
					if(part == null){
						continue;
					}
					if(part.getPartNumber() == partNumber){
						PartETag ee = new PartETag(partNumber, part.getETag());
						eTags.add(ee);
						continue partsLoop;
					}
				}
			}
			long startSize = partSize * i;
			long size = file.length() - startSize;
			size = size > partSize ? partSize : size;
			UploadPartThread uploadRunnable = new UploadPartThread(client,partNumber, startSize, size);
			pool.execute(uploadRunnable);
		}
		new Thread(new SystemOutUploadFileThread(partCount)).start();
		pool.shutdown();
		while (!pool.isTerminated()) {
			try {
				pool.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(),e);
			}
		}
		if (eTags.size() != partCount )
        {
           if(reuploadFile <= reUploadPartCounts){
        	   reuploadFile ++;
        	   uploadPart(partCount, client);
           }else{
        	   throw new IllegalStateException("Multipart上传失败，有Part未上传成功。");
	       }
        }
		try{
			MultipartUploadUtil.completeMultipartUpload(client, bucket, key, uploadId, eTags);
		}catch(ClientException e){
			if(reuploadFile <= reUploadPartCounts){
        	   reuploadFile ++;
        	   uploadPart(partCount, client);
            }else{
        	   throw e;
	        }
		}
	}

	private class UploadPartThread implements Runnable {
		private long start;
		private long size;
		private int partId;
		private OSS client;
		private int reUploadTimes = 0;

		public UploadPartThread(OSS client, int partId, long start,
				long partSize) {
			this.start = start;
			this.size = partSize;
			this.partId = partId;
			this.client = client;
		}

		public void uploadImpl(UploadPartRequest uploadPartRequest)
				throws OSSException, ClientException, Exception {
			try {
				UploadPartResult uploadPartResult = client
						.uploadPart(uploadPartRequest);
				eTags.add(uploadPartResult.getPartETag());
			} catch (ClientException e) {
				if (reUploadTimes > reUploadPartCounts) {
					throw new Exception(" == oss client Exception 网络连接异常 ,已经重试3次失败  ==");
				}
				Thread.sleep(10000);
				reUploadTimes++;
				uploadImpl(uploadPartRequest);
			} catch (OSSException e) {
				throw e;
			}
		}

		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				in.skip(start);
				UploadPartRequest uploadPartRequest = new UploadPartRequest();
				uploadPartRequest.setBucketName(bucket);
				uploadPartRequest.setKey(key);
				uploadPartRequest.setUploadId(uploadId);
				uploadPartRequest.setInputStream(in);
				uploadPartRequest.setPartSize(size);
				uploadPartRequest.setPartNumber(partId);
				uploadImpl(uploadPartRequest);
				//UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
				//eTags.add(uploadPartResult.getPartETag());
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					} finally {
						in = null;
					}
				}
			}
			long endtTime = System.currentTimeMillis();
			long times = (endtTime - startTime) / 1000L;
			times = times == 0 ? 1 : times;
			long curPartSize = size / 1024;
			logger.debug("part :: " + partId + " size :: " + curPartSize + "  times ::: " + times + " speed:::" + (curPartSize / times) + "kb/s");
		}
	}
	
	/**
	 * 打印进度信息
	 * 
	 * @author Administrator
	 *
	 */
	private class SystemOutUploadFileThread implements Runnable {

		private int partCount;

		SystemOutUploadFileThread(int partCount) {
			this.partCount = partCount;
		}

		@Override
		public void run() {
			int outStr = -1;
			while (eTags.size() != partCount) {
				int p = (eTags.size() * 100) / partCount;
				if (outStr == p) {
					continue;
				} else {
					outStr = p;
					logger.debug(outStr + "%  " + message);
				}

			}
		}
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getMessage() {
		return message;
	}

}
