package com.atk.module.web.cms.service;

import com.atk.mybatis.model.TCmsItem;
import com.atk.mybatis.model.TCmsUserItem;
import com.zhiliao.common.base.BaseService;
import com.zhiliao.mybatis.model.TSysUser;

import java.util.List;

public interface ItemService extends BaseService<TCmsItem,Long> {

    String update(TSysUser user, Long[] itemId);

    List<TCmsItem> findItemListByPid(Long pid);

    List<TCmsItem> findItemListBySiteId(Integer siteId);

    List<TCmsItem> findItemListByPid(Long pid,Integer siteId);

    TCmsItem findByAlias(String alias);

    Integer findPageSize(Long itemId);

    TCmsItem findByAliasAndSiteId(String alias,Integer siteId);

    Integer AllCount();

    public Integer findItemCountByUserId(Integer userId, Long itemId);

    public Integer saveUserItem(TCmsUserItem userItem);

    public String deleteItemById(Long id);

    List<TCmsItem> findItemListByUserId(Integer userId);

    List<TCmsItem> selectItemListByUserIdAndParentId(Integer userId, Long parentId);
}
