package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/19.
 */
@XStreamAlias("SvcCont")
public class SvcCont {

    @XStreamAlias("BizProcReq")
    private BizProcReq bizProcReq;

    public BizProcReq getBizProcReq() {
        return bizProcReq;
    }

    public void setBizProcReq(BizProcReq bizProcReq) {
        this.bizProcReq = bizProcReq;
    }
}
