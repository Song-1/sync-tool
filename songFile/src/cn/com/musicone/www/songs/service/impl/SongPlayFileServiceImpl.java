/**
 * 
 */
package cn.com.musicone.www.songs.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.songs.dao.mybatis.SongPlayFileQueryDao;
import cn.com.musicone.www.songs.service.SongPlayFileService;

import com.song1.www.songs.pojo.SongPlayFile;

/**
 * @author Administrator
 *
 */
public class SongPlayFileServiceImpl implements SongPlayFileService{
	
	protected SongPlayFileQueryDao songPlayFileQueryDao;
	
	public SongPlayFileQueryDao getDao()throws Exception{
		SqlSession session = MybatisUtil.getSqlSession();
		if(session == null){
			IllegalArgumentException e = new IllegalArgumentException("数据库连接session不存在");
			throw e;
		}
		songPlayFileQueryDao = session.getMapper(SongPlayFileQueryDao.class);
		return songPlayFileQueryDao;
	}

	@Override
	public List<SongPlayFile> listFiles() throws Exception {
		return getDao().listFilesToUpload();
	}

	@Override
	public void updateFileStatus(SongPlayFile file) throws Exception {
		getDao().updateFileStatus(file);
	}

}
