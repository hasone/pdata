package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年3月9日 下午3:30:05
*/
@XStreamAlias("GetOrderInfoReq")
public class GetOrderInfoReq {
    @XStreamAlias("SerialNum")
    String serialNum;
    
    @XStreamAlias("OrderId")
    String orderId;
    
    @XStreamAlias("Mobile")
    String mobile;

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
}
