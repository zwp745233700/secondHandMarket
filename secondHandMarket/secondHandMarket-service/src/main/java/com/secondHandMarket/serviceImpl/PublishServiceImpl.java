package com.secondHandMarket.serviceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.SftpException;
import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.PictureResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.util.ExceptionUtil;
import com.secondHandMarket.common.util.IDUtils;
import com.secondHandMarket.common.util.SFTPUtil;
import com.secondHandMarket.dto.CheckResultDto;
import com.secondHandMarket.dto.PublishDto;
import com.secondHandMarket.mapper.GoodsMapper;
import com.secondHandMarket.mapper.OrdersMapper;
import com.secondHandMarket.mapper.TypeMapper;
import com.secondHandMarket.mapper.WantMapper;
import com.secondHandMarket.pojo.Goods;
import com.secondHandMarket.pojo.GoodsExample;
import com.secondHandMarket.pojo.Orders;
import com.secondHandMarket.pojo.OrdersExample;
import com.secondHandMarket.pojo.Type;
import com.secondHandMarket.pojo.Want;
import com.secondHandMarket.pojo.WantExample;
import com.secondHandMarket.pojo.WantExample.Criteria;
import com.secondHandMarket.service.PublishService;
import com.secondHandMarket.vo.GoodsSellVo;
import com.secondHandMarket.vo.GoodsVo;
import com.secondHandMarket.vo.WantVo;


/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月28日 下午7:36:31 
* 类说明 商品管理与 用户需求管理的service实现层：
*/
@Service
public class PublishServiceImpl implements PublishService{

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
	private GoodsMapper goodsMapper;
	@Autowired
	private WantMapper wantMapper;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private TypeMapper typeMapper;
	
	//发布商品
	@Override
	public SecondMarketResult addGoods(PublishDto publishDto, MultipartFile[] pubishPic) {
		
		String picture="";
		if(pubishPic != null && pubishPic.length != 0){
			//保存图片
			try{
				picture=uploadPicture(pubishPic,publishDto.getUserId());
			}catch (Exception e) {
				return SecondMarketResult.build(500, "图片上传失败");
			}
		}else{
			return SecondMarketResult.build(500, "没有选择图片");
		}
		
		Goods goods=new Goods();
		goods.setPicture(picture);
		goods.setCreateTime(new Date());
		goods.setIsDel(0);//0代表没有删除
		goods.setViewCount(0);
		setGoodsProperties(goods,publishDto);
	
		try{
			goodsMapper.insert(goods);
		}catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, "发布商品失败");
		}
		return SecondMarketResult.ok();
	}
	
	@Override
	public SecondMarketResult updateGoods(PublishDto publishDto, MultipartFile[] pubishPic) {
		
		String url="";
		String picture="";//新上传的文件url
		if(pubishPic != null && pubishPic.length != 0){
			try{
				picture=uploadPicture(pubishPic,publishDto.getUserId());
			}catch (Exception e) {
				return SecondMarketResult.build(500, "图片上传失败");
			}
		}
		
		String[] oriPic = publishDto.getOriPic();
		for(String origin:oriPic){
			url=url+origin+";";
		}
		
		Goods goods=new Goods();
		goods.setPicture(url+picture);
		setGoodsProperties(goods,publishDto);
		
		try{
			goodsMapper.updateByPrimaryKeySelective(goods);
		}catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, "修改商品失败");
		}
		return SecondMarketResult.ok();
	}
	
	
	//发布需求
	@Override
	public SecondMarketResult addWant(PublishDto publishDto){
		
		Want want=new Want();
		want.setIsDel(0);//0代表没有删除
		want.setCreatTime(new Date());
		setWantProperties(want,publishDto);
		
		try{
			wantMapper.insert(want);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "发布需求失败");
		}
		
		return SecondMarketResult.ok();
	}


	@Override
	public SecondMarketResult updateWant(PublishDto publishDto) {
		
		Want want=new Want();
		setWantProperties(want,publishDto);
	
		try{
			wantMapper.updateByPrimaryKeySelective(want);
		}catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, "修改需求失败");
		}
		
		return SecondMarketResult.ok();
	}

	
	@Override
	public SecondMarketResult deleteGoods(Integer goodsId) {
		
		Goods goods=new Goods();
		goods.setId(goodsId);
		goods.setIsDel(1);
		try{
			goodsMapper.updateByPrimaryKeySelective(goods);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "删除商品失败");
		}		
		return SecondMarketResult.ok();
	}

	@Override
	public SecondMarketResult deleteWant(Integer wantId) {
		
		Want want=new Want();
		want.setId(wantId);
		want.setIsDel(1);
		try{
			wantMapper.updateByPrimaryKeySelective(want);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "删除需求失败");
		}
		return SecondMarketResult.ok();
	}


	@Override
	public SecondMarketResult getMyWants(Integer userId, Integer page,Integer row) {
		
		//创建查询条件
		WantExample example=new WantExample();
		Criteria  criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andIsDelEqualTo(0);
		example.setOrderByClause("creat_time DESC");
		PageHelper.startPage(page, row);//page是第几页，每页的行数row
		
		try{
			List<Want> wantList=wantMapper.selectByExample(example);
			List<WantVo> voList = packWantToVo(wantList);
			//创建一个返回值对象
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			//取记录总条数
			PageInfo<Want> pageInfo=new PageInfo<>(wantList);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
			
		}catch (Exception e) {
			return SecondMarketResult.build(500, "获取需求失败");
		}
		
	}


	@Override
	public SecondMarketResult getWantDetail(Integer wantId) {
		
		WantVo wantVo=new WantVo();
		
		try{
			Want want=wantMapper.selectByPrimaryKey(wantId);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			wantVo.setId(want.getId());
			wantVo.setTitle(want.getTitle());
			wantVo.setAddress(want.getAddress());
			wantVo.setPrice(want.getPrice());
			wantVo.setDetail(want.getDetail());
			wantVo.setLinkPeople(want.getLinkPeople());
			wantVo.setLinkWay(want.getLinkWay());
			//wantVo.setNum(want.getNum());
			wantVo.setTime(sdf.format(want.getCreatTime()));
			wantVo.setUserId(want.getUserId());
			
			Type type=typeMapper.selectByPrimaryKey(want.getTypeId());
			wantVo.setTypeId(want.getTypeId());
			wantVo.setType(type.getName());
			//父节点id和name
			if(type.getParentId()!=0){
				Type parType=typeMapper.selectByPrimaryKey(type.getParentId());
				wantVo.setParentTypeId(parType.getId());
				wantVo.setParentTypeName(parType.getName());
			}
			
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询出错");
		}
		return SecondMarketResult.ok(wantVo);
	}


	@Override
	public SecondMarketResult getMyGoods(Integer userId, Integer page,Integer row,Integer status) {
		
		//创建查询条件
		GoodsExample example=new GoodsExample();
		com.secondHandMarket.pojo.GoodsExample.Criteria criteria=example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andIsDelEqualTo(0);
		
		if(status!=0){
			criteria.andCheckStatusEqualTo(status);
		}
		example.setOrderByClause("create_time DESC");
		PageHelper.startPage(page, row);
		
		try{
			List<Goods> list = goodsMapper.selectByExample(example);
			List<GoodsVo> voList = packGoodsToVo(list);
			
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			PageInfo<Goods> pageInfo=new PageInfo<>(list);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询商品失败");
		}
	}
	

	@Override
	public SecondMarketResult getGoodsDetails(Integer goodsId,Integer status) {
		
		Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
		GoodsVo goodsVo=new GoodsVo();
		
		if(goods!=null){
			try{
				if(status==1){
					//浏览次数加一
					goods.setViewCount(goods.getViewCount()+1);
					goodsMapper.updateByPrimaryKeySelective(goods);
				}
			
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//切割图片路径
				String[] picArr = goods.getPicture().split(";");
				
				String url=goods.getPicture();
				String[] urlArr=url.split(";");
				goodsVo.setPicture(urlArr[0]);
				goodsVo.setPic(picArr);
				
				goodsVo.setId(goods.getId());
				goodsVo.setTitle(goods.getTitle());
				goodsVo.setAddress(goods.getAddress());
				goodsVo.setPrice(goods.getPrice());
				goodsVo.setDetail(goods.getDetail());
				goodsVo.setLinkPeople(goods.getLinkPeople());
				goodsVo.setLinkWay(goods.getLinkWay());
				goodsVo.setTime(sdf.format(goods.getCreateTime()));
				goodsVo.setCheckStatus(goods.getCheckStatus());
				goodsVo.setCheckResult(goods.getCheckResult());
				goodsVo.setUserId(goods.getUserId());
				goodsVo.setViewCount(goods.getViewCount());
				goodsVo.setNum(goods.getNum());
				//goodsVo.setDetailImg(goods.getPicture());
				
				Type type = typeMapper.selectByPrimaryKey(goods.getTypeId());
				goodsVo.setType(type.getName());
				goodsVo.setTypeId(goods.getTypeId());
				if(type.getParentId()!=0){
					Type parType=typeMapper.selectByPrimaryKey(type.getParentId());
					goodsVo.setParentTypeId(parType.getId());
					goodsVo.setParentTypeName(parType.getName());
				}
			}catch (Exception e) {
				return SecondMarketResult.build(500, "商品查询失败");
			}
		}
		return SecondMarketResult.ok(goodsVo);
	}


	@Override
	public SecondMarketResult getMySell(Integer userId, Integer page,Integer row) {
		
		//查询条件
		OrdersExample OrdersExample=new OrdersExample();
		com.secondHandMarket.pojo.OrdersExample.Criteria criteria = OrdersExample.createCriteria();
		criteria.andSellerIdEqualTo(userId);
		criteria.andOrdersStatusEqualTo(2);
		criteria.andDelStatusEqualTo(0);
		OrdersExample.setOrderByClause("creat_time DESC");
		PageHelper.startPage(page, row);
		
		try{
			List<Orders> ordersList = ordersMapper.selectByExample(OrdersExample);
			
			List<GoodsSellVo> sellVo=new ArrayList<>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(Orders order:ordersList){
				Goods goods=goodsMapper.selectByPrimaryKey(order.getGoodsId());
				
				GoodsSellVo goodSellVo=new GoodsSellVo();
				if(goods!=null){
					
					goodSellVo.setId(goods.getId());
					goodSellVo.setTitle(goods.getTitle());
					//设置图片
					String url=goods.getPicture();
					String[] urlArr=url.split(";");
					goodSellVo.setPicture(urlArr[0]);
					
					goodSellVo.setAddress(goods.getAddress());
					
					Type type = typeMapper.selectByPrimaryKey(goods.getTypeId());
					goodSellVo.setType(type.getName());
					goodSellVo.setTypeId(goods.getTypeId());
					goodSellVo.setPrice(goods.getPrice());
					goodSellVo.setNum(goods.getNum());
					goodSellVo.setTime(sdf.format(goods.getCreateTime()));
					goodSellVo.setCheckStatus(goods.getCheckStatus());
					goodSellVo.setOrdersId(order.getId());
					
					sellVo.add(goodSellVo);
				}
			}
			
			PageDateResult result=new PageDateResult();
			result.setRows(sellVo);
			PageInfo<Orders> pageInfo=new PageInfo<>(ordersList);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询出错");
		}
	}


	@Override
	public SecondMarketResult getListByCondition(Integer typeId,String title,Integer status, Integer page,Integer row) {
	
		PageDateResult result=new PageDateResult();
		if(status==1){
			//查询商品
			return getGoodsByCondition(typeId,title,page,row);
		}
		if(status==2){
			//查询需求
			return getWantByCondition(typeId,title,page,row);
		}
		//没有对应的类别
		result.setRows(null);
		result.setTotal(0);
		return SecondMarketResult.ok(result);
	}

	
	@Override
	public SecondMarketResult getCheckingGoods(Integer page,Integer row) {
		
		GoodsExample example=new GoodsExample();
		example.setOrderByClause("create_time DESC");
		com.secondHandMarket.pojo.GoodsExample.Criteria criteria = example.createCriteria();
		criteria.andCheckStatusEqualTo(1);
		PageHelper.startPage(page, row);
		
		try{
			List<Goods> goodsList = goodsMapper.selectByExample(example);
			
			List<GoodsVo> voList = packGoodsToVo(goodsList);
			
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			PageInfo<Goods> pageInfo=new PageInfo<>(goodsList);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "获取待审核的物品失败");
		}
	}


	@Override
	public SecondMarketResult checkGoods(CheckResultDto checkResultDto) {
		
		Goods goods=new Goods();
		goods.setId(checkResultDto.getId());
		goods.setCheckStatus(checkResultDto.getCheckStatus());
		goods.setCheckResult(checkResultDto.getCheckResult());
		
		int i=0;
		try{
			
			i=goodsMapper.updateByPrimaryKeySelective(goods);
			if(i==0){
				return SecondMarketResult.build(500, "审核商品失败:"+checkResultDto.getCheckResult()+":"+checkResultDto.getCheckStatus()+":"+checkResultDto.getId());
			}
		}catch (Exception e) {
			return SecondMarketResult.build(500, "审核商品失败");
		}
		
		return SecondMarketResult.ok();
	}


	@Override
	public SecondMarketResult lastestGoods(Integer row,Integer page,Integer type) {
		
		//创建查询条件
		GoodsExample example=new GoodsExample();
		com.secondHandMarket.pojo.GoodsExample.Criteria criteria=example.createCriteria();
		criteria.andIsDelEqualTo(0);
		criteria.andCheckStatusEqualTo(2);
		criteria.andNumGreaterThan(0);
		if(type==1){
			example.setOrderByClause("create_time DESC");
		}if(type==2){
			example.setOrderByClause("view_count DESC");
		}
		
		PageHelper.startPage(page, row);
		
		try{
			List<Goods> list = goodsMapper.selectByExample(example);
			List<GoodsVo> voList = packGoodsToVo(list);
			
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			PageInfo<Goods> pageInfo=new PageInfo<>(list);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询商品失败");
		}
	}
	
	
	@Override
	public SecondMarketResult lastestWant(Integer row, Integer page) {
		
		WantExample example = new WantExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDelEqualTo(0);
		example.setOrderByClause("creat_time DESC");
		PageHelper.startPage(page, row);
		
		try{
			List<Want> wantList=wantMapper.selectByExample(example);
			List<WantVo> WantVoList = packWantToVo(wantList);
			
			PageDateResult result=new PageDateResult();
			result.setRows(WantVoList);
			PageInfo<Want> pageInfo=new PageInfo<>(wantList);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "获取最新需求失败");
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
			String filePath="/secondHand/"+userId+"/goods";//图片目录路径
			
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
	
	
	//设置goods的属性
	private void setGoodsProperties(Goods goods,PublishDto publishDto){
		
		if(publishDto.getUserId()!=null){
			goods.setUserId(publishDto.getUserId());		
		}
		if(publishDto.getId()!=null){
			goods.setId(publishDto.getId());
		}
		goods.setCheckStatus(1);//1代表正在审核中
		goods.setTypeId(publishDto.getTypeId());//商品分类id
		goods.setTitle(publishDto.getTitle());
		goods.setPrice(publishDto.getPrice());
		goods.setNum(publishDto.getNum());
		goods.setDetail(publishDto.getDetail());
		goods.setAddress(publishDto.getAddress());
		goods.setLinkWay(publishDto.getLinkWay());
		goods.setLinkPeople(publishDto.getLinkPeople());
	}
	
	
	//设置want的属性
	private void setWantProperties(Want want,PublishDto publishDto){
		if(publishDto.getUserId()!=null){
			want.setUserId(publishDto.getUserId());
		}
		if(publishDto.getId()!=null){
			want.setId(publishDto.getId());
		}
		
		want.setDetail(publishDto.getDetail());
		want.setPrice(publishDto.getPrice());
		want.setTitle(publishDto.getTitle());
		want.setTypeId(publishDto.getTypeId());
		want.setAddress(publishDto.getAddress());
		//want.setNum(publishDto.getNum());
		want.setLinkPeople(publishDto.getLinkPeople());
		want.setLinkWay(publishDto.getLinkWay());
	}

	//封装需求的概要信息,将wantList包装成WantVoList
	private List<WantVo> packWantToVo(List<Want> wantList){
		
		List<WantVo> voList=new ArrayList<WantVo>();
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Want want:wantList){
			WantVo wantVo=new WantVo();
			wantVo.setId(want.getId());
			wantVo.setTitle(want.getTitle());
			wantVo.setAddress(want.getAddress());
			wantVo.setTypeId(want.getTypeId());
			Type type=typeMapper.selectByPrimaryKey(want.getTypeId());
			wantVo.setTypeId(type.getId());
			wantVo.setType(type.getName());
			wantVo.setPrice(want.getPrice());
			wantVo.setLinkPeople(want.getLinkPeople());
			wantVo.setLinkWay(want.getLinkWay());
			//wantVo.setNum(want.getNum());
			wantVo.setTime(sdf.format(want.getCreatTime()));
			wantVo.setDetail(want.getDetail());
			wantVo.setUserId(want.getUserId());
			
			voList.add(wantVo);
		}
		return voList;
	}
	
	//封装商品的概要信息,将goodsList包装成goodsVoList
	private List<GoodsVo> packGoodsToVo(List<Goods> list){
		List<GoodsVo> voList=new ArrayList<>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Goods goods:list){
			GoodsVo goodsVo=new GoodsVo();
			goodsVo.setId(goods.getId());
			goodsVo.setTitle(goods.getTitle());
			//设置图片
			String url=goods.getPicture();
			String[] urlArr=url.split(";");
			goodsVo.setPicture(urlArr[0]);
			
			goodsVo.setAddress(goods.getAddress());
			goodsVo.setTypeId(goods.getTypeId());
			
			Type type = typeMapper.selectByPrimaryKey(goods.getTypeId());
			goodsVo.setType(type.getName());
			goodsVo.setPrice(goods.getPrice());
			goodsVo.setNum(goods.getNum());
			goodsVo.setUserId(goods.getUserId());
			goodsVo.setLinkPeople(goods.getLinkPeople());
			goodsVo.setLinkWay(goods.getLinkWay());
			goodsVo.setTime(sdf.format(goods.getCreateTime()));
			goodsVo.setCheckStatus(goods.getCheckStatus());
			goodsVo.setViewCount(goods.getViewCount());
			goodsVo.setDetail(goods.getDetail());
			
			voList.add(goodsVo);
		}
		return voList;
	}
	
	//根据条件查询商品
	private SecondMarketResult getGoodsByCondition(Integer typeId,String title, Integer page,Integer row){
		
		GoodsExample example = new GoodsExample();
		com.secondHandMarket.pojo.GoodsExample.Criteria criteria=example.createCriteria();
		example.setOrderByClause("create_time DESC");
		if(title==null || title ==""){
			if(typeId!=0){
				criteria.andTypeIdEqualTo(typeId);
			}
		}else{
			criteria.andTitleLike("%"+title+"%");
		}
		criteria.andIsDelEqualTo(0);
		criteria.andCheckStatusEqualTo(2);
		criteria.andNumGreaterThan(0);
		
		PageHelper.startPage(page, row);
		
		try{
			List<Goods> goodsList = goodsMapper.selectByExample(example);
			List<GoodsVo> voList = packGoodsToVo(goodsList);
			
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			PageInfo<Goods> pageInfo=new PageInfo<>(goodsList);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询商品失败");
		}
	}
	
	//根据条件查询需求
	private SecondMarketResult getWantByCondition(Integer typeId,String title, Integer page,Integer row){
		WantExample example=new WantExample();
		Criteria criteria = example.createCriteria();
		
		if(title==null || title ==""){
			if(typeId!=0){
				criteria.andTypeIdEqualTo(typeId);
			}
		}else{
			criteria.andTitleLike("%"+title+"%");
		}
		criteria.andIsDelEqualTo(0);
		example.setOrderByClause("creat_time DESC");
		
		PageHelper.startPage(page, row);
		
		try{
			List<Want> wantList = wantMapper.selectByExample(example);
			List<WantVo> voList = packWantToVo(wantList);
			
			PageDateResult result=new PageDateResult();
			result.setRows(voList);
			PageInfo<Want> pageInfo=new PageInfo<>(wantList);
			result.setTotal(pageInfo.getTotal());
			
			return SecondMarketResult.ok(result);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询需求失败");
		}
	}




}
