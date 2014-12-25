/**
 * 
 */
package cn.com.musicone.www.songs.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.song1.www.book.pojo.BookAudioNew;

import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.songs.dao.mybatis.BookAudioQueryDao;
import cn.com.musicone.www.songs.dao.mybatis.SongPlayFileQueryDao;
import cn.com.musicone.www.songs.service.BookAudioService;

/**
 * @author Administrator
 *
 */
public class BookAudioServiceImpl implements BookAudioService{
	protected BookAudioQueryDao bookAudioQueryDao;
	
	public BookAudioQueryDao getDao()throws Exception{
		SqlSession session = MybatisUtil.getSqlSession();
		if(session == null){
			IllegalArgumentException e = new IllegalArgumentException("数据库连接session不存在");
			throw e;
		}
		bookAudioQueryDao = session.getMapper(BookAudioQueryDao.class);
		return bookAudioQueryDao;
	}

	@Override
	public int listUploadDataCounts() throws Exception {
		return getDao().listAudiosToUploadCount();
	}

	@Override
	public List<BookAudioNew> listFiles() throws Exception {
		return getDao().listAudiosToUpload();
	}

	@Override
	public void updateFileStatus(BookAudioNew file) throws Exception {
		getDao().updateAudioStatus(file);		
	}
}
