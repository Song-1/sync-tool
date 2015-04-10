/**   
 * @Title: SongFormBean.java 
 * @Package com.song1.www.songs.http.action.mgr 
 * @Description: TODO
 * @author Jelly.Liu   
 * @date 2014年11月26日 下午7:06:51 
 * @version V1  
 */
package com.song1.www.song1FormBean.songs;

import java.util.List;

import com.song1.www.song1FormBean.core.BaseFormBean;


/**
 * @ClassName: SongFormBean
 * @Description: TODO
 * @author Jelly.Liu
 * @date 2014年11月26日 下午7:06:51
 * 
 */
public class SongFormBean extends BaseFormBean {

	private int id;
	private String songid;
	private String songname;
	private int duration;
	private String singerid;
	private String singername;
	private String albumid;
	private String albumname;
	private List<SongFormFileBean> files;
	private String fileid;
	
	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSongname() {
		return songname;
	}

	public void setSongname(String songname) {
		this.songname = songname;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getSingername() {
		return singername;
	}

	public void setSingername(String singername) {
		this.singername = singername;
	}

	public String getAlbumname() {
		return albumname;
	}

	public void setAlbumname(String albumname) {
		this.albumname = albumname;
	}

	public String getSingerid() {
		return singerid;
	}

	public void setSingerid(String singerid) {
		this.singerid = singerid;
	}

	public String getAlbumid() {
		return albumid;
	}

	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}

	public String getSongid() {
		return songid;
	}

	public void setSongid(String songid) {
		this.songid = songid;
	}

	public List<SongFormFileBean> getFiles() {
		return files;
	}

	public void setFiles(List<SongFormFileBean> files) {
		this.files = files;
	}
}
