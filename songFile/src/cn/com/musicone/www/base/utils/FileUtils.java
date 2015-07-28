/**
 * 
 */
package cn.com.musicone.www.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

/**
 * @author Administrator
 *
 */
public class FileUtils {
	protected static final Logger logger = LogManager.getLogger(FileUtils.class);
	/**
	 * 日志debug
	 * @param message
	 */
	public static void debug(String message){
		logger.debug(message);
		System.out.println(message);
	}

	/**
	 * 查找文件
	 * 
	 * @param fileRelativePath
	 * @return File
	 */
	public static File findFile(String fileRelativePath) {
		String path = "" ;//= getBasePath();
		path += fileRelativePath;
		LogUtil.debug(logger,path);
		File file = new File(path);
		if (!file.exists()) {
			path  = getBasePath();
			path += fileRelativePath;
			file = new File(path);
		}
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 获取文件的基路径
	 * 
	 * @return
	 */
	public static String getBasePath() {
		String path = FileUtils.class.getResource("/").toString();
		path = path.replace("file:/", "");
		return path;
	}

	/**
	 * 创建文件的目录路径并创建文件.<br>
	 * 
	 * @param file
	 */
	public static void mkDirs(File file) {
		if (file != null && !file.exists()) {
			String path = file.getAbsolutePath();
			int index = path.lastIndexOf(File.separator);
			String dirsPath = new String(path.substring(0, index));
			File filedirs = new File(dirsPath);
			if (!filedirs.exists()) {
				filedirs.mkdirs();
			}
		}
	}
	
	/**
	 * 加载配置文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Properties loadProperties(String path) throws Exception{
		Properties p = new Properties();
		FileInputStream fileInputStream = null;
		File file = findFile(path);
		if (file != null) {
			if (!file.exists()) {
				throw new FileNotFoundException("加载的配置文件不存在" + file.getAbsolutePath());
			}
		} else {
			throw new FileNotFoundException("加载的配置文件为空");
		}
		try {
			fileInputStream = new FileInputStream(file);
			p.load(fileInputStream);
			fileInputStream.close();
		} catch (Exception e) {
			IllegalArgumentException ie = new IllegalArgumentException("加载的配置文件发生异常");
			ie.initCause(e);
			throw ie;
		}
		return p;
	}


}
