/**
 * 
 */
package cn.com.musicone.www.counts.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.counts.dao.mybatis.CountQueryDao;
import cn.com.musicone.www.counts.model.CountModel;
import cn.com.musicone.www.counts.model.SearchReqLogModel;
import cn.com.musicone.www.counts.service.CountQueryService;
import cn.com.musicone.www.mybatis.MybatisUtil;

/**
 * @author Administrator
 *
 */
public class CountQuerySerivceImpl implements CountQueryService{
	
	protected static final Logger logger = LogManager.getLogger(CountQuerySerivceImpl.class);
	protected CountQueryDao countQueryDao;

	public CountQueryDao getDao() throws Exception {
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			IllegalArgumentException e = new IllegalArgumentException(
					"数据库连接session不存在");
			throw e;
		}
		countQueryDao = session.getMapper(CountQueryDao.class);
		return countQueryDao;
	}


	@Override
	public void addSingerCountLog(List<CountModel> datas) throws Exception {
		if(datas == null || datas.isEmpty()){
			return;
		}
		getDao().addSingerCountLog(datas);
	}

	@Override
	public List<CountModel> querySingerCountsByDate(SearchReqLogModel qbo)
			throws Exception {
		if(qbo == null){
			logger.error("获取日志分析数据参数对象为空");
			return null;
		}
		return getDao().querySingerCountsByDate(qbo);
	}

	@Override
	public void addAlbumCountLog(List<CountModel> datas) throws Exception {
		if(datas == null || datas.isEmpty()){
			return;
		}
		getDao().addAlbumCountLog(datas);
	}

	@Override
	public void addSongCountLog(List<CountModel> datas) throws Exception {
		if(datas == null || datas.isEmpty()){
			return;
		}
		getDao().addSongCountLog(datas);
	}


	@Override
	public void addAlbumsCountLog(CountModel data) throws Exception {
		if(data == null ){
			return ;
		}
		getDao().addAlbumsCountLog(data);
	}


	@Override
	public CountModel countAlbumCollect(SearchReqLogModel qbo) throws Exception {
		if(qbo == null){
			logger.error("获取日志分析数据参数对象为空");
			return null;
		}
		return getDao().countAlbumCollect(qbo);
	}

}
