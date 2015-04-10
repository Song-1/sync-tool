package com.song1.www.song1FormBean.fm;
import java.io.Serializable;
/**
* @ClassName: FmRadioPlayList
 * @Description: 环球FM_电台播放地址管理
 * @author Jeckey.Liu
 * @date 2015年01月06日
 */
public class FmRadioPlayList implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 *  主键ID
	 */
	private int id;
	/**
	 *  电台ID
	 */
	private int radioId;
	/**
	 *  类型
	 */
	private String type;
	/**
	 *  地址
	 */
	private String playUrl;
	/**
	 *  是否默认
	 */
	private int isDefault;
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id= id;
	}
	public int getRadioId(){
		return radioId;
	}
	public void setRadioId(int radioId){
		this.radioId= radioId;
	}
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type= type;
	}
	public String getPlayUrl(){
		return playUrl;
	}
	public void setPlayUrl(String playUrl){
		this.playUrl= playUrl;
	}
	public int getIsDefault(){
		return isDefault;
	}
	public void setIsDefault(int isDefault){
		this.isDefault= isDefault;
	}
}
