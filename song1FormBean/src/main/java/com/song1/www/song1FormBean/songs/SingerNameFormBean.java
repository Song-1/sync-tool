/**   
* @Title: SingerNameFormBean.java 
* @Package com.song1.www.songs.http.action.mgr.bean 
* @Description: TODO
* @author Jelly.Liu   
* @date 2014年11月27日 下午7:02:37 
* @version V1  
*/
package com.song1.www.song1FormBean.songs;

/** 
 * @ClassName: SingerNameFormBean 
 * @Description: TODO
 * @author Jelly.Liu
 * @date 2014年11月27日 下午7:02:37 
 *  
 */
public class SingerNameFormBean {
	private int id;
	private String singername;
	private int type;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSingername() {
		return singername;
	}
	public void setSingername(String singername) {
		this.singername = singername;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
