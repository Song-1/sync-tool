package com.song1.www.song1FormBean.songs;

import com.song1.www.song1FormBean.core.BaseFormBean;


/**
 * @ClassName: SongFormFileBean
 * @Description: 歌曲播放文件FormBean
 * @author Jelly.Liu
 * @date 2014年11月26日 下午7:08:57
 * 
 */
public class SongFormFileBean extends BaseFormBean {
	private int id;
	private String filename;
	private String filepath;
	private int stream;
	private String suffix;
	private long filesize;
	private String key;
	private String md5;
	private String from;
	private String rootpath;
	private String oss_type;
	private String songListId;
	private String songListName;
	private String singerName;
	private String diffSong;
	// // 歌曲
	private int album;
	private int singer;
	// //歌曲 end

	// /// 爱听书
	private String author;
	private String player;
	private String chapterDesc;
	private String lastestTime;

	// /// 爱听书 end

	public int getAlbum() {
		return album;
	}

	public void setAlbum(int album) {
		this.album = album;
	}

	public int getSinger() {
		return singer;
	}

	public void setSinger(int singer) {
		this.singer = singer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int getStream() {
		return stream;
	}

	public void setStream(int stream) {
		this.stream = stream;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getRootpath() {
		return rootpath;
	}

	public void setRootpath(String rootpath) {
		this.rootpath = rootpath;
	}

	public String getOss_type() {
		return oss_type;
	}

	public void setOss_type(String oss_type) {
		this.oss_type = oss_type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getChapterDesc() {
		return chapterDesc;
	}

	public void setChapterDesc(String chapterDesc) {
		this.chapterDesc = chapterDesc;
	}

	public String getLastestTime() {
		return lastestTime;
	}

	public void setLastestTime(String lastestTime) {
		this.lastestTime = lastestTime;
	}

	public String getSongListId() {
		return songListId;
	}

	public void setSongListId(String songListId) {
		this.songListId = songListId;
	}

	public String getSongListName() {
		return songListName;
	}

	public void setSongListName(String songListName) {
		this.songListName = songListName;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getDiffSong() {
		return diffSong;
	}

	public void setDiffSong(String diffSong) {
		this.diffSong = diffSong;
	}


}
