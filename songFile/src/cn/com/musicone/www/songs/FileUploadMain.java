/**
 * 
 */
package cn.com.musicone.www.songs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;

/**
 * @author Administrator
 *
 */
public class FileUploadMain {
	protected static final Logger logger = LogManager.getLogger(FileUploadMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AliyunOSSUtil.init();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		MybatisUtil.init();
		UploadSongFileMain.start();
		UploadBookAudioFileMain.start();
	}

}
