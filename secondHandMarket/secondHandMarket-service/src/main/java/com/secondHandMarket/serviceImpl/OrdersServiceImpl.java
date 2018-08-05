package com.secondHandMarket.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.dto.OrdersDto;
import com.secondHandMarket.mapper.GoodsMapper;
import com.secondHandMarket.mapper.OrdersMapper;
import com.secondHandMarket.pojo.Goods;
import com.secondHandMarket.pojo.Orders;
import com.secondHandMarket.pojo.OrdersExample;
import com.secondHandMarket.pojo.OrdersExample.Criteria;
import com.secondHandMarket.pojo.User;
import com.secondHandMarket.pojo.Want;
import com.secondHandMarket.service.OrdersService;
import com.secondHandMarket.vo.ReportOrdersVo;

/**  
 * Description: TODO
 * @author Kanject  
 * @date 2018年4月26日
 */  
@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	GoodsMapper goodsMapper;
	
	/**
	 * Description: 用户下单
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult place(Orders orders, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		//判断商品库存是否足够
		Integer supply = goodsMapper.selectByPrimaryKey(orders.getGoodsId()).getNum();
		Integer need = orders.getGoodsNum();
		if(need > supply) {
			return SecondMarketResult.build(421, "商品库存不足");
		}
		//补全数据
		orders.setCreatTime(new Date());
		orders.setOrdersStatus(1);
		orders.setDelStatus(0);
		orders.setReportStatus(0);
		//向数据库插入数据
		ordersMapper.insert(orders);
		
		return SecondMarketResult.ok();
	}
	
	/**
	 * Description: 将orders封装成ordersDto
	 * @author Kanject
	 */
	public List<OrdersDto> packOrdersDtoList(List<Orders> ordersList) {
		List<OrdersDto> dtoList = new ArrayList<>();
		for (Orders orders : ordersList) {
			OrdersDto ordersDto = new OrdersDto();
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Goods goods = goodsMapper.selectByPrimaryKey(orders.getGoodsId());
			
			ordersDto.setId(orders.getId());
			ordersDto.setTitle(goods.getTitle());
			ordersDto.setUserId(orders.getUserId());
			ordersDto.setUserName(orders.getUserName());
			ordersDto.setLinkPhone(orders.getLinkPhone());
			ordersDto.setSellerId(orders.getSellerId());
			ordersDto.setGoodsId(orders.getGoodsId());
			ordersDto.setGoodsNum(orders.getGoodsNum());
			ordersDto.setGoodsPrice(orders.getGoodsPrice());
			ordersDto.setGoodsTotal(orders.getGoodsTotal());
			ordersDto.setMessage(orders.getMessage());
			ordersDto.setCreatTime(sdf.format(new Date()));
			ordersDto.setOrdersStatus(orders.getOrdersStatus());
			ordersDto.setDelStatus(orders.getDelStatus());
			ordersDto.setId(orders.getId());
			
			dtoList.add(ordersDto);
		}
		
		return dtoList;
	}

	/**
	 * Description: 用户的订单展示
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult listByUserId(int page, int rows, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		OrdersExample example = new OrdersExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(user.getId());
		
		List<Orders> orderslist = ordersMapper.selectByExample(example);
		List<OrdersDto> dtoList = packOrdersDtoList(orderslist);
		
		//分页处理
		PageHelper.startPage(page, rows);
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<Orders> pageInfo = new PageInfo<>(orderslist);
		result.setTotal(pageInfo.getTotal());
		return SecondMarketResult.ok(result);
	}

	/**
	 * Description: 商家的订单展示
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult listBysellerId(int page, int rows, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		OrdersExample example = new OrdersExample();
		Criteria criteria = example.createCriteria();
		criteria.andSellerIdEqualTo(user.getId());
		List<Orders> orderslist = ordersMapper.selectByExample(example);
		List<OrdersDto> dtoList = packOrdersDtoList(orderslist);
		
		//分页处理
		PageHelper.startPage(page, rows);
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<Orders> pageInfo = new PageInfo<>(orderslist);
		result.setTotal(pageInfo.getTotal());
		return SecondMarketResult.ok(result);
	}

	/**
	 * Description: 商家的订单确认
	 * 	根据订单id查出订单信息，得到商品id
	 * 	根据商品id更改商品剩余数量
	 * 	更改订单状态
	 * @author Kanject
	 */
	@Override
	@Transactional
	public SecondMarketResult confirm(Integer ordersId, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		Orders orders = ordersMapper.selectByPrimaryKey(ordersId);
		Goods goods = goodsMapper.selectByPrimaryKey(orders.getGoodsId());
		goods.setNum(goods.getNum()-orders.getGoodsNum());
		orders.setOrdersStatus(2);
		try{
			goodsMapper.updateByPrimaryKey(goods);
			ordersMapper.updateByPrimaryKey(orders);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return SecondMarketResult.ok();
	}

	/**
	 * Description: 用户、商家取消订单
	 * 	根据订单id查出订单信息，得到商品id，查看订单状态
	 * 	订单状态为1，直接修改订单状态
	 * 	订单状态为2，修改商品数量后再修改订单状态
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult cancel(Integer ordersId, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		Orders orders = ordersMapper.selectByPrimaryKey(ordersId);
		if(orders.getOrdersStatus() == 2 || orders.getOrdersStatus() == 1) {
			Goods goods = goodsMapper.selectByPrimaryKey(orders.getGoodsId());
			goods.setNum(goods.getNum()+orders.getGoodsNum());
		}
		orders.setOrdersStatus(0);
		
		ordersMapper.updateByPrimaryKey(orders);
		return SecondMarketResult.ok();
	}

	/**
	 * Description: 用户确认订单
	 * 	根据订单id查出订单信息，得到商品id，查看订单状态
	 * 	订单状态为2，直接修改订单状态
	 * 	订单状态为不为2，返回“订单状态异常”
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult receive(Integer ordersId, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		Orders orders = ordersMapper.selectByPrimaryKey(ordersId);
		if(orders.getOrdersStatus() == 2) {
			orders.setOrdersStatus(3);
			ordersMapper.updateByPrimaryKey(orders);
			return SecondMarketResult.ok();
		}
		return SecondMarketResult.build(422, "订单状态异常");
	}

	/**
	 * Description: 用户的取消订单、确认收货展示
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult operateListByUserId(int page, int rows, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		List<Integer> statusList = new ArrayList<>();
		statusList.add(1);
		statusList.add(2);
		OrdersExample example = new OrdersExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(user.getId());
		criteria.andOrdersStatusIn(statusList);
		List<Orders> orderslist = ordersMapper.selectByExample(example);
		List<OrdersDto> dtoList = packOrdersDtoList(orderslist);
		
		//分页处理
		PageHelper.startPage(page, rows);
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<Orders> pageInfo = new PageInfo<>(orderslist);
		result.setTotal(pageInfo.getTotal());
		return SecondMarketResult.ok(result);
	}

	/**
	 * Description: 商家的确认订单展示
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult operateListBySellerId(int page, int rows, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		OrdersExample example = new OrdersExample();
		Criteria criteria = example.createCriteria();
		criteria.andSellerIdEqualTo(user.getId());
		criteria.andOrdersStatusEqualTo(1);
		//分页处理
		PageHelper.startPage(page, rows);
		
		List<Orders> orderslist = ordersMapper.selectByExample(example);
		List<OrdersDto> dtoList = packOrdersDtoList(orderslist);
		
		
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<Orders> pageInfo = new PageInfo<>(orderslist);
		result.setTotal(pageInfo.getTotal());
		return SecondMarketResult.ok(result);
	}
	
	
	//返回用户待交易的订单
	@Override
	public SecondMarketResult waitTransaction(int page, int rows, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		List<Integer> statusList = new ArrayList<>();
		statusList.add(2);
		OrdersExample example = new OrdersExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(user.getId());
		criteria.andOrdersStatusIn(statusList);
		
		//分页处理
		PageHelper.startPage(page, rows);
		
		List<Orders> orderslist = ordersMapper.selectByExample(example);
		List<OrdersDto> dtoList = packOrdersDtoList(orderslist);
		
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<Orders> pageInfo = new PageInfo<>(orderslist);
		result.setTotal(pageInfo.getTotal());
		return SecondMarketResult.ok(result);
	}

	/**
	 * Description: 交易成功的订单展示
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult listSuccessful(int page, int rows, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		OrdersExample example = new OrdersExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(user.getId());
		criteria.andOrdersStatusEqualTo(3);
		criteria.andReportStatusEqualTo(0);
		//分页处理
		PageHelper.startPage(page, rows);
		
		List<Orders> orderslist = ordersMapper.selectByExample(example);
		List<OrdersDto> dtoList = packOrdersDtoList(orderslist);
		
		
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<Orders> pageInfo = new PageInfo<>(orderslist);
		result.setTotal(pageInfo.getTotal());
		return SecondMarketResult.ok(result);
	}

}
