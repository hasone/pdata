package com.cmcc.vrp.province.model;

import java.util.Date;

public class EntBillRecord {
    private Long id;
    private String type;//活动类型
    private String productName;
    private String phone;
    private Date chargeTime;
    private Date updateChargeTime;
    private String intervalDate;//请求时长
    private Long price;
    private Long enterId;
    private String systemNum;
    private Integer status;
    private Integer financeStatus;
    private Integer changeAccountStatus;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Date getChargeTime() {
        return chargeTime;
    }
    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }
    public String getIntervalDate() {
        return intervalDate;
    }
    public void setIntervalDate(String intervalDate) {
        this.intervalDate = intervalDate;
    }
    public Long getEnterId() {
        return enterId;
    }
    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }
    public String getSystemNum() {
        return systemNum;
    }
    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getFinanceStatus() {
        return financeStatus;
    }
    public void setFinanceStatus(Integer financeStatus) {
        this.financeStatus = financeStatus;
    }
    public Integer getChangeAccountStatus() {
        return changeAccountStatus;
    }
    public void setChangeAccountStatus(Integer changeAccountStatus) {
        this.changeAccountStatus = changeAccountStatus;
    }
    public Date getUpdateChargeTime() {
        return updateChargeTime;
    }
    public void setUpdateChargeTime(Date updateChargeTime) {
        this.updateChargeTime = updateChargeTime;
    }
    public Long getPrice() {
        return price;
    }
    public void setPrice(Long price) {
        this.price = price;
    }
}
