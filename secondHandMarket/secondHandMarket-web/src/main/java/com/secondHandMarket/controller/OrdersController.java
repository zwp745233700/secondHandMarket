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

import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.util.ExceptionUtil;
import com.secondHandMarket.pojo.Orders;
import com.secondHandMarket.service.OrdersService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**  
 * @author Kanject  
 * @date 2018年4月26日
 * Description: 订单管理的相关操作
 */  
@Controller
@RequestMapping("/orders")
@Api(tags = "OrdersController", description = "订单管理的相关操作")
@EnableSwagger2
public class OrdersController {
	@Autowired
	OrdersService ordersService;
	
	/**
	 * Description: 用户下单接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/place", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "用户下单接口", notes = "参数：用户id、买方用户名username、买方联系方式linkPhone、商品id、卖家id、商品价格goodsPrice、备注留言message")
	public SecondMarketResult place(@RequestBody Orders orders, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.place(orders, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 用户的订单展示接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/listByUserId", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "用户的订单展示接口", notes = "参数：用户id")
	public SecondMarketResult listByUserId(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.listByUserId(page, rows, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 商家的订单展示接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/listBySellerId", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "商家的订单展示接口", notes = "参数：商家id")
	public SecondMarketResult listBySellerId(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.listBysellerId(page, rows, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 商家的确认订单接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/confirm/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "商家的确认订单接口", notes = "参数：商家id")
	public SecondMarketResult confirm(@PathVariable Integer ordersId, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.confirm(ordersId, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 用户、商家的取消订单接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/cancel/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "用户、商家的取消订单接口", notes = "参数：订单id")
	public SecondMarketResult cancel(@PathVariable Integer ordersId, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.cancel(ordersId, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 用户的确认收货接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/receive/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "用户的确认收货接口", notes = "参数：商家id")
	public SecondMarketResult receive(@PathVariable Integer ordersId, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.receive(ordersId, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 用户的取消订单、确认收货展示接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/operateListByUserId", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "用户的取消订单、确认收货展示接口", notes = "参数：用户id")
	public SecondMarketResult operateListByUserId(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.operateListByUserId(page, rows, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 商家的确认订单展示接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/operateListBySellerId", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "商家的确认订单展示接口", notes = "参数：商家id")
	public SecondMarketResult operateListBySellerId(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.operateListBySellerId(page, rows, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	//获取用户等待交易的订单
	@RequestMapping(value = "/waitTransaction", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "获取用户等待交易的订单", notes = "参数：商家id")
	public SecondMarketResult waitTransaction(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.waitTransaction(page, rows, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 交易成功的订单展示接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/listSuccessful", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "交易成功的订单展示", notes = "无参数")
	public SecondMarketResult listSuccessful(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows, HttpServletRequest request, HttpServletResponse response) {
		try {
			return ordersService.listSuccessful(page, rows, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
}
