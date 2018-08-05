package com.secondHandMarket.dto;

/**  
* Description: TODO
* @author Kanject  
* @date 2018年5月15日
*/ 
public class ReportedUserDto {
	private Integer id;

    private String username;

    private String mobile;

    private String email;

    private String identity;

    private Integer reportStatus;

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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * @return the reportStatus
	 */
	public Integer getReportStatus() {
		return reportStatus;
	}

	/**
	 * @param reportStatus the reportStatus to set
	 */
	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}
}
