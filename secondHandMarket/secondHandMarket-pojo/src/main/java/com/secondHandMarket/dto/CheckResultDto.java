package com.secondHandMarket.dto; 
/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月30日 下午6:20:32 
* 类说明 
*/
public class CheckResultDto {

	private int id;
	private int checkStatus;
	private String checkResult;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
}
