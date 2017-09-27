package com.atk.component.beetl.tag.data;

import com.atk.module.web.cms.service.DataService;
import com.atk.module.web.cms.service.ItemService;
import com.atk.module.web.cms.service.PatternService;
import com.atk.mybatis.model.TCmsItem;
import com.atk.mybatis.model.TCmsPattern;
import com.zhiliao.common.exception.CmsException;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class QuoteListTag extends GeneralVarTagBinding {
    @Autowired
    private DataService dataService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PatternService patternService;

    @Override
    public void render() {
        Integer siteId=  (this.getAttributeValue("siteId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("siteId")):(Integer)this.getAttributeValue("siteId");
        Long parentId=  (this.getAttributeValue("parentId") instanceof String)?Long.parseLong((String) this.getAttributeValue("parentId")):(Long) this.getAttributeValue("parentId");
        List<TCmsItem> items =  dataService.getLeafChildren(siteId, parentId);
        TCmsItem item = itemService.findById(parentId);
        TCmsPattern pattern = patternService.findById(item.getPatternId());
        try {
            wrapRender(items, pattern.getTableName());
        } catch (Exception e) {
            throw new CmsException(e.getMessage());
        }
    }

    private void wrapRender(List<TCmsItem>  items, String table) throws Exception {
        List<Long> ids = new ArrayList<Long>();
        for(TCmsItem i: items) {
            ids.add(i.getItemId());
        }
        for(Object o: dataService.findDataByItemList(ids, table)) {
            Map result = (Map)o;
            this.binds(result);
            this.doBodyRender();
        }
    }
}
