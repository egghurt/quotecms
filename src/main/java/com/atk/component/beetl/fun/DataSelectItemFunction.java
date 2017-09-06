package com.atk.component.beetl.fun;

import com.atk.module.web.cms.service.ItemService;
import com.atk.mybatis.model.TCmsItem;
import com.zhiliao.common.utils.StrUtil;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSelectItemFunction implements Function {
    @Autowired
    private ItemService service;
    
    @Override
    public Object call(Object[] objects, Context context) {
        Integer siteId = Integer.parseInt(objects[0].toString()); /*站点Id*/
        Long currentId =Long.parseLong(objects[1].toString()); /*当前品种Id*/
        return recursion(currentId,0l,"",siteId);
    }

    /*递归输出子节点*/
    private String recursion(Long cid, Long pid, String tag, Integer siteId){
         /*临时拼凑看不懂就放弃*/
        tag+=(StrUtil.isBlank(tag)?"&nbsp;&nbsp;":"&nbsp;&nbsp;&nbsp;&nbsp;");
        StringBuffer sbf = new StringBuffer();
        List<TCmsItem> items  = service.findItemListByPid(pid, siteId);
        if(items!=null&&items.size()>0){
            for(TCmsItem item:items){
                if(item.getAlone())
                    continue;
                if(service.findItemListByPid(item.getItemId(),siteId).size()>0) {
                    if(item.getParentId()==0)
                        sbf.append("<optgroup label=\""+item.getItemName() + "\">");
                    else
                        sbf.append("<optgroup label=\"|—"+item.getItemName() + "\">");
                    sbf.append(recursion(cid,item.getItemId(),tag,siteId));
                    sbf.append("</optgroup>");
                }else {
                    if (cid.longValue() == item.getItemId().longValue())
                        sbf.append("<option value=\"" + item.getItemId() + "\" selected='selected' >" + tag + "&nbsp;|—" + item.getItemName() + "</option>");
                    else
                        sbf.append("<option value=\"" + item.getItemId() + "\" >" + tag + "&nbsp;|—" + item.getItemName() + "</option>");
                }
            }
            return  sbf.toString();
        }
        return "";
    }
}
