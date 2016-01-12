package cn.com.musicone.www.songs.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.song1.www.puresong.pojo.PurSong;

import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.songs.dao.mybatis.PureSongFileQueryDao;
import cn.com.musicone.www.songs.service.PureSongFileService;

public class PureSongFileServiceImpl implements PureSongFileService {
	
	protected PureSongFileQueryDao pureSongFileQueryDao;
	
	public PureSongFileQueryDao getDao()throws Exception{
		SqlSession session = MybatisUtil.getSqlSession();
		if(session == null){
			IllegalArgumentException e = new IllegalArgumentException("数据库连接session不存在");
			throw e;
		}
		pureSongFileQueryDao = session.getMapper(PureSongFileQueryDao.class);
		return pureSongFileQueryDao;
	}

	@Override
	public int listUploadDataCounts() throws Exception {
		return getDao().listFilesToUploadCount();
	}

	@Override
	public List<PurSong> listFiles() throws Exception {
		return getDao().listFilesToUpload();
	}

	@Override
	public void updateFileStatus(PurSong file) throws Exception {
		getDao().updateFileStatus(file);
	}

	@Override
	public void updateFileStatus1(PurSong file) throws Exception {
		getDao().updateFileStatus1(file);
	}

	@Override
	public List<PurSong> listUpYunFiles(int start) throws Exception {
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
	public void updateUpyunFileStatus(List<Integer> files) throws Exception {
		if(files == null){
			return;
		}else if(files.isEmpty()){
			return;
		}
		getDao().updateUpyunFileStatus(files);
	}

	@Override
	public int listAliyunFilesCount() throws Exception {
		return getDao().listAliyunFilesCount();
	}

}
