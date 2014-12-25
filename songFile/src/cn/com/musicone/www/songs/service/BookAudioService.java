/**
 * 
 */
package cn.com.musicone.www.songs.service;

import java.util.List;

import com.song1.www.book.pojo.BookAudioNew;

/**
 * @author Administrator
 *
 */
public interface BookAudioService {
	public int listUploadDataCounts() throws Exception;

	public List<BookAudioNew> listFiles() throws Exception;

	public void updateFileStatus(BookAudioNew file) throws Exception;

}
