package com.cmcc.vrp.boss.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunyiwei on 2015/11/28.
 */
@XStreamAlias("FailInfo")
public class FailInfo {
    @XStreamAlias("MobNum")
    private String mobNum;

    @XStreamAlias("FailDesc")
    private String failDesc;

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getFailDesc() {
        return failDesc;
    }

    public void setFailDesc(String failDesc) {
        this.failDesc = failDesc;
    }
}
