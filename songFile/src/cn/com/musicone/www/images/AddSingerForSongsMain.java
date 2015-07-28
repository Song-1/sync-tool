/**
 * 
 */
package cn.com.musicone.www.images;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.musicone.www.base.utils.HttpClientUtil;
import cn.com.musicone.www.base.utils.HttpResponseData;
import cn.com.musicone.www.images.model.SongModel;
import cn.com.musicone.www.images.service.ImageService;
import cn.com.musicone.www.images.service.impl.ImageServiceImpl;
import cn.com.musicone.www.mybatis.MybatisUtil;
import cn.com.musicone.www.oss.aliyun.AliyunOSSUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.song1.www.songs.http.action.mgr.bean.SingerFormBean;
import com.song1.www.songs.http.action.mgr.bean.SingerNameFormBean;

/**
 * @author Administrator
 *
 */
public class AddSingerForSongsMain {

	private static final String BASEAPIPATH = "http://localhost:8080/song1/mgr/songs";
	protected static final Logger logger = LogManager
			.getLogger(CopyImageMain.class);
	public static ImageService imageService = new ImageServiceImpl();

	public static void main(String[] args) throws Exception {
		AliyunOSSUtil.init();
		MybatisUtil.init();
		//// 新增歌手
		//addSingers();
		//// 更新歌曲的所属歌手信息
		addSongSinger();
	}

	public static void addSingers() {
		try {
			List<String> singerNames = imageService.listSongSingerName();
			if (singerNames == null) {
				logger.debug("没有要新增的歌手数据");
				return;
			}
			int count = singerNames.size();
			int index = 0;
			for (String singerName : singerNames) {
				addSinger(singerName);
				index ++;
				System.out.println("新增歌手进度 :::: count = " +count + " ,current index = " + index + ", p :::: " + ( (index * 100) / count ) + "% ");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void addSinger(String name) {
		if (StringUtils.isBlank(name)) {
			return;
		}
		name = name.trim();
//		logger.debug("新增歌手::::" + name);
		long start = System.currentTimeMillis();
		String url = BASEAPIPATH + "/singer";
		SingerFormBean bean = new SingerFormBean();
		bean.setAreacode("other");
		List<SingerNameFormBean> names = new ArrayList<SingerNameFormBean>();
		SingerNameFormBean zhName = new SingerNameFormBean();
		zhName.setSingername(name);
		zhName.setType(1);
		names.add(zhName);
		bean.setNames(names);
		String datas = bean.toJson();
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", datas);
		HttpResponseData data = HttpClientUtil.doPost(url, params);
		System.out.println(data.getCode());
		if (data.getCode() == HttpStatus.SC_OK) {
			logger.debug("新增歌手::::" + name + "  成功  ");
		} else {
			logger.debug("新增歌手::::" + name + "  失败  ");
			System.out.println(data.getData());
		}
		long end = System.currentTimeMillis();
		System.out.println("cost times ::: " + (end - start) + "  ms ");
	}

	public static void addSongSinger() {
		try {
			long start = System.currentTimeMillis();
			List<SongModel> singerNames = imageService.listSongs();
			long end = System.currentTimeMillis();
			System.out.println("cost times ::: " + (end - start) + "  ms ");
			if (singerNames == null) {
				logger.debug("没有要新增的歌手的歌曲数据");
				return;
			}
			int count = singerNames.size();
			int index = 0;
			for (SongModel data : singerNames) {
				index ++;
				if (data == null) {
					continue;
				}
				addSongSinger(data);
				System.out.println("歌曲新增歌手进度 :::: count = " +count + " ,current index = " + index + ", p :::: " + ( (index * 100) / count ) + "% ");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void addSongSinger(SongModel model) {
		try {
			if (model == null) {
				return;
			}
			int id = model.getId();
			if (id <= 0) {
				return;
			}
			int songId = model.getSongId();
			String singerName = model.getSinger();
			if (songId <= 0) {
				return;
			}
			if (StringUtils.isBlank(singerName)) {
				return;
			}
			singerName = singerName.trim();
//			long start = System.currentTimeMillis();
			String url = BASEAPIPATH + "/song/updatesinger/" + songId;
			Map<String, String> params = new HashMap<String, String>();
			params.put("singer", singerName);
			HttpResponseData data = HttpClientUtil.doPost(url, params);
//			System.out.print(data.getCode());
			int code = data.getCode();
			String json = data.getData();
			if ( code == HttpStatus.SC_OK) {
				model.setStatus("OK");
			}else if(code == 420){
				//// 操作失败:songId已经存在
				ResultDataModel result = formatJson(json);
				if(result != null){
					logger.info(id + " , " + songId + " , " + singerName + " , " + result.getMessage());
				}else{
					logger.info(id + " , " + songId + " , " + singerName );
				}
			}
			else {
				logger.error(id + " , " + songId + " , " + singerName  + " 失败   code ::: " + code + "  message ::: " + json);
//				model.setStatus("FAIL");
//				logger.debug("URL ::: " + url + "  失败  ");
//				model.setRemark(data.getData());
//				System.out.print ("  " + data.getData());
				//imageService.updateSongs(model);
			}
//			long end = System.currentTimeMillis();
//			System.out.println("  " + id + " cost times ::: " + (end - start) + "  ms ");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private static ResultDataModel formatJson(String json){
		if(StringUtils.isBlank(json)){
			return null;
		}
		Gson gson = new Gson();
		Type gsonType = new TypeToken<ResultDataModel>() {}.getType();
		ResultDataModel bean = gson.fromJson(json, gsonType);
		return bean;
	}
	
}

class ResultDataModel{
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
