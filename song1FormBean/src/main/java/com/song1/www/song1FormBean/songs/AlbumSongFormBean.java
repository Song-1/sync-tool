/**   
* @Title: AlbumSongFormBean.java 
* @Package com.song1.www.songs.http.action.mgr.bean 
* @Description: TODO
* @author Jelly.Liu   
* @date 2014年12月2日 下午1:29:44 
* @version V1  
*/
package com.song1.www.song1FormBean.songs;

/** 
 * @ClassName: AlbumSongFormBean 
 * @Description: 专辑歌曲FormBean
 * @author Jelly.Liu
 * @date 2014年12月2日 下午1:29:44 
 *  
 */
public class AlbumSongFormBean {

	private int id;
	private int seat;
	private String songid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
	public String getSongid() {
		return songid;
	}
	public void setSongid(String songid) {
		this.songid = songid;
	}
	
}
