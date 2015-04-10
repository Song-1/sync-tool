/**   
* @Title: BaseFormBean.java 
* @Package com.song1.www.songs.http.action.mgr.bean 
* @Description: TODO
* @author Jelly.Liu   
* @date 2014年12月2日 下午1:32:11 
* @version V1  
*/
package com.song1.www.song1FormBean.core;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** 
 * @ClassName: BaseFormBean 
 * @Description: FormBean基类
 * @author Jelly.Liu
 * @date 2014年12月2日 下午1:32:11 
 *  
 */
public class BaseFormBean {
	/**
	 * 将对象本身转换成json字符串
	 * 
	 * @return String
	 */
	public String toJson() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	/**
	 * 将json字符串转换成对象
	 * 
	 * @param String
	 * @return 
	 * @return SingerFormBean
	 */
	public <T> T parsetBean(String json,T bean) {
		if (json == null || "".equals(json) || bean == null) {
			return null;
		}
		Gson gson = new Gson();
//		Type type = new TypeToken<BaseFormBean>(){}.getType();
		Type type = TypeToken.get(bean.getClass()).getType();
		bean = gson.fromJson(json, type);
		return bean;
	}
}
