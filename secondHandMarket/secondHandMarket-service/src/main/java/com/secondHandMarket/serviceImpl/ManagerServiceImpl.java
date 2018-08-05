package com.secondHandMarket.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.pojo.TypeResult;
import com.secondHandMarket.dto.OrdersDto;
import com.secondHandMarket.dto.ReportedUserDto;
import com.secondHandMarket.dto.TypeDto;
import com.secondHandMarket.mapper.ManagerMapper;
import com.secondHandMarket.mapper.TypeMapper;
import com.secondHandMarket.mapper.UserMapper;
import com.secondHandMarket.pojo.Goods;
import com.secondHandMarket.pojo.Manager;
import com.secondHandMarket.pojo.ManagerExample;
import com.secondHandMarket.pojo.Orders;
import com.secondHandMarket.pojo.OrdersExample;
import com.secondHandMarket.pojo.Type;
import com.secondHandMarket.pojo.TypeExample;
import com.secondHandMarket.pojo.TypeExample.Criteria;
import com.secondHandMarket.pojo.User;
import com.secondHandMarket.pojo.UserExample;
import com.secondHandMarket.service.ManagerService;
import com.secondHandMarket.vo.ManagerVo;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月17日 下午8:43:05 
* 类说明 
*/

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	ManagerMapper managerMapper;
	@Autowired
	TypeMapper typeMapper;
	@Autowired
	UserMapper userMapper;
	

	@Override
	public SecondMarketResult addType(TypeDto typeDto) {
		
		Type type=new Type();
		type.setName(typeDto.getName());
		type.setParentId(typeDto.getParentId());
		
		type.setCreateTime(new Date());
		type.setUpdateTime(new Date());
		type.setDelStatus(0);
		type.setIsParent(0);
		
		try{
			if(typeDto.getParentId()!=0){
				Type parentType=typeMapper.selectByPrimaryKey(typeDto.getParentId());
				if(parentType.getIsParent()==0){
					parentType.setIsParent(1);
					typeMapper.updateByPrimaryKeySelective(parentType);
				}
			}
			typeMapper.insert(type);
		}catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, "添加分类失败");
		}
		return SecondMarketResult.ok();
	}

	@Override
	public SecondMarketResult updateType(TypeDto typeDto) {
		
		Type type=new Type();
		type.setId(typeDto.getId());
		type.setName(typeDto.getName());
		type.setUpdateTime(new Date());
		
		try{
			typeMapper.updateByPrimaryKeySelective(type);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "修改分类失败");
		}
		return SecondMarketResult.ok();
	}

	@Override
	public SecondMarketResult deleteType(Integer typeId) {
		
		//查询该分类
		Type type = typeMapper.selectByPrimaryKey(typeId);
		
		try{
			//如果是父节点，先将其子节点删除，
			if(type.getIsParent()==1){
				TypeExample example=new TypeExample();
				Criteria criteria = example.createCriteria();
				criteria.andParentIdEqualTo(type.getId());
				
				List<Type> childList = typeMapper.selectByExample(example);
				
				for(Type childType:childList){
					childType.setDelStatus(1);
					childType.setUpdateTime(new Date());
					typeMapper.updateByPrimaryKeySelective(childType);
				}
			}
			//删除自己
			type.setDelStatus(1);
			type.setUpdateTime(new Date());
			typeMapper.updateByPrimaryKeySelective(type);
			
		}catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, "删除分类失败");
		}
		return SecondMarketResult.ok();
	}

	@Override
	public SecondMarketResult getTypeList(Integer typeId) {
		
		List result;
		try{
			result=getType(typeId);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "获取类别失败");
		}
		return SecondMarketResult.ok(result);
	}
	
	private  List<?> getType(Integer typeId) {
		List resultList=new ArrayList<>();
		
		try{
			//创建查询条件
			TypeExample example=new TypeExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(typeId);
			criteria.andDelStatusEqualTo(0);
			
			List<Type> list = typeMapper.selectByExample(example);
			
			if(list!=null && list.size()!=0){
				for(Type childType:list){
					
					TypeResult result=new TypeResult();
					result.setId(childType.getId());
					result.setName(childType.getName());
					result.setParentId(childType.getParentId());
					result.setIsParent(childType.getIsParent());
					
					if(childType.getIsParent()==1){
						//是父节点
						result.setChildList(getType(childType.getId()));
						
					}else{
						result.setChildList(null);
					}
					resultList.add(result);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public SecondMarketResult login(Manager manager) {
		
		ManagerExample example=new ManagerExample();
		com.secondHandMarket.pojo.ManagerExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(manager.getUsername());
		criteria.andPasswordEqualTo(manager.getPassword());
		
		try{
			List<Manager> list = managerMapper.selectByExample(example);
			if(list.isEmpty()){
				return SecondMarketResult.build(500, "用户名或密码错误");
			}else{
				ManagerVo managerVo=new ManagerVo();
				managerVo.setId(list.get(0).getId());
				managerVo.setUsername(list.get(0).getUsername());
				return SecondMarketResult.ok(managerVo);
			}
		}catch (Exception e) {
			return SecondMarketResult.build(500, "登陆失败");
		}
	}

	@Override
	public SecondMarketResult restore(Integer userId) {
		
		try{
			User user = userMapper.selectByPrimaryKey(userId);
			user.setReportStatus(0);
			userMapper.updateByPrimaryKeySelective(user);
			
		}catch (Exception e) {
			return SecondMarketResult.build(500, "取消禁止登陆失败");
		}
		
		return SecondMarketResult.ok();
	}

	/**
	 * Description: 被举报用户展示
	 * @author Kanject
	 */
	@Override
	public SecondMarketResult listReportedUser(int page, int rows) {
		UserExample example = new UserExample();
		com.secondHandMarket.pojo.UserExample.Criteria criteria = example.createCriteria();
		criteria.andReportStatusEqualTo(1);
		//分页处理
		PageHelper.startPage(page, rows);
		
		List<User> userList = userMapper.selectByExample(example);
		List<ReportedUserDto> dtoList = packReportedDtoList(userList);
		
		
		//创建一个返回值对象
		PageDateResult result = new PageDateResult();
		result.setRows(dtoList);
		//取记录总条数
		PageInfo<User> pageInfo = new PageInfo<>(userList);
		result.setTotal(pageInfo.getTotal());
		return SecondMarketResult.ok(result);
	}

	/**  
	 * Description: 从User对象中选取有用的属性封装为ReportedUserDto
	 * @author Kanject  
	 */
	private List<ReportedUserDto> packReportedDtoList(List<User> userList) {
		List<ReportedUserDto> dtoList = new ArrayList<>();
		for (User user : userList) {
			ReportedUserDto reportedUserDto = new ReportedUserDto();
			
			reportedUserDto.setId(user.getId());
			reportedUserDto.setIdentity(user.getIdentity());
			reportedUserDto.setMobile(user.getMobile());
			reportedUserDto.setEmail(user.getEmail());
			reportedUserDto.setUsername(user.getUsername());
			reportedUserDto.setReportStatus(user.getReportStatus());
			
			dtoList.add(reportedUserDto);
		}
		
		return dtoList;
	}




	
}
