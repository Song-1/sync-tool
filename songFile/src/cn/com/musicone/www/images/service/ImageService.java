/**
 * 
 */
package cn.com.musicone.www.images.service;

import java.util.List;
import java.util.Map;

import cn.com.musicone.www.images.model.ImageModel;
import cn.com.musicone.www.images.model.SongModel;

/**
 * @author Administrator
 *
 */
public interface ImageService {
public void updateSongs(SongModel model)throws Exception;
	
	public List<SongModel> listSongs()throws Exception;
	public List<String> listSongSingerName()throws Exception;
	
	public void updateImage(ImageModel model) throws Exception;

	public List<ImageModel> queryImages();
	
	public void save(ImageModel model)throws Exception;
	
	/**
	 * 专辑图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryAlbumImages();

	/**
	 * 歌手 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> querySingerImages() ;

	/**
	 * banner 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryBannerImages() ;

	/**
	 * 书籍 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryBookImages() ;

	/**
	 * 歌单环境标签 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryEnvironmentImages() ;

	/**
	 * 歌单 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> querySonglistImages() ;

	/**
	 * 专辑风格 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryStyleImages() ;

}
