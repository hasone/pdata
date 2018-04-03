package com.cmcc.vrp.boss.shangdong.boss.model;

/**
 * 提取山东userOrder中最重要的四个字段，用于生成话单使用
 * @author Administrator
 *
 */
public class UserOrder {
    private String customerID;//客户ID，最大长度为64
    
    private String bizId;//规格编号，最大长度64
    
    private String userID;//订单编号，主要订单变更关联变更信息的订单编号，最大长度64
    
    private String discount;//0323 新增的折扣字段

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
    
    
}
