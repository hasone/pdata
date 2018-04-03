package com.cmcc.vrp.wx.flowcoin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("SvcCont")
public class SvcCont {

    @XStreamAlias("MemberShipRequest")
    private MemberShipRequest request;

    public MemberShipRequest getRequest() {
        return request;
    }

    public void setRequest(MemberShipRequest request) {
        this.request = request;
    }
}
