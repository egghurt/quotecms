package com.atk.component.beetl.fun;

import com.atk.module.web.cms.service.ItemService;
import com.atk.mybatis.model.TCmsItem;
import com.zhiliao.common.utils.ControllerUtil;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeItemFunction implements Function {
    @Autowired
    private ItemService itemService;
    @Value("${system.http.protocol}")
    private String httpProtocol;
    @Override
    public Object call(Object[] objects, Context context) {
        Long pid = Long.parseLong(objects[0].toString());
        Integer siteId = Integer.parseInt(objects[1].toString());
        return recursion(pid,siteId);
    }

    private String recursion(Long pid,Integer siteId){
        StringBuffer sbf = new StringBuffer();
        List<TCmsItem> cats  = itemService.findItemListByPid(pid, siteId);
        if(cats!=null&&cats.size()>0){
            for(TCmsItem cat:cats){
                sbf.append("  <li data-id=\""+cat.getItemId()+"\" data-pid=\""+pid+"\" data-url=\""+httpProtocol+"://"+ ControllerUtil.getDomain()+"/system/cms/item/input?id="+cat.getItemId()+"\" data-divid=\"#layout-item\">"+cat.getItemName()+" ["+cat.getItemId()+"] </li>");
                sbf.append(recursion(cat.getItemId(),siteId));
            }
            return  sbf.toString();
        }
        return "";
    }
}
