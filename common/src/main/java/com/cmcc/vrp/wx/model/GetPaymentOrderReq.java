package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月8日 下午3:32:10
*/
@XStreamAlias("GetPaymentOrderReq")
public class GetPaymentOrderReq {
    @XStreamAlias("ChannelID")
    private String channelID;
    
    @XStreamAlias("PaymentOrderID")
    private String paymentOrderID;
    
    @XStreamAlias("OrderID")
    private String orderID;

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
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

    @Override
    public String toString() {
        return "GetPaymentOrderReq [channelID=" + channelID + ", paymentOrderID=" + paymentOrderID + ", orderID="
                + orderID + "]";
    }

}
