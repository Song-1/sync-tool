/**
 * 
 */
package cn.com.musicone.www.songs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.upyun.utils.UpYun;

/**
 * @author Administrator
 *
 */
public class FileUploadMain {
	protected static final Logger logger = LogManager.getLogger(FileUploadMain.class);
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		UpYun.init();
		AliyunOSSUtil.init();
		MybatisUtil.init();
		UploadSongFileMain.start();
		UploadBookAudioFileMain.start();
	}

}
