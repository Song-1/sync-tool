package com.song1.www.song1FormBean.songs;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: StyleSong
 * @Description: 风格与歌曲关系管理
 * @author Jeckey.Liu
 * @date 2014年12月24日
 */
public class StyleSong implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	private int id;
	/**
	 * 风格ID
	 */
	private int styleId;
	/**
	 * 歌曲ID
	 */
	private int songId;
	/**
	 * 排序
	 */
	private int seat;
	/**
	 * 数据类型::1:歌曲;2:专辑
	 */
	private int dataType;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 创建者
	 */
	private String createUser;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStyleId() {
		return styleId;
	}

	public void setStyleId(int styleId) {
		this.styleId = styleId;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
}
