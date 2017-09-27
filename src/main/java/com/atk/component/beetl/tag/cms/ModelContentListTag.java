package com.atk.component.beetl.tag.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.model.TCmsSite;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class ModelContentListTag extends GeneralVarTagBinding {
    @Autowired
    private ContentService contentService;
    @Autowired
    private SiteService siteService;

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Value("${system.site.subfix}")
    private String siteSubfix;

    @Value("${system.http.host}")
    private String httpHost;

    @Value("${system.site.prefix}")
    private String sitePrefix;

    @Override
    public void render() {
        if (CmsUtil.isNullOrEmpty(this.args[1]))
            throw  new SystemException("标签参数不能为空！");
        Integer titleLen =  Integer.parseInt((String) this.getAttributeValue("titleLen"));
        Integer siteId=  (this.getAttributeValue("siteId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("siteId")):(Integer)this.getAttributeValue("siteId");
        Integer modelId=  (this.getAttributeValue("modelId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("modelId")):(Integer) this.getAttributeValue("modelId");
        Integer isPic =  Integer.parseInt(CmsUtil.isNullOrEmpty(this.getAttributeValue("isPic"))?"0":(String)this.getAttributeValue("isPic"));
        Integer isRecommend =  Integer.parseInt(CmsUtil.isNullOrEmpty(this.getAttributeValue("isRecommend"))?"0":(String) this.getAttributeValue("isRecommend"));
        Integer orderBy =  Integer.parseInt((String) this.getAttributeValue("orderBy"));
        Integer pageNumber =  Integer.parseInt((CmsUtil.isNullOrEmpty(this.getAttributeValue("pageNumber"))?"1":(String) this.getAttributeValue("pageNumber")));
        Integer pageSize =  Integer.parseInt((String) this.getAttributeValue("size"));
        Integer isHot =  Integer.parseInt((String) this.getAttributeValue("isHot"));

        PageInfo<Map> pageInfo = contentService.findContentListBySiteIdAndModelId(siteId, modelId, orderBy, pageNumber, pageSize, isHot, isPic,isRecommend);
        if(CmsUtil.isNullOrEmpty(pageInfo.getList())) return;
        this.wrapRender(pageInfo.getList(),titleLen,siteId);
    }

    private void wrapRender(List<Map>  contentList, int titleLen, int siteId) {
        int i = 1;
        for (Map content : contentList) {
            String title = content.get("title").toString();
            int length = title.length();
            if (length > titleLen) {
                content.put("title",title.substring(0, titleLen) + "...");
            }
            if (StrUtil.isBlank(content.get("url").toString())) {
                TCmsSite site = siteService.findById(siteId);
                if(CmsUtil.isNullOrEmpty(site)) throw new CmsException("站点不存在[siteId:"+siteId+"]");
                String url = httpProtocol + "://" + (StrUtil.isBlank(site.getDomain())?httpHost:site.getDomain()) + "/"+sitePrefix+"/"+site.getSiteId()+"/";
                url+=content.get("categoryId")+"/"+content.get("contentId");
                content.put("url",url+siteSubfix);
            }
            content.put("index",i);
            this.binds(content);
            this.doBodyRender();
            i++;
        }

    }
}
