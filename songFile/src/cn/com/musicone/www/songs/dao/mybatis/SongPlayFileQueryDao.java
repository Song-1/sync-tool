/**
 * 
 */
package cn.com.musicone.www.songs.dao.mybatis;

import java.util.List;

import com.song1.www.songs.pojo.SongPlayFile;

/**
 * @author Administrator
 *
 */
public interface SongPlayFileQueryDao {
	
	public int listFilesToUploadCount()throws Exception;
	
	public List<SongPlayFile> listFilesToUpload()throws Exception;
	
	public void updateFileStatus(SongPlayFile file)throws Exception;

}
