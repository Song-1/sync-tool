/**
 * 
 */
package cn.com.musicone.www.images.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.images.dao.mybatis.ImageQueryDao;
import cn.com.musicone.www.images.model.ImageModel;
import cn.com.musicone.www.images.service.ImageService;
import cn.com.musicone.www.mybatis.MybatisUtil;

/**
 * @author Administrator
 *
 */
public class ImageServiceImpl implements ImageService {
	protected static final Logger logger = LogManager.getLogger(ImageServiceImpl.class);
	protected ImageQueryDao imageQueryDao;

	public ImageQueryDao getDao() throws Exception {
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			IllegalArgumentException e = new IllegalArgumentException(
					"数据库连接session不存在");
			throw e;
		}
		imageQueryDao = session.getMapper(ImageQueryDao.class);
		return imageQueryDao;
	}

	@Override
	public void save(ImageModel model) throws Exception {
		if(model == null){
			return;
		}
		String  imgKey = model.getImg();
		if(StringUtils.isBlank(imgKey)){
			imgKey = model.getIcon();
		}
		if(StringUtils.isBlank(imgKey)){
			return ;
		}
		getDao().logImageData(model);
	}

	@Override
	public List<ImageModel> queryAlbumImages() {
		List<ImageModel> datas = null;
		try {
			datas = getDao().queryAlbumImages();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			datas = null;
		}
		return datas;
	}

	@Override
	public List<ImageModel> querySingerImages() {
		List<ImageModel> datas = null;
		try {
			datas = getDao().querySingerImages();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			datas = null;
		}
		return datas;
	}

	@Override
	public List<ImageModel> queryBannerImages()  {
		List<ImageModel> datas = null;
		try {
			datas = getDao().queryBannerImages();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			datas = null;
		}
		return datas;
	}

	@Override
	public List<ImageModel> queryBookImages() {
		List<ImageModel> datas = null;
		try {
			datas = getDao().queryBookImages();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			datas = null;
		}
		return datas;
	}

	@Override
	public List<ImageModel> queryEnvironmentImages() {
		List<ImageModel> datas = null;
		try {
			datas = getDao().queryEnvironmentImages();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			datas = null;
		}
		return datas;
	}

	@Override
	public List<ImageModel> querySonglistImages(){
		List<ImageModel> datas = null;
		try {
			datas = getDao().querySonglistImages();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			datas = null;
		}
		return datas;
	}

	@Override
	public List<ImageModel> queryStyleImages(){
		List<ImageModel> datas = null;
		try {
			datas = getDao().queryStyleImages();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			datas = null;
		}
		return datas;
	}

}
