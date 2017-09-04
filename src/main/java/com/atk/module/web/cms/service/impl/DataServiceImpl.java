package com.atk.module.web.cms.service.impl;

import com.atk.module.web.cms.service.DataService;
import com.atk.module.web.cms.service.ItemService;
import com.atk.module.web.cms.vo.TCmsDataVo;
import com.atk.mybatis.mapper.TCmsDataMapper;
import com.atk.mybatis.model.TCmsData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "cms-data-cache")
public class DataServiceImpl implements DataService {
    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Autowired
    private TCmsDataMapper dataMapper;

    @Autowired
    private DataSource dataSource;

    @Cacheable(key = "'find-dataId-'+#p0+'-tableName-'+#p1")
    @Override
    public Map findDataByDataIdAndTableName(Long dataId, String tableName) {
        Map result = dataMapper.selectByDataIdAndTableName(dataId, tableName);
        if(CmsUtil.isNullOrEmpty(result))
            throw new CmsException("数据项不存在或已删除！");
        return result;
    }

    @Autowired
    private ItemService itemService;

    @Override
    public String save(TCmsData pojo) {
        return null;
    }

    @Override
    public String update(TCmsData pojo) {
        return null;
    }

    @Override
    public String delete(Long[] ids) {
        return null;
    }

    @Override
    public TCmsData findById(Long id) {
        return null;
    }

    @Override
    public List<TCmsData> findList(TCmsData pojo) {
        return null;
    }

    @Override
    public List<TCmsData> findAll() {
        return null;
    }

    @Override
    public PageInfo<TCmsData> page(Integer pageNumber, Integer pageSize, TCmsData pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(dataMapper.selectByCondition(pojo));
    }

    @Override
    public PageInfo<TCmsData> page(Integer pageNumber, Integer pageSize, TCmsDataVo pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(dataMapper.selectByCondition(pojo));
    }

    @Override
    public PageInfo<TCmsData> page(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
