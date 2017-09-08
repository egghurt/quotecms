package com.atk.module.web.cms.service.impl;

import com.atk.common.constant.CmsConst;
import com.atk.module.web.cms.service.ItemService;
import com.atk.mybatis.mapper.TCmsDataMapper;
import com.atk.mybatis.mapper.TCmsItemMapper;
import com.atk.mybatis.mapper.TCmsUserItemMapper;
import com.atk.mybatis.model.TCmsItem;
import com.atk.mybatis.model.TCmsUserItem;
import com.github.pagehelper.PageInfo;

import com.zhiliao.common.utils.*;
import com.zhiliao.mybatis.model.TSysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cms-item-cache")
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TCmsItemMapper itemMapper;

    @Autowired
    private TCmsDataMapper dataMapper;

    @Autowired
    private TCmsUserItemMapper userItemMapper;

    @Cacheable(key = "'find-list-pid-'+#p0")
    @Override
    public List<TCmsItem> findItemListByPid(Long pid) {
        TCmsItem item = new TCmsItem();
        item.setParentId(pid);
        return itemMapper.select(item);
    }

    @Override
    public List<TCmsItem> findItemListBySiteId(Integer siteId) {
        TCmsItem item = new TCmsItem();
        item.setSiteId(siteId);
        return itemMapper.select(item);
    }

    @Cacheable(key = "'find-list-pid-'+#p0+'-siteId-'+#p1")
    @Override
    public List<TCmsItem> findItemListByPid(Long pid, Integer siteId) {
        TCmsItem item = new TCmsItem();
        item.setParentId(pid);
        item.setSiteId(siteId);
        return itemMapper.select(item);
    }

    @Cacheable(key = "'find-alias-'+#p0")
    @Override
    public TCmsItem findByAlias(String alias) {
        TCmsItem item = new TCmsItem();
        item.setAlias(alias);
        return itemMapper.selectOne(item);
    }

    @Cacheable(key = "'find-page-size-'+#p0")
    @Override
    public Integer findPageSize(Long itemId) {
        return this.itemMapper.selectByPrimaryKey(itemId).getPageSize();
    }

    @Cacheable(key = "'find-alias-'+#p0+'-siteId-'+#p1")
    @Override
    public TCmsItem findByAliasAndSiteId(String alias, Integer siteId) {
        TCmsItem item = new TCmsItem();
        item.setAlias(alias);
        item.setSiteId(siteId);
        return itemMapper.selectOne(item);
    }

    @Override
    public Integer AllCount() {
        return this.itemMapper.selectCount(new TCmsItem());
    }

    @CacheEvict(cacheNames = "cms-item-cache",allEntries = true,beforeInvocation = true)
    @Override
    public String save(TCmsItem pojo) {
        /* 将品种名称转换为拼音设置别名 */
        pojo.setAlias(PinyinUtil.convertLower(HtmlKit.getText(pojo.getItemName())));
        /* 判断是否设置当前品种为数据类别 */
        if(pojo.getParentId()!=0L){
            TCmsItem parentItem =itemMapper.selectByPrimaryKey(pojo.getParentId());
            parentItem.setHasChild(true);
            itemMapper.updateByPrimaryKeySelective(parentItem);

            if(StrUtil.isBlank(pojo.getListTpl()))
                pojo.setListTpl(StrUtil.isBlank(parentItem .getListTpl()) ? CmsConst.ITEM_LIST_TPL :parentItem .getListTpl());
        }else {
            pojo.setListTpl(StrUtil.isBlank(pojo.getListTpl()) ? CmsConst.ITEM_LIST_TPL : pojo.getListTpl());
        }
        if(itemMapper.insert(pojo)>0){
            return JsonUtil.toSUCCESS("品种添加成功！","item-tab",true);
        }
        return JsonUtil.toERROR("品种添加失败！");
    }

    @Override
    @Transactional(transactionManager = "masterTransactionManager")
    public String update(TSysUser user, Long[] itemId) {
        if(user != null) {
            if(!CmsUtil.isNullOrEmpty(itemId)) {
                userItemMapper.deleteByUserId(user.getUserId());
            }
            if(!CmsUtil.isNullOrEmpty(itemId)&&itemId.length>0){
                for (long id : itemId) {
                    TCmsUserItem ui = new TCmsUserItem();
                    ui.setUserId(user.getUserId());
                    ui.setItemId(id);
                    saveUserItem(ui);
                }
            }
            return JsonUtil.toSUCCESS("操作成功","user-item",true);
        }
        return JsonUtil.toERROR("操作失败！");
    }

    @Transactional(transactionManager = "masterTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    @CacheEvict(cacheNames = "cms-item-cache",allEntries = true,beforeInvocation = true)
    @Override
    public String update(TCmsItem pojo) {
        /* 将品种名称转换为拼音设置别名 */
        pojo.setAlias(PinyinUtil.convertLower(HtmlKit.getText(pojo.getItemName())));

        /*  判断当前品种父Id是不是数据分类(顶级) */
        if(itemMapper.selectByPrimaryKey(pojo.getItemId()).getParentId()==0&&pojo.getParentId()!=0L){
            /* 父类交换 */
            TCmsItem parentItem = itemMapper.selectByPrimaryKey(pojo.getParentId());
            parentItem.setParentId(0l);
            parentItem.setHasChild(true);
            pojo.setParentId(pojo.getParentId());
            itemMapper.updateByPrimaryKey(parentItem);

            /*判断当前栏目是否需要继承父类模板*/
            if(StrUtil.isBlank(pojo.getListTpl()))
                pojo.setListTpl(StrUtil.isBlank(parentItem .getListTpl()) ? CmsConst.ITEM_LIST_TPL :parentItem .getListTpl());
        }else {
            pojo.setListTpl(StrUtil.isBlank(pojo.getListTpl()) ? CmsConst.ITEM_LIST_TPL : pojo.getListTpl());
        }
        if(itemMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("品种更新成功！","item-tab",false);
        return JsonUtil.toERROR("品种更新失败！");
    }

    @Override
    public Integer findItemCountByUserId(Integer userId, Long itemId) {
        return userItemMapper.selectCountByUserIdAndItemId(userId, itemId);
    }

    @Override
    public Integer saveUserItem(TCmsUserItem userItem) {
        return userItemMapper.insert(userItem);
    }

    @Override
    @Transactional(transactionManager = "masterTransactionManager")
    public String deleteItemById(Long id) {
        if(itemMapper.deleteByPrimaryKey(id) > 0) {
            userItemMapper.deleteByItemId(id);
            List<TCmsItem> items = itemMapper.selectByPid(id);
            if(items != null && items.size()>0) {
                for(TCmsItem i:items) {
                    userItemMapper.deleteByItemId(i.getItemId());
                }
            }
            itemMapper.deleteByPid(id);
            return  JsonUtil.toSUCCESS("品种删除成功","item",true);
        }
        return JsonUtil.toERROR("品种删除失败");
    }

    @Cacheable(key = "'find-itemlist-byuser-'+#p0")
    @Override
    public List<TCmsItem> findItemListByUserId(Integer userId) {
        return itemMapper.selectItemListByUserId(userId);
    }

    @Override
    public List<TCmsItem> selectItemListByUserIdAndParentId(Integer userId, Long parentId) {
        List<TCmsItem> result = new ArrayList<TCmsItem>();
        List<TCmsItem> children = getChildren(findById(parentId));
        List<TCmsItem> check = findItemListByUserId(userId);
        for(TCmsItem s:children) {
            for(TCmsItem t:check) {
                if(s.getItemId() == t.getItemId()) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    public List<TCmsItem> getChildren(TCmsItem item) {
        List<TCmsItem> result = new ArrayList<TCmsItem>();
        List<TCmsItem> children = findItemListByPid(item.getItemId());
        if(children != null) {
            for (TCmsItem i : children) {
                if (i.getHasChild()) {
                    result.addAll(getChildren(i));
                }
                else {
                    result.add(i);
                }
            }
        }
        return result;
    }

    @Override
    public String delete(Long[] ids) {
        return null;
    }

    @Override
    public TCmsItem findById(Long id) {
        return itemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsItem> findList(TCmsItem pojo) {
        return null;
    }

    @Override
    public List<TCmsItem> findAll() {
        return null;
    }

    @Override
    public PageInfo<TCmsItem> page(Integer pageNumber, Integer pageSize, TCmsItem pojo) {
        return null;
    }

    @Override
    public PageInfo<TCmsItem> page(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
