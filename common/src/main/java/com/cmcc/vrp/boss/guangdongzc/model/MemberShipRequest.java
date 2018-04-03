package com.cmcc.vrp.boss.guangdongzc.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("MemberShipRequest")
public class MemberShipRequest {

    @XStreamAlias("BODY")
    private GdBody body;

    public GdBody getBody() {
        return body;
    }

    public void setBody(GdBody body) {
        this.body = body;
    }
}
