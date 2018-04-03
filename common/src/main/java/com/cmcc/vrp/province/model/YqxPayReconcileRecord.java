package com.cmcc.vrp.province.model;

import java.util.Date;


/**
 * 支付对账前端展示数据
 *
 */
public class YqxPayReconcileRecord {
    private String orderSerialNum;//订单流水号
    
    private String paySerialNum;//支付订单号
    
    private String payTransactionId;//支付流水号
    
    private String mobile;//手机号
    
    private String prdName;//产品名称
    
    private Long price;//价格
    
    private Integer payStatus;//支付状态
    
    private Integer reconcileStatus;//对账状态
    
    private String reconcileMsg;//对账相关信息
    
    private Date payCreateTime;//订单创建时间
    
    private String payTime;
    
    private String doneCode;//支付平台返回流水号

    public String getOrderSerialNum() {
        return orderSerialNum;
    }

    public void setOrderSerialNum(String orderSerialNum) {
        this.orderSerialNum = orderSerialNum;
    }

    public String getPaySerialNum() {
        return paySerialNum;
    }

    public void setPaySerialNum(String paySerialNum) {
        this.paySerialNum = paySerialNum;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getReconcileStatus() {
        return reconcileStatus;
    }

    public void setReconcileStatus(Integer reconcileStatus) {
        this.reconcileStatus = reconcileStatus;
    }

    public String getReconcileMsg() {
        return reconcileMsg;
    }

    public void setReconcileMsg(String reconcileMsg) {
        this.reconcileMsg = reconcileMsg;
    }

    public Date getPayCreateTime() {
        return payCreateTime;
    }

    public void setPayCreateTime(Date payCreateTime) {
        this.payCreateTime = payCreateTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getDoneCode() {
        return doneCode;
    }

    public void setDoneCode(String doneCode) {
        this.doneCode = doneCode;
    }

    
}
