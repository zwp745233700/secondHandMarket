package com.secondHandMarket.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.pojo.User;

/**  
* Description: TODO
* @author Kanject  
* @date 2018年4月21日
*/ 
public interface UserService {

	SecondMarketResult checkData(String param, int type);//数据校验
	SecondMarketResult register(User user);//注册
	SecondMarketResult login(String username, String password, HttpServletRequest request, HttpServletResponse response);//登陆
	SecondMarketResult cancel(HttpServletRequest request, HttpServletResponse response);//注销
	SecondMarketResult modify(User user, MultipartFile uploadFile, HttpServletRequest request, HttpServletResponse response);//修改用户信息
	SecondMarketResult modifyPassword(HttpServletRequest request, HttpServletResponse response,  String oldPwd,  String newPwd);//修改密码
	String updHeadImage(MultipartFile uploadFile, Integer userId);
	SecondMarketResult getUserInfo(Integer userId);
	
}
