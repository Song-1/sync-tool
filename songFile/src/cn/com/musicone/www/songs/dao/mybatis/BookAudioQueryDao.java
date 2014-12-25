/**
 * 
 */
package cn.com.musicone.www.songs.dao.mybatis;

import java.util.List;

import com.song1.www.book.pojo.BookAudioNew;

/**
 * @author Administrator
 *
 */
public interface BookAudioQueryDao {
	public int listAudiosToUploadCount()throws Exception;
	
	public List<BookAudioNew> listAudiosToUpload()throws Exception;
	
	public void updateAudioStatus(BookAudioNew file)throws Exception;
}
