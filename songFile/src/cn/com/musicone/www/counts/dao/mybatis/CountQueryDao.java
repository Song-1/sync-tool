/**
 * 
 */
package cn.com.musicone.www.counts.dao.mybatis;

import java.util.List;

import cn.com.musicone.www.counts.model.CountModel;
import cn.com.musicone.www.counts.model.SearchReqLogModel;

/**
 * @author Administrator
 *
 */
public interface CountQueryDao {

	public List<CountModel> querySingerCountsByDate(SearchReqLogModel qbo)
			throws Exception;

	public void addSingerCountLog(List<CountModel> datas) throws Exception;

	public void addAlbumCountLog(List<CountModel> datas) throws Exception;

	public void addSongCountLog(List<CountModel> datas) throws Exception;

	public void addAlbumsCountLog(CountModel data) throws Exception;

	public CountModel countAlbumCollect(SearchReqLogModel qbo) throws Exception;
}
