/**
 * 
 */
package cn.com.musicone.www.images.model;

/**
 * @author Administrator
 *
 */
public class SongModel {
	
	private int id;
	private int songId;
	private String singer;
	private String remark;
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSongId() {
		return id + 100000;
	}
	public void setSongId(int songId) {
		this.songId = songId;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
