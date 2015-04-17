/**
 * 
 */
package cn.com.musicone.www;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.images.ImageMain;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.upyun.utils.UpYun;
import cn.com.musicone.www.songs.UploadBookAudioFileMain;
import cn.com.musicone.www.songs.UploadSongFileMain;

/**
 * @author Administrator
 *
 */
public class MusicOneMain {
	protected static final Logger logger = LogManager
			.getLogger(MusicOneMain.class);

	// // main
	public static void main(String[] args) {
		/*
		 * init
		 */
		try {
			// // up yun oss init
			UpYun.init();
			// // aliyun oss init
			AliyunOSSUtil.init();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// // db connection init
		MybatisUtil.init();
		
		/*
		 * start to do jobs
		 */
		//// begin upload song play file to aliyun or make sure the song file have upload to upyun oss
		UploadSongFileMain.start();
		//// begin upload book audio file to aliyun or make sure the book audio file have upload to upyun oss
		UploadBookAudioFileMain.start();
		//// copy the system images from bucket[cherrytime] to bucket[songimage]
//		ImageMain.start();
	}

}
