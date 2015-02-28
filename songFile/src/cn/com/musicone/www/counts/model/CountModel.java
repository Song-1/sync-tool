/**
 * 
 */
package cn.com.musicone.www.counts.model;

import java.util.Date;

/**
 * @author Administrator
 *
 */
public class CountModel {
	
	private String reqPath;
	private int clickCount;
	
	private int reqId;
	private Date logTime;
	public String getReqPath() {
		return reqPath;
	}
	public void setReqPath(String reqPath) {
		this.reqPath = reqPath;
	}
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public int getReqId() {
		return reqId;
	}
	public void setReqId(int reqId) {
		this.reqId = reqId;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

}
