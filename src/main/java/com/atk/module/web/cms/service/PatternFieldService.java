package com.atk.module.web.cms.service;

import com.atk.mybatis.model.TCmsPatternField;
import com.zhiliao.common.base.BaseService;

import java.util.List;

public interface PatternFieldService extends BaseService<TCmsPatternField, Integer> {
    TCmsPatternField findPatternFieldByFieldName(String filedName);
    List<TCmsPatternField> findPatternFieldListByPatternId(Integer patternId);
}
