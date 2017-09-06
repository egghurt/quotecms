package com.atk.mybatis.mapper;

import com.atk.mybatis.model.TCmsUserItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TCmsUserItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TCmsUserItem record);

    TCmsUserItem selectByPrimaryKey(Integer id);

    List<TCmsUserItem> selectAll();

    int updateByPrimaryKey(TCmsUserItem record);

    @Select("select count(0) from t_cms_user_item where user_id =#{userId} and item_id =#{itemId} ")
    int selectCountByUserIdAndItemId(@Param("userId") Integer userId, @Param("itemId") Long itemId);

    @Deprecated
    @Select("select * from t_cms_user_item where user_id =#{userId} and item_id =#{itemId} ")
    TCmsUserItem selectByUserIdAndItemId(@Param("userId") Integer userId,@Param("itemId") Long itemId);

    int deleteByUserId(@Param("userId") Integer userId);

    int deleteByItemId(@Param("itemId") Long itemId);
}
