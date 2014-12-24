/**
 * 
 */
package cn.com.musicone.www.songs.service;

import java.util.List;

import com.song1.www.songs.pojo.SongPlayFile;

/**
 * @author Administrator
 *
 */
public interface SongPlayFileService {
	
	public int listUploadDataCounts()throws Exception;
	
	public List<SongPlayFile> listFiles()throws Exception;
	
	public void updateFileStatus(SongPlayFile file)throws Exception;

}
