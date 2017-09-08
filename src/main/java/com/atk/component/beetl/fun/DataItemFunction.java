package com.atk.component.beetl.fun;

import com.atk.module.web.cms.service.ItemService;
import com.atk.module.web.cms.service.PatternFieldService;
import com.atk.module.web.cms.service.PatternService;
import com.atk.mybatis.model.TCmsItem;
import com.atk.mybatis.model.TCmsPattern;
import com.atk.mybatis.model.TCmsPatternField;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataItemFunction implements Function {
    @Autowired
    private ItemService itemService;
    @Autowired
    private PatternService patternService;
    @Autowired
    private PatternFieldService patternFieldService;

    @Override
    public Object call(Object[] objects, Context context) {
        Integer userId = (Integer) objects[0];
        Long itemId = (Long) objects[1];
        List<TCmsItem> items = itemService.findItemListByUserId(userId);
        if(items!=null&&items.size()>0) {
            StringBuffer sbf = new StringBuffer();
            for(TCmsItem i:items) {
                TCmsPattern pattern = patternService.findById(i.getPatternId());
                sbf.append("<tr><td>" + i.getItemName() + "<input type=\"hidden\" id=\"item-" + i.getItemId() + "\" name=\"itemId\" value=\""+i.getItemId()+"\"></td>");
                /*List<TCmsPatternField> cmsPatternFields = patternFieldService.findPatternFieldListByPatternId(pattern.getPatternId());
                for(TCmsPatternField f: cmsPatternFields) {
                    sbf.append("<td><label for=\"\" class=\"control-label x85\">"+f.getAlias()+"</label>");
                    sbf.append("<td><input type=\"text\" id=\"" + f.getFieldId() + "\" name=\"" + f.getFieldName()+ "\"></td>");
                }*/
                sbf.append("</tr>");
            }
            return sbf.toString();
        }
        return null;
    }
}
