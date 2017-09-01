package com.atk.module.web.cms;

import com.atk.module.web.cms.service.PatternService;
import com.atk.mybatis.model.TCmsPattern;
import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.base.BaseController;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.common.utils.UserUtil;
import com.zhiliao.module.web.system.vo.UserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@Controller
@RequestMapping("/system/cms/pattern")
public class PatternController extends BaseController<TCmsPattern> {
    @Autowired
    private PatternService patternService;

    @RequiresPermissions({"pattern:admin"})
    @RequestMapping
    @Override
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                        TCmsPattern pojo,
                        Model model) {
        UserVo userVo = UserUtil.getSysUserVo();
        pojo.setSiteId(userVo.getSiteId());
        model.addAttribute("model", patternService.page(pageNumber,pageSize,pojo));
        model.addAttribute("pojo",pojo);
        return "data/pattern_list";
    }

    @SysLog("模型添加")
    @RequiresPermissions({"pattern:input"})
    @RequestMapping("/input")
    @Override
    public String input(@RequestParam(value = "id",required = false) Integer Id, Model model) {
        if(Id!=null)
            model.addAttribute("pojo", patternService.findById(Id));
        return "data/pattern_input";
    }

    @RequiresPermissions({"pattern:save"})
    @RequestMapping("/save")
    @ResponseBody
    @Override
    public String save(TCmsPattern pojo) throws SQLException {
        UserVo userVo = UserUtil.getSysUserVo();
        pojo.setSiteId(userVo.getSiteId());
        if(pojo.getPatternId()!=null)
            patternService.update(pojo);
        return patternService.save(pojo);
    }

    @SysLog("模型删除")
    @RequiresPermissions({"pattern:delete"})
    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids) throws SQLException {
        return patternService.delete(ids);
    }

    @RequestMapping("/checkPatternName")
    @ResponseBody
    public String checkModelName(@RequestParam("modelName") String modelName) {
        if(patternService.findPatternByPatternName(PinyinUtil.convertLower(modelName))!=null)
            return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
    }

    @RequestMapping("/checkTableName")
    @ResponseBody
    public String checkTableName(@RequestParam("tableName") String tableName) {
        if(patternService.findPatternByTableName(PinyinUtil.convertLower(tableName)) != null)
            return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
    }
}
