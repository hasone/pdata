package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 支付通知请求对象
 * create by qinqinyan
 * */
@XStreamAlias("Request")
public class PaymentReq {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("Payment")
    private PaymentPojo paymentPojo;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public PaymentPojo getPaymentPojo() {
        return paymentPojo;
    }

    public void setPaymentPojo(PaymentPojo paymentPojo) {
        this.paymentPojo = paymentPojo;
    }
}
