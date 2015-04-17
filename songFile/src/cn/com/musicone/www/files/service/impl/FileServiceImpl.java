/**
 * 
 */
package cn.com.musicone.www.files.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.counts.service.impl.CountQuerySerivceImpl;
import cn.com.musicone.www.files.dao.mybatis.FileQueryDao;
import cn.com.musicone.www.files.model.FileLocalPathModel;
import cn.com.musicone.www.files.model.SongFileInfoModel;
import cn.com.musicone.www.files.service.FileService;
import cn.com.musicone.www.mybatis.MybatisUtil;

/**
 * @author Administrator
 *
 */
public class FileServiceImpl implements FileService{
	protected static final Logger logger = LogManager.getLogger(CountQuerySerivceImpl.class);
	protected FileQueryDao fileQueryDao;

	public FileQueryDao getDao() throws Exception {
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			IllegalArgumentException e = new IllegalArgumentException(
					"数据库连接session不存在");
			throw e;
		}
		fileQueryDao = session.getMapper(FileQueryDao.class);
		return fileQueryDao;
	}

	@Override
	public void addFileLocalPath(List<FileLocalPathModel> datas)
			throws Exception {
		if(datas == null || datas.isEmpty()){
			return ;
		}
		getDao().addFileLocalPath(datas);
	}

	@Override
	public int getFileLocalPathData(String path) throws Exception {
		if(StringUtils.isBlank(path)){
			return 0;
		}
		return getDao().getFileLocalPathData(path);
	}

	@Override
	public List<FileLocalPathModel> listFileLocalPathDatas(int start)
			throws Exception {
		return getDao().listFileLocalPathDatas(start);
	}

	@Override
	public List<SongFileInfoModel> listFileInfos(FileLocalPathModel model)
			throws Exception {
//		if(model == null){
//			return null;
//		}
//		if(StringUtils.isBlank(model.getMd5()) && StringUtils.isBlank(model.getOssKey())){
//			return null;
//		}
		return getDao().listFileInfos(model);
	}

	@Override
	public void updateFileLocalPaths(List<FileLocalPathModel> datas)
			throws Exception {
		if(datas == null || datas.isEmpty()){
			return ;
		}
		getDao().updateFileLocalPaths(datas);
	}

	@Override
	public void updateFileLocalPath(FileLocalPathModel data) throws Exception {
		if(data == null){
			return;
		}
		getDao().updateFileLocalPath(data);
	}
}
