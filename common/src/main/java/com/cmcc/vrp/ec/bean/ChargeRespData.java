package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/25.
 */
@XStreamAlias("ChargeData")
public class ChargeRespData {

    @XStreamAlias("SerialNum")
    String serialNum;

    @XStreamAlias("SystemNum")
    String SystemNum;

    public String getSystemNum() {
        return SystemNum;
    }

    public void setSystemNum(String systemNum) {
        SystemNum = systemNum;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
