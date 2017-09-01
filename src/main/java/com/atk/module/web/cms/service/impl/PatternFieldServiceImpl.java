package com.atk.module.web.cms.service.impl;

import com.atk.common.db.DataTableAssistantService;
import com.atk.module.web.cms.service.PatternFieldService;
import com.atk.mybatis.mapper.TCmsPatternFieldMapper;
import com.atk.mybatis.mapper.TCmsPatternMapper;
import com.atk.mybatis.model.TCmsPattern;
import com.atk.mybatis.model.TCmsPatternField;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.db.kit.DbTableKit;
import com.zhiliao.common.db.vo.FiledTypeVo;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.HtmlKit;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.PinyinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cms_patternField-cache")
public class PatternFieldServiceImpl implements PatternFieldService {
    @Autowired
    private TCmsPatternMapper patternMapper;
    @Autowired
    private TCmsPatternFieldMapper fieldMapper;
    @Autowired
    private DataTableAssistantService dbTableAssistant;

    @Override
    public TCmsPatternField findPatternFieldByFieldName(String fieldName) {
        TCmsPatternField field = new TCmsPatternField();
        field.setFieldName(PinyinUtil.convertLower(fieldName));
        return fieldMapper.selectOne(field);
    }

    @Override
    public List<TCmsPatternField> findPatternFieldListByPatternId(Integer patternId) {
        List<TCmsPatternField> result = fieldMapper.selectByPatternId(patternId);
        return result;
    }

    @Override
    @Transactional(transactionManager = "masterTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public String save(TCmsPatternField pojo) {
        pojo.setFieldName(PinyinUtil.convertLower(HtmlKit.getText(pojo.getFieldName())));
        FiledTypeVo fieldTypeVo = DbTableKit.getFiledTypeVo(pojo.getFieldClass(),pojo.getFieldType(),pojo.getFieldLength(),pojo.getFieldValue());
        pojo.setFieldLength(fieldTypeVo.getLength());
        pojo.setFieldType(fieldTypeVo.getM().toString());
        if (fieldMapper.insertSelective(pojo)>0) {
            TCmsPattern pattern = patternMapper.selectByPrimaryKey(pojo.getPatternId());
            /* 根据模型表名添加数据库字段 */
            try {
                dbTableAssistant.addDbTableColumn(pattern.getTableName(),pojo.getFieldName(),fieldTypeVo.getM(),fieldTypeVo.getLength(),false,fieldTypeVo.getDefaultValue(),pojo.getNotNull(),false);
            } catch (SQLException e) {
                throw  new SystemException(e.getMessage());
            }
            return JsonUtil.toSUCCESS("操作成功", "", "pattern-field-tab",true);
        }
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String update(TCmsPatternField pojo) {
        if (fieldMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","pattern-field-tab",false);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Integer[] ids) {
        if(ids!=null) {
            for (Integer id : ids) {
                TCmsPatternField field =fieldMapper.selectByPrimaryKey(id);
                TCmsPattern model = patternMapper.selectByPrimaryKey(field.getPatternId());
                /* 根据模型表名删除表字段 */
                if(model!=null) {
                    try {
                        dbTableAssistant.deleteDbTableColumn(model.getTableName(), field.getFieldName(), false);
                    } catch (SQLException e) {
                        throw  new SystemException(e.getMessage());
                    }
                }
                fieldMapper.deleteByPrimaryKey(id);
            }
            return JsonUtil.toSUCCESS("操作成功", "pattern-field-tab", false);
        }
        return  JsonUtil.toERROR("操作失败");
    }

    @Cacheable(key = "#p0")
    @Override
    public TCmsPatternField findById(Integer id) {
        return fieldMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsPatternField> findList(TCmsPatternField pojo) {
        return fieldMapper.select(pojo);
    }

    @Override
    public List<TCmsPatternField> findAll() {
        return fieldMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsPatternField> page(Integer pageNumber, Integer pageSize, TCmsPatternField pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findList(pojo));
    }

    @Override
    public PageInfo<TCmsPatternField> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }
}
