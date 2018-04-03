package com.cmcc.vrp.province.mdrc.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunyiwei on 2016/6/21.
 */
public class MdrcEcChargeRequestData {
    @XStreamAlias("CardNum")
    private String cardNum;

    @XStreamAlias("Password")
    private String password;

    @XStreamAlias("Mobile")
    private String mobile;

    @XStreamAlias("SerialNum")
    private String serialNum;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
