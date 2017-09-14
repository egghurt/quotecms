package com.atk.module.web.cms;

import com.atk.module.web.cms.service.DataService;
import com.atk.module.web.cms.service.ItemService;
import com.atk.module.web.cms.service.PatternFieldService;
import com.atk.module.web.cms.service.PatternService;
import com.atk.module.web.cms.vo.TCmsDataVo;
import com.atk.mybatis.model.TCmsData;
import com.atk.mybatis.model.TCmsItem;
import com.atk.mybatis.model.TCmsPattern;
import com.atk.mybatis.model.TCmsPatternField;
import com.google.common.collect.Maps;
import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.UserUtil;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
        TCmsItem item = itemService.findById(data.getItemId());
        TCmsPattern pattern = patternService.findById(item.getPatternId());
        String table = pattern.getTableName();
        model.addAttribute("model", dataService.page(pageNumber, pageSize, data));
        model.addAttribute("pojo", data);
        model.addAttribute("item", item);
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
    public String batch(@RequestParam(value = "itemId",required = false) Long itemId,
                        Model model) {
        UserVo userVo = ((UserVo) ControllerUtil.getHttpSession().getAttribute(CmsConst.SITE_USER_SESSION_KEY));
        if(CmsUtil.isNullOrEmpty(userVo))
            throw new UnauthenticatedException();
        TCmsItem item = itemService.findById(itemId);
        if(CmsUtil.isNullOrEmpty(item))
            throw new CmsException("当前数据品种不存在或已经被删！");
        TCmsPattern pattern = patternService.findById(item.getPatternId());
        List<TCmsPatternField> cmsPatternFields = patternFieldService.findPatternFieldListByPatternId(pattern.getPatternId());
        model.addAttribute("items", itemService.selectItemListByUserIdAndParentId(userVo.getUserId(), itemId));
        model.addAttribute("field", cmsPatternFields);
        return "data/data_batch";
    }

    @RequestMapping("batchSave")
    @ResponseBody
    public String batchSave(HttpServletRequest request) {
        UserVo userVo = UserUtil.getSysUserVo();

        Enumeration<String> parameters = request.getParameterNames();

        List<TCmsItem> cmsItems = new ArrayList<TCmsItem>();

        Map<Long, Long> map = Maps.newHashMap();
        while (parameters.hasMoreElements()) {
            String name = parameters.nextElement();
            if(name.equals("itemId")) {
                String[] items = request.getParameterValues(name);
                for(String i:items) {
                    TCmsItem item = itemService.findById(Long.parseLong(i));
                    TCmsPattern pattern = patternService.findById(item.getPatternId());
                    TCmsData data = new TCmsData();
                    data.setSiteId(userVo.getSiteId());
                    data.setUserId(userVo.getUserId());
                    data.setInputDate(new Date());
                    data.setStatus(0);
                    data.setName(item.getItemName());
                    data.setItemId(item.getItemId());
                    data.setPatternId(item.getPatternId());
                    Long dataId = dataService.save(data, pattern.getTableName());
                    map.put(item.getItemId(), dataId);
                    cmsItems.add(item);
                }
            }
            else {
                String[] quotes = request.getParameterValues(name);
                for(int i=0; i<quotes.length; i++) {
                    TCmsItem item = cmsItems.get(i);
                    TCmsPattern pattern = patternService.findById(item.getPatternId());
                    Long dataId = map.get(item.getItemId());
                    dataService.updatePatternData(pattern.getTableName(), dataId, name, quotes[i]);
                }
            }
        }

        return JsonUtil.toSUCCESS("保存成功", "layout-data", true);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
