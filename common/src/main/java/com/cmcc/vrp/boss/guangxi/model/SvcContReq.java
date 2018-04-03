package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("SvcCont")
public class SvcContReq {

    @XStreamAlias("AdditionInfo")
    private AdditionInfoReq additionInfo;

    public AdditionInfoReq getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(AdditionInfoReq additionInfo) {
        this.additionInfo = additionInfo;
    }
}
