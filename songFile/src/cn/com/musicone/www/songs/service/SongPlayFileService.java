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
	public List<SongPlayFile> listUpYunFiles(int start)throws Exception;
	public int listUpYunFilesCount()throws Exception;
	public void updateUpyunFileStatus(List<Integer> files)throws Exception;

}
