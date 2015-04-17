/**
 * 
 */
package cn.com.musicone.www.files.model;

/**
 * @author Administrator
 *
 */
public class SongFileFormBean {
	/**
	 * 文件的md5值
	 */
	private String md5;
	/**
	 * 歌曲名称
	 */
	private String songName;
	/**
	 * 歌手名称
	 */
	private String singerName;
	/**
	 * 专辑名称
	 */
	private String albumName;
	/**
	 * 发行日期
	 */
	private String year;
	/**
	 * 码率
	 */
	private long bitrate;
	/**
	 * 文件后缀
	 */
	private String suffix;
	/**
	 * 时长
	 */
	private long duration;
	/**
	 * 采样率
	 */
	private String samplerate;
	/**
	 * 声道
	 */
	private String channels;
	/**
	 * 流派类型
	 */
	private String genre;
	/**
	 * 作曲
	 */
	private String compose;
	/**
	 * 指挥
	 */
	private String command;
	/**
	 * 音轨号
	 */
	private String trackNo;
	/**
	 * 合计音轨
	 */
	private String trackTotal;
	/**
	 * 碟片编号
	 */
	private String discNo;
	/**
	 * 合计碟片
	 */
	private String discTotal;
	/**
	 * 文件大小
	 */
	private long fileSize;

	/**
	 * 预留字段1
	 */
	private int bak;
	/**
	 * 预留字段2
	 */
	private String bak1;

	/**
	 * oss类型
	 */
	private String ossType;

	/**
	 * 文件的本地路径
	 */
	private String localPath;

	/**
	 * 文件路径的根路径(用于获取又拍云key)
	 */
	private String rootPath;

	/**
	 * 默认云类型
	 */
	private String defaultOssType;
	
	private String ossKey;
	private String songid;

	public String getSongid() {
		return songid;
	}

	public void setSongid(String songid) {
		this.songid = songid;
	}

	public String getOssKey() {
		return ossKey;
	}

	public void setOssKey(String ossKey) {
		this.ossKey = ossKey;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public long getBitrate() {
		return bitrate;
	}

	public void setBitrate(long bitrate) {
		this.bitrate = bitrate;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getSamplerate() {
		return samplerate;
	}

	public void setSamplerate(String samplerate) {
		this.samplerate = samplerate;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getCompose() {
		return compose;
	}

	public void setCompose(String compose) {
		this.compose = compose;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTrackNo() {
		return trackNo;
	}

	public void setTrackNo(String trackNo) {
		this.trackNo = trackNo;
	}

	public String getTrackTotal() {
		return trackTotal;
	}

	public void setTrackTotal(String trackTotal) {
		this.trackTotal = trackTotal;
	}

	public String getDiscNo() {
		return discNo;
	}

	public void setDiscNo(String discNo) {
		this.discNo = discNo;
	}

	public String getDiscTotal() {
		return discTotal;
	}

	public void setDiscTotal(String discTotal) {
		this.discTotal = discTotal;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getBak() {
		return bak;
	}

	public void setBak(int bak) {
		this.bak = bak;
	}

	public String getBak1() {
		return bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getOssType() {
		return ossType;
	}

	public void setOssType(String ossType) {
		this.ossType = ossType;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getDefaultOssType() {
		return defaultOssType;
	}

	public void setDefaultOssType(String defaultOssType) {
		this.defaultOssType = defaultOssType;
	}
	
}
