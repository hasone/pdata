package com.cmcc.vrp.boss.guangdongcard.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 请求内容
 *
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("MemberOrderRequest")
public class MemberOrderRequest {
    @XStreamAlias("CustInfo")
    private CustInfo custInfo;

    public CustInfo getCustInfo() {
        return custInfo;
    }

    public void setCustInfo(CustInfo custInfo) {
        this.custInfo = custInfo;
    }
}
