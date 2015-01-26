package cn.com.musicone.www.images.dao.mybatis;

import java.util.List;

import cn.com.musicone.www.images.model.ImageModel;

public interface ImageQueryDao {

	public void logImageData(ImageModel model) throws Exception;

	/**
	 * 专辑图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryAlbumImages() throws Exception;

	/**
	 * 歌手 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> querySingerImages() throws Exception;

	/**
	 * banner 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryBannerImages() throws Exception;

	/**
	 * 书籍 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryBookImages() throws Exception;

	/**
	 * 歌单环境标签 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryEnvironmentImages() throws Exception;

	/**
	 * 歌单 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> querySonglistImages() throws Exception;

	/**
	 * 专辑风格 图片查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ImageModel> queryStyleImages() throws Exception;

}
