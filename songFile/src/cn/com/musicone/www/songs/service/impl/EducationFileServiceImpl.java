/**
 * 
 */
package cn.com.musicone.www.songs.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.songs.dao.mybatis.EducationFileQueryDao;
import cn.com.musicone.www.songs.service.EducationFileService;

import com.song1.www.education.pojo.EducFile;
import com.song1.www.songs.pojo.SongPlayFile;

/**
 * @author Administrator
 *
 */
public class EducationFileServiceImpl implements EducationFileService{
	
	protected EducationFileQueryDao educationFileQueryDao;
	
	public EducationFileQueryDao getDao()throws Exception{
		SqlSession session = MybatisUtil.getSqlSession();
		if(session == null){
			IllegalArgumentException e = new IllegalArgumentException("数据库连接session不存在");
			throw e;
		}
		educationFileQueryDao = session.getMapper(EducationFileQueryDao.class);
		return educationFileQueryDao;
	}

	@Override
	public List<EducFile> listFiles() throws Exception {
		return getDao().listFilesToUpload();
	}

	@Override
	public void updateFileStatus(EducFile file) throws Exception {
		getDao().updateFileStatus(file);
	}

	@Override
	public int listUploadDataCounts() throws Exception {
		return getDao().listFilesToUploadCount();
	}
	
	@Override
	public int listAliyunFilesCount() throws Exception {
		return getDao().listAliyunFilesCount();
	}


	@Override
	public List<EducFile> listUpYunFiles(int start) throws Exception {
		if(start < 0){
			start = 0;
		}
		int count = listUpYunFilesCount();
		if(start > count){
			return null;
		}
		return getDao().listUpYunFiles(start);
	}

	@Override
	public int listUpYunFilesCount() throws Exception {
		return getDao().listUpYunFilesCount();
	}

	@Override
	public void updateUpyunFileStatus(List<Integer> files)
			throws Exception {
		if(files == null){
			return;
		}else if(files.isEmpty()){
			return;
		}
		getDao().updateUpyunFileStatus(files);
	}

}
