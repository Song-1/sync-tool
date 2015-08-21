package cn.com.musicone.www.songs;

import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;
import cn.com.musicone.www.oss.aliyun.multipart.MultipartLocalFileUpload;
import cn.com.musicone.www.oss.upyun.utils.UpYun;
import cn.com.musicone.www.songs.service.SongPlayFileService;
import cn.com.musicone.www.songs.service.impl.SongPlayFileServiceImpl;
import com.song1.www.songs.pojo.SongPlayFile;
import com.test.www.oss.FileMultipartBucketDemo;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpyunSongUploadFileMain {
	protected static final Logger logger = LogManager
			.getLogger(UpyunSongUploadFileMain.class);
	protected static SongPlayFileService songPlayFileService = new SongPlayFileServiceImpl();
	private static final String DIR_ROOT = "/";
	private static final String DIR_MORE = "/1/2/3/";
	private static List<Integer> updateUpyunFiles = new ArrayList();
	private static String bucket = AliyunOSSUtil.OSS_BUCKET;
	private static boolean flag = false;

	public static void main(String[] args) throws Exception {
		UpYun.init();
		MybatisUtil.init();
		uploadDataTimer();
	}

	public static void start() {
		uploadDataTimer();
	}

	public static void uploadDataTimer() {
		Timer timer = new Timer("TIMER-UPLOADFILE-SONG");
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					UpyunSongUploadFileMain.uploadFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0L, SongsConstants.UPLOAD_FILE_PERIOD_TIMES);
	}

	public static void uploadFile() {
		List files = null;
		while (true) {
			if (hasDataToDo()) {
				try {
					files = songPlayFileService.listFiles();
				} catch (Exception e) {
				}

				toDoSongFile(files);
				try {
					songPlayFileService.updateUpyunFileStatus(updateUpyunFiles);
				} catch (Exception e) {
					e.printStackTrace();
				}
				updateUpyunFiles = new ArrayList();
			}
			SongsConstants.setPeriodTimes();
		}
	}

	public static boolean hasDataToDo() {
		try {
			int counts = songPlayFileService.listUploadDataCounts();
			if (counts <= 0) {
				logger.debug("本次数据库遍历结束");
				return false;
			}
		} catch (Exception e) {
			logger.error("获取数据上传到阿里云发生异常");
			logger.error(e.getMessage(), e);
			try {
				Thread.sleep(60000L);
			} catch (InterruptedException e1) {
				logger.error(e1.getMessage(), e1);
			}
			return true;
		}
		return true;
	}

	public static void toDoSongFile(List<SongPlayFile> files) {
		if (files == null) {
			return;
		}
		for (SongPlayFile songPlayFile : files) {
			if (songPlayFile == null) {
				continue;
			}
			String localPath = songPlayFile.getLocalPath();
			localPath = localPath.replace("I:/upyun/", "H:/");
			songPlayFile.setLocalPath(localPath);
			if (StringUtil.isBlank(localPath)) {
				logger.debug("文件上传失败,本地路径为空 ::: " + songPlayFile.getAliyunKey());
			} else
				uploadFileToOSS(songPlayFile);
		}
	}

	private static String isSuccess(boolean result) {
		return (result) ? " 成功" : " 失败";
	}

	private static boolean isExistOss(String key) {
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME,
				UpYun.OPERATOR_PWD);
		Map map = upyun.getFileInfo(key);

		return map != null;
	}

	public static void uploadFile(String key, String localfilePath)
			throws IOException {
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME,
				UpYun.OPERATOR_PWD);

		String filePath = "/" + key;

		File file4 = new File(localfilePath);
		if (!file4.exists()) {
			System.out.println("上传 " + filePath + ":" + localfilePath + "失败");
			return;
		}

		boolean existOss = isExistOss(filePath);
		if (existOss) {
			System.out.println("上传 " + filePath + ":" + localfilePath + "已经存在");
			return;
		}

		upyun.setContentMD5(UpYun.md5(file4));
		boolean result4 = upyun.writeFile(filePath, file4, true);
		System.out.println("上传 " + filePath + isSuccess(result4));
	}

	public static void uploadbigFile(SongPlayFile songPlayFile)
			throws IOException {
		FileMultipartBucketDemo.UploadMultipartTask(songPlayFile);
	}

	public static void uploadFileToOSS(SongPlayFile songPlayFile) {
		if (songPlayFile == null)
			return;
		try {
			String remark = null;
			File uploadFile = null;
			String key = songPlayFile.getAliyunKey();
			if (StringUtil.isBlank(key)) {
				remark = "文件上传失败,阿里云路径为空";
				songPlayFile.setStatus(4);
				songPlayFile.setBak1(remark);
				songPlayFileService.updateFileStatus(songPlayFile);
			}

			upyunUpload(songPlayFile, key);
		} catch (Exception e) {
			logger.error("处理文件失败::: " + songPlayFile.getAliyunKey());
			logger.error(e.getMessage(), e);
		}
	}

	private static void upyunUpload(SongPlayFile songPlayFile, String key)
			throws IOException {
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME,
				UpYun.OPERATOR_PWD);
		Map map = upyun.getFileInfo(key);
		if (map != null)
			return;
		if ("upyun".equalsIgnoreCase(songPlayFile.getOssType())) {
			uploadToUpaiyun(songPlayFile, key);
			logger.debug("又拍云文件 [  key ::::" + key + " ]   上传失败   ");
			return;
		}

		logger.debug("又拍云文件 [  key ::::" + key + " ]   上传成功    ");
		updateUpyunFiles.add(Integer.valueOf(songPlayFile.getId()));

		songPlayFile.setStatus(3);

		return;
	}

	private static void uploadToUpaiyun(SongPlayFile songPlayFile, String key)
			throws IOException {
		long fileSize = songPlayFile.getFileSize();
		if (fileSize == 0L) {
			fileSize = new File(songPlayFile.getLocalPath()).length();
		}
		if (fileSize >= 20000000L)
			uploadbigFile(songPlayFile);
		else
			uploadFile(key, songPlayFile.getLocalPath());
	}

	private static void dealFile(SongPlayFile songPlayFile) throws Exception,
			IOException {
		boolean valiDateFailFlag = false;
		String remark = null;
		File uploadFile = null;
		String localPath = songPlayFile.getLocalPath();
		String key = songPlayFile.getAliyunKey();
		if (StringUtil.isBlank(key)) {
			remark = "文件上传失败,阿里云路径为空";
			songPlayFile.setStatus(4);
			songPlayFile.setBak1(remark);
			songPlayFileService.updateFileStatus(songPlayFile);
		}

		if ("upyun".equalsIgnoreCase(songPlayFile.getOssType()))
			uploadto(songPlayFile, valiDateFailFlag, remark, uploadFile,
					localPath, key);
	}

	private static void uploadto(SongPlayFile songPlayFile,
			boolean valiDateFailFlag, String remark, File uploadFile,
			String localPath, String key) throws IOException, Exception {
		UpYun upyun = new UpYun(UpYun.BUCKET_NAME, UpYun.OPERATOR_NAME,
				UpYun.OPERATOR_PWD);
		Map map = upyun.getFileInfo(key);
		if (map == null) {
			long fileSize = songPlayFile.getFileSize();
			if (fileSize == 0L) {
				fileSize = new File(songPlayFile.getLocalPath()).length();
			}
			if (fileSize >= 200000000L)
				uploadbigFile(songPlayFile);
			else {
				uploadFile(key, songPlayFile.getLocalPath());
			}
			logger.debug("又拍云文件 [  key ::::" + key + " ]   上传失败   ");
			return;
		}

		logger.debug("又拍云文件 [  key ::::" + key + " ]   上传成功    ");
		updateUpyunFiles.add(Integer.valueOf(songPlayFile.getId()));

		songPlayFile.setStatus(3);

		MultipartLocalFileUpload partUpload = new MultipartLocalFileUpload(
				bucket, key, songPlayFile.getMd5(), uploadFile);
		flag = partUpload.upload();
		if (flag) {
			songPlayFile.setStatus(3);
		} else {
			songPlayFile.setStatus(8);
			songPlayFile.setBak1("文件上传失败:" + partUpload.getMessage());

			return;
		}

		if (StringUtil.isBlank(localPath)) {
			remark = "文件上传失败,本地路径为空";
			valiDateFailFlag = true;
		} else {
			uploadFile = new File(localPath);
			if (!uploadFile.exists()) {
				remark = "文件上传失败,本地文件不存在";
				valiDateFailFlag = true;
			}
		}
		if (valiDateFailFlag) {
			logger.error(remark + " " + key);
			songPlayFile.setStatus(4);
			songPlayFile.setBak1(remark);
			songPlayFileService.updateFileStatus(songPlayFile);
			return;
		}
		String bucket = AliyunOSSUtil.getDeafultBucket();
		partUpload = new MultipartLocalFileUpload(bucket, key,
				songPlayFile.getMd5(), uploadFile);
		flag = partUpload.upload();
		if (flag) {
			songPlayFile.setStatus(3);
		} else {
			songPlayFile.setStatus(8);
			songPlayFile.setBak1("文件上传失败:" + partUpload.getMessage());
		}
		songPlayFileService.updateFileStatus(songPlayFile);
	}
}