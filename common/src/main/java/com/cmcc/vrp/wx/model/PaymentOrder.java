package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月18日 下午2:45:10
*/
@XStreamAlias("PaymentOrder")
public class PaymentOrder {
    @XStreamAlias("ChannelID")
    private String channelID;
    
    @XStreamAlias("ServiceID")
    private String serviceID;
    
    @XStreamAlias("PaymentOrderID")
    private String paymentOrderID;
    
    @XStreamAlias("OrderID")
    private String orderID;
    
    @XStreamAlias("OrderTime")
    private String orderTime;
    
    @XStreamAlias("Merchant")
    private String merchant;
    
    @XStreamAlias("User")
    private String user;
    
    @XStreamAlias("PayUser")
    private String payUser;
    
    @XStreamAlias("Amount")
    private String amount;
    
    @XStreamAlias("Status")
    private String status;
    
    @XStreamAlias("PayTime")
    private String payTime;
    
    @XStreamAlias("PayErrorCode")
    private String payErrorCode;
    
    @XStreamAlias("NotifyStatus")
    private String notifyStatus;
    
    @XStreamAlias("NotifyTime")
    private String notifyTime;
    
    @XStreamAlias("DeliveryStatus")
    private String deliveryStatus;
    
    @XStreamAlias("DeliveryTime")
    private String deliveryTime;
    
    @XStreamAlias("DeliveryErrorCode")
    private String deliveryErrorCode;
    
    @XStreamAlias("ReversalTime")
    private String reversalTime;
    
    @XStreamAlias("ReversalReason")
    private String reversalReason;
    
    @XStreamAlias("Title")
    private String title;
    
    @XStreamAlias("OrderUrl")
    private String orderUrl;
    
    @XStreamAlias("BackUrl")
    private String backUrl;
    
    @XStreamAlias("NotifyUrl")
    private String notifyUrl;
    
    @XStreamAlias("PayOrgCode")
    private String payOrgCode;
    
    @XStreamAlias("PayMethodCode")
    private String payMethodCode;
    
    @XStreamAlias("PayOrgTransSN")
    private String payOrgTransSN;
    
    @XStreamAlias("Description")
    private String description;

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayErrorCode() {
        return payErrorCode;
    }

    public void setPayErrorCode(String payErrorCode) {
        this.payErrorCode = payErrorCode;
    }

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryErrorCode() {
        return deliveryErrorCode;
    }

    public void setDeliveryErrorCode(String deliveryErrorCode) {
        this.deliveryErrorCode = deliveryErrorCode;
    }

    public String getReversalTime() {
        return reversalTime;
    }

    public void setReversalTime(String reversalTime) {
        this.reversalTime = reversalTime;
    }

    public String getReversalReason() {
        return reversalReason;
    }

    public void setReversalReason(String reversalReason) {
        this.reversalReason = reversalReason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
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

    public String getPayOrgTransSN() {
        return payOrgTransSN;
    }

    public void setPayOrgTransSN(String payOrgTransSN) {
        this.payOrgTransSN = payOrgTransSN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
