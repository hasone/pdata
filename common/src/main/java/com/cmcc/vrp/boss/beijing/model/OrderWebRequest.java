package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/26.
 */
@XStreamAlias("WebRequest")
public class OrderWebRequest {

    @XStreamAlias("Header")
    private OrderReqHeader reqHeader;

    @XStreamAlias("WebBody")
    private OrderReqBody reqBody;

    public OrderReqHeader getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(OrderReqHeader reqHeader) {
        this.reqHeader = reqHeader;
    }

    public OrderReqBody getReqBody() {
        return reqBody;
    }

    public void setReqBody(OrderReqBody reqBody) {
        this.reqBody = reqBody;
    }
}
