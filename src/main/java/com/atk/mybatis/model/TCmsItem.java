package com.atk.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_cms_item")
public class TCmsItem implements Serializable {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long itemId;

    /**
     * 别名
     */
    private String alias;

    /**
     * 分类明细
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 站点编号
     */
    @Column(name = "site_id")
    private Integer siteId;

    /**
     * 父类编号
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 模型编号
     */
    @Column(name = "pattern_id")
    private Integer patternId;

    /**
     * 单页（0：不是，1：是）
     */
    private Boolean alone;

    /**
     * 列表页面
     */
    @Column(name = "list_tpl")
    private String listTpl;

    /**
     * 是否有子类
     */
    @Column(name = "has_child")
    private Boolean hasChild;

    /**
     * 是否为通用品种（子站点也默认继承此品种）
     */
    @Column(name = "is_common")
    private Boolean isCommon;

    /**
     * 栏目分页数量
     */
    @Column(name = "page_size")
    private Integer pageSize;


    private static final long serialVersionUID = 1L;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getPatternId() {
        return patternId;
    }

    public void setPatternId(Integer patternId) {
        this.patternId = patternId;
    }

    public Boolean getAlone() {
        return alone;
    }

    public void setAlone(Boolean alone) {
        this.alone = alone;
    }

    public String getListTpl() {
        return listTpl;
    }

    public void setListTpl(String listTpl) {
        this.listTpl = listTpl;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Boolean getIsCommon() {
        return isCommon;
    }

    /**
     * 设置是否为通用栏目（子站点也默认继承此栏目）
     *
     * @param isCommon 是否为通用栏目（子站点也默认继承此栏目）
     */
    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
