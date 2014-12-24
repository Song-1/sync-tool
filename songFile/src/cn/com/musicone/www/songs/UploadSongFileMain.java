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
import cn.com.musicone.www.songs.service.SongPlayFileService;
import cn.com.musicone.www.songs.service.impl.SongPlayFileServiceImpl;

import com.song1.www.songs.pojo.SongPlayFile;

/**
 * @author Administrator
 *
 */
public class UploadSongFileMain {
	protected static final Logger logger = LogManager.getLogger(UploadSongFileMain.class);
	protected static SongPlayFileService songPlayFileService = new SongPlayFileServiceImpl();
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
	public static void uploadDataTimer(){
		Timer timer = new Timer("TIMER-UPLOADFILE");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				uploadFile();
			}
		}, 0L, SongsConstants.UPLOAD_FILE_PERIOD_TIMES);
	}
	
	
	public static void uploadFile(){
		List<SongPlayFile> files = null;
		while(true){
			if(!hasDataToDo()){
				SongsConstants.setPeriodTimes();
				return ;
			}
			try{
				files = songPlayFileService.listFiles();
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
			Thread.sleep(60000);
			int counts = songPlayFileService.listUploadDataCounts();
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
	public static void toDoSongFile(List<SongPlayFile> files){
		if(files == null){
			return;
		}
		for (SongPlayFile songPlayFile : files) {
			if(songPlayFile == null){
				continue;
			}
			logger.debug("开始处理文件 ::: " + songPlayFile.getAliyunKey());
			String localPath = songPlayFile.getLocalPath();
			if(StringUtil.isBlank(localPath)){
				logger.debug("文件上传失败,本地路径为空 ::: " + songPlayFile.getAliyunKey());
				continue;
			}
			uploadFileToOSS(songPlayFile);
		}
	}
	
	public static void uploadFileToOSS(SongPlayFile songPlayFile) {
		if(songPlayFile == null){
			return;
		}
		try{
			boolean valiDateFailFlag = false;
			String remark = null;
			File uploadFile = null;
			String localPath = songPlayFile.getLocalPath();
			String key = songPlayFile.getAliyunKey();
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
				songPlayFile.setStatus(4); //// 待审核
				songPlayFile.setBak1(remark);
				songPlayFileService.updateFileStatus(songPlayFile);
				return ;
			}
			boolean flag = false;
			String bucket = AliyunOSSUtil.getDeafultBucket();
			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, songPlayFile.getMd5(), uploadFile);
			flag = partUpload.upload();
			if(flag){
				songPlayFile.setStatus(3); //// OK
			}else{
				songPlayFile.setStatus(8); //// OK
				songPlayFile.setBak1("文件上传失败:"+partUpload.getMessage());
			}
			songPlayFileService.updateFileStatus(songPlayFile);
		}catch(Exception e){
			logger.error("处理文件失败::: " + songPlayFile.getAliyunKey());
			logger.error(e.getMessage(),e);
		}
	}

}
