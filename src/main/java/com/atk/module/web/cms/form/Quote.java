package com.atk.module.web.cms.form;

public class Quote {
    private Long itemId;
    private Integer low;
    private Integer high;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "itemId=" + itemId +
                ", low=" + low +
                ", high=" + high +
                "}";
    }
}
