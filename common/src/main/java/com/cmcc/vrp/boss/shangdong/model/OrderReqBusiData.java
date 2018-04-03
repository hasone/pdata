package com.cmcc.vrp.boss.shangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/3/30.
 */
@XStreamAlias("BusiData")
public class OrderReqBusiData {

    @XStreamAlias("CreateTime")
    String createTime;

    @XStreamAlias("ChargePhoneNum")
    String chargePhoneNum;

    @XStreamAlias("UserID")
    String userID;

    @XStreamAlias("ProductCode")
    String productCode;

    @XStreamAlias("ChargeNum")
    String chargeNum;


    public String getChargePhoneNum() {
        return chargePhoneNum;
    }

    public void setChargePhoneNum(String chargePhoneNum) {
        this.chargePhoneNum = chargePhoneNum;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getChargeNum() {
        return chargeNum;
    }

    public void setChargeNum(String chargeNum) {
        this.chargeNum = chargeNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
