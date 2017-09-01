package com.atk.module.web.cms.service.impl;

import com.atk.common.db.DataTableAssistantService;
import com.atk.module.web.cms.service.PatternService;
import com.atk.mybatis.mapper.TCmsItemMapper;
import com.atk.mybatis.mapper.TCmsPatternFieldMapper;
import com.atk.mybatis.mapper.TCmsPatternMapper;
import com.atk.mybatis.model.TCmsItem;
import com.atk.mybatis.model.TCmsPattern;
import com.atk.mybatis.model.TCmsPatternField;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.PinyinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cms-pattern-cache")
public class PatternServiceImpl implements PatternService {
    @Autowired
    TCmsPatternMapper patternMapper;
    @Autowired
    DataTableAssistantService dbTableAssistant;
    @Autowired
    TCmsItemMapper itemMapper;
    @Autowired
    TCmsPatternFieldMapper patternFieldMapper;

    @Override
    public TCmsPattern findPatternByPatternName(String patternName) {
        TCmsPattern model = new TCmsPattern();
        model.setPatternName(patternName);
        return patternMapper.selectOne(model);
    }

    @Override
    public TCmsPattern findPatternByTableName(String tableName) {
        TCmsPattern model = new TCmsPattern();
        model.setTableName(tableName);
        return patternMapper.selectOne(model);
    }

    @Cacheable(key = "'list-status-'+#p0+'-site-'+#p1")
    @Override
    public List<TCmsPattern> findPatternListByStatusAndSiteId(boolean status, Integer siteId) {
        TCmsPattern model = new TCmsPattern();
        model.setStatus(status);
        model.setSiteId(siteId);
        return patternMapper.select(model);
    }

    @Cacheable(key = "'list-status-'+#p0")
    @Override
    public List<TCmsPattern> findPatternListByStatus(boolean status) {
        TCmsPattern model = new TCmsPattern();
        model.setStatus(status);
        return patternMapper.select(model);
    }

    @CacheEvict(cacheNames = "cms-pattern-cache",allEntries = true)
    @Override
    public String save(TCmsPattern pojo) {
        String tableName = PinyinUtil.convertLower(pojo.getTableName());
        pojo.setTableName(tableName);
        try {
            dbTableAssistant.createDbtable(tableName);
        } catch (SQLException e) {
            throw  new SystemException(e.getMessage());
        }
        if (patternMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","pattern-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @CacheEvict(cacheNames = "cms-pattern-cache",allEntries = true)
    @Override
    public String update(TCmsPattern pojo) {
        if (patternMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","pattern-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Integer[] ids) {
        if(ids!=null) {
            for (Integer id : ids) {

               /*检查当前模型是否管理栏目*/
                TCmsItem item = new TCmsItem();
                item.setPatternId(id);
                List<TCmsItem> items = itemMapper.select(item);
                if(!CmsUtil.isNullOrEmpty(items)&&items.size()>0)
                    throw new CmsException("当前模型关联多个栏目[size:"+items.size()+"],请先删除栏目！");
               /*删除数据库中的模型表*/
                TCmsPattern pattern =patternMapper.selectByPrimaryKey(id);
                try {
                    dbTableAssistant.deleteDbtable(pattern.getTableName());
                } catch (SQLException e) {
                    throw  new SystemException(e.getMessage());
                }
                patternMapper.deleteByPrimaryKey(id);
               /*清空与当前模型相关的字段*/
                TCmsPatternField patternField = new TCmsPatternField();
                patternField.setPatternId(id);
                patternFieldMapper.delete(patternField);

            }
            return JsonUtil.toSUCCESS("操作成功", "pattern-tab", false);
        }
        return  JsonUtil.toERROR("操作失败");
    }

    @Cacheable(key = "#p0")
    @Override
    public TCmsPattern findById(Integer id) {
        TCmsPattern pattern = patternMapper.selectByPrimaryKey(id);
        if(CmsUtil.isNullOrEmpty(pattern))
            throw new SystemException("模型（"+id+"）不存在！");
        return pattern;
    }

    @Override
    public List<TCmsPattern> findList(TCmsPattern pojo) {
        return patternMapper.select(pojo);
    }

    @Override
    public List<TCmsPattern> findAll() {
        return patternMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsPattern> page(Integer pageNumber, Integer pageSize, TCmsPattern pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findList(pojo));

    }

    @Override
    public PageInfo<TCmsPattern> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }
}
