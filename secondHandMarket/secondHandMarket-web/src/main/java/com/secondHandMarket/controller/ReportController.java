package com.secondHandMarket.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.dto.PublishDto;
import com.secondHandMarket.dto.ReportDto;
import com.secondHandMarket.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年5月12日 下午10:17:10 
* 类说明 :举报controller
*/
@Controller
@Api(tags="ReportController",description="商品举报的Controller")
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	//举报用户
	@RequestMapping(value="/post",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod="POST",value="举报商品",
	notes="需要用户userId,订单orderId,举报原因reason,n张图片pubishPic")
	public SecondMarketResult addReport(ReportDto reportDto,@RequestParam("pubishPic") MultipartFile[] pubishPic){
		
		return reportService.addReport(reportDto, pubishPic);
	}
	
	//取消举报
	@RequestMapping(value="/delete/{reportId}")
	@ResponseBody
	@ApiOperation(httpMethod="DELETE",value="取消举报",notes="需要reportId")
	public SecondMarketResult deleteWant(@PathVariable(value="reportId") Integer reportId){
		
		return reportService.delReports(reportId);
	}
	
	//用户获取自己的举报列表(所有)，举报历史
	@RequestMapping(value="/getMyReport",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看我的举报列表(所有)，即举报历史")
	public SecondMarketResult getMyReport(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page,HttpServletRequest request, HttpServletResponse response){
		
		return reportService.getMyReports(0,page, row,request,response);
	}
	
	//查看举报并且未审核的订单
	@RequestMapping(value="/getWaitCheck",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看举报并且未审核的订单")
	public SecondMarketResult getWaitCheck(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page,HttpServletRequest request, HttpServletResponse response){
		
		return reportService.getMyReports(1,page, row,request,response);
	}
	
	//查看举报并且已审核成功的订单
	@RequestMapping(value="/getCheckedSuccess",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看举报并且已审核成功的订单")
	public SecondMarketResult getCheckedSuccess(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page,HttpServletRequest request, HttpServletResponse response){
		
		return reportService.getMyReports(2,page, row,request,response);
	}
	
	//查看举报但是已审核不通过的订单
	@RequestMapping(value="/getCheckedFail",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看举报但是已审核不通过的订单")
	public SecondMarketResult getCheckedFail(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page,HttpServletRequest request, HttpServletResponse response){
		
		return reportService.getMyReports(3,page, row,request,response);
	}
	
	//管理员查看举报列表
	@RequestMapping(value="/getReportList",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="管理员查看举报列表",
	notes="不需要参数")
	public SecondMarketResult getReportList(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page){
		
		return reportService.getAllReports(page, row);
	}
	

	//管理员审核用户的举报
	@RequestMapping(value="/checkReport",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(httpMethod="PUT",value="管理员审核用户的举报",
	notes="需要report表的id,审核状态checkStatus(2举报通过，3举报不通过),审核结果checkResult")
	public SecondMarketResult checkReport(@RequestBody ReportDto reportDto){
		
		return reportService.checkReport(reportDto);
	}
	
}
