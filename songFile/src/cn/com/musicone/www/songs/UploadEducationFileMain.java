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
import cn.com.musicone.www.songs.service.EducationFileService;
import cn.com.musicone.www.songs.service.SongPlayFileService;
import cn.com.musicone.www.songs.service.impl.EducationFileServiceImpl;
import cn.com.musicone.www.songs.service.impl.SongPlayFileServiceImpl;

import com.song1.www.education.pojo.EducFile;
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
public class UploadEducationFileMain {
	protected static final Logger logger = LogManager.getLogger(UploadEducationFileMain.class);
	protected static EducationFileService eductionFileService = new EducationFileServiceImpl();

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
		List<EducFile> files = null;
		while (true) {
			if (hasDataToDo()) {
				files = eductionFileService.listFiles();
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
		return eductionFileService.listAliyunFilesCount() > 0;
	}
	
	/**
	 * 文件处理,挨个文件上传
	 * @param files
	 * @throws Exception 
	 */
	public static void toDoSongFile(List<EducFile> files) throws Exception{
		if(files != null){
			for (EducFile educFile : files) {
				uploadFileToOSS(educFile);
			}
		}
	}

	/**
	 * 验证文件是否上传失败，如果失败返回true，成功返回false
	 */
	public static boolean validateFailFlag = false;

	/**
	 * 文件上传云端
	 * @param songPlayFile
	 * @throws Exception 
	 */
	public static void uploadFileToOSS(EducFile educFile) throws Exception {
		if(educFile != null){
			String remark = null;
			String key = educFile.getAliyunKey();
			if(!StringUtil.isBlank(key)){
				if (SongsConstants.OSS_TYPE_UPYUN.equalsIgnoreCase(educFile.getOssType())) {
					try {
						songUpyunUpload(educFile, key);
					} catch (Exception e) {
						validateFailFlag = true;
						logger.error(educFile.getLocalPath() + "文件上传失败!");
					}
				}else if (SongsConstants.OSS_TYPE_ALIYUN.equalsIgnoreCase(educFile.getOssType())) {
					songUploadAliyun(educFile);
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
	 * 上传文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param songPlayFile
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean songUploadAliyun(EducFile educFile) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		String localPath = educFile.getLocalPath();
		String key = educFile.getAliyunKey();
		LogUtil.debug(logger,key);
		File uploadFile = new File(localPath);
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, educFile.getMd5(), uploadFile);
		boolean uplod = partUpload.upload();
		if(uplod){
			LogUtil.debug(logger, "key:" + key + "上传成功,开始修改数据库");
			educFile.setStatus(3); //// OK
			eductionFileService.updateFileStatus(educFile);
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
	private static boolean songUploadAliyun(EducFile educFile, File uploadFile,
			String key) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, educFile.getMd5(), uploadFile);
		if(partUpload.upload()){
			educFile.setStatus(3); //// OK
			eductionFileService.updateFileStatus(educFile);
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
	private static void songUpyunUpload(EducFile educFile, String key)
			throws Exception {
		UpYun.init();
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME, UpYun.OPERATOR_PWD);
		Map<String,String> map = upyun.getFileInfo(key);//文件不存在
		if(map == null){
			logger.debug("又拍云音乐文件："+ key + " 不存在则暂时不处理");
			return;
		}
		educFile.setStatus(3); 
		eductionFileService.updateFileStatus(educFile);
	}
}