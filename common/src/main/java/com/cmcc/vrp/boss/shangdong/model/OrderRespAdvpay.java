package com.cmcc.vrp.boss.shangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/3/31.
 */
@XStreamAlias("AdvPay")
public class OrderRespAdvpay {

    @XStreamAlias("PubInfo")
    OrderRespPubInfo respPubInfo;

    public OrderRespPubInfo getRespPubInfo() {
        return respPubInfo;
    }

    public void setRespPubInfo(OrderRespPubInfo respPubInfo) {
        this.respPubInfo = respPubInfo;
    }
}
