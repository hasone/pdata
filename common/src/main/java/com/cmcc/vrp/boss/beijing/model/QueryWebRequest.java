package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/11.
 */
@XStreamAlias("WebRequest")
public class QueryWebRequest {

    @XStreamAlias("Header")
    private OrderReqHeader header;

    @XStreamAlias("WebBody")
    private QueryReqBody webBody;

    public OrderReqHeader getHeader() {
        return header;
    }

    public void setHeader(OrderReqHeader header) {
        this.header = header;
    }

    public QueryReqBody getWebBody() {
        return webBody;
    }

    public void setWebBody(QueryReqBody webBody) {
        this.webBody = webBody;
    }
}
