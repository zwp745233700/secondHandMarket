package com.secondHandMarket.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.dto.ReportDto;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年5月12日 下午10:19:44 
* 类说明 举报service接口
*/
@Service
public interface ReportService {

	//用户发起举报
	SecondMarketResult addReport(ReportDto reportDto,MultipartFile[] pubishPic);
	
	//查看我的举报历史
	SecondMarketResult getMyReports(Integer status,Integer page,Integer row,HttpServletRequest request, HttpServletResponse response);
	
	//用户取消举报
	SecondMarketResult delReports(Integer id);
	
	//举报审核
	SecondMarketResult checkReport(ReportDto reportDto);
	
	//管理员查看所有的举报列表
	SecondMarketResult getAllReports(Integer page, Integer row);
}
