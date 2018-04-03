package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月6日 上午10:44:13
*/
@XStreamAlias("NotifyReq")
public class NotifyReq {
    @XStreamAlias("ADCOrderID")
    private String aDCOrderID;
    
    @XStreamAlias("MessageID")
    private String messageID;
    
    @XStreamAlias("SentTimes")
    private String sentTimes;
    
    @XStreamAlias("AckRequired")
    private String ackRequired;
    
    @XStreamAlias("Category")
    private String category;
    
    @XStreamAlias("PaymentOrderID")
    private String paymentOrderID;
    
    @XStreamAlias("OrderID")
    private String orderID;
    
    @XStreamAlias("Points")
    private String points;
    
    @XStreamAlias("Amount")
    private String amount;
    
    @XStreamAlias("PaymentStatus")
    private String paymentStatus;
    
    @XStreamAlias("Data")
    private String data;
    
    @XStreamAlias("PayOrgCode")
    private String payOrgCode;
    
    @XStreamAlias("PayMethodCode")
    private String payMethodCode;
    
    @XStreamAlias("OriginPayErrorCode")
    private String originPayErrorCode;
    
    @XStreamAlias("Rev1")
    private String rev1;
    
    @XStreamAlias("Rev2")
    private String rev2;
    
    @XStreamAlias("Digest")
    private String digest;

    /**
     * @return
     */
    public String getaDCOrderID() {
        return aDCOrderID;
    }

    /**
     * @param aDCOrderID
     */
    public void setaDCOrderID(String aDCOrderID) {
        this.aDCOrderID = aDCOrderID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getSentTimes() {
        return sentTimes;
    }

    public void setSentTimes(String sentTimes) {
        this.sentTimes = sentTimes;
    }

    public String getAckRequired() {
        return ackRequired;
    }

    public void setAckRequired(String ackRequired) {
        this.ackRequired = ackRequired;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentOrderID() {
        return paymentOrderID;
    }

    public void setPaymentOrderID(String paymentOrderID) {
        this.paymentOrderID = paymentOrderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPayOrgCode() {
        return payOrgCode;
    }

    public void setPayOrgCode(String payOrgCode) {
        this.payOrgCode = payOrgCode;
    }

    public String getPayMethodCode() {
        return payMethodCode;
    }

    public void setPayMethodCode(String payMethodCode) {
        this.payMethodCode = payMethodCode;
    }

    public String getOriginPayErrorCode() {
        return originPayErrorCode;
    }

    public void setOriginPayErrorCode(String originPayErrorCode) {
        this.originPayErrorCode = originPayErrorCode;
    }

    public String getRev1() {
        return rev1;
    }

    public void setRev1(String rev1) {
        this.rev1 = rev1;
    }

    public String getRev2() {
        return rev2;
    }

    public void setRev2(String rev2) {
        this.rev2 = rev2;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
    
    
}
