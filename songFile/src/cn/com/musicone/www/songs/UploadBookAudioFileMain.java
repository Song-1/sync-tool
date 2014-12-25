/**
 * 
 */
package cn.com.musicone.www.songs;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.aliyun.multipart.MultipartLocalFileUpload;
import cn.com.musicone.www.songs.service.BookAudioService;
import cn.com.musicone.www.songs.service.impl.BookAudioServiceImpl;

import com.song1.www.book.pojo.BookAudioNew;

/**
 * @author Administrator
 *
 */
public class UploadBookAudioFileMain {
	protected static final Logger logger = LogManager.getLogger(UploadBookAudioFileMain.class);
	protected static BookAudioService bookAudioService = new BookAudioServiceImpl();
	public static void main(String[] args) {
		try {
			AliyunOSSUtil.init();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		MybatisUtil.init();
		uploadDataTimer();
		//uploadFile();
	}
	
	public static void start(){
		uploadDataTimer();
	}
	
	public static void uploadDataTimer(){
		Timer timer = new Timer("TIMER-UPLOADFILE-BOOKAUDIO");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				uploadFile();
			}
		}, 0L, SongsConstants.UPLOAD_FILE_PERIOD_TIMES);
	}
	
	
	public static void uploadFile(){
		List<BookAudioNew> files = null;
		while(true){
			if(!hasDataToDo()){
				SongsConstants.setPeriodTimes();
				return ;
			}
			try{
				files = bookAudioService.listFiles();
			}catch(Exception e){
				logger.error("获取数据上传到阿里云发生异常");
				logger.error(e.getMessage(),e);
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e1) {
					logger.error(e1.getMessage(),e1);
				}
				continue;
			}
			toDoSongFile(files);
		}
	}
	
	public static boolean hasDataToDo(){
		try{
			int counts = bookAudioService.listUploadDataCounts();
			if(counts <= 0){
				logger.debug("本次数据库遍历结束");
				return false;
			}
		}catch(Exception e){
			logger.error("获取数据上传到阿里云发生异常");
			logger.error(e.getMessage(),e);
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e1) {
				logger.error(e1.getMessage(),e1);
			}
			return true;
		}
		return true;
	}
	public static void toDoSongFile(List<BookAudioNew> files){
		if(files == null){
			return;
		}
		for (BookAudioNew audio : files) {
			if(audio == null){
				continue;
			}
			logger.debug("开始处理文件 ::: " + audio.getUrl());
			String localPath = audio.getLocalPath();
			if(StringUtil.isBlank(localPath)){
				logger.debug("文件上传失败,本地路径为空 ::: " + audio.getUrl());
				continue;
			}
			uploadFileToOSS(audio);
		}
	}
	
	public static void uploadFileToOSS(BookAudioNew audio) {
		if(audio == null){
			return;
		}
		try{
			boolean valiDateFailFlag = false;
			String remark = null;
			File uploadFile = null;
			String localPath = audio.getLocalPath();
			String key = audio.getUrl();
			if(StringUtil.isBlank(localPath)){
				remark = "文件上传失败,本地路径为空";
				valiDateFailFlag = true;
			}else if(StringUtil.isBlank(key)){
				remark = "文件上传失败,阿里云路径为空";
				valiDateFailFlag = true;
			}else{
				uploadFile = new File(localPath);
				if(!uploadFile.exists()){
					remark = "文件上传失败,本地文件不存在";
					valiDateFailFlag = true;
				}
			}
			if(valiDateFailFlag){
				logger.error(remark + " " + key);
				audio.setStatus(4); //// 待审核
				bookAudioService.updateFileStatus(audio);
				return ;
			}
			boolean flag = false;
			String bucket = AliyunOSSUtil.getDeafultBucket();
			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, audio.getMd5Value(), uploadFile);
			flag = partUpload.upload();
			if(flag){
				audio.setStatus(3); //// OK
			}else{
				audio.setStatus(8); //// OK
			}
			bookAudioService.updateFileStatus(audio);
		}catch(Exception e){
			logger.error("处理文件失败::: " + audio.getUrl());
			logger.error(e.getMessage(),e);
		}
	}

}
