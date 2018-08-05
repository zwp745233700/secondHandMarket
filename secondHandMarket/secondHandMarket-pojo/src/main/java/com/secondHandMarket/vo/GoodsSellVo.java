package com.secondHandMarket.vo;

import com.secondHandMarket.pojo.Goods;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月30日 下午2:40:34 
* 类说明 :卖出的商品的Vo
*/
public class GoodsSellVo extends Goods {
	
	private int ordersId;
	private String type;
	private String Time;

	public int getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(int ordersId) {
		this.ordersId = ordersId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}
	

}
