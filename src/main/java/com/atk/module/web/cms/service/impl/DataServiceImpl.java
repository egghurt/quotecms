package com.atk.module.web.cms.service.impl;

import com.atk.module.web.cms.service.DataService;
import com.atk.module.web.cms.service.ItemService;
import com.atk.module.web.cms.vo.TCmsDataVo;
import com.atk.mybatis.mapper.TCmsDataMapper;
import com.atk.mybatis.model.TCmsData;
import com.atk.mybatis.model.TCmsItem;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

    @Cacheable(key = "'find-itemIds-'+#p0+'-tableName-'+#p1")
    @Override
    public List findDataByItemList(List<Long> itemIds, String tableName) {
        return dataMapper.selectByItemList(tableName, itemIds);
    }

    @Autowired
    private ItemService itemService;

    @Override
    public String save(TCmsData pojo) {
        dataMapper.insert(pojo);
        System.out.println("What you saved is: " + pojo.getDataId());
        return null;
    }

    @Override
    public Long save(TCmsData pojo, String table) {
        if(dataMapper.insert(pojo) > 0) {
            String insert = "insert into t_cms_data_"+table+" set " +
                    "data_id=" + pojo.getDataId();
            try {
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                int status = stmt.executeUpdate(insert);
                if(status > 0) {
                    return pojo.getDataId();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String updatePatternData(String table, Long dataId, String field, String value) {
        String update = "update t_cms_data_" + table + " set "
                + field + " = " + value +" where data_id = " + dataId;
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            int status = stmt.executeUpdate(update);
            if(status > 0) {
                TCmsData data = findById(dataId);
                data.setUpdateDate(new Date());
                return update(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void disableHomeShow(Long itemId) {
        String update = "update t_cms_data set recent = 0 where item_id = " + itemId;
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(update);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String update(TCmsData pojo) {
        if(dataMapper.updateByPrimaryKeySelective(pojo) > 0)
            return JsonUtil.toSUCCESS("操作成功");
        return JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Long[] ids) {
        return null;
    }

    @Override
    public TCmsData findById(Long id) {
        return dataMapper.selectByPrimaryKey(id);
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
    public PageInfo<TCmsData> findDataListByPatternFiledValue(int pageNumber, Long itemId, String tableName, Map<String, Object> param) {
        PageHelper.startPage(pageNumber, itemService.findPageSize(itemId));
        return new PageInfo<>(dataMapper.selectByTableNameAndMap(tableName,itemId,param));
    }

    @Override
    public PageInfo<TCmsData> page(Integer pageNumber, Integer pageSize, TCmsDataVo pojo) {
        Long itemId = pojo.getItemId();
        TCmsItem item = itemService.findById(itemId);
        PageHelper.startPage(pageNumber,pageSize);
        List<TCmsData> data = new ArrayList<TCmsData>();
        if(item.getHasChild()) {
            List<TCmsItem> items = recursion(itemId, pojo.getSiteId());
            for(TCmsItem i:items) {
                TCmsDataVo dataVo = (TCmsDataVo)pojo.clone();
                dataVo.setItemId(i.getItemId());
                data.addAll(dataMapper.selectByCondition(dataVo));
            }
            return new PageInfo(data);
        }
        return new PageInfo(dataMapper.selectByCondition(pojo));
    }

    @Override
    public List<TCmsItem> getLeafChildren(Integer siteId, Long parentId) {
        return recursion(parentId, siteId);
    }

    public List<TCmsItem> recursion(Long itemId, Integer siteId) {
        List<TCmsItem> children = new ArrayList<TCmsItem>();
        for(TCmsItem item:itemService.findItemListByPid(itemId, siteId)) {
            if(item.getHasChild()) {
                children = recursion(item.getItemId(), siteId);
            }
            else {
                children.add(item);
            }
        }
        return children;
    }

    @Override
    public PageInfo<TCmsData> page(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
