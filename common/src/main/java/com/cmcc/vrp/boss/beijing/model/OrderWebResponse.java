package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/26.
 */
@XStreamAlias("WebResponse")
public class OrderWebResponse {

    @XStreamAlias("Header")
    private OrderRespHeader respHeader;

    @XStreamAlias("WebBody")
    private OrderRespBody respBody;

    public OrderRespHeader getRespHeader() {
        return respHeader;
    }

    public void setRespHeader(OrderRespHeader respHeader) {
        this.respHeader = respHeader;
    }

    public OrderRespBody getRespBody() {
        return respBody;
    }

    public void setRespBody(OrderRespBody respBody) {
        this.respBody = respBody;
    }
}
