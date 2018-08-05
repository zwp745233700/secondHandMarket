package com.secondHandMarket.dto; 
/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年5月2日 下午2:21:00 
* 类说明 
*/
public class LoginDto {

	private String username;
	private String password;
	private String code;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
