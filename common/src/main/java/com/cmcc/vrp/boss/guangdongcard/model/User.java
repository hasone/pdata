package com.cmcc.vrp.boss.guangdongcard.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 用户订购信息
 *
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("User")
public class User {
    @XStreamAlias("ECPrdCode")
    private String ecPrdCode;

    @XStreamAlias("ProductCode")
    private String productCode;

    @XStreamAlias("SyAppCode")
    private String syAppCode;

    @XStreamAlias("Member")
    private Member member;

    public String getEcPrdCode() {
        return ecPrdCode;
    }

    public void setEcPrdCode(String ecPrdCode) {
        this.ecPrdCode = ecPrdCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSyAppCode() {
        return syAppCode;
    }

    public void setSyAppCode(String syAppCode) {
        this.syAppCode = syAppCode;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
