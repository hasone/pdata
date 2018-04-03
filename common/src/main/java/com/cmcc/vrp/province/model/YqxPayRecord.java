package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 
 * YqxPayRecord
 *
 */
public class YqxPayRecord {
    private Long id;

    private String orderSerialNum;

    private String payOrderId;

    private String payTransactionId;
    
    private Integer payType;

    private Integer status;

    private String statusInfo;
    
    private String doneCode;

    private Date resultReturnTime;

    private Date createTime;

    private Date updateTime;
    
    private Integer reconcileStatus;

    private String reconcileMsg;
    
    private Integer chargeStatus;//充值状态
    
    private Date chargeTime;//充值时间
    
    //extends
    private String chargeMsg;//充值信息
    
    private Long payPrice;//支付价格
    
    private boolean needChangeDbStatus = false; //对账是否需要更新Db支付状态

    private boolean canRefund = false; //是否可以进行售后处理退款操作：true可以，false不可以
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderSerialNum() {
        return orderSerialNum;
    }

    public void setOrderSerialNum(String orderSerialNum) {
        this.orderSerialNum = orderSerialNum == null ? null : orderSerialNum.trim();
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId == null ? null : payOrderId.trim();
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId == null ? null : payTransactionId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo == null ? null : statusInfo.trim();
    }

    public Date getResultReturnTime() {
        return resultReturnTime;
    }

    public void setResultReturnTime(Date resultReturnTime) {
        this.resultReturnTime = resultReturnTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDoneCode() {
        return doneCode;
    }

    public void setDoneCode(String doneCode) {
        this.doneCode = doneCode;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeMsg() {
        return chargeMsg;
    }

    public void setChargeMsg(String chargeMsg) {
        this.chargeMsg = chargeMsg;
    }

    public Long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Long payPrice) {
        this.payPrice = payPrice;
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

    public boolean isNeedChangeDbStatus() {
        return needChangeDbStatus;
    }

    public void setNeedChangeDbStatus(boolean needChangeDbStatus) {
        this.needChangeDbStatus = needChangeDbStatus;
    }

    public boolean isCanRefund() {
        return canRefund;
    }

    public void setCanRefund(boolean canRefund) {
        this.canRefund = canRefund;
    }  
    
    
}