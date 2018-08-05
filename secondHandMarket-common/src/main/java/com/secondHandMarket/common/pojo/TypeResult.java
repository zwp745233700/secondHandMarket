package com.secondHandMarket.common.pojo;

import java.util.List;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月30日 下午9:59:14 
* 类说明 
*/
public class TypeResult {

	private int id;
	private String name;
	private int parentId;
	private int isParent;
	
	private List<?> childList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public List<?> getChildList() {
		return childList;
	}

	public void setChildList(List<?> childList) {
		this.childList = childList;
	}

	public int getIsParent() {
		return isParent;
	}

	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}

}
