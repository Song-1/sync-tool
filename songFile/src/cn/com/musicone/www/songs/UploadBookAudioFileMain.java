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
import cn.com.musicone.www.songs.service.BookAudioService;
import cn.com.musicone.www.songs.service.impl.BookAudioServiceImpl;

import com.song1.www.book.pojo.BookAudioNew;

/**
 * 
 * @ClassName: UploadBookAudioFileMain 
 * @Description: 上传书籍
 * @author Jeckey Lau
 * @date 2015年7月14日 下午1:54:37
 */
public class UploadBookAudioFileMain {
	protected static final Logger logger = LogManager.getLogger(UploadBookAudioFileMain.class);
	protected static BookAudioService bookAudioService = new BookAudioServiceImpl();
	public static void main(String[] args) throws Exception {
		AliyunOSSUtil.init();
		MybatisUtil.init();
		uploadDataTimer();
		//uploadFile();
	}
	
	public static void start(){
		uploadDataTimer();
	}
	
	public static void uploadDataTimer(){
		Timer timer = new Timer("TIMER-UPLOADFILE-BOOKAUDIO");
		TimerTask task = new TimerTask() {
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
	
	
	public static void uploadFile() throws Exception{
		List<BookAudioNew> files = null;
		while(true){
			if(hasDataToDo()){
				files = bookAudioService.listFiles();
				toDoSongFile(files);
			}else {
				SongsConstants.setPeriodTimes();
				logger.debug("本次上传完毕");
			}
		}
	}
	
	/**
	 * 是否有书籍需要上传，有书籍需要上传则返回true，没有则返回false
	 * @return
	 */
	public static boolean hasDataToDo(){
		return bookAudioService.listUploadDataCounts() > 0;
	}
	/**
	 * 书籍处理,挨个书籍文件上传
	 * @param files
	 * @throws Exception 
	 */
	public static void toDoSongFile(List<BookAudioNew> files) throws Exception{
		if(files != null){
			for (BookAudioNew audio : files) {
				uploadFileToOSS(audio);
			}
		}
	}
	
	/**
	 * 验证书籍是否上传失败，如果失败返回true，成功返回false
	 */
	public static boolean validateFailFlag = false;
	/**
	 * 上传书籍到云端,
	 * @param audio
	 * @throws Exception 
	 */
	public static void uploadFileToOSS(BookAudioNew audio) throws Exception {
		if(audio != null){
			String remark = null;
			String key = audio.getUrl();
			if(!StringUtil.isBlank(key)){
				if (SongsConstants.OSS_TYPE_UPYUN.equalsIgnoreCase(audio.getOssType())) {
					try {
						bookUpyunUpload(audio, key);
					} catch (Exception e) {
						validateFailFlag = true;
						logger.error(audio.getLocalPath() + "书籍文件上传失败!");
					}
				}else if (SongsConstants.OSS_TYPE_ALIYUN.equalsIgnoreCase(audio.getOssType())) {
					bookUploadAliyun(audio);
				}else{
					remark = "暂时不能支持除了阿里云和又拍云之外的文件上传";
				}
			}
			remark = "文件上传失败,阿里云路径为空";
			if(validateFailFlag){
				logger.error(remark + "" + key);
//				bookAudioService.updateFileStatus(audio);
				return ;
			}
		}
	}

	/**
	 * 上传书籍文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param audio
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean bookUploadAliyun(BookAudioNew audio, File uploadFile,
			String key) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, audio.getMd5Value(), uploadFile);
		if(partUpload.upload()){
			audio.setStatus(3); //// OK
			bookAudioService.updateFileStatus(audio);
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 上传书籍文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param audio
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean bookUploadAliyun(BookAudioNew audio,String key) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		String localPath = audio.getLocalPath();
		File uploadFile = new File(localPath);
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, audio.getMd5Value(), uploadFile);
		if(partUpload.upload()){
			audio.setStatus(3); //// OK
			bookAudioService.updateFileStatus(audio);
			return true;
		}//else{
		//	audio.setStatus(7); //不Ok则全部继续上传
		//}
		return false;
	}
	
	/**
	 * 上传书籍文件到阿里云,如果成功则返回true，如果不成功则返回false
	 * @param audio
	 * @param uploadFile
	 * @param key
	 * @throws Exception
	 */
	private static boolean bookUploadAliyun(BookAudioNew audio) throws Exception{
		String bucket = AliyunOSSUtil.getDeafultBucket();
		String localPath = audio.getLocalPath();
		String key = audio.getUrl();
		LogUtil.debug(logger,key);
		File uploadFile = new File(localPath);
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, audio.getMd5Value(), uploadFile);
		boolean upload = partUpload.upload();
		if(upload){
			LogUtil.debug(logger, "key:" + key + "上传成功,开始修改数据库");
			audio.setStatus(3); //// OK
			bookAudioService.updateFileStatus(audio);
			return true;
		}else {
			LogUtil.debug(logger, "key:" + key + "上传失败");
			return false;
		}
	}

	/**
	 * 又拍云书籍处理，只检查又拍云是否存在，如果不存在则暂时不处理
	 * @param audio
	 * @param key
	 * @throws Exception
	 */
	private static void bookUpyunUpload(BookAudioNew audio, String key)
			throws Exception {
		UpYun.init();
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME, UpYun.OPERATOR_PWD);
		Map<String,String> map = upyun.getFileInfo(key);//文件不存在
		if(map == null){
			logger.debug("又拍云书籍文件："+ key + " 不存在则暂时不处理");
			return;
		}
		audio.setStatus(3); 
		bookAudioService.updateFileStatus(audio);
	}

}
