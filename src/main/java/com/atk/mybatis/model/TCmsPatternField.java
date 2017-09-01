package com.atk.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_cms_pattern_field")
public class TCmsPatternField implements Serializable {
    /**
     * 字段编号
     */
    @Id
    @Column(name = "field_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer fieldId;

    /**
     * 模型编号
     */
    @Column(name = "pattern_id")
    private Integer patternId;

    /**
     * 字段名称
     */
    @Column(name = "field_name")
    private String fieldName;

    /**
     * 字段类型（如文本）
     */
    @Column(name = "field_class")
    private String fieldClass;

    /**
     * 字段类别（数据库类别）
     */
    @Column(name = "field_type")
    private String fieldType;

    /**
     * 别名
     */
    private String alias;

    /**
     * 是否为空
     */
    @Column(name = "not_null")
    private Boolean notNull;

    /**
     * 字段长度
     */
    @Column(name = "field_length")
    private Integer fieldLength;

    /**
     * 是否为主键
     */
    @Column(name = "is_primary")
    private Boolean isPrimary;

    /**
     * 字段设置
     */
    private String setting;

    /**
     * 字段值
     */
    @Column(name = "field_value")
    private String fieldValue;

    private static final long serialVersionUID = 1L;

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getPatternId() {
        return patternId;
    }

    public void setPatternId(Integer patternId) {
        this.patternId = patternId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
