package com.secondHandMarket.controller;

import java.io.File;

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
import com.secondHandMarket.dto.PublishDto;
import com.secondHandMarket.service.PublishService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月28日 下午7:30:29 
* 类说明 	:发布商品与需求的Controller
*/

@Controller
@Api(tags="PublishController",description="发布商品与需求的Controller")
@RequestMapping("/publish")
public class PublishController {
	
	@Autowired
	private PublishService publishService;
	
	//1、商品发布
	@RequestMapping(value="/goods/post",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod="POST",value="发布商品",
	notes="需要标题title、图片pubishPic、交易地点address、物品类别typeId、价格price、详情描述detail、联系方式linkWay、联系人linkPeople、用户userId")
	public SecondMarketResult addGoods(PublishDto publishDto,@RequestParam("pubishPic") MultipartFile[] pubishPic){
		
		return publishService.addGoods(publishDto, pubishPic);
	}
	
	//1、需求发布
	@RequestMapping(value="/want/post",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod="POST",value="发布需求",
	notes="需要地址address、需求详情detail、联系人linkPeople、联系方式linkWay、价格price、标题title、用户userId、需求类别typeId")
	public SecondMarketResult addWant(@RequestBody PublishDto publishDto){
		
		return publishService.addWant(publishDto);
	}
	
	
	//2、商品的修改
	@RequestMapping(value="/goods/put",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod="POST",value="修改商品",
	notes="需要商品id、标题title、原有图片的路径oriPic、新图片文件pubishPic、交易地点address、物品类别typeId、价格price、详情描述detail、联系方式linkWay、联系人linkPeople、userId")
	public SecondMarketResult updateGoods(PublishDto publishDto,@RequestParam("pubishPic") MultipartFile[] pubishPic){
		
		return publishService.updateGoods(publishDto, pubishPic);
	}
	
	//需求的修改
	@RequestMapping(value="/want/put",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(httpMethod="PUT",value="修改需求",
	notes="需要需求id、地址address、需求详情detail、联系人linkPeople、联系方式linkWay、价格price、标题title、需求类别typeId")
	public SecondMarketResult updateWant(@RequestBody PublishDto publishDto){
		
		return publishService.updateWant(publishDto);
	}
	
	//3、取消商品的发布
	@RequestMapping(value="/goods/delete/{goodsId}")
	@ResponseBody
	@ApiOperation(httpMethod="DELETE",value="取消商品的发布",notes="需要商品id")
	public SecondMarketResult deleteGoods(@PathVariable(value="goodsId") Integer goodsId){
		
		return publishService.deleteGoods(goodsId);
	}
	
	//取消需求的发布
	//3、取消商品的发布
	@RequestMapping(value="/want/delete/{wantId}")
	@ResponseBody
	@ApiOperation(httpMethod="DELETE",value="取消需求的发布",notes="需要需求id")
	public SecondMarketResult deleteWant(@PathVariable(value="wantId") Integer wantId){
		
		return publishService.deleteWant(wantId);
	}
	
	
	//商品查询：
	//4、查看我发布的需求
	@RequestMapping(value="/want/list/{userId}")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看我发布的需求(概要信息)",notes="需要用户id和第几页")
	public SecondMarketResult getMyWants(@PathVariable(value="userId") Integer userId,@RequestParam(value="page",defaultValue="1") Integer page,@RequestParam(value="row",defaultValue="15") Integer row){
		
		return publishService.getMyWants(userId,page,row);
		
	}
	
	//根据需求id查看需求的详细信息
	@RequestMapping(value="/want/get/{wantId}")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看需求的详细信息",notes="需要需求id")
	public SecondMarketResult getWantDetail(@PathVariable(value="wantId") Integer wantId){
		
		return publishService.getWantDetail(wantId);
		
	}
	
	//5、查询全部，已经审核通过的、审核中的、未通过的商品
	@RequestMapping(value="/goods/list/{userId}")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看我发布的商品(概要信息)",
	notes="需要用户id,审核状态status和第几页，status:0代表全部，1代表正在审核，2代表审核通过，3代表审核不通过")
	public SecondMarketResult getMyGoods(@PathVariable(value="userId") Integer userId,@RequestParam(value="status",defaultValue="0") Integer status,
			@RequestParam(value="page",defaultValue="1") Integer page,@RequestParam(value="row",defaultValue="15") Integer row){
		
		return publishService.getMyGoods(userId,page,row,status);
		
	}
	
	//查询商品的详细信息
	@RequestMapping(value="/goods/get/{goodsId}")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看商品的详细信息",
	notes="需要商品id和status。status为1时，商品的浏览次数会加一，其他值不加一")
	public SecondMarketResult geGoodsDetail(@PathVariable(value="goodsId") Integer goodsId,@RequestParam(value="status") Integer status){
		
		return publishService.getGoodsDetails(goodsId,status);
		
	}
	
	//6、查看我售出的物品
	@RequestMapping(value="/sell/get/{userId}")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看我售出的商品(概要信息)",notes="需要用户id,第几页")
	public SecondMarketResult getMySell(@PathVariable(value="userId") Integer userId,
			@RequestParam(value="page",defaultValue="1") Integer page,@RequestParam(value="row",defaultValue="15") Integer row){
		
		return publishService.getMySell(userId,page,row);
		
	}
	
	
	//查看所有人发布的商品与需求：
	//10、按照分类查询：
	@RequestMapping(value="/list")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="按照分类查询商品与需求",
	notes="需要商品或需求类别typeId,默认为0查询所有。供需类别status,1为商品，2为需求")
	public SecondMarketResult getListByType(@RequestParam(value="typeId",defaultValue="0") Integer typeId,
			@RequestParam(value="status",defaultValue="1") Integer status,
			@RequestParam(value="page",defaultValue="1") Integer page,@RequestParam(value="row",defaultValue="15") Integer row){
		
		return publishService.getListByCondition(typeId,"",status,page,row);
	}
			
	//13、按照关键字查询(商品Title)
	@RequestMapping(value="/get")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="按照关键字查询(商品Title)",
	notes="需要商品或需求的标题Title。供需类别status,1为商品，2为需求")
	public SecondMarketResult getListByTitle(@RequestParam(value="title") String title,
			@RequestParam(value="status",defaultValue="1") Integer status,
			@RequestParam(value="page",defaultValue="1") Integer page,@RequestParam(value="row",defaultValue="15") Integer row){
		
		return publishService.getListByCondition(0,title,status,page,row);
	}
	
	//首页展示商品的接口
	@RequestMapping(value="/first")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="首页展示商品信息",notes="需要每页的行数")
	public SecondMarketResult firstPage(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page){
		
		return publishService.lastestGoods(row,page,1);
	}
	
	//展示最新商品
	@RequestMapping(value="/lastestGoods")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="展示最新商品")
	public SecondMarketResult lastestGoods(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page){
		
		return publishService.lastestGoods(row,page,1);
	}
	
	//展示最新的需求
	@RequestMapping(value="/lastestWant")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="展示最新的需求")
	public SecondMarketResult lastestWant(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page){
		
		return publishService.lastestWant(row,page);
	}
	
	//获取最热门商品
	@RequestMapping(value="/hotSpotGoods")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="获取最热门商品")
	public SecondMarketResult hotSpotGoods(@RequestParam(value="row",defaultValue="15") Integer row,@RequestParam(value="page",defaultValue="1") Integer page){
		
		return publishService.lastestGoods(row,page,2);
	}
		
}