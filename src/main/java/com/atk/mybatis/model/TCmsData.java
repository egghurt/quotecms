package com.atk.mybatis.model;

import com.zhiliao.common.annotation.ExcelField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "t_cms_data")
public class TCmsData implements Serializable {
    @ExcelField("数据编号")
    @Id
    @Column(name = "data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long dataId;

    /**
     * 站点编号
     */
    @ExcelField("站点编号")
    @Column(name = "site_id")
    private Integer siteId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 栏目
     */
    @ExcelField("品种编号")
    @Column(name = "item_id")
    private Long itemId;

    /**
     * 模型id
     */
    @Column(name = "pattern_id")
    private Integer patternId;

    /**
     * 品种
     */
    @ExcelField("品种")
    private String name;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 发布时间
     */
    private Date inputDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新时间
     */
    private Date checkDate;

    /**
     * 首页显示
     */
    private Boolean recent;

    /**
     * 说明
     */
    private String remark;

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getPatternId() {
        return patternId;
    }

    public void setPatternId(Integer patternId) {
        this.patternId = patternId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Boolean getRecent() {
        return recent;
    }

    public void setRecent(Boolean recent) {
        this.recent = recent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
