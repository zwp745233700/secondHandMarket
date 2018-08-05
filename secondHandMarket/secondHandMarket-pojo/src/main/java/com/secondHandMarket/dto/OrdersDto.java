package com.secondHandMarket.dto;

import java.util.Date;

/**  
 * Description: TODO
 * @author Kanject  
 * @date 2018年5月3日
 */  
public class OrdersDto {
	private String title;
	
	private Integer id;

    private Integer userId;

    private String userName;

    private String linkPhone;

    private Integer sellerId;

    private Integer goodsId;

    private Integer goodsNum;

    private Long goodsPrice;

    private Long goodsTotal;

    private String message;

    private String creatTime;

    private Integer ordersStatus;

    private Integer delStatus;
    
    private Integer reportStatus;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the linkPhone
	 */
	public String getLinkPhone() {
		return linkPhone;
	}

	/**
	 * @param linkPhone the linkPhone to set
	 */
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	/**
	 * @return the sellerId
	 */
	public Integer getSellerId() {
		return sellerId;
	}

	/**
	 * @param sellerId the sellerId to set
	 */
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * @return the goodsId
	 */
	public Integer getGoodsId() {
		return goodsId;
	}

	/**
	 * @param goodsId the goodsId to set
	 */
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * @return the goodsNum
	 */
	public Integer getGoodsNum() {
		return goodsNum;
	}

	/**
	 * @param goodsNum the goodsNum to set
	 */
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	/**
	 * @return the goodsPrice
	 */
	public Long getGoodsPrice() {
		return goodsPrice;
	}

	/**
	 * @param goodsPrice the goodsPrice to set
	 */
	public void setGoodsPrice(Long goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	/**
	 * @return the goodsTotal
	 */
	public Long getGoodsTotal() {
		return goodsTotal;
	}

	/**
	 * @param goodsTotal the goodsTotal to set
	 */
	public void setGoodsTotal(Long goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the creatTime
	 */
	public String getCreatTime() {
		return creatTime;
	}

	/**
	 * @param creatTime the creatTime to set
	 */
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	/**
	 * @return the ordersStatus
	 */
	public Integer getOrdersStatus() {
		return ordersStatus;
	}

	/**
	 * @param ordersStatus the ordersStatus to set
	 */
	public void setOrdersStatus(Integer ordersStatus) {
		this.ordersStatus = ordersStatus;
	}

	/**
	 * @return the delStatus
	 */
	public Integer getDelStatus() {
		return delStatus;
	}

	/**
	 * @param delStatus the delStatus to set
	 */
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}

}
