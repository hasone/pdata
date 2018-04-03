package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/11.
 */
@XStreamAlias("WebResponse")
public class QueryWebResponse {

    @XStreamAlias("Header")
    private QueryRespHeader respHeader;

    @XStreamAlias("WebBody")
    private QueryRespBody respBody;

    public QueryRespBody getRespBody() {
        return respBody;
    }

    public void setRespBody(QueryRespBody respBody) {
        this.respBody = respBody;
    }

    public QueryRespHeader getRespHeader() {
        return respHeader;
    }

    public void setRespHeader(QueryRespHeader respHeader) {
        this.respHeader = respHeader;
    }
}
