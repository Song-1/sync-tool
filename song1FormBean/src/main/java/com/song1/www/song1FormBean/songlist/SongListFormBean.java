/**   
* @Title: SongListFormBean.java 
* @Package com.song1.www.songlist.http.action.mgr.bean 
* @Description: TODO
* @author Jelly.Liu   
* @date 2014年12月11日 下午6:29:45 
* @version V1  
*/
package com.song1.www.song1FormBean.songlist;

import java.util.List;

import com.song1.www.song1FormBean.core.BaseFormBean;

/** 
 * @ClassName: SongListFormBean 
 * @Description: TODO
 * @author Jelly.Liu
 * @date 2014年12月11日 下午6:29:45 
 *  
 */
public class SongListFormBean extends BaseFormBean{
	
	private int id;
	private String name;
	private String desc;
	private String icon;
	private String img;
	private List<SongListSongsFormBean> songs;
	private List<SongListTagFormBean> tags;
	private String remark;
	//更新于2015-08-15
	private int hot;
	
	public int getHot() {
		return hot;
	}
	public void setHot(int hot) {
		this.hot = hot;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public List<SongListSongsFormBean> getSongs() {
		return songs;
	}
	public void setSongs(List<SongListSongsFormBean> songs) {
		this.songs = songs;
	}
	public List<SongListTagFormBean> getTags() {
		return tags;
	}
	public void setTags(List<SongListTagFormBean> tags) {
		this.tags = tags;
	}

}
