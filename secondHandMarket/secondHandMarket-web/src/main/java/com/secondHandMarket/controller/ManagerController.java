package com.secondHandMarket.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.pojo.TypeResult;
import com.secondHandMarket.common.util.ExceptionUtil;
import com.secondHandMarket.dto.CheckResultDto;
import com.secondHandMarket.dto.PublishDto;
import com.secondHandMarket.dto.TypeDto;
import com.secondHandMarket.pojo.Manager;
import com.secondHandMarket.service.ManagerService;
import com.secondHandMarket.service.PublishService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author 作者 张维鹏:
 * @version 创建时间：2018年4月17日 下午8:50:43 类说明
 */
@Controller
@RequestMapping(value = "manager")
@Api(tags = "ManagerController", description = "管理员的相关操作")
public class ManagerController {

	@Autowired
	ManagerService managerService;
	@Autowired
	PublishService publishService;

	// 查看等待审核的物品
	@RequestMapping(value = "/goods/list")
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "查看等待审核的物品", notes = "只需要第几页，不需要其他参数")
	public SecondMarketResult getCheckingGoods(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "row", defaultValue = "15") Integer row) {

		return publishService.getCheckingGoods(page, row);
	}

	// 审核商品
	@RequestMapping(value = "/goods/check", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(httpMethod = "PUT", value = "审核商品", notes = "需要商品Id,审核结果的标记checkStatus:2代表通过，3代表不通过。审核评语checkResult")
	public SecondMarketResult checkGoods(@RequestBody CheckResultDto checkResultDto) {

		return publishService.checkGoods(checkResultDto);
	}

	// 添加分类
	@RequestMapping(value = "/type/add", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "添加分类", notes = "需要类别的名字name以及父节点parentId,如果没有父节点则写0")
	public SecondMarketResult addType(@RequestBody TypeDto typeDto) {

		return managerService.addType(typeDto);
	}

	// 修改分类
	@RequestMapping(value = "/type/update", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(httpMethod = "PUT", value = "修改分类", notes = "需要类别的id,名字name")
	public SecondMarketResult updateType(@RequestBody TypeDto typeDto) {

		return managerService.updateType(typeDto);
	}

	// 删除分类
	@RequestMapping(value = "/type/delete/{typeId}")
	@ResponseBody
	@ApiOperation(httpMethod = "DELETE", value = "删除分类", notes = "需要类别的id")
	public SecondMarketResult deleteType(@PathVariable(value = "typeId") Integer typeId) {

		return managerService.deleteType(typeId);
	}

	// 查询分类
	@RequestMapping(value = "/type/get/{typeId}")
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "查询分类,传入父节点id,返回所有的子分类", notes = "需要类别的id，如果查询全部则传入 0")
	public SecondMarketResult getType(@PathVariable(value = "typeId") Integer typeId) {

		return managerService.getTypeList(typeId);
	}

	// 管理员登陆
	@RequestMapping(value = "/login")
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "管理员登陆", notes = "需要管理员用户名username，密码password")
	public SecondMarketResult login(@RequestBody Manager manager, HttpServletRequest request) {

		SecondMarketResult result = managerService.login(manager);
		if (result.getStatus() == 200) {
			// 保存到session中
			HttpSession session = request.getSession();
			session.setAttribute("manager", result.getData());
		}
		return SecondMarketResult.ok(result);
	}

	// 管理员注销登陆
	@RequestMapping(value = "/logout")
	@ResponseBody
	@ApiOperation(httpMethod = "DELETE", value = "安全退出,注销登陆", notes = "不需要参数")
	public void logout(HttpSession session) throws Exception {
		session.invalidate();
	}

	// 取消惩罚，取消禁止用户登陆
	@RequestMapping(value = "/delpunish/{userId}")
	@ResponseBody
	@ApiOperation(httpMethod = "DELETE", value = "取消惩罚，取消禁止用户登陆", notes = "需要用户id")
	public SecondMarketResult delpunish(@PathVariable(value = "userId") Integer userId) {

		return managerService.restore(userId);
	}

	/**
	 * Description: 被举报用户的展示接口
	 * 
	 * @author Kanject
	 */
	@RequestMapping(value = "/user/listReported", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "被举报用户展示", notes = "无参数")
	public SecondMarketResult listReportedUser(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "10") Integer rows) {
		try {
			return managerService.listReportedUser(page, rows);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
}
