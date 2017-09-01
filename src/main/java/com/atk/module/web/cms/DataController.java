package com.atk.module.web.cms;

import com.atk.module.web.cms.vo.TCmsDataVo;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.module.web.system.vo.UserVo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/cms/data")
public class DataController {
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
        model.addAttribute("pojo",data);
        return "data/data_list";
    }
}
