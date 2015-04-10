/**   
* @Title: FmAreaRadioFormBean.java 
* @Package com.song1.www.fm.http.action.mgr.bean 
* @Description: TODO
* @author Jelly.Liu   
* @date 2015年1月6日 下午2:02:25 
* @version V1  
*/
package com.song1.www.song1FormBean.fm;

import com.song1.www.song1FormBean.core.BaseFormBean;

/** 
 * @ClassName: FmAreaRadioFormBean 
 * @Description: TODO
 * @author Jelly.Liu
 * @date 2015年1月6日 下午2:02:25 
 *  
 */
public class FmAreaRadioFormBean extends BaseFormBean{
	private int id;
	private int area_pid;
	private int area_id;
	private int seat;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getArea_pid() {
		return area_pid;
	}
	public void setArea_pid(int area_pid) {
		this.area_pid = area_pid;
	}
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
}
