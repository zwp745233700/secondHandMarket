package com.secondHandMarket.serviceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.SftpException;
import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.util.IDUtils;
import com.secondHandMarket.common.util.SFTPUtil;
import com.secondHandMarket.dto.OrdersDto;
import com.secondHandMarket.dto.ReportDto;
import com.secondHandMarket.mapper.GoodsMapper;
import com.secondHandMarket.mapper.OrdersMapper;
import com.secondHandMarket.mapper.ReportGoodsMapper;
import com.secondHandMarket.mapper.UserMapper;
import com.secondHandMarket.pojo.Goods;
import com.secondHandMarket.pojo.Orders;
import com.secondHandMarket.pojo.OrdersExample;
import com.secondHandMarket.pojo.ReportGoods;
import com.secondHandMarket.pojo.ReportGoodsExample;
import com.secondHandMarket.pojo.ReportGoodsExample.Criteria;
import com.secondHandMarket.pojo.User;
import com.secondHandMarket.service.ReportService;
import com.secondHandMarket.vo.ReportGoodsVo;
import com.secondHandMarket.vo.ReportOrdersVo;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年5月12日 下午10:20:44 
* 类说明 举报service接口实现层：
*/
@Service
public class ReportServiceImpl implements ReportService {

	@Value("${SFTP_ADDRESS}")
	private String SFTP_ADDRESS;
	@Value("${SFTP_PORT}")
	private Integer SFTP_PORT;
	@Value("${SFTP_USERNAME}")
	private String SFTP_USERNAME;
	@Value("${SFTP_PASSWORD}")
	private String SFTP_PASSWORD;
	@Value("${SFTP_BASE_PATH}")
	private String SFTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	
	@Autowired
	private ReportGoodsMapper reportGoodsMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Override
	public SecondMarketResult addReport(ReportDto reportDto, MultipartFile[] pubishPic) {
		
		try{
			Orders order = ordersMapper.selectByPrimaryKey(reportDto.getOrderId());
			if(order.getOrdersStatus()!=3){
				return SecondMarketResult.build(500, "该订单未完成交易，无法举报");
			}
		}catch (Exception e) {
			return SecondMarketResult.build(500, "举报出错");
		}
		
		String picture="";
		if(pubishPic != null && pubishPic.length != 0){
			//保存图片
			try{
				picture=uploadPicture(pubishPic,reportDto.getUserId());
			}catch (Exception e) {
				return SecondMarketResult.build(500, "图片上传失败");
			}
		}else{
			return SecondMarketResult.build(500, "没有选择图片");
		}
		
		ReportGoods reportGoods =new ReportGoods();
		reportGoods.setUserid(reportDto.getUserId());
		reportGoods.setOrderid(reportDto.getOrderId());
		reportGoods.setReason(reportDto.getReason());
		reportGoods.setPicture(picture);
		reportGoods.setCheckStatus(1);
		reportGoods.setCheckResult("");
		reportGoods.setCreateTime(new Date());
		
		Orders order = ordersMapper.selectByPrimaryKey(reportDto.getOrderId());
		order.setReportStatus(1);
		try{
			int j=ordersMapper.updateByPrimaryKeySelective(order);
			int i=reportGoodsMapper.insert(reportGoods);
			if(i+j==2){
				return SecondMarketResult.ok();
			}
		}catch (Exception e) {
			return SecondMarketResult.build(500, "举报失败");
		}
		return SecondMarketResult.build(500, "举报失败");
	}
	

	@Override
	public SecondMarketResult delReports(Integer id) {
		
		try{
			ReportGoods reportGoods = reportGoodsMapper.selectByPrimaryKey(id);
			if(reportGoods.getCheckStatus()==2 || reportGoods.getCheckStatus()==3){
				return SecondMarketResult.build(500, "该商品已经被审核，无法取消");
			}
			reportGoods.setCheckStatus(4);
			Orders order = ordersMapper.selectByPrimaryKey(reportGoods.getOrderid());
			order.setReportStatus(0);
			
			int j=ordersMapper.updateByPrimaryKeySelective(order);
			int i =reportGoodsMapper.updateByPrimaryKeySelective(reportGoods);
			if(i+j==2){
				return SecondMarketResult.ok();
			}
		}catch (Exception e) {
			return SecondMarketResult.build(500, "取消举报失败");
		}
		return SecondMarketResult.build(500, "取消举报失败");
	}


	@Override
	public SecondMarketResult checkReport(ReportDto reportDto) {
		
		ReportGoods reportGoods=reportGoodsMapper.selectByPrimaryKey(reportDto.getId());
		reportGoods.setId(reportDto.getId());
		reportGoods.setCheckResult(reportDto.getCheckResult());
		int checkStauts=reportDto.getCheckStatus();
		
		//先查询订单
		Orders order=ordersMapper.selectByPrimaryKey(reportGoods.getOrderid());
		
		if(checkStauts==2){
			//审核通过
			reportGoods.setCheckStatus(2);
			try{
				//通过订单的商家selli查询商家
				User user=userMapper.selectByPrimaryKey(order.getSellerId());
				//审核通过，需要禁止用户登陆
				user.setReportStatus(1);
				userMapper.updateByPrimaryKeySelective(user);
				reportGoodsMapper.updateByPrimaryKeySelective(reportGoods);
				
			}catch (Exception e) {
				return SecondMarketResult.build(500, "审核失败");
			}
		}if(checkStauts==3){
			//审核不通过
			reportGoods.setCheckStatus(3);
			try{
				reportGoodsMapper.updateByPrimaryKeySelective(reportGoods);
			}catch (Exception e) {
				return SecondMarketResult.build(500, "审核失败");
			}
		}
		
		order.setReportStatus(2);
		ordersMapper.updateByPrimaryKeySelective(order);
		
		return SecondMarketResult.ok();
	}
	
	
	
	//查看所有举报,审核与未审核的订单
	@Override
	public SecondMarketResult getMyReports(Integer status,Integer page,Integer row,HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		ReportGoodsExample example=new ReportGoodsExample();
		Criteria criteria = example.createCriteria();
		
		//用户查看自己的举报列表
		criteria.andUseridEqualTo(user.getId());
		example.setOrderByClause("create_time DESC");
		if(status==0){
			//查询所有的订单
		}else if(status==1){
			//查询等待审核的订单
			criteria.andCheckStatusEqualTo(1);
		}else if(status==2){
			//查询已经审核通过的订单
			criteria.andCheckStatusEqualTo(2);
		}else if(status==3){
			//查询审核不通过的订单
			criteria.andCheckStatusEqualTo(3);
		}
		
		PageHelper.startPage(page, row);
		
		try{
			List<ReportGoods> list = reportGoodsMapper.selectByExample(example);
			List<ReportGoodsVo> voList = packReportGoodsToVo(list);
			
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			PageInfo<ReportGoods> pageInfo=new PageInfo<>(list);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询举报列表失败");
		}
	}
	


	
	//管理员查看所有的举报
	@Override
	public SecondMarketResult getAllReports(Integer page, Integer row) {

		ReportGoodsExample example=new ReportGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andCheckStatusEqualTo(1);
		example.setOrderByClause("create_time DESC");
		PageHelper.startPage(page, row);
		
		try{
			List<ReportGoods> list = reportGoodsMapper.selectByExample(example);
			List<ReportGoodsVo> voList = packReportGoodsToVo(list);
			
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			PageInfo<ReportGoods> pageInfo=new PageInfo<>(list);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询举报列表失败");
		}
	}
	
	
	
	
	
	
	
	//上传多张商品图片
	private String uploadPicture(MultipartFile[] uploadFiles,Integer userId) throws SftpException, IOException {
		
		String url="";
		for(MultipartFile uploadFile:uploadFiles){
			
			//获取文件拓展名
			String originalFilename=uploadFile.getOriginalFilename();
			String ext=originalFilename.substring(originalFilename.lastIndexOf("."));
			//生成新的文件名
			String imageName=IDUtils.genImageName();
			
			//把图片上传到sftp服务器（图片服务器）
			String filePath="/secondHand/"+userId+"/report";//图片目录路径
			
			//sftp连接方式上传文件
			SFTPUtil sftp = new SFTPUtil(SFTP_USERNAME, SFTP_PASSWORD, SFTP_ADDRESS, SFTP_PORT);  
			sftp.login();
			sftp.upload(SFTP_BASE_PATH, filePath, imageName+ext, uploadFile.getInputStream());
			sftp.logout();  
							
			//可以访问到图片的url
			String path=IMAGE_BASE_URL + filePath + "/" + imageName + ext+";";
			url=url+path;
		}
			return url;
	}
	
	//装换为查询出来的举报订单voList
	private OrdersDto toOrderDto(Orders orders) {
		
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
		ordersDto.setReportStatus(orders.getReportStatus());
		
		return ordersDto;
	}
	
	
	private List<ReportGoodsVo> packReportGoodsToVo(List<ReportGoods> list){
		
		List<ReportGoodsVo> voList=new ArrayList<ReportGoodsVo>();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(list.size()>0){
			for(ReportGoods reportGoods:list){
				ReportGoodsVo reportGoodsVo=new ReportGoodsVo(); 
				reportGoodsVo.setId(reportGoods.getId());
				reportGoodsVo.setUserid(reportGoods.getUserid());
				reportGoodsVo.setOrderid(reportGoods.getOrderid());
				reportGoodsVo.setReason(reportGoods.getReason());
				reportGoodsVo.setReportTime(sdf.format(reportGoods.getCreateTime()));
				reportGoodsVo.setCheckStatus(reportGoods.getCheckStatus());
				reportGoodsVo.setCheckResult(reportGoods.getCheckResult());
				String[] picArray = reportGoods.getPicture().split(";");
				reportGoodsVo.setPicArray(picArray);
				
				Orders order = ordersMapper.selectByPrimaryKey(reportGoods.getOrderid());
				//将order装换成Vo
				OrdersDto orderDto = toOrderDto(order);
				reportGoodsVo.setOrderDto(orderDto);
				
				voList.add(reportGoodsVo);
			}
		}
		return voList;
	}

}
