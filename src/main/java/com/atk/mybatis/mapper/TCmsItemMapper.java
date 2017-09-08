package com.atk.mybatis.mapper;

import com.atk.mybatis.model.TCmsItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TCmsItemMapper extends Mapper<TCmsItem> {
    @Select("SELECT * FROM t_cms_item where parent_id=#{pid}")
    @ResultMap("BaseResultMap")
    List<TCmsItem> selectByPid(Long pid);

    @Delete("DELETE FROM t_cms_item WHERE parent_id=#{pid}")
    int deleteByPid(Long pid);

    @Select("SELECT i.* FROM t_cms_item i LEFT JOIN t_cms_user_item u ON i.has_child = 0 AND i.item_id = u.item_id WHERE u.user_id =#{userId}")
    @ResultMap("BaseResultMap")
    List<TCmsItem> selectItemListByUserId(Integer userId);
}
