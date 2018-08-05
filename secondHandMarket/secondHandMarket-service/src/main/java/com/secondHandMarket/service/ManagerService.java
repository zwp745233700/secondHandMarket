package com.secondHandMarket.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.dto.TypeDto;
import com.secondHandMarket.pojo.Manager;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月17日 下午8:40:58 
* 类说明 
*/

@Service
public interface ManagerService {

	SecondMarketResult addType(TypeDto typeDto);

	SecondMarketResult updateType(TypeDto typeDto);

	SecondMarketResult deleteType(Integer typeId);

	SecondMarketResult getTypeList(Integer typeId);

	SecondMarketResult login(Manager manager);
	
	SecondMarketResult restore(Integer userId);
	
	SecondMarketResult listReportedUser(int page, int rows);//被举报用户展示

}
