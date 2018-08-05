package com.secondHandMarket.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.mapper.GoodsMapper;
import com.secondHandMarket.mapper.OrdersMapper;
import com.secondHandMarket.mapper.UserMapper;
import com.secondHandMarket.pojo.Goods;
import com.secondHandMarket.pojo.Orders;
import com.secondHandMarket.pojo.User;

import io.swagger.annotations.Api;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**  
 * Description: 测试用
 * @author Kanject  
 * @date 2018年4月29日
 */  
@Controller
@RequestMapping("/test")
@Api(tags = "TestController", description = "测试用，直接往数据库插入数据")
@EnableSwagger2
public class TestController {
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	GoodsMapper goodsMapper;
	
	@Autowired 
	OrdersMapper ordersMapper;
	
	/**
	 * Description: 插入用户接口
	 * 
	 * @author Kanject
	 */
	@RequestMapping(value = "/insertUsers", method = RequestMethod.GET)
	@ResponseBody
	public SecondMarketResult insertUsers() {
		for(int i=1; i<=20; i++) {
			User user = new User();
			user.setUsername("testUser"+i);
			user.setPassword(DigestUtils.md5DigestAsHex("testtest".getBytes()));
			user.setMobile("testMobile"+i);
			user.setEmail("test"+i+".@test.com");
			user.setIdentity("testIndentity"+i);
			user.setHeadpic("testHeadpic"+i);
			user.setSalt("test");
			
			userMapper.insert(user);
		}
		return SecondMarketResult.ok("已插入");
	}
	
	/**
	 * Description: 插入商品接口
	 * 
	 * @author Kanject
	 */
	@RequestMapping(value = "/insertGoods", method = RequestMethod.GET)
	@ResponseBody
	public SecondMarketResult insertGoods() {
		for(int i=1; i<=20; i++) {
			Goods goods = new Goods();
			goods.setTitle("goodsTitle"+i);
			goods.setPicture("goodsPicture"+i);
			goods.setAddress("goodsAddress"+i);
			goods.setTypeId(1);
			goods.setPrice(10l);
			goods.setDetail("goodsDetail"+i);
			goods.setDetailImg("http://h.hiphotos.baidu.com/image/h%3D300/sign=04128f390f4f78f09f0b9cf349300a83/63d0f703918fa0ecf70575602a9759ee3c6ddb99.jpg");
			goods.setNum(100);
			goods.setLinkWay("linkWays"+i);
			goods.setLinkPeople("goodsLinkPeople"+i);
			goods.setCreateTime(new Date());
			goods.setCheckResult("goodsCheckResult"+i);
			goods.setCheckStatus(1);
			goods.setIsDel(0);
			goods.setUserId(1);
			goods.setViewCount(0);
			
			goodsMapper.insert(goods);
		}
		return SecondMarketResult.ok("已插入");
	}
	
	/**
	 * Description: 插入订单接口
	 * 
	 * @author Kanject
	 */
	@RequestMapping(value = "/insertOrders", method = RequestMethod.GET)
	@ResponseBody
	public SecondMarketResult insertOrders() {
		for(int i=1; i<=5; i++) {
			Orders orders = new Orders();
			orders.setUserId(47+i);
			orders.setUserName("buyer"+i);
			orders.setLinkPhone("buyerPhone"+i);
			orders.setSellerId(2);
			orders.setGoodsId(4);
			orders.setGoodsNum(1);
			orders.setGoodsPrice(10l);
			orders.setGoodsTotal(10l);
			orders.setMessage("GoodsMessage"+i);
			orders.setCreatTime(new Date());
			orders.setOrdersStatus(1);
			orders.setDelStatus(0);
			
			ordersMapper.insert(orders);
		}
		return SecondMarketResult.ok("已插入");
	}

}
