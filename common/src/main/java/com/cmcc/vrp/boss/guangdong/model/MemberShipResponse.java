package com.cmcc.vrp.boss.guangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/12.
 */
@XStreamAlias("MemberShipResponse")
public class MemberShipResponse {

    @XStreamAlias("BODY")
    private GdRespBody body;

    public GdRespBody getBody() {
        return body;
    }

    public void setBody(GdRespBody body) {
        this.body = body;
    }
}
