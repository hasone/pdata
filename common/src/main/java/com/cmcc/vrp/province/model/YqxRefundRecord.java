package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 
 * YqxRefundRecord
 *
 */
public class YqxRefundRecord {
    private Long id;

    private String doneCode;

    private String reason;

    private Double refundAmount;

    private Integer refundType;

    private String refundSerialNum;

    private Integer status;

    private String msg;

    private Date createTime;

    private Date resultReturnTime;

    private Date updateTime;
    
    private Long operatorId;
    
    private String operatorName;
    
    private String operatorMobile;
    
    //extends
    private String orderSerialNum;//订单号
    
    private Date orderCreateTime;//订购创建时间
    
    private String mobile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoneCode() {
        return doneCode;
    }

    public void setDoneCode(String doneCode) {
        this.doneCode = doneCode == null ? null : doneCode.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public String getRefundSerialNum() {
        return refundSerialNum;
    }

    public void setRefundSerialNum(String refundSerialNum) {
        this.refundSerialNum = refundSerialNum == null ? null : refundSerialNum.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getResultReturnTime() {
        return resultReturnTime;
    }

    public void setResultReturnTime(Date resultReturnTime) {
        this.resultReturnTime = resultReturnTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderSerialNum() {
        return orderSerialNum;
    }

    public void setOrderSerialNum(String orderSerialNum) {
        this.orderSerialNum = orderSerialNum;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorMobile() {
        return operatorMobile;
    }

    public void setOperatorMobile(String operatorMobile) {
        this.operatorMobile = operatorMobile;
    }
}