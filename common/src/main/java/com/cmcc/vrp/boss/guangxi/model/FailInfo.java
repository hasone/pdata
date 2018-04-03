package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/23.
 */
@XStreamAlias("FailInfo")
public class FailInfo {

    @XStreamAlias("MobNum")
    private String mobNum;

    @XStreamAlias("Rsp")
    private String rsp;

    @XStreamAlias("RspDesc")
    private String rspDesc;


    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getRsp() {
        return rsp;
    }

    public void setRsp(String rsp) {
        this.rsp = rsp;
    }

    public String getRspDesc() {
        return rspDesc;
    }

    public void setRspDesc(String rspDesc) {
        this.rspDesc = rspDesc;
    }
}
