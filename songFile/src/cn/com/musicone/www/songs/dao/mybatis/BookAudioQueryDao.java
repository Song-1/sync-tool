/**
 * 
 */
package cn.com.musicone.www.songs.dao.mybatis;

import java.util.List;

import com.song1.www.book.pojo.BookAudioNew;

/**
 * 
 * @ClassName: BookAudioQueryDao 
 * @Description: 书籍音频dao
 * @author Jeckey Lau
 * @date 2015年7月14日 下午1:57:15
 */
public interface BookAudioQueryDao {
	/**
	 * 查询书籍需要上传的总数
	 * @return
	 * @throws Exception
	 */
	public int listAudiosToUploadCount()throws Exception;
	
	/**
	 * 查询需要上传的书籍
	 * @return
	 * @throws Exception
	 */
	public List<BookAudioNew> listAudiosToUpload()throws Exception;
	
	/**
	 * 更新书籍状态
	 * @param file
	 * @throws Exception
	 */
	public void updateAudioStatus(BookAudioNew file)throws Exception;
}
