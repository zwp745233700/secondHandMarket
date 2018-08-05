package com.secondHandMarket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.secondHandMarket.common.pojo.PageDateResult;
import com.secondHandMarket.common.pojo.PictureResult;
import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.dto.CheckResultDto;
import com.secondHandMarket.dto.PublishDto;

/** 
* @author 作者 张维鹏: 
* @version 创建时间：2018年4月28日 下午7:36:02 
* 类说明 
*/
@Service
public interface PublishService {
	
	SecondMarketResult addGoods(PublishDto publishDto, MultipartFile[] pubishPic);

	SecondMarketResult addWant(PublishDto publishDto);

	SecondMarketResult updateGoods(PublishDto publishDto, MultipartFile[] pubishPic);

	SecondMarketResult updateWant(PublishDto publishDto);

	SecondMarketResult deleteGoods(Integer goodsId);

	SecondMarketResult deleteWant(Integer wantId);

	SecondMarketResult getMyWants(Integer userId, Integer page,Integer row);

	SecondMarketResult getWantDetail(Integer wantId);

	SecondMarketResult getMyGoods(Integer userId, Integer page,Integer row,Integer status);

	SecondMarketResult getGoodsDetails(Integer goodsId,Integer status);

	SecondMarketResult getMySell(Integer userId, Integer page,Integer row);

	SecondMarketResult getListByCondition(Integer typeId, String title,Integer status,Integer page,Integer row);

	SecondMarketResult getCheckingGoods(Integer page,Integer row);

	SecondMarketResult checkGoods(CheckResultDto checkResultDto);

	SecondMarketResult lastestGoods(Integer row,Integer page,Integer type);

	SecondMarketResult lastestWant(Integer row, Integer page);

}
