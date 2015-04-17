/**
 * 
 */
package cn.com.musicone.www.files;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.omg.CORBA.StringHolder;

import cn.com.musicone.www.base.utils.BaseMD5Util;
import cn.com.musicone.www.base.utils.HttpClientUtil;
import cn.com.musicone.www.base.utils.HttpResponseData;
import cn.com.musicone.www.files.model.SongFileFormBean;

import com.google.gson.Gson;
import com.jelly.liu.audiotag.AudioTagUtil;
import com.jelly.liu.audiotag.ReadFileTag;
import com.jelly.liu.audiotag.TagModel;

/**
 * @author Administrator
 *
 */
public class SongFileApi {
	private static final String BASEAPIPATH = "http://localhost:8080/song1/mgr/songs/songfile";

	// // main test
	public static void main(String[] args) throws Exception {
//		update();
//		batchAdd();
		sync();
	}
	
	public static void sync(){
		String url = BASEAPIPATH + "/syncdata?id=1,2,3,4,5,6,7,8,9,10,11,12";
		long start = System.currentTimeMillis();
		HttpResponseData data = HttpClientUtil.doGet(url);
		long end = System.currentTimeMillis();
		System.out.println((end - start) + " ms ");
		System.out.println(data.getCode());
		if (data.getCode() == HttpStatus.SC_OK) {
			System.out.println(data.getData());
		}
	}
	
	public static SongFileFormBean convert(File f) throws Exception{
		if (f == null) {
			return null;
		}
		if (!f.exists()) {
			return null;
		}
		TagModel tagModel1 = AudioTagUtil.readTag(f);
		SongFileFormBean bean = new SongFileFormBean();
		String md5 = BaseMD5Util.getMd5ByFile(f);
		////
		bean.setSongName(tagModel1.getSongName());
		bean.setAlbumName(tagModel1.getAlbumName());
		bean.setSingerName(tagModel1.getSingerName());
		bean.setBitrate(tagModel1.getBitrate());
		bean.setSamplerate(tagModel1.getSamplerate());
		bean.setChannels(tagModel1.getChannels());
		bean.setDuration(tagModel1.getDuration());
		bean.setYear(tagModel1.getYear());
		bean.setSuffix(ReadFileTag.getSuffix(f.getName()));
		///
		bean.setGenre(tagModel1.getGenre());
		bean.setCommand(tagModel1.getCommand());
		bean.setCompose(tagModel1.getCompose());
		bean.setTrackNo(tagModel1.getTrackNo());
		bean.setTrackTotal(tagModel1.getTrackTotal());
		bean.setDiscNo(tagModel1.getDiscNo());
		bean.setDiscTotal(tagModel1.getDiscTotal());
		////
		bean.setOssType("upyun");
		bean.setLocalPath(f.getAbsolutePath());
		bean.setFileSize(f.length());
		bean.setMd5(md5);
		return bean;
	}
	
	public static boolean add(SongFileFormBean bean,StringHolder strh){
		if(bean == null){
			strh.value = "文件保存信息为空";
			return false;
		}
		String url = BASEAPIPATH ;
		Map<String, String> params = new HashMap<String, String>();
		params.put("genre",bean.getGenre());
		params.put("songName",bean.getSongName());
		params.put("albumName",bean.getAlbumName());
		params.put("singerName",bean.getSingerName());
		params.put("year",bean.getYear());
		params.put("bitrate",bean.getBitrate()+"");
		params.put("suffix",bean.getSuffix());
		/////
		params.put("samplerate",bean.getSamplerate());
		params.put("duration",bean.getDuration()+"");
		params.put("channels",bean.getChannels());
		////
		params.put("command",bean.getCommand());
		params.put("compose",bean.getCompose());
		params.put("trackNo",bean.getTrackNo());
		params.put("trackTotal",bean.getTrackTotal());
		params.put("discNo",bean.getDiscNo());
		params.put("discTotal",bean.getDiscTotal());
		////
		params.put("ossType",bean.getOssType());
		params.put("localPath",bean.getLocalPath());
		params.put("fileSize",bean.getFileSize()+"");
		params.put("md5",bean.getMd5());
		params.put("ossKey",bean.getOssKey());
		params.put("songid",bean.getSongid());
		HttpResponseData data = HttpClientUtil.doPost(url, params);
		strh.value = data.getData();
		if (data.getCode() == HttpStatus.SC_OK) {
			return true;
		}
		return false;
	}

	public static boolean batchAdd(List<SongFileFormBean> beans) {
		String url = BASEAPIPATH + "/batch_new";
		Gson gson = new Gson();
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", gson.toJson(beans));
		HttpResponseData data = HttpClientUtil.doPost(url, params);
		System.out.println(data.getCode());
		if (data.getCode() == HttpStatus.SC_OK) {
			System.out.println(data.getData());
		}
		return false;
	}

	public static void update() throws Exception {
		String path = "E:\\享CD\\20140930-刘柱栋\\Hip-hop\\Jennifer Lopez\\Rebirth\\08 - Ryde Or Die.flac";
		File f = new File(path);
		if (!f.exists()) {
			return;
		}
		TagModel tagModel1 = AudioTagUtil.readTag(f);
		Map<String, String> params = new HashMap<String, String>();
		String md5 = BaseMD5Util.getMd5ByFile(f);
		params.put("md5",md5);
//		params.put("genre",tagModel1.getGenre());
		params.put("songName",tagModel1.getSongName());
		params.put("albumName",tagModel1.getAlbumName());
		params.put("singerName",tagModel1.getSingerName());
		params.put("bitrate",tagModel1.getBitrate()+"");
		params.put("samplerate",tagModel1.getSamplerate());
		params.put("channels",tagModel1.getChannels());
		params.put("command",tagModel1.getCommand());
		params.put("compose",tagModel1.getCompose());
		params.put("trackNo",tagModel1.getTrackNo());
		params.put("trackTotal",tagModel1.getTrackTotal());
		params.put("discNo",tagModel1.getDiscNo());
		params.put("discTotal",tagModel1.getDiscTotal());
		params.put("duration",tagModel1.getDuration()+"");
		params.put("suffix",ReadFileTag.getSuffix(f.getName()));
		params.put("md5","aliyun");
		params.put("localPath",f.getAbsolutePath());
		params.put("fileSize",f.length()+"");
		params.put("year",tagModel1.getYear());
		params.put("genre", "古典");
		long start = System.currentTimeMillis();
		String url = BASEAPIPATH + "/39";
		HttpResponseData data = HttpClientUtil.doPost(url, params);
		long end = System.currentTimeMillis();
		System.out.println((end - start) + " ms ");
		System.out.println(data.getCode());
		if (data.getCode() == HttpStatus.SC_OK) {
			System.out.println(data.getData());
		}else{
			System.out.println(data.getData());
		}
	}

}
