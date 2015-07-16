/**   
 * @Title: AlbumFormBean.java 
 * @Package com.song1.www.songs.http.action.mgr.bean 
 * @Description: TODO
 * @author Jelly.Liu   
 * @date 2014年12月2日 上午10:30:54 
 * @version V1  
 */
package com.song1.www.song1FormBean.songs;

import java.util.List;

import com.song1.www.song1FormBean.core.BaseFormBean;

/**
 * @ClassName: AlbumFormBean
 * @Description: 专辑formbean
 * @author Jelly.Liu
 * @date 2014年12月2日 上午10:30:54
 * 
 */
public class AlbumFormBean extends BaseFormBean{
	private int id;
	private String albumid;
	private String albumname;
	private String albumaliases;
	private String desc;
	private String releasecompany;
	private String releasedate;
	private String albumlanguage;
	private String imgkey;
	private String iconkey;
	private List<String> singers;
	private List<AlbumStyleFormBean> styles;
	private List<AlbumSongFormBean> songs;
	//// 新增 (2015-05-23)
	private String remark;
	//// 新增(2015-07-15)
	private int hitCounts;
	////新增(2015-07-16)
	private String albumSource;
	
	public String getHitCounts() {
		return albumSource;
	}

	public void setAlbumSource(String albumSource) {
		this.albumSource = albumSource;
	}
	
	public int getAlbumSource() {
		return albumSource;
	}

	public void setHitCounts(int hitCounts) {
		this.hitCounts = hitCounts;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAlbumid() {
		return albumid;
	}

	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}

	public String getAlbumname() {
		return albumname;
	}

	public void setAlbumname(String albumname) {
		this.albumname = albumname;
	}

	public String getAlbumaliases() {
		return albumaliases;
	}

	public void setAlbumaliases(String albumaliases) {
		this.albumaliases = albumaliases;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getReleasecompany() {
		return releasecompany;
	}

	public void setReleasecompany(String releasecompany) {
		this.releasecompany = releasecompany;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public String getAlbumlanguage() {
		return albumlanguage;
	}

	public void setAlbumlanguage(String albumlanguage) {
		this.albumlanguage = albumlanguage;
	}

	public String getImgkey() {
		return imgkey;
	}

	public void setImgkey(String imgkey) {
		this.imgkey = imgkey;
	}

	public String getIconkey() {
		return iconkey;
	}

	public void setIconkey(String iconkey) {
		this.iconkey = iconkey;
	}

	public List<String> getSingers() {
		return singers;
	}

	public void setSingers(List<String> singers) {
		this.singers = singers;
	}

	public List<AlbumStyleFormBean> getStyles() {
		return styles;
	}

	public void setStyles(List<AlbumStyleFormBean> styles) {
		this.styles = styles;
	}

	public List<AlbumSongFormBean> getSongs() {
		return songs;
	}

	public void setSongs(List<AlbumSongFormBean> songs) {
		this.songs = songs;
	}

}
