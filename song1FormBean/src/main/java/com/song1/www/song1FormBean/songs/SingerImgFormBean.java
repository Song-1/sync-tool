/**   
* @Title: SingerImgFormBean.java 
* @Package com.song1.www.songs.http.action.mgr.bean 
* @Description: TODO
* @author Jelly.Liu   
* @date 2014年11月28日 下午2:28:03 
* @version V1  
*/
package com.song1.www.song1FormBean.songs;

/** 
 * @ClassName: SingerImgFormBean 
 * @Description: TODO
 * @author Jelly.Liu
 * @date 2014年11月28日 下午2:28:03 
 *  
 */
public class SingerImgFormBean {
	private int id;
	private String md5;
	private String key;
	private int isdefault;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}
}
