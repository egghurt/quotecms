package com.atk.mybatis.mapper;

import com.atk.mybatis.model.TCmsData;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TCmsDataMapper extends Mapper<TCmsData> {
    List<TCmsData> selectByCondition(TCmsData data);

    List<TCmsData> selectByTableNameAndMap(@Param("tableName") String tableName,@Param("itemId") Long itemId, @Param("param") Map param);


    Map selectByDataIdAndTableName(Long dataId, String tableName);
    List selectByItemList(@Param("tableName")String tableName, @Param("itemIds")List<Long> itemIds);
}
