package com.cmcc.vrp.boss.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunyiwei on 2015/11/28.
 */
@XStreamAlias("SuccInfo")
public class SuccInfo {
    @XStreamAlias("MobNum")
    private String mobNum;

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }
}
