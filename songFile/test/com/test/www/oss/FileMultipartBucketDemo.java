package com.test.www.oss;

import java.io.File;
import java.text.NumberFormat;
import java.util.Map;

import com.song1.www.songs.pojo.SongPlayFile;

import cn.com.musicone.www.base.utils.FileUtils;
import cn.com.musicone.www.oss.upyun.listener.CompleteListener;
import cn.com.musicone.www.oss.upyun.listener.ProgressListener;
import cn.com.musicone.www.oss.upyun.main.UploaderManager;
import cn.com.musicone.www.oss.upyun.utils.UpYun;
import cn.com.musicone.www.oss.upyun.utils.UpYunUtils;

/**
 * 文件类空间的demo
 */
public class FileMultipartBucketDemo {

	/** 本地待上传的测试文件 */
	private static final String SAMPLE_TXT_FILE = System
			.getProperty("user.dir") + "/test.txt";


	static {
		File txtFile = new File(SAMPLE_TXT_FILE);

		if (!txtFile.isFile()) {
			System.out.println("本地待上传的测试文件不存在！");
		}
	}

	public static void main(String[] args) throws Exception {
		// 初始化空间
		//upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
		uploadMultipartTask();
	}
	
	public static void uploadMultipartTask() {
		
		try {
			UpYun.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String localFilePath = "H:/201501/01 - Concerto for Piano and Orchestra G minor I.Allegro agitato.flac";
		File localFile = new File(localFilePath);
		try {
			/*
			 * 设置进度条回掉函数
			 * 
			 * 注意：由于在计算发送的字节数中包含了图片以外的其他信息，最终上传的大小总是大于图片实际大小，
			 * 为了解决这个问题，代码会判断如果实际传送的大小大于图片
			 * ，就将实际传送的大小设置成'totalBytes-1000'（最小为0）
			 */
			ProgressListener progressListener = new ProgressListener() {
				@Override
				public void transferred(long transferedBytes, long totalBytes) {
					// do something...
					System.out.println("trans:" + transferedBytes + "; total:" + totalBytes);
					double  p3  =  Double.parseDouble(String.valueOf(transferedBytes))/Double.parseDouble(String.valueOf(totalBytes));System.out.println(p3);
			        NumberFormat nf  =  NumberFormat.getPercentInstance();
			        nf.setMinimumFractionDigits(2);
					System.out.println(nf.format(p3));
				}

				@Override
				public void transferred(long transferedBytes, long totalBytes,
						long times) {

					// do something...
					System.out.println("trans:" + transferedBytes + "; total:" + totalBytes);
					double  p3  =  Double.parseDouble(String.valueOf(transferedBytes))/Double.parseDouble(String.valueOf(totalBytes));
					System.out.println(p3);
					NumberFormat nf  =  NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(2);
					System.out.println(nf.format(p3));
					double p4 = Double.parseDouble(String.valueOf(transferedBytes))/Double.parseDouble(String.valueOf(times));
					System.out.println(p4);
					NumberFormat nf1  =  NumberFormat.getPercentInstance();
					nf1.setMinimumFractionDigits(2);
					System.out.println(nf.format(p4));
				
					
				}
			};
			
			CompleteListener completeListener = new CompleteListener() {
				@Override
				public void result(boolean isComplete, String result, String error) {
					// do something...
					System.out.println("isComplete:"+isComplete+";result:"+result+";error:"+error);
				}
			};
			
			UploaderManager uploaderManager = UploaderManager.getInstance(UpYun.BUCKET_NAME);
			uploaderManager.setBlockSize(Integer.parseInt(UpYun.MULTI_PART_SIZE));
			Map<String, Object> paramsMap = uploaderManager.fetchFileInfoDictionaryWith(localFile, localFilePath.replace("H:/", ""));
			paramsMap.put("return_url", UpYun.RETURN_URL);
			// signature & policy 建议从服务端获取
			String policyForInitial = UpYunUtils.getPolicy(paramsMap);
			String signatureForInitial = UpYunUtils.getSignature(paramsMap, UpYun.FORM_API_SECRET);
			uploaderManager.upload(policyForInitial, signatureForInitial, localFile, progressListener, completeListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 大文件上传
	 * @param songPlayFile
	 */
	public static void UploadMultipartTask(SongPlayFile songPlayFile) {
		
		try {
			UpYun.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String localFilePath = songPlayFile.getLocalPath();
		File localFile = new File(localFilePath);
		try {
			/*
			 * 设置进度条回掉函数
			 * 
			 * 注意：由于在计算发送的字节数中包含了图片以外的其他信息，最终上传的大小总是大于图片实际大小，
			 * 为了解决这个问题，代码会判断如果实际传送的大小大于图片
			 * ，就将实际传送的大小设置成'totalBytes-1000'（最小为0）
			 */
			ProgressListener progressListener = new ProgressListener() {
				@Override
				public void transferred(long transferedBytes, long totalBytes) {
					// do something...
					System.out.println("trans:" + transferedBytes + "; total:" + totalBytes);
					double  p3  =  Double.parseDouble(String.valueOf(transferedBytes))/Double.parseDouble(String.valueOf(totalBytes));System.out.println(p3);
					NumberFormat nf  =  NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(2);
					System.out.println(nf.format(p3));
				}

				@Override
				public void transferred(long transferedBytes, long totalBytes,
						long times) {

					// do something...
					float trans = FileUtils.convertFileSizem(transferedBytes);
					float total = FileUtils.convertFileSizem(totalBytes);
					System.out.println("trans:" + trans + "MB; total:" + total + "MB");
					double  p3  =  Double.parseDouble(String.valueOf(transferedBytes))/Double.parseDouble(String.valueOf(totalBytes));
//					System.out.println(p3);
					NumberFormat nf  =  NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(2);
					System.out.println(nf.format(p3));
					long t = times/1000;
					double p4 = Double.parseDouble(String.valueOf(transferedBytes))/Double.parseDouble(String.valueOf(t));
//					System.out.println(p4);
					NumberFormat nf1  =  NumberFormat.getPercentInstance();
					nf1.setMinimumFractionDigits(2);
					System.out.println(nf.format(p4) + "MB/s");
				
					
				}
			};
			
			CompleteListener completeListener = new CompleteListener() {
				@Override
				public void result(boolean isComplete, String result, String error) {
					// do something...
					System.out.println("isComplete:"+isComplete+";result:"+result+";error:"+error);
				}
			};
			
			UploaderManager uploaderManager = UploaderManager.getInstance(UpYun.BUCKET_NAME);
			uploaderManager.setBlockSize(Integer.parseInt(UpYun.MULTI_PART_SIZE));
			Map<String, Object> paramsMap = uploaderManager.fetchFileInfoDictionaryWith(songPlayFile);
			paramsMap.put("return_url", UpYun.RETURN_URL);
			// signature & policy 建议从服务端获取
			String policyForInitial = UpYunUtils.getPolicy(paramsMap);
			String signatureForInitial = UpYunUtils.getSignature(paramsMap, UpYun.FORM_API_SECRET);
			uploaderManager.upload(policyForInitial, signatureForInitial, localFile, progressListener, completeListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}