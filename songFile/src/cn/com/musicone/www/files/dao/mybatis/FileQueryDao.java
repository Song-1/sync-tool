/**
 * 
 */
package cn.com.musicone.www.files.dao.mybatis;

import java.util.List;

import cn.com.musicone.www.files.model.FileLocalPathModel;
import cn.com.musicone.www.files.model.SongFileInfoModel;

/**
 * @author Administrator
 *
 */
public interface FileQueryDao {

	public void addFileLocalPath(List<FileLocalPathModel> datas)
			throws Exception;
	public void updateFileLocalPaths(List<FileLocalPathModel> datas)
			throws Exception;

	public int getFileLocalPathData(String path) throws Exception;

	public List<FileLocalPathModel> listFileLocalPathDatas(int start)
			throws Exception;

	public List<SongFileInfoModel> listFileInfos(FileLocalPathModel model)
			throws Exception;
	
	
	public void updateFileLocalPath(FileLocalPathModel data)
			throws Exception;

}
