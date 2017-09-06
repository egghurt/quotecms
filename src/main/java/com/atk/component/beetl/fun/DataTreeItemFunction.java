package com.atk.component.beetl.fun;

import com.atk.module.web.cms.service.ItemService;
import com.atk.mybatis.model.TCmsItem;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.StrUtil;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTreeItemFunction implements Function {
    @Autowired
    private ItemService service;

    @Value("${system.http.protocol}")
    private String httpProtocol;

    private String url = "/system/cms/item/input?id";

    @Override
    public Object call(Object[] objects, Context context) {
        Long pid = Long.parseLong(objects[0].toString());
        Integer siteId = Integer.parseInt(objects[1].toString());
        if(objects[2]!=null&&!StrUtil.isBlank(objects[2].toString()))
            this.url=objects[2].toString();
        return recursion(pid,siteId);
    }

    /* 递归函数 */
    private String recursion(Long pid,Integer siteId) {
        StringBuffer sbf = new StringBuffer();
        List<TCmsItem> items = service.findItemListByPid(pid, siteId);
        if (items != null && items.size() > 0) {
            for (TCmsItem item : items) {
                if (item.getAlone())
                    continue;
                if (items != null && service.findItemListByPid(item.getItemId(), siteId).size() > 0) {
                    sbf.append("  <li data-id=\"" + item.getItemId() + "\" data-pid=\"" + pid + "\" data-url=\"" + httpProtocol + "://" + ControllerUtil.getDomain() + url + "=" + item.getItemId() + "\" data-divid=\"#layout-data\">" + item.getItemName() + " [" + item.getItemId() + "] </li>");
                    //sbf.append("  <li data-id=\"" + item.getItemId() + "\" data-pid=\"" + pid + "\" >" + item.getItemName() + " [" + item.getItemId() + "] </li>");
                } else {
                    sbf.append("  <li data-id=\"" + item.getItemId() + "\" data-pid=\"" + pid + "\" data-url=\"" + httpProtocol + "://" + ControllerUtil.getDomain() + url + "=" + item.getItemId() + "\" data-divid=\"#layout-data\">" + item.getItemName() + " [" + item.getItemId() + "] </li>");
                }
                sbf.append(recursion(item.getItemId(), siteId));
            }
            return sbf.toString();
        }
        return "";
    }
}
