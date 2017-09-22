package com.atk.module.web.cms.vo;

import com.atk.mybatis.model.TCmsData;

public class TCmsDataVo extends TCmsData implements Cloneable{
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

    @Override
    public Object clone() {
        TCmsDataVo data = null;
        try{
            data = (TCmsDataVo) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return  data;
    }

}
