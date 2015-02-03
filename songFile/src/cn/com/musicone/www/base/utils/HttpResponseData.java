/**
 * 
 */
package cn.com.musicone.www.base.utils;

import org.apache.http.HttpStatus;

/**
 * HTTP请求返回数据封装对象
 * 
 * @author Jelly.Liu
 *
 */
public class HttpResponseData {

	private int code;
	private String msg;
	private String data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		switch (code) {
		case HttpStatus.SC_OK:
			msg = "请求成功";
			break;
		case HttpStatus.SC_NOT_FOUND:
			msg = "请求失败:请求资源未找到";
			break;
		case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			msg = "请求失败:服务器程序处理发生异常";
			break;
		default:
			msg = "请求失败";
			break;
		}
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		if(data == null){
			return "";
		}
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
