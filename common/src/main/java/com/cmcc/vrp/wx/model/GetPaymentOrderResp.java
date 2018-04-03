package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月18日 下午2:43:59
*/
@XStreamAlias("GetPaymentOrderResp")
public class GetPaymentOrderResp {
    @XStreamAlias("Result")
    private String result;
    @XStreamAlias("ResultMsg")
    private String resultMsg;
    @XStreamAlias("PaymentOrder")
    private PaymentOrder paymentOrder;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

}
