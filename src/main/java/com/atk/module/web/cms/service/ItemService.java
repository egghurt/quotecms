package com.atk.module.web.cms.service;

import com.atk.mybatis.model.TCmsItem;
import com.zhiliao.common.base.BaseService;

import java.util.List;

public interface ItemService extends BaseService<TCmsItem,Long> {
    List<TCmsItem> findItemListByPid(Long pid);

    List<TCmsItem> findItemListBySiteId(Integer siteId);

    List<TCmsItem> findItemListByPid(Long pid,Integer siteId);

    TCmsItem findByAlias(String alias);

    Integer findPageSize(Long itemId);

    TCmsItem findfindByAliasAndSiteId(String alias,Integer siteId);

    Integer AllCount();
}
