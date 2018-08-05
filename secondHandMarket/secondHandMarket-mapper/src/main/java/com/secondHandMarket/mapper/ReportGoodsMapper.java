package com.secondHandMarket.mapper;

import com.secondHandMarket.pojo.ReportGoods;
import com.secondHandMarket.pojo.ReportGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReportGoodsMapper {
    int countByExample(ReportGoodsExample example);

    int deleteByExample(ReportGoodsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReportGoods record);

    int insertSelective(ReportGoods record);

    List<ReportGoods> selectByExample(ReportGoodsExample example);

    ReportGoods selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReportGoods record, @Param("example") ReportGoodsExample example);

    int updateByExample(@Param("record") ReportGoods record, @Param("example") ReportGoodsExample example);

    int updateByPrimaryKeySelective(ReportGoods record);

    int updateByPrimaryKey(ReportGoods record);
}