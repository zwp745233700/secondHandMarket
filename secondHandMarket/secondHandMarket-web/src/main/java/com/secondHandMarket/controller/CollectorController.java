package com.secondHandMarket.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.util.ExceptionUtil;
import com.secondHandMarket.service.CollectorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description: 用户收藏夹管理的相关操作
 * 
 * @author Kanject
 * @date 2018年4月26日
 */
@Controller
@RequestMapping("/collector")
@Api(tags = "CollectorController", description = "用户收藏夹管理的相关操作")
@EnableSwagger2
public class CollectorController {

	@Autowired
	CollectorService collectorService;

	/**
	 * Description: 添加商品到收藏夹
	 * 
	 * @author Kanject
	 */
	@RequestMapping(value = "/{userId}/add/{goodsId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "添加接口", notes = "参数：用户id、商品id")
	public SecondMarketResult add(@PathVariable Integer userId, @PathVariable Integer goodsId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			return collectorService.add(userId, goodsId, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * Description: 从收藏夹删除商品
	 * 
	 * @author Kanject
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "删除接口", notes = "参数：收藏夹条目id")
	public SecondMarketResult delete(@PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			return collectorService.delete(id, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * Description: 查看收藏的商品
	 * 
	 * @author Kanject
	 */
	@RequestMapping(value = "/list/{userId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "查看收藏夹", notes = "参数：用户id")
	public SecondMarketResult list(@PathVariable Integer userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "10") Integer rows, HttpServletRequest request,
			HttpServletResponse response) {
		return collectorService.list(userId, page, rows, request, response);
	}

}
