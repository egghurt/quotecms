package com.atk.module.web.cms;

import com.atk.module.web.cms.service.DataService;
import com.atk.module.web.cms.service.ItemService;
import com.atk.module.web.cms.service.PatternFieldService;
import com.atk.module.web.cms.service.PatternService;
import com.atk.module.web.cms.vo.TCmsDataVo;
import com.atk.mybatis.model.TCmsItem;
import com.atk.mybatis.model.TCmsPattern;
import com.atk.mybatis.model.TCmsPatternField;
import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.module.web.system.vo.UserVo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/system/cms/data")
public class DataController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private DataService dataService;
    @Autowired
    private PatternService patternService;
    @Autowired
    private PatternFieldService patternFieldService;

    @RequestMapping
    public String index() {
        return "data/data";
    }

    @RequiresPermissions("data:admin")
    @RequestMapping("/page")
    public String page(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                       @RequestParam(value = "pageSize",defaultValue = "50") Integer pageSize,
                       TCmsDataVo data,
                       Model model){
        UserVo userVo = ((UserVo) ControllerUtil.getHttpSession().getAttribute(CmsConst.SITE_USER_SESSION_KEY));
        if(CmsUtil.isNullOrEmpty(userVo))
            throw  new UnauthenticatedException();
        data.setSiteId(userVo.getSiteId());
        data.setUserId(userVo.getUserId());
        model.addAttribute("model", dataService.page(pageNumber, pageSize, data));
        model.addAttribute("pojo",data);
        return "data/data_list";
    }

    @SysLog("内容添加")
    @RequiresPermissions("data:input")
    @RequestMapping("/input")
    public String input(@RequestParam(value = "itemId",required = false) Long itemId,
                        @RequestParam(value = "dataId",required = false) Long dataId,
                        @RequestParam(value = "isWindow",defaultValue = "NO") String isWindow,
                        Model model) {
        TCmsItem item = itemService.findById(itemId);
        if(CmsUtil.isNullOrEmpty(item))
            throw new CmsException("当前数据所属品种已被删除！");
        TCmsPattern pattern = patternService.findById(item.getPatternId());
        List<TCmsPatternField> cmsPatternFields = patternFieldService.findPatternFieldListByPatternId(pattern.getPatternId());
        if(dataId != null) {
            model.addAttribute("data", dataService.findDataByDataIdAndTableName(dataId, pattern.getTableName()));
        }
        model.addAttribute("patternField",cmsPatternFields);
        model.addAttribute("item", item);
        model.addAttribute("isWindow",isWindow);
        return "data/data_input";
    }

    @SysLog("批量添加")
    @RequiresPermissions("data:batch")
    @RequestMapping("/batch")
    public String batch(@RequestParam(value = "categoryId",required = false) Long categoryId,
                        @RequestParam(value = "contentId",required = false) Long contentId,
                        @RequestParam(value = "isWindow",defaultValue = "NO") String isWindow,
                        Model model) {
        return "data/data_batch";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
