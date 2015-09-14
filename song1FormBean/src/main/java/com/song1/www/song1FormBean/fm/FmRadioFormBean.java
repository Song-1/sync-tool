/**   
* @Title: FmRadioFormBean.java 
* @Package com.song1.www.fm.http.action.mgr.bean 
* @Description: TODO
* @author Jelly.Liu   
* @date 2015年1月6日 下午2:01:25 
* @version V1  
*/
package com.song1.www.song1FormBean.fm;

import java.util.List;

import com.song1.www.song1FormBean.core.BaseFormBean;

/** 
 * @ClassName: FmRadioFormBean 
 * @Description: TODO
 * @author Jelly.Liu
 * @date 2015年1月6日 下午2:01:25 
 *  
 */
public class FmRadioFormBean extends BaseFormBean{
	private int id;
	private String name;
	private String img;
	private List<FmAreaRadioFormBean> areas;
	private List<FmRadioPlayList> playlists;
	private String labelType;
	private String source;
	private String description;
	private String remark;
	
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public List<FmAreaRadioFormBean> getAreas() {
		return areas;
	}
	public void setAreas(List<FmAreaRadioFormBean> areas) {
		this.areas = areas;
	}
	public List<FmRadioPlayList> getPlaylists() {
		return playlists;
	}
	public void setPlaylists(List<FmRadioPlayList> playlists) {
		this.playlists = playlists;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLabelType() {
		return labelType;
	}
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}
}
