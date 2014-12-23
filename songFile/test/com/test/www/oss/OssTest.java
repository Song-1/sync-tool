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
		String path = "E:/享CD/20140930-刘柱栋/Hip-hop/Jennifer Lopez/Rebirth";
		File file = new File(path);
		File[] files = file.listFiles();
		for (File file2 : files) {
			long start = System.currentTimeMillis();
			String key = "bigfile_upload_test/Rebirth/" + file2.getName();
			String md5Value = BaseMD5Util.getMd5ByFile(file2);
			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(AliyunOSSUtil.getDeafultBucket(), key, md5Value, file2);
			partUpload.upload();
			long end = System.currentTimeMillis();
			System.out.println(file2.getName() + " upload file cost times ::: " + (end - start) + " ms  file size :: "+ file.length());
		}
//		String key = "bigfile_upload_test/余文乐 - 两汤一面3.mp3";
//		String md5Value = BaseMD5Util.getMd5ByFile(file);
//		
//		//AliyunOSSUtil.uploadFile(AliyunOSSUtil.getDeafultBucket(), key, file, null, md5Value);
//		
//		long end = System.currentTimeMillis();
//		System.out.println("upload file cost times ::: " + (end - start) + " ms ");
		System.out.println("OK");
	}

}
