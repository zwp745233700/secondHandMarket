package com.secondHandMarket.vo;

import com.secondHandMarket.dto.OrdersDto;
import com.secondHandMarket.pojo.ReportGoods;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年5月13日 下午2:32:05 
* 类说明 
*/
public class ReportGoodsVo extends ReportGoods {

	private String reportTime;
	private String[] picArray;
	
	private OrdersDto orderDto;

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String[] getPicArray() {
		return picArray;
	}

	public void setPicArray(String[] picArray) {
		this.picArray = picArray;
	}

	public OrdersDto getOrderDto() {
		return orderDto;
	}

	public void setOrderDto(OrdersDto orderDto) {
		this.orderDto = orderDto;
	}

}
