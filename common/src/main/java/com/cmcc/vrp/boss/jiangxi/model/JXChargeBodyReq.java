package com.cmcc.vrp.boss.jiangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 江西渠道充值请求BODY
 * Created by leelyn on 2016/7/7.
 */
@XStreamAlias("BODY")
public class JXChargeBodyReq {

    @XStreamAlias("ECCODE")
    private String ecCode;
    @XStreamAlias("PHONE")
    private String phone;
    @XStreamAlias("VOLUME")
    private String volume;

    public String getEcCode() {
        return ecCode;
    }

    public void setEcCode(String ecCode) {
        this.ecCode = ecCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
