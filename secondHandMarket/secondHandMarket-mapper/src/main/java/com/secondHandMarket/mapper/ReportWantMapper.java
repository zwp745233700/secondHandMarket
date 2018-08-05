package com.secondHandMarket.mapper;

import com.secondHandMarket.pojo.ReportWant;
import com.secondHandMarket.pojo.ReportWantExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReportWantMapper {
    int countByExample(ReportWantExample example);

    int deleteByExample(ReportWantExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReportWant record);

    int insertSelective(ReportWant record);

    List<ReportWant> selectByExample(ReportWantExample example);

    ReportWant selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReportWant record, @Param("example") ReportWantExample example);

    int updateByExample(@Param("record") ReportWant record, @Param("example") ReportWantExample example);

    int updateByPrimaryKeySelective(ReportWant record);

    int updateByPrimaryKey(ReportWant record);
}