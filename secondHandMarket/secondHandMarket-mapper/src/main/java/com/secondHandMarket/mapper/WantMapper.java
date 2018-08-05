package com.secondHandMarket.mapper;

import com.secondHandMarket.pojo.Want;
import com.secondHandMarket.pojo.WantExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WantMapper {
    int countByExample(WantExample example);

    int deleteByExample(WantExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Want record);

    int insertSelective(Want record);

    List<Want> selectByExample(WantExample example);

    Want selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Want record, @Param("example") WantExample example);

    int updateByExample(@Param("record") Want record, @Param("example") WantExample example);

    int updateByPrimaryKeySelective(Want record);

    int updateByPrimaryKey(Want record);
}