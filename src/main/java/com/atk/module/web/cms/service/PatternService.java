package com.atk.module.web.cms.service;

import com.atk.mybatis.model.TCmsPattern;
import com.zhiliao.common.base.BaseService;

import java.util.List;

public interface PatternService extends BaseService<TCmsPattern, Integer> {
    TCmsPattern findPatternByPatternName(String patternName);
    TCmsPattern findPatternByTableName(String tableName);
    List<TCmsPattern> findPatternListByStatusAndSiteId(boolean status,Integer siteId);
    List<TCmsPattern> findPatternListByStatus(boolean status);
}
