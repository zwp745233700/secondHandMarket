package com.secondHandMarket.vo;

import com.secondHandMarket.pojo.Want;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月29日 下午10:08:26 
* 类说明 
*/
public class WantVo extends Want {

	private String type;
	
	private String time;
	
	private int parentTypeId;
	private String parentTypeName;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(int parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	public String getParentTypeName() {
		return parentTypeName;
	}

	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}
	
}
