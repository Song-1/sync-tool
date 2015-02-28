/**
 * 
 */
package cn.com.musicone.www.counts.model;

/**
 * @author Administrator
 *
 */
public class SearchReqLogModel {
	
	private String startDate;
	private String endDate;
	private String reqPathLike;
	private int pageStart = 0;
	private int pageSize = 1000;
	private boolean isFilterReqParams = true;
	
	public boolean isFilterReqParams() {
		return isFilterReqParams;
	}
	public void setFilterReqParams(boolean isFilterReqParams) {
		this.isFilterReqParams = isFilterReqParams;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getReqPathLike() {
		return reqPathLike;
	}
	public void setReqPathLike(String reqPathLike) {
		this.reqPathLike = reqPathLike;
	}
	public int getPageStart() {
		return pageStart;
	}
	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
