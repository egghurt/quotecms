package com.atk.mybatis.mapper;

import com.atk.mybatis.model.TCmsPatternField;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TCmsPatternFieldMapper extends Mapper<TCmsPatternField> {
    @Select("select * from  t_cms_pattern_field where pattern_id = #{id}")
    @ResultMap("BaseResultMap")
    List<TCmsPatternField> selectByPatternId(@Param("id") Integer patternId);
}
