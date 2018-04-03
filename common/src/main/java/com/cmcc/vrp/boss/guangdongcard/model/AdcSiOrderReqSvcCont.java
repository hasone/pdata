package com.cmcc.vrp.boss.guangdongcard.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 集团成员开通与变更产品接口， 请求体（广东流量卡接口5.1.2）
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public class AdcSiOrderReqSvcCont {
    @XStreamAlias("MemberOrderRequest")
    private MemberOrderRequest memberOrderRquest;

    public MemberOrderRequest getMemberOrderRquest() {
        return memberOrderRquest;
    }

    public void setMemberOrderRquest(MemberOrderRequest memberOrderRquest) {
        this.memberOrderRquest = memberOrderRquest;
    }
}
