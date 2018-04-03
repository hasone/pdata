package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("SuccInfo")
public class SuccInfo {
    @XStreamAlias("SuccTel")
    private String succTel;

    public String getSuccTel() {
        return succTel;
    }

    public void setSuccTel(String succTel) {
        this.succTel = succTel;
    }
}
