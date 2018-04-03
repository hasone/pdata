package com.cmcc.vrp.boss.shangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/3/31.
 */
@XStreamAlias("AdvPay")
public class OrderReqAdvPay {

    @XStreamAlias("PubInfo")
    OrderReqPubInfo orderReqPubInfo;

    @XStreamAlias("BusiData")
    OrderReqBusiData orderReqBusiData;

    public OrderReqPubInfo getOrderReqPubInfo() {
        return orderReqPubInfo;
    }

    public void setOrderReqPubInfo(OrderReqPubInfo orderReqPubInfo) {
        this.orderReqPubInfo = orderReqPubInfo;
    }

    public OrderReqBusiData getOrderReqBusiData() {
        return orderReqBusiData;
    }

    public void setOrderReqBusiData(OrderReqBusiData orderReqBusiData) {
        this.orderReqBusiData = orderReqBusiData;
    }
}
