package cn.com.musicone.www.songs;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
				logger.debug("本次上传完毕");
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
	
	/**
	 * 是否有书籍需要上传，有书籍需要上传则返回true，没有则返回false
	 * @return
	 */
	public static boolean hasDataToDo(){
		return bookAudioService.listUploadDataCounts() > 0;
	}
	/**
	 * 书籍处理
	 * @param files
	 */
	public static void toDoSongFile(List<BookAudioNew> files){
		if(files != null){
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
	}
	
	
	/**
	 * 上传书籍到云端,
	 * @param audio
	 */
	public static boolean validateFailFlag = false;
	public static void uploadFileToOSS(BookAudioNew audio) {
		if(audio != null){
			String remark = null;
			File uploadFile = null;
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
					bookUploadAliyun(audio, uploadFile, key);
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
			String key){
		boolean flag = false;
		String bucket = AliyunOSSUtil.getDeafultBucket();
		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(bucket, key, audio.getMd5Value(), uploadFile);
		flag = partUpload.upload();
		if(flag){
			audio.setStatus(3); //// OK
		}//else{
		//	audio.setStatus(7); //不Ok则全部继续上传
		//}
		try {
			bookAudioService.updateFileStatus(audio);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
