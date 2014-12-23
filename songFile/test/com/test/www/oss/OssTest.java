/**
 * 
 */
package com.test.www.oss;

import java.io.File;

import cn.com.musicone.www.base.utils.BaseMD5Util;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.aliyun.OSSMultipartUploadUtil;
import cn.com.musicone.www.oss.aliyun.multipart.MultipartLocalFileUpload;

/**
 * @author Administrator
 *
 */
public class OssTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		AliyunOSSUtil.init();
//		String path = "E:/享CD/20140930-刘柱栋/Hip-hop/Jennifer Lopez/Rebirth";
//		File file = new File(path);
//		File[] files = file.listFiles();
//		for (File file2 : files) {
//			long start = System.currentTimeMillis();
//			String key = "bigfile_upload_test/Rebirth/" + file2.getName();
//			String md5Value = BaseMD5Util.getMd5ByFile(file2);
//			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(AliyunOSSUtil.getDeafultBucket(), key, md5Value, file2);
//			partUpload.upload();
//			long end = System.currentTimeMillis();
//			System.out.println(file2.getName() + " upload file cost times ::: " + (end - start) + " ms  file size :: "+ file.length());
//		}
		long start = System.currentTimeMillis();
		String path = "E:/13716743108169350_hd.mp3";
		File file = new File(path);
		String key = "songs/liuzd_test/hell0006.mp3";
		String md5Value = BaseMD5Util.getMd5ByFile(file);
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(AliyunOSSUtil.getDeafultBucket(), key, md5Value, file);
		boolean flag = partUpload.upload();
		System.out.println(partUpload.getMessage());
//		//AliyunOSSUtil.uploadFile(AliyunOSSUtil.getDeafultBucket(), key, file, null, md5Value);
		long end = System.currentTimeMillis();
		long cost_times = (end - start);
		double times = cost_times > 0 ?cost_times / 1000 : 0;
		System.out.println("upload file cost times ::: " + times + " s ");
		System.out.println("OK");
	}

}
