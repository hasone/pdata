package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("InterBOSS")
public class GxChargeReqBody {

    @XStreamAlias("SvcCont")
    private SvcContReq svcCont;

    public SvcContReq getSvcCont() {
        return svcCont;
    }

    public void setSvcCont(SvcContReq svcCont) {
        this.svcCont = svcCont;
    }
}
