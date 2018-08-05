package com.secondHandMarket.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;

/**  
 * Description: 收藏夹管理接口
 * @author Kanject  
 * @date 2018年4月26日
 */  
public interface CollectorService {
	SecondMarketResult add(Integer userId, Integer goodsId, HttpServletRequest request, HttpServletResponse response);//添加
	SecondMarketResult delete(Integer id, HttpServletRequest request, HttpServletResponse response);//删除
	SecondMarketResult list(Integer userId, int page, int rows, HttpServletRequest request, HttpServletResponse response);//查看

}
