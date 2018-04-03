package com.cmcc.vrp.province.model;

/**
 * YqxChargeInfo.java
 * @author wujiamin
 */
public class YqxChargeInfo {
    private Long id;

    private String serialNum;

    private String returnSystemNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum == null ? null : serialNum.trim();
    }

    public String getReturnSystemNum() {
        return returnSystemNum;
    }

    public void setReturnSystemNum(String returnSystemNum) {
        this.returnSystemNum = returnSystemNum == null ? null : returnSystemNum.trim();
    }
}