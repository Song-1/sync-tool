package cn.com.musicone.www.songs;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.LogUtil;
import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.aliyun.multipart.MultipartLocalFileUpload;
import cn.com.musicone.www.oss.upyun.utils.UpYun;
import cn.com.musicone.www.songs.service.SongPlayFileService;
import cn.com.musicone.www.songs.service.impl.SongPlayFileServiceImpl;

import com.song1.www.songs.pojo.SongPlayFile;

/**
 * 
 * @ClassName: UploadSongFileMain 
 * @Description: 上传歌曲文件
 * @author Jeckey Lau
 * @date 2015年7月28日 上午9:18:03
 * 
 * 
 * 
 */
public class UploadSongFileMain {
	protected static final Logger logger = LogManager.getLogger(UploadSongFileMain.class);
	protected static SongPlayFileService songPlayFileService = new SongPlayFileServiceImpl();

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
		Timer timer = new Timer("TIMER-UPLOADFILE-SONG");
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
	 * 上传音乐文件
	 * @throws Exception
	 */
	public static void uploadFile() throws Exception {
		List<SongPlayFile> files = null;
		while (true) {
			if (hasDataToDo()) {
				files = songPlayFileService.listFiles();
				toDoSongFile(files);
			}else{
				SongsConstants.setPeriodTimes();
				logger.debug("本次上传完毕");
			}
			
			
		}
	}
	
	/**
	 * 是否有歌曲需要上传，有歌曲需要上传则返回true，没有则返回false
	 * @return
	 * @throws Exception 
	 */
	public static boolean hasDataToDo() throws Exception {
		return songPlayFileService.listAliyunFilesCount() > 0;
	}
	
	/**
	 * 音乐处理,挨个音乐文件上传
	 * @param files
	 * @throws Exception 
	 */
	public static void toDoSongFile(List<SongPlayFile> files) throws Exception{
		if(files != null){
			for (SongPlayFile songPlayFile : files) {
				uploadFileToOSS(songPlayFile);
			}
		}
	}

	/**
	 * 验证音乐文件是否上传失败，如果失败返回true，成功返回false
	 */
	public static boolean validateFailFlag = false;

	/**
	 * 音乐上传云端
	 * @param songPlayFile
	 * @throws Exception 
	 */
	public static void uploadFileToOSS(SongPlayFile songPlayFile) throws Exception {
		if(songPlayFile != null){
			String remark = null;
			String key = songPlayFile.getAliyunKey();
			if(!StringUtil.isBlank(key)){
				if (SongsConstants.OSS_TYPE_UPYUN.equalsIgnoreCase(songPlayFile.getOssType())) {
					try {
						songUpyunUpload(songPlayFile, key);
					} catch (Exception e) {
						validateFailFlag = true;
						logger.error(songPlayFile.getLocalPath() + "音乐文件上传失败!");
					}
				}else if (SongsConstants.OSS_TYPE_ALIYUN.equalsIgnoreCase(songPlayFile.getOssType())) {
					songUploadAliyun(songPlayFile);
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
	}
	
	/**
	 * 上传音乐文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param songPlayFile
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean songUploadAliyun(SongPlayFile songPlayFile) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		String localPath = songPlayFile.getLocalPath();
		String key = songPlayFile.getAliyunKey();
		LogUtil.debug(logger,key);
		File uploadFile = new File(localPath);
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, songPlayFile.getMd5(), uploadFile);
		boolean uplod = partUpload.upload();
		if(uplod){
			LogUtil.debug(logger, "key:" + key + "上传成功,开始修改数据库");
			songPlayFile.setStatus(3); //// OK
			songPlayFileService.updateFileStatus(songPlayFile);
			return true;
		}else{
			LogUtil.debug(logger, "key:" + key + "上传失败");
			return false;
		}
	}
	
	/**
	 * 上传音乐文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param songPlayFile
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean songUploadAliyun(SongPlayFile songPlayFile, File uploadFile,
			String key) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, songPlayFile.getMd5(), uploadFile);
		if(partUpload.upload()){
			songPlayFile.setStatus(3); //// OK
			songPlayFileService.updateFileStatus(songPlayFile);
			return true;
		}
		return false;
	}

	/**
	 * 又拍云音乐处理，只检查又拍云是否存在，如果不存在则暂时不处理
	 * @param audio
	 * @param key
	 * @throws Exception
	 */
	private static void songUpyunUpload(SongPlayFile songPlayFile, String key)
			throws Exception {
		UpYun.init();
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME, UpYun.OPERATOR_PWD);
		Map<String,String> map = upyun.getFileInfo(key);//文件不存在
		if(map == null){
			logger.debug("又拍云音乐文件："+ key + " 不存在则暂时不处理");
			return;
		}
		songPlayFile.setStatus(3); 
		songPlayFileService.updateFileStatus(songPlayFile);
	}
}