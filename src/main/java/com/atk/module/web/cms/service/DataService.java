package com.atk.module.web.cms.service;

import com.atk.module.web.cms.vo.TCmsDataVo;
import com.atk.mybatis.model.TCmsData;
import com.atk.mybatis.model.TCmsItem;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.base.BaseService;

import java.util.List;
import java.util.Map;

public interface DataService extends BaseService<TCmsData, Long> {
    PageInfo<TCmsData> page(Integer pageNumber, Integer pageSize, TCmsDataVo pojo);

    PageInfo<TCmsData>  findDataListByPatternFiledValue(int pageNumber,Long itemId, String tableName, Map<String, Object> param);

    Map findDataByDataIdAndTableName(Long dataId, String tableName);
    Long save(TCmsData pojo, String table);
    String updatePatternData(String table, Long dataId, String field, String value);

    void disableHomeShow(Long itemId);

    List<TCmsItem> getLeafChildren(Integer siteId, Long parentId);
    List findDataByItemList(List<Long> itemIds, String table);

}
