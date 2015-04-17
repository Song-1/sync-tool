/**
 * 
 */
package cn.com.musicone.www.files;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.FileUtils;
import cn.com.musicone.www.base.utils.MemcachedUtil;
import cn.com.musicone.www.files.model.FileLocalPathModel;
import cn.com.musicone.www.files.service.FileService;
import cn.com.musicone.www.files.service.impl.FileServiceImpl;
import cn.com.musicone.www.mybatis.MybatisUtil;

import com.jelly.liu.audiotag.ReadFileTag;

/**
 * @author Administrator
 *
 */
public class EachFile {
	protected static final Logger logger = LogManager.getLogger(EachFile.class);
	public static String rootPath = "";
	public static String fileEachPath = "";

	public static FileService fileService = null;
	public static List<FileLocalPathModel> fileModels = new ArrayList<FileLocalPathModel>();
	public static String MEMCACHED_DATA_KEY = "memcache_local_file_data_key";
	public static Map<String, String> cacheMap = null;

	/**
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		MybatisUtil.init();
//		MemcachedUtil.init();
		 start();
		// MemcachedUtil.set(MEMCACHED_DATA_KEY, new HashMap<String, String>());
		// MemcachedUtil.getClient().delete(MEMCACHED_DATA_KEY);
	}

//	@SuppressWarnings("unchecked")
	public static void start() throws Exception {
		init();
		// cacheMap = (Map<String, String>)
		// MemcachedUtil.get(MEMCACHED_DATA_KEY);
		// if (cacheMap == null) {
		// cacheMap = new HashMap<String, String>();
		// }
		File file = new File(fileEachPath);
		eachFiles(file);
		saveFiles(true);
		// MemcachedUtil.set(MEMCACHED_DATA_KEY, cacheMap);
	}

	/**
	 * init
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		Properties p = FileUtils.loadProperties("aliyun_oss_config.properties");
		if (p == null) {
			throw new RuntimeException("load properties error...........");
		}
		rootPath = p.getProperty("file.local.path.oss.key.root", "");
		fileEachPath = p.getProperty("file.local.path.root");
		fileService = new FileServiceImpl();
	}

	public static void eachFiles(File directory) {
		if (!directory.exists()) {
			return;
		}
		if (directory.isFile()) {
			long start = System.currentTimeMillis();
			addFile(directory);
			long end = System.currentTimeMillis();
			logger.info(directory.getAbsolutePath() + "   cost times :::: "
					+ (end - start) + " ms ");
		}
		if (!directory.isDirectory()) {
			return;
		}
		logger.info("加载文件夹:::" + directory.getAbsolutePath());
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname == null) {
					return false;
				}
				if (!pathname.exists()) {
					return false;
				}
				if (pathname.isDirectory()) {
					return true;
				}
				if (pathname.isFile()) {
					String path = pathname.getName();
					String suffix = ReadFileTag.getSuffix(path);
					if (ReadFileTag.isMacth(suffix, ReadFileTag.SUFFIX_FLAC)) {
						return true;
					} else if (ReadFileTag.isMacth(suffix,
							ReadFileTag.SUFFIX_MP3)) {
						return true;
					}
				}
				return false;
			}
		};
		File[] files = directory.listFiles(filter);
		if (files == null) {
			return;
		}
		logger.info("开始遍历文件夹:::" + directory.getAbsolutePath());
		for (File file : files) {
			eachFiles(file);
		}
	}

	public static void addFile(File file) {
		String path = file.getAbsolutePath();
		// if (cacheMap.get(path) != null) {
		// return;
		// }
		// try {
		// datas = fileService.getFileLocalPathData(path);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		FileLocalPathModel model = new FileLocalPathModel();
		model.setFilePath(path);
		// String md5 = null;
		// try {
		// md5 = BaseMD5Util.getMd5ByFile(file);
		// } catch (Exception e) {
		// logger.error("计算md5值发生异常",e);
		// }
		// model.setMd5(md5);
		String ossKey = getOssKey(path);
		model.setOssKey(ossKey);
//		cacheMap.put(path, ossKey);
		fileModels.add(model);
		saveFiles(false);
	}

	public static void saveFiles(boolean isOver) {
		if (fileModels == null) {
			fileModels = new ArrayList<FileLocalPathModel>();
			return;
		}
		if (isOver || fileModels.size() == 100) {
			try {
				fileService.addFileLocalPath(fileModels);
			} catch (Exception e) {
				logger.error("保存文件数据发生异常", e);
			}
			fileModels = new ArrayList<FileLocalPathModel>();
		}
	}

	public static String getOssKey(String filePath) {
		if (StringUtils.isBlank(filePath)) {
			return null;
		}
		String osskey = StringUtils.replace(filePath, rootPath, "");
		if (!"/".equals(File.separator)) {
			osskey = StringUtils.replace(osskey, File.separator, "/");
		}
		osskey = StringUtils.replace(osskey, rootPath, "");
		if (StringUtils.startsWith(osskey, "/")) {
			osskey = StringUtils.substring(osskey, 1);
		}
		return osskey;
	}

}
