package cn.com.musicone.www.songs;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.song1.www.education.pojo.EducFile;
import com.song1.www.puresong.pojo.PurSong;

import cn.com.musicone.www.base.utils.LogUtil;
import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.aliyun.multipart.MultipartLocalFileUpload;
import cn.com.musicone.www.oss.upyun.utils.UpYun;
import cn.com.musicone.www.songs.service.PureSongFileService;
import cn.com.musicone.www.songs.service.impl.PureSongFileServiceImpl;

public class UploadPureSongFileMain {
	protected static final Logger logger = LogManager.getLogger(UploadPureSongFileMain.class);
	protected static PureSongFileService pureSongFileService = new PureSongFileServiceImpl();
	
	public static void main(String[] args) throws Exception {
		AliyunOSSUtil.init();//初始化aliyun oss key bucket等属性
		MybatisUtil.init();//Mybatis 数据库配置初始化
		uploadDataTimer();
	}

	public static void start() {
		uploadDataTimer();
	}
	/**
	 * 上传文件Timer
	 */
	public static void uploadDataTimer() {
		Timer timer = new Timer("TIMER-UPLOADFILE-EDUCATION");
		TimerTask task = new TimerTask()  {
			@Override
			public void run() {
				try {
					uploadFile();
				} catch (Exception e) {
					e.printStackTrace();
					System.gc();
				}
			}
		};
		timer.schedule(task, 0L, SongsConstants.UPLOAD_FILE_PERIOD_TIMES);
		System.gc();
	}
	/**
	 * 上传文件
	 * @throws Exception
	 */
	public static void uploadFile() throws Exception {
		List<PurSong> files = null;
		while (true) {
			if (hasDataToDo()) {
				files = pureSongFileService.listFiles();
				toDoSongFile(files);
			}else{
				SongsConstants.setPeriodTimes();
				logger.debug("本次上传完毕");
			}
		}
	}
	
	/**
	 * 是否有文件需要上传，有文件需要上传则返回true，没有则返回false
	 * @return
	 * @throws Exception 
	 */
	public static boolean hasDataToDo() throws Exception {
		return pureSongFileService.listAliyunFilesCount() > 0;
	}
	
	/**
	 * 文件处理,挨个文件上传
	 * @param files
	 * @throws Exception 
	 */
	public static void toDoSongFile(List<PurSong> files) throws Exception{
		if(files != null){
			for (PurSong purSong : files) {
				uploadFileToOSS(purSong);
			}
		}
	}

	/**
	 * 验证文件是否上传失败，如果失败返回true，成功返回false
	 */
	public static boolean validateFailFlag = false;
	public static boolean validateFailFlag1 = false;

	/**
	 * 文件上传云端
	 * @param songPlayFile
	 * @throws Exception 
	 */
	public static void uploadFileToOSS(PurSong purSong) throws Exception {
		if(purSong != null){
			if(!StringUtil.isBlank(purSong.getAliyunKey()) && purSong.getFileStatus() == 7){
				String remark = null;
				String key = purSong.getAliyunKey();
				if(!StringUtil.isBlank(key)){
					if (SongsConstants.OSS_TYPE_UPYUN.equalsIgnoreCase(purSong.getOssType())) {
						try {
							songUpyunUpload(purSong, key);
						} catch (Exception e) {
							validateFailFlag = true;
							logger.error(purSong.getLocalPath() + "文件上传失败!");
						}
					}else if (SongsConstants.OSS_TYPE_ALIYUN.equalsIgnoreCase(purSong.getOssType())) {
						songUploadAliyun(purSong);
					}else{
						remark = "暂时不能支持除了阿里云和又拍云之外的文件上传";
					}
				}
				remark = "文件上传失败,阿里云路径为空";
				if(validateFailFlag){
					logger.error(remark + "" + key);
					return ;
				}
			}
			if(!StringUtil.isBlank(purSong.getKey1()) && purSong.getFileStatus1() == 7){
				String remark = null;
				String key1 = purSong.getKey1();
				if(!StringUtil.isBlank(key1)){
					if (SongsConstants.OSS_TYPE_UPYUN.equalsIgnoreCase(purSong.getOssType1())) {
						try {
							songUpyunUpload(purSong, key1);
						} catch (Exception e) {
							validateFailFlag1 = true;
							logger.error(purSong.getLocalPath1() + "文件上传失败!");
						}
					}else if (SongsConstants.OSS_TYPE_ALIYUN.equalsIgnoreCase(purSong.getOssType1())) {
						songUploadAliyun(purSong);
					}else{
						remark = "暂时不能支持除了阿里云和又拍云之外的文件上传";
					}
				}
				remark = "文件上传失败,阿里云路径为空";
				if(validateFailFlag1){
					logger.error(remark + "" + key1);
					return ;
				}
			}
			
		}
	}
	
	/**
	 * 上传文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param songPlayFile
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean songUploadAliyun(PurSong purSong) throws Exception{
		if(!StringUtil.isBlank(purSong.getAliyunKey()) && purSong.getFileStatus()==7){
			String bucket = AliyunOSSUtil.getDeafultBucket();
			String localPath = purSong.getLocalPath();
			String key = purSong.getAliyunKey();
			LogUtil.debug(logger,key);
			File uploadFile = new File(localPath);
			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, purSong.getMd5(), uploadFile);
			boolean uplod = partUpload.upload();
			if(uplod){
				LogUtil.debug(logger, "key:" + key + "上传成功,开始修改数据库");
				purSong.setFileStatus(3); //// OK
				pureSongFileService.updateFileStatus(purSong);
				return true;
			}else{
				LogUtil.debug(logger, "key:" + key + "上传失败");
				return false;
			}
		}
		if(!StringUtil.isBlank(purSong.getKey1()) && purSong.getFileStatus1()==7){
			String bucket = AliyunOSSUtil.getDeafultBucket();
			String localPath1 = purSong.getLocalPath1();
			String key1 = purSong.getKey1();
			LogUtil.debug(logger,key1);
			File uploadFile = new File(localPath1);
			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key1, purSong.getMd51(), uploadFile);
			boolean uplod = partUpload.upload();
			if(uplod){
				LogUtil.debug(logger, "key:" + key1 + "上传成功,开始修改数据库");
				purSong.setFileStatus1(3); //// OK
				pureSongFileService.updateFileStatus1(purSong);
				return true;
			}else{
				LogUtil.debug(logger, "key:" + key1 + "上传失败");
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 上传音乐文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param songPlayFile
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean songUploadAliyun(PurSong purSong, File uploadFile,
			String key) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		if(!StringUtil.isBlank(purSong.getAliyunKey()) && purSong.getFileStatus()==7){
			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, purSong.getMd5(), uploadFile);
			if(partUpload.upload()){
				purSong.setFileStatus(3); //// OK
				pureSongFileService.updateFileStatus(purSong);
				return true;
			}
		}
		if(!StringUtil.isBlank(purSong.getKey1()) && purSong.getFileStatus1()==7){
			MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, purSong.getMd51(), uploadFile);
			if(partUpload.upload()){
				purSong.setFileStatus1(3); //// OK
				pureSongFileService.updateFileStatus1(purSong);
				return true;
			}
		}
		return false;
	}

	/**
	 * 又拍云音乐处理，只检查又拍云是否存在，如果不存在则暂时不处理
	 * @param audio
	 * @param key
	 * @throws Exception
	 */
	private static void songUpyunUpload(PurSong purSong, String key)
			throws Exception {
		UpYun.init();
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME, UpYun.OPERATOR_PWD);
		Map<String,String> map = upyun.getFileInfo(key);//文件不存在
		if(!StringUtil.isBlank(purSong.getAliyunKey()) && purSong.getFileStatus()==7){
			if(map == null){
				logger.debug("又拍云音乐文件："+ key + " 不存在则暂时不处理");
				return;
			}
			purSong.setFileStatus(3); 
			pureSongFileService.updateFileStatus(purSong);
		}
		if(!StringUtil.isBlank(purSong.getKey1()) && purSong.getFileStatus1()==7){
			if(map == null){
				logger.debug("又拍云音乐文件："+ key + " 不存在则暂时不处理");
				return;
			}
			purSong.setFileStatus1(3); 
			pureSongFileService.updateFileStatus1(purSong);
		}
	}
}
