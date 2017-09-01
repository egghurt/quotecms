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
public class SelectItemFunction implements Function {
    @Autowired
    private ItemService itemService;

    private final String head = "<option value=\"0\" >顶级节点</option>";

    private final String isSelected = "selected=\"selected\"";
    
    @Override
    public Object call(Object[] objects, Context context) {
        Long pid = Long.parseLong(objects[0].toString());
        Long currentId =Long.parseLong(objects[1].toString());
        Integer siteId =Integer.parseInt(objects[2].toString());
        if(pid!=null)
            return head+recursion(currentId,pid,0l,"",siteId);
        return head+recursion(currentId,0l,0l,"",siteId);
    }

    /*递归输出子节点*/
    private String recursion(Long cid,Long pid,Long sPid,String tag,Integer siteId){
         /*临时拼凑看不懂就放弃*/
        tag+=(StrUtil.isBlank(tag)?"&nbsp;&nbsp;":"&nbsp;&nbsp;&nbsp;&nbsp;");
        StringBuffer sbf = new StringBuffer();
        List<TCmsItem> items  = itemService.findItemListByPid(sPid,siteId);
        if(items!=null&&items.size()>0){
            for(TCmsItem cat:items){
                   /*如果是自己就不输出了*/
                if(cid!=cat.getItemId()&&cid!=cat.getParentId()||cid==0)
                    sbf.append("<option value=\"" + cat.getItemId() + "\" " + isSelected(cat.getItemId(), pid) + ">" + tag + "|—" + cat.getItemName()+ "</option>");
                sbf.append(recursion(cid,pid,cat.getItemId(),tag,siteId));
            }
            return  sbf.toString();
        }
        return "";
    }

    private String isSelected(Long id,Long perId){
        if(id==perId)
            return isSelected;
        return "";
    }
}
