package com.secondHandMarket.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.dto.CollectDto;
import com.secondHandMarket.mapper.CollectMapper;
import com.secondHandMarket.mapper.GoodsMapper;
import com.secondHandMarket.pojo.Collect;
import com.secondHandMarket.pojo.CollectExample;
import com.secondHandMarket.pojo.CollectExample.Criteria;
import com.secondHandMarket.pojo.Goods;
import com.secondHandMarket.pojo.User;
import com.secondHandMarket.pojo.Want;
import com.secondHandMarket.service.CollectorService;

/**  
 * Description: 收藏夹管理接口实现(需要登陆)
 * @author Kanject  
 * @date 2018年4月26日
 */  
@Service
public class CollectorServiceImpl implements CollectorService {

	@Autowired
	CollectMapper collectMapper;
	@Autowired
	GoodsMapper goodsMapper;
	
	/**  
	* Description: 添加商品到收藏夹
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult add(Integer userId, Integer goodsId, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		Collect collect = new Collect();
		collect.setUserId(userId);
		collect.setGoodsId(goodsId);
		
		collectMapper.insert(collect);
		
		return SecondMarketResult.ok();
	}

	/**  
	* Description: 从收藏夹删除商品
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult delete(Integer id, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		collectMapper.deleteByPrimaryKey(id);
		return SecondMarketResult.ok();
	}

	/**  
	* Description: 查看收藏的商品
	* 	根据用户id获取其收藏关系列表
	* 	遍历列表根据商品id查找商品信息，补全dto的信息，并将dto插入到列表
	* 	进行分页处理，返回dto列表
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult list(Integer userId, int page, int rows, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		CollectExample example = new CollectExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<Collect> collecList = collectMapper.selectByExample(example);
		
		List<CollectDto> dtoList = new ArrayList<>();
		for (Collect collect : collecList) {
			Goods goods = goodsMapper.selectByPrimaryKey(collect.getGoodsId());
			
			CollectDto collectDto = new CollectDto();
			collectDto.setId(collect.getId());
			collectDto.setNum(goods.getNum());
			collectDto.setPicture(goods.getPicture());
			collectDto.setPrice(goods.getPrice());
			collectDto.setTitle(goods.getTitle());
			
			dtoList.add(collectDto);
		}
		
		//分页处理
		PageHelper.startPage(page, rows);
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<Collect> pageInfo = new PageInfo<>(collecList);
		result.setTotal(pageInfo.getTotal());
		
		return SecondMarketResult.ok(result);
	}

}
