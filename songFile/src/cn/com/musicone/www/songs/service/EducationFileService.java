/**
 * 
 */
package cn.com.musicone.www.songs.service;

import java.util.List;

import com.song1.www.education.pojo.EducFile;
import com.song1.www.songs.pojo.SongPlayFile;

/**
 * 
 * @ClassName: SongPlayFileService 
 * @Description: 歌曲文件服务
 * @author Jeckey Lau
 * @date 2015年7月14日 下午12:24:57
 */
public interface EducationFileService {
	
	public int listUploadDataCounts()throws Exception;
	
	/**
	 * 获取待上传的歌曲文件
	 * @return
	 * @throws Exception
	 */
	public List<EducFile> listFiles()throws Exception;
	
	public void updateFileStatus(EducFile file)throws Exception;
	public List<EducFile> listUpYunFiles(int start)throws Exception;
	/**
	 * 查询又拍云需要上传的文件数量
	 * @return
	 * @throws Exception
	 */
	public int listUpYunFilesCount() throws Exception;
	public void updateUpyunFileStatus(List<Integer> files)throws Exception;

	/**
	 * 查询
	 * @return
	 */
	public int listAliyunFilesCount() throws Exception;

}
