package com.secondHandMarket.common.pojo;


public class PictureResult {
	private int error;
	private String url;
	private String message;
	
	private PictureResult(int error,String url,String message){
		this.error=error;
		this.url=url;
		this.message=message;	
	}
	
	//成功时调用的方法：
	public static PictureResult ok(String url){
		return new PictureResult(0, url, null);
	}
	//失败是调用的方法：
	public static PictureResult error(String message){
		return new PictureResult(1, null, message);
	}

	
	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
