package com.secondHandMarket.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.pojo.Orders;

/**  
 * Description: TODO
 * @author Kanject  
 * @date 2018年4月26日
 */  
public interface OrdersService {
	SecondMarketResult place(Orders orders, HttpServletRequest request, HttpServletResponse response);//用户下单
	SecondMarketResult listByUserId(int page, int rows, HttpServletRequest request, HttpServletResponse response);//用户的订单展示
	SecondMarketResult listBysellerId(int page, int rows, HttpServletRequest request, HttpServletResponse response);//用户的订单展示
	SecondMarketResult confirm(Integer ordersId, HttpServletRequest request, HttpServletResponse response);//商家的确认订单
	SecondMarketResult cancel(Integer ordersId, HttpServletRequest request, HttpServletResponse response);//用户取消订单
	SecondMarketResult receive(Integer ordersId, HttpServletRequest request, HttpServletResponse response);//用户取消订单
	SecondMarketResult operateListByUserId(int page, int rows, HttpServletRequest request, HttpServletResponse response);//用户的取消订单、确认收货展示接口
	SecondMarketResult operateListBySellerId(int page, int rows, HttpServletRequest request, HttpServletResponse response);//商家的确认订单展示接口
	SecondMarketResult waitTransaction(int page, int rows, HttpServletRequest request, HttpServletResponse response);//获取用户待交易的订单
	
	//交易成功，未举报
	SecondMarketResult listSuccessful(int page, int rows, HttpServletRequest request, HttpServletResponse response);//交易成功的订单展示接口

}
