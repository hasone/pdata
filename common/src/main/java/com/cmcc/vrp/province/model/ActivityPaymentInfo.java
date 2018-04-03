package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 广东众筹支付记录对象类
 * Created by qinqinyan on 2017/1/6.
 * */
public class ActivityPaymentInfo {
    private Long id;

    private String winRecordId;

    private String sysSerialNum;

    private String chargeType;

    private Date chargeTime;

    private Date chargeUpdateTime;

    private Date refundTime;

    private Integer status;

    private String returnSerialNum;

    private String returnPayNum;

    private Integer returnPayStatus;

    private Date returnPayTime;

    private Long returnPayAmount;

    private Integer returnCategory;

    private Long returnRefundAmount;

    private String errorMessage;

    private Integer deleteFlag;
    
    private Long payAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWinRecordId() {
        return winRecordId;
    }

    public void setWinRecordId(String winRecordId) {
        this.winRecordId = winRecordId == null ? null : winRecordId.trim();
    }

    public String getSysSerialNum() {
        return sysSerialNum;
    }

    public void setSysSerialNum(String sysSerialNum) {
        this.sysSerialNum = sysSerialNum == null ? null : sysSerialNum.trim();
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType == null ? null : chargeType.trim();
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Date getChargeUpdateTime() {
        return chargeUpdateTime;
    }

    public void setChargeUpdateTime(Date chargeUpdateTime) {
        this.chargeUpdateTime = chargeUpdateTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReturnSerialNum() {
        return returnSerialNum;
    }

    public void setReturnSerialNum(String returnSerialNum) {
        this.returnSerialNum = returnSerialNum == null ? null : returnSerialNum.trim();
    }

    public String getReturnPayNum() {
        return returnPayNum;
    }

    public void setReturnPayNum(String returnPayNum) {
        this.returnPayNum = returnPayNum == null ? null : returnPayNum.trim();
    }

    public Integer getReturnPayStatus() {
        return returnPayStatus;
    }

    public void setReturnPayStatus(Integer returnPayStatus) {
        this.returnPayStatus = returnPayStatus;
    }

    public Date getReturnPayTime() {
        return returnPayTime;
    }

    public void setReturnPayTime(Date returnPayTime) {
        this.returnPayTime = returnPayTime;
    }

    public Long getReturnPayAmount() {
        return returnPayAmount;
    }

    public void setReturnPayAmount(Long returnPayAmount) {
        this.returnPayAmount = returnPayAmount;
    }

    public Integer getReturnCategory() {
        return returnCategory;
    }

    public void setReturnCategory(Integer returnCategory) {
        this.returnCategory = returnCategory;
    }

    public Long getReturnRefundAmount() {
        return returnRefundAmount;
    }

    public void setReturnRefundAmount(Long returnRefundAmount) {
        this.returnRefundAmount = returnRefundAmount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage == null ? null : errorMessage.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }
}