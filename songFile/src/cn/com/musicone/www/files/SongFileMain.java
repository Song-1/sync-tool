/**
 * 
 */
package cn.com.musicone.www.files;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.StringHolder;

import cn.com.musicone.www.base.utils.BaseMD5Util;
import cn.com.musicone.www.base.utils.StringUtil;
import cn.com.musicone.www.files.model.FileLocalPathModel;
import cn.com.musicone.www.files.model.SongFileFormBean;
import cn.com.musicone.www.files.model.SongFileInfoModel;
import cn.com.musicone.www.files.service.FileService;
import cn.com.musicone.www.files.service.impl.FileServiceImpl;
import cn.com.musicone.www.mybatis.MybatisUtil;

import com.jelly.liu.audiotag.AudioTagUtil;
import com.jelly.liu.audiotag.TagModel;

/**
 * @author Administrator
 *
 */
public class SongFileMain {
	protected static final Logger logger = LogManager.getLogger(SongFileMain.class);
	//// test
	public static void main(String[] args) throws Exception {
		MybatisUtil.init();
//		EachFile.start();//// 检索文件
		getFiles();
//		listFiles();
	}
	
	public static FileService fileService = new FileServiceImpl();
	
	public static void listFiles(){
		List<FileLocalPathModel> datas = null;
		while(true){
			try {
				logger.debug("获取未处理的文件数据");
				datas = fileService.listFileLocalPathDatas(0);
			} catch (Exception e) {
				logger.error("获取未处理的文件数据发生异常",e);
			}
			if(datas == null || datas.isEmpty()){
				logger.debug("处理文件结束");
				break;
			}
			writeTags(datas);
		}
	}
	
	public static void writeTags(List<FileLocalPathModel> datas){
		if(datas == null || datas.isEmpty()){
			return ;
		}
		for (FileLocalPathModel fileLocalPathModel : datas) {
			if(fileLocalPathModel == null){
				continue;
			}
			logger.info(fileLocalPathModel.getFilePath() + " 计算文件MD5");
//			writeTag(fileLocalPathModel);
			File f = new File(fileLocalPathModel.getFilePath());
			String md5 =null;
			try {
				md5 = BaseMD5Util.getMd5ByFile(f);
			} catch (Exception e) {
				logger.error(fileLocalPathModel.getFilePath() + " 计算文件MD5 发生异常");
			} 
			if(StringUtils.isBlank(md5)){
				continue;
			}
			fileLocalPathModel.setMd5(md5);
			try {
				fileService.updateFileLocalPath(fileLocalPathModel);
			} catch (Exception e) {
				logger.error(fileLocalPathModel.getFilePath() + " 更新文件数据发生异常",e);
			}
//			try {
//				List<SongFileInfoModel> tagmodels = fileService.listFileInfos(fileLocalPathModel);
//				if(tagmodels == null || tagmodels.isEmpty()){
//					try {
//						fileService.updateFileLocalPath(fileLocalPathModel);
//					} catch (Exception e) {
//						logger.error(fileLocalPathModel.getFilePath() + " 更新文件数据发生异常",e);
//					}
//					continue;
//				}
//				writeFileTags(tagmodels, md5);
//			} catch (Exception e) {
//				logger.error(fileLocalPathModel.getFilePath() + " 获取未处理的文件数据发生异常",e);
//			}
//			
//			try {
//				fileService.updateFileLocalPath(fileLocalPathModel);
//			} catch (Exception e) {
//				logger.error(fileLocalPathModel.getFilePath() + " 更新文件数据发生异常",e);
//			}
		}
//		try {
//			fileService.updateFileLocalPaths(datas);
//		} catch (Exception e) {
//			logger.error("批量更新文件数据发生异常",e);
//		}
	}
	
	public static void getFiles(){
		List<SongFileInfoModel> tagmodels = null;
		while(true){
			try {
				logger.debug("获取未处理的文件数据");
				tagmodels = fileService.listFileInfos(null);
			} catch (Exception e) {
				logger.error("获取未处理的文件数据发生异常",e);
			}
			if(tagmodels == null || tagmodels.isEmpty()){
				logger.debug("处理文件结束");
				break;
			}
			writeFileTags(tagmodels,null);
		}
	}
	public static void writeFileTags(List<SongFileInfoModel> datas,String md5){
		if(datas == null || datas.isEmpty()){
			return ;
		}
		for (SongFileInfoModel tags : datas) {
			if(tags == null){
				continue;
			}
			FileLocalPathModel fileLocalPathModel = new FileLocalPathModel();
			fileLocalPathModel.setId(tags.getId());
			if(StringUtil.isNotBlank(md5)){
				fileLocalPathModel.setMd5(md5);
			}
			writeTag(tags,fileLocalPathModel);
			try {
				fileService.updateFileLocalPath(fileLocalPathModel);
			} catch (Exception e) {
				logger.error(fileLocalPathModel.getFilePath() + " 更新文件数据发生异常",e);
			}
		}
//		try {
//			fileService.updateFileLocalPaths(datas);
//		} catch (Exception e) {
//			logger.error("批量更新文件数据发生异常",e);
//		}
	}
	
	public static void writeTag(SongFileInfoModel tag,FileLocalPathModel fileLocalPathModel ){
		if(tag == null){
			return;
		}
		String path = tag.getFilePath();
		if(StringUtil.isBlank(path)){
			fileLocalPathModel.setRemark("本地路径为空");
			fileLocalPathModel.setStatus(2);
			return;
		}
		File f = new File(path);
		if(!f.exists()){
			fileLocalPathModel.setRemark("路径::"+path + "  文件不存在 ");
			fileLocalPathModel.setStatus(6);
			return;
		}
		////
		TagModel tagModel = new TagModel();
		tagModel.setSongName(tag.getSongName());
		tagModel.setAlbumName(tag.getAlbumName());
		tagModel.setSingerName(tag.getSingerName());
		tagModel.setYear(tag.getYear());
		tagModel.setGenre(tag.getGenre());
		if(StringUtil.isBlank(tagModel.getSongName()) || StringUtil.isBlank(tagModel.getAlbumName()) || StringUtil.isBlank(tagModel.getSingerName()) || StringUtil.isBlank(tagModel.getYear())){
			fileLocalPathModel.setRemark("根据文件的md5或osskey 查询到相关数据必填数据不全");
			fileLocalPathModel.setStatus(10);
			return;
		}
		/////
		try {
			if(!f.canWrite()){
				f.setWritable(true);
			}
			boolean flag = AudioTagUtil.writeTag(f, tagModel);
			if(!flag){
				fileLocalPathModel.setRemark("写入文件ID3 失败");
				fileLocalPathModel.setStatus(7);
				return;
			}
		} catch (Exception e) {
			fileLocalPathModel.setRemark("写入文件ID3发生异常");
			fileLocalPathModel.setStatus(7);
			logger.error(path +"写入文件ID3发生异常",e);
			return;
		}
		/////
		SongFileFormBean bean = null;
		try {
			 bean = SongFileApi.convert(f);
		} catch (Exception e) {
			logger.error(path +"获取文件ID3发生异常",e);
		}
		if(bean == null ){
			fileLocalPathModel.setRemark("获取文件ID3 失败");
			fileLocalPathModel.setStatus(7);
			return;
		}
		////
		String songid = tag.getSongId() + "";
		bean.setSongid(songid);
		bean.setOssKey(fileLocalPathModel.getOssKey());
		StringHolder strh = new StringHolder("");
		boolean flag = SongFileApi.add(bean, strh);
		if(flag){
			fileLocalPathModel.setStatus(1);
			fileLocalPathModel.setMd5(bean.getMd5());
		}else{
			String str = strh.value;
			if(str.indexOf("此文件数据已经存在") > 0){
				fileLocalPathModel.setStatus(1);
			}else{
				logger.info(path + "add to server error ::: " + strh.value);
				//fileLocalPathModel.setRemark(strh.value);
				fileLocalPathModel.setStatus(9);
			}
		}
	}

}
