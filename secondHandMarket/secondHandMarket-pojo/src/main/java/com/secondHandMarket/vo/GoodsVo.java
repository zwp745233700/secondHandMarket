package com.secondHandMarket.vo;

import com.secondHandMarket.pojo.Goods;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月30日 上午10:41:03 
* 类说明 
*/
public class GoodsVo extends Goods {
	private String type;
	
	private String time;
	
	private String[] pic;
	
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

	public String[] getPic() {
		return pic;
	}

	public void setPic(String[] pic) {
		this.pic = pic;
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
