/**
 * 
 */
package cn.com.musicone.www.songs.dao.mybatis;

import java.util.List;

import com.song1.www.education.pojo.EducFile;

/**
 * 
 * @ClassName: SongPlayFileQueryDao 
 * @Description: 歌曲文件dao
 * @author Jeckey Lau
 * @date 2015年7月14日 下午1:32:27
 */
public interface EducationFileQueryDao {
	
	/**
	 * 阿里云文件个数
	 * @return
	 * @throws Exception
	 */
	public int listAliyunFilesCount()throws Exception;
	/**
	 * 阿里云上传的文件
	 * @return
	 * @throws Exception
	 */
	public List<EducFile> listAliyunToUpload(int start)throws Exception;
	/**
	 * 查询歌曲需要上传的总数
	 * @return
	 * @throws Exception
	 */
	public int listFilesToUploadCount()throws Exception;
	/**
	 * 查询需要上传的歌曲,每次获取100条
	 * @return
	 * @throws Exception
	 */
	public List<EducFile> listFilesToUpload()throws Exception;
	/**
	 * 
	 * 又拍云需要上传的文件
	 * @param start 开始文件
	 * @return
	 * @throws Exception
	 */
	public List<EducFile> listUpYunFiles(int start)throws Exception;
	/**
	 * 又拍云需要上传的文件总数
	 * @return
	 * @throws Exception
	 */
	public int listUpYunFilesCount()throws Exception;
	
	/**
	 * 更新文件状态
	 * @param file
	 * @throws Exception
	 */
	public void updateFileStatus(EducFile file)throws Exception;
	
	/**
	 * 
	 * 批量更新文件状态
	 * @param files
	 * @throws Exception
	 */
	public void updateUpyunFileStatus(List<Integer> files)throws Exception;

}
