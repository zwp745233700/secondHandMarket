package com.secondHandMarket.dto;

import com.secondHandMarket.pojo.Goods;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月28日 下午8:20:38 
* 类说明 
*/
public class PublishDto extends Goods {

	private String type;
	
	private String time;
	
	private String[] oriPic;
	
	private int publishType;

	public int getPublishType() {
		return publishType;
	}

	public void setPublishType(int publishType) {
		this.publishType = publishType;
	}

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

	public String[] getOriPic() {
		return oriPic;
	}

	public void setOriPic(String[] oriPic) {
		this.oriPic = oriPic;
	}
	
	

	
}
