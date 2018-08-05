package com.secondHandMarket.dto; 
/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月30日 下午8:47:33 
* 类说明 : 类别的DTO
*/
public class TypeDto {
	private int id;
	private int parentId;
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	
}
