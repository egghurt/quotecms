package com.atk.component.beetl.fun;

import com.atk.module.web.cms.service.ItemService;
import com.atk.mybatis.model.TCmsItem;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemFunction implements Function {
    @Autowired
    private ItemService service;

    private final String isChecked = " data-checked='true' ";

    @Override
    public Object call(Object[] objects, Context context) {
        /*父节点Id*/
        Long pid = (Long) objects[0];
         /*角色Id*/
        Integer userId = (Integer) objects[1];

        List<TCmsItem> items = service.findItemListByPid(pid);
        TCmsItem item=service.findById(pid);
        if(items!=null&&items.size()>0) {
            StringBuffer sbf = new StringBuffer();
            sbf.append("<li data-id='"+item.getItemId()+"' data-pid='"+item.getParentId()+"'"+isChecked(pid,userId)+">"+item.getItemName()+"</li>");
            for (TCmsItem i:items) {
                sbf.append("<li data-id='"+i.getItemId()+"' data-pid='"+i.getParentId()+"' "+isChecked(i.getItemId(),userId)+">"+i.getItemName()+"</li>");
                sbf.append(subItem(i.getItemId(), userId));
            }
            return sbf.toString();
        }else{
            return "<li data-id='"+item.getItemId()+"' data-pid='"+item.getParentId()+"'"+isChecked(pid,userId)+">"+item.getItemName()+"</li>";
        }
    }

    /**
     * 递归查询子节点
     * @param pid
     * @param userId
     * @return
     */
    public String subItem(Long pid,Integer userId){
        StringBuffer sbf = new StringBuffer();
        List<TCmsItem> items = service.findItemListByPid(pid);
        for (TCmsItem i : items) {
            sbf.append("<li data-id='"+i.getItemId()+"' data-pid='"+i.getParentId()+"' "+isChecked(i.getItemId(),userId)+">"+i.getItemName()+"</li>");
            sbf.append(subItem(i.getItemId(),userId));
        }
        return sbf.toString();
    }

    /**
     * 判断是否已分配品种
     * @param itemId
     * @param userId
     * @return
     */
    public String isChecked(Long itemId, Integer userId){
        if(service.findItemCountByUserId(userId, itemId)>0)
            return isChecked;
        return "";
    }
}
