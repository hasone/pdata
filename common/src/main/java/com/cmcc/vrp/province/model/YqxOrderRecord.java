package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * YqxOrderRecord.java
 * @author wujiamin
 * @date 2017年5月5日
 */
public class YqxOrderRecord {
    private Long id;

    private String mobile;

    private Long individualProductId;

    private Date vpmnTime;

    private Integer discount;

    private Long payPrice;

    private String serialNum;

    private Integer tradeStatus;

    private Integer payStatus;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private Date chargeTime;//充值发起时间
    
    private Integer chargeStatus;//充值状态

    private Date chargeReturnTime;//充值返回时间
    
    private String chargeMsg;//充值信息
    
    private Integer refundStatus;//退款状态，0-未申请退款；1-退款处理中；2-退款成功；3-退款失败
    
    private Date refundApprovalTime;//退款申请时间
    
    private String payTransactionId;//支付的流水号
    
    private Integer approvalRefund;//记录是否提交过退款申请；0-从未提交；1-提交过
    
    //extends
    private Long productSize;  
    
    private String doneCode;//支付平台成功时返回的流水号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getIndividualProductId() {
        return individualProductId;
    }

    public void setIndividualProductId(Long individualProductId) {
        this.individualProductId = individualProductId;
    }

    public Date getVpmnTime() {
        return vpmnTime;
    }

    public void setVpmnTime(Date vpmnTime) {
        this.vpmnTime = vpmnTime;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Long payPrice) {
        this.payPrice = payPrice;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum == null ? null : serialNum.trim();
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
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

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getChargeReturnTime() {
        return chargeReturnTime;
    }

    public void setChargeReturnTime(Date chargeReturnTime) {
        this.chargeReturnTime = chargeReturnTime;
    }

    public String getChargeMsg() {
        return chargeMsg;
    }

    public void setChargeMsg(String chargeMsg) {
        this.chargeMsg = chargeMsg;
    }

    public Date getRefundApprovalTime() {
        return refundApprovalTime;
    }

    public void setRefundApprovalTime(Date refundApprovalTime) {
        this.refundApprovalTime = refundApprovalTime;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
    }

    public String getDoneCode() {
        return doneCode;
    }

    public void setDoneCode(String doneCode) {
        this.doneCode = doneCode;
    }

    public Integer getApprovalRefund() {
        return approvalRefund;
    }

    public void setApprovalRefund(Integer approvalRefund) {
        this.approvalRefund = approvalRefund;
    }
}