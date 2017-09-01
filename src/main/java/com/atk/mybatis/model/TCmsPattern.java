package com.atk.mybatis.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_cms_pattern")
public class TCmsPattern implements Serializable {
    @Id
    @Column(name = "pattern_id")
    private Integer patternId;

    /**
     * 模型名称
     */
    @Column(name = "pattern_name")
    private String patternName;

    /**
     * 模型表名称
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 站点id
     */
    @Column(name = "site_id")
    private Integer siteId;

    /**
     * 字段描述
     */
    private String des;

    /**
     * 状态
     */
    private Boolean status;

    private static final long serialVersionUID = 1L;

    public Integer getPatternId() {
        return patternId;
    }

    public void setPatternId(Integer patternId) {
        this.patternId = patternId;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
