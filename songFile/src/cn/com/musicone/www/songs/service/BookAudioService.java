/**
 * 
 */
package cn.com.musicone.www.songs.service;

import java.util.List;

import com.song1.www.book.pojo.BookAudioNew;

/**
 * 
 * @ClassName: BookAudioService 
 * @Description: 书籍管理服务
 * @author Jeckey Lau
 * @date 2015年7月28日 上午9:27:04
 */
public interface BookAudioService {
	/**
	 * 计算需要上传书籍数据总数
	 * @return
	 * @throws Exception
	 */
	public int listUploadDataCounts();

	/**
	 * 查询书籍文件
	 * @return
	 * @throws Exception
	 */
	public List<BookAudioNew> listFiles() throws Exception;

	/**
	 * 更新书籍文件状态
	 * @param file
	 * @throws Exception
	 */
	public void updateFileStatus(BookAudioNew file) throws Exception;

}
