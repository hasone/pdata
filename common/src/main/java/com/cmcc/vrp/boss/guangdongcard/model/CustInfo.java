package com.cmcc.vrp.boss.guangdongcard.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 客户信息
 *
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("CustInfo")
public class CustInfo {
    @XStreamAlias("CustId")
    private String custId;

    @XStreamAlias("CustType")
    private String custType;

    @XStreamAlias("EcCode")
    private String ecCode;

    @XStreamAlias("User")
    private User user;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getEcCode() {
        return ecCode;
    }

    public void setEcCode(String ecCode) {
        this.ecCode = ecCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
