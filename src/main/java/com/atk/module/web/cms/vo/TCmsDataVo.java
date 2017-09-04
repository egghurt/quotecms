package com.atk.module.web.cms.vo;

import com.atk.mybatis.model.TCmsData;

public class TCmsDataVo extends TCmsData {
    private String orderField;
    private String orderDirection;

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
