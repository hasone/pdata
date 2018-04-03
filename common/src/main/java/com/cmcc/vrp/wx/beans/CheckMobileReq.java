package com.cmcc.vrp.wx.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2017/1/5.
 */
@XStreamAlias("CheckMobileReq")
public class CheckMobileReq {

    @XStreamAlias("ECCode")
    private String ecCode;

    @XStreamAlias("Mobile")
    private String mobile;

    public String getEcCode() {
        return ecCode;
    }

    public void setEcCode(String ecCode) {
        this.ecCode = ecCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
