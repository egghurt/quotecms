package com.atk.module.web.cms;

import com.atk.module.web.cms.service.PatternFieldService;
import com.atk.mybatis.model.TCmsPatternField;
import com.zhiliao.common.base.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

import static com.zhiliao.common.db.kit.DbTableKit.PREPARED_MODEL_FILED_NAME;

@Controller
@RequestMapping("system/cms/pattern/field")
public class PatternFieldController extends BaseController<TCmsPatternField> {
    @Autowired
    private PatternFieldService fieldService;

    @RequestMapping
    @Override
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100")Integer pageSize,
                        TCmsPatternField pojo, Model model) {
        model.addAttribute("pojo",pojo);
        model.addAttribute("model",fieldService.page(pageNumber,pageSize,pojo));
        return "data/pattern_field_list";
    }

    @RequiresPermissions("patternField:input")
    @RequestMapping("/input")
    @Override
    public String input(@RequestParam(value = "patternId",required = false)Integer patternId, Model model) {
        model.addAttribute("patternId", patternId);
        return "data/pattern_field_input";
    }

    @RequiresPermissions("patternField:save")
    @RequestMapping("/save")
    @ResponseBody
    @Override
    public String save(TCmsPatternField pojo) throws SQLException {
        if(pojo.getFieldId()!=null)
            return fieldService.update(pojo);
        return fieldService.save(pojo);
    }

    @Override
    public String delete(Integer[] ids) throws SQLException {
        return fieldService.delete(ids);
    }

    @RequestMapping("/checkFieldName")
    @ResponseBody
    public String checkFiledName(@RequestParam("fieldName") String fieldName){
        Boolean flag = false;
        for(String s :PREPARED_MODEL_FILED_NAME){
            if(fieldName.toLowerCase().equals(s.toLowerCase())){
                flag =true;
                break;
            }
        }
        if((fieldService.findPatternFieldByFieldName(fieldName)!=null)||flag)
            return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
    }


}
