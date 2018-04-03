package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/23.
 */
@XStreamAlias("InterBOSS")
public class GxCallbackBody {

    @XStreamAlias("SvcCont")
    private String svcCont;

    public String getSvcCont() {
        return svcCont;
    }

    public void setSvcCont(String svcCont) {
        this.svcCont = svcCont;
    }
}
