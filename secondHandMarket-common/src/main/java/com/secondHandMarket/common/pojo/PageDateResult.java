package com.secondHandMarket.common.pojo;

import java.util.List;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年2月10日 下午5:48:03 
* 类说明 分页数据的Result
*/
public class PageDateResult {
	private long total;
	private List<?> rows;
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
}
