
package com.song1.www.song1FormBean.songs;

import java.util.List;

import com.song1.www.song1FormBean.core.BaseFormBean;

/**
 * @ClassName: SingerFormBean
 * @Description: 歌手的Form数据对象
 * @author Jelly.Liu
 * @date 2014年11月27日 下午7:02:22
 * 
 */
public class SingerFormBean extends BaseFormBean{
	private int id;
	private String singerid;
	private String desc;
	private SingerDetailDescFormBean detailDesc;
	private String birth;
	private String country;
	private String gender;
	private int areaid;
	private String areacode;
	private List<SingerNameFormBean> names;
	private List<SingerImgFormBean> imgs;

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public List<SingerNameFormBean> getNames() {
		return names;
	}

	public void setNames(List<SingerNameFormBean> names) {
		this.names = names;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public SingerDetailDescFormBean getDetailDesc() {
		return detailDesc;
	}

	public void setDetailDesc(SingerDetailDescFormBean detailDesc) {
		this.detailDesc = detailDesc;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAreaid() {
		return areaid;
	}

	public void setAreaid(int areaid) {
		this.areaid = areaid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSingerid() {
		return singerid;
	}

	public void setSingerid(String singerid) {
		this.singerid = singerid;
	}

	public List<SingerImgFormBean> getImgs() {
		return imgs;
	}

	public void setImgs(List<SingerImgFormBean> imgs) {
		this.imgs = imgs;
	}

}

